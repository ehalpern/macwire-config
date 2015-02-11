package macwire.config

import scala.collection.JavaConversions._
import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigValue
import com.typesafe.config.ConfigValueType._

import scala.reflect.internal.util.ScalaClassLoader
import scala.reflect.runtime.universe._


/**
 */
object ConfigReader
{
  /**
   * Flattens the configuration and returns a list of (key, type) tuples representing
   * all of the configuration properties and their associated types.  Types are inferred
   * from property values.  This means that every
   * by the the most specific type that can be inferred from reading the configuration.
   */
  def readConfig(cl: ClassLoader): Seq[(String, TypeTag[_ <: Any])] =
  {
    //System.setProperty("config.trace", "loads")

    // Set the ctx classloader for ConfigFactory to find the conf file in the classpath
    val origLoader = ScalaClassLoader.contextLoader
    ScalaClassLoader.setContext(this.getClass.getClassLoader)
    val config = ConfigFactory.load
    ScalaClassLoader.setContext(origLoader)
    val set = for (
      entry <- config.entrySet
    ) yield {
      val cv = entry.getValue
      cv.valueType match {
        case STRING | NUMBER | BOOLEAN =>
          (entry.getKey, primitiveType(entry.getValue))
        case LIST =>
          (entry.getKey, listType(entry.getValue))
        case NULL =>
          throw new AssertionError(
            s"Did not expect NULL entry in ConfigValue.entrySet: ${cv.origin}"
          )
        case OBJECT =>
          throw new AssertionError(
            s"Did not expect OBJECT entry in ConfigValue.entrySet: ${cv.origin}"
          )
      }
    }
    set.toSeq
  }

  /**
   * Convert the given config value to a String, Boolean, Int or Double
   */
  private def primitiveType(valueHolder: ConfigValue): TypeTag[_ <: Any]
  = {
    val value: TypeTag[_] = valueHolder.valueType match {
      case STRING => typeTag[String]
      case BOOLEAN => typeTag[Boolean]
      case NUMBER => valueHolder.unwrapped match {
        case i: java.lang.Integer => typeTag[Int]
        case d: java.lang.Double => typeTag[Double]
        case _ => throw new AssertionError("Unsupported type " + valueHolder.unwrapped.getClass.getName + ": " + valueHolder)
      }
      case OBJECT => throw new AssertionError("Unexpected type OBJECT: " + valueHolder)
      case NULL => throw new AssertionError("Unsupported type NULL: " + valueHolder)
      case LIST => throw new AssertionError("Unexpected type LIST: " + valueHolder)
    }
    value
  }

  /**
   * Convert the given config value list to an Array[String|Boolean|Int|Double]
   */
  private def listType(value: ConfigValue): TypeTag[_ <: Seq[_ <: Any]]
  = {
    val list = value.unwrapped.asInstanceOf[java.util.List[Any]]

    if (list.size == 0) {
      typeTag[Seq[String]]
    } else {
      list.get(0) match {
        case x: String => typeTag[Seq[String]]
        case x: Boolean => typeTag[Seq[Boolean]]
        case x: java.lang.Integer => typeTag[Seq[Int]]
        case x: Double => typeTag[Seq[Double]]
        case x =>
          throw new AssertionError("Unsupported list type " + x.getClass)
      }
    }
  }
}


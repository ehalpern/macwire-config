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
   * Flattens the configuration and returns a list of (key, value) tuples representing
   * all of the configuration properties and their associated values.  Values are represented
   * by the the most specific type that can be inferred from reading the configuration.
   */
  def readConfig(cl: ClassLoader): Seq[(String, Any)] =
  {
    System.setProperty("config.trace", "loads")

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
          (entry.getKey, primitiveValue(entry.getValue))
        case LIST =>
          (entry.getKey, listValue(entry.getValue))
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
  private def primitiveValue(valueHolder: ConfigValue): Any
  = {
    val value: Any = valueHolder.valueType match {
      case STRING => valueHolder.unwrapped.toString
      case BOOLEAN => Boolean.unbox(valueHolder.unwrapped)
      case NUMBER => valueHolder.unwrapped match {
        case i: java.lang.Integer => Int.unbox(i)
        case d: java.lang.Double => Double.unbox(d)
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
  private def listValue(value: ConfigValue): Seq[Any]
  = {
    val list = value.unwrapped.asInstanceOf[java.util.List[Any]]

    if (list.size == 0) {
      Seq[String]()
    } else {
      val seq = list.get(0) match {
        case x: String =>
          list.collect({case x: String => x}).toSeq
        case x: Boolean =>
          list.collect({case x: java.lang.Boolean => x.booleanValue}).toSeq
        case x: java.lang.Integer =>
          list.collect({case x: java.lang.Integer => x.intValue}).toSeq
        case x: Double =>
          list.collect({case x: java.lang.Double => x.doubleValue}).toSeq
        case x =>
          throw new AssertionError("Unsupported list type " + x.getClass)
      }
      seq
    }
  }
}


package twine.macwire.config

import org.specs2.mutable.Specification
import scala.reflect.runtime.universe._

/**
  */
class ConfigReaderSpec extends Specification
{
  "The default ConfigReader" should {
    "read some properties" in {
      val map = ConfigReader.readConfig(this.getClass.getClassLoader).toMap
      map should not be empty
      map should havePair("types.boolean" -> typeTag[Boolean])
      map should havePair("types.int"     -> typeTag[Int])
      map should havePair("types.double"  -> typeTag[Double])
      map should havePair("types.string"  -> typeTag[String])
      map should havePair("types.stringList" -> typeTag[Seq[String]])
    }
  }
}
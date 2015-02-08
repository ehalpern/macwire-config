package macwire.config

import org.specs2.mutable.Specification

/**
  */
class ConfigReaderSpec extends Specification
{
  "The default ConfigReader" should {
    "read some properties" in {
      val map = ConfigReader.readConfig(this.getClass.getClassLoader).toMap
      map should not be empty
      map should havePair("types.boolean" -> false)
      map should havePair("types.int" -> 1)
      map should havePair("types.double"  -> 1.1)
      map should havePair("types.string"  -> "test")
      map should havePair("types.stringList" -> Seq("one", "two", "three"))
    }
  }
}
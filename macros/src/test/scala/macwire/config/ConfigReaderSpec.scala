package macwire.config

import org.specs2.mutable.Specification

/**
 */
class ConfigReaderSpec extends Specification
{
  "The default ConfigReader" should {
    "read some properties" in {
      val map = ConfigReader.readConfig.toMap
      map should not be empty
      map should havePair("test.boolean" -> true)
      map should havePair("test.integer" -> 5)
      map should havePair("test.double"  -> 5.5)
      map should havePair("test.string"  -> "five")
      map should havePair("test.stringList"  -> Seq("one", "two", "three"))
    }
  }
}
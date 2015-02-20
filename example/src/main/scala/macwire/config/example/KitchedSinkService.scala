package macwire.config.example

import com.softwaremill.macwire.Tagging._
import Config.Tags._ // imported so that tags don't have to be qualified with Config.Tags

/**
 */
trait KitchenSinkService
{
  def configString(): String
}

/**
 * This example service illustrates how to inject each of the possible config values types
 * from a Typesafe config.  Each config value is identified by a unique qualifier tag
 * (like `kitchensink.string`).  This tag is a generated type that corresponds to the
 * config property of the same name (`kitchensink.string` corresponds to the kitchensink.boolean
 * property in application.json).
 */
class KitchenSinkServiceImpl(
  stringProp: String @@ `kitchensink.string`,
  booleanProp: Boolean @@ `kitchensink.boolean`,
  intProp: Int @@ `kitchensink.int`,
  doubleProp: Double @@ `kitchensink.double`,
  stringList: Seq[String] @@ `kitchensink.stringList`,
  booleanList: Seq[Boolean] @@ `kitchensink.booleanList`,
  intList: Seq[Int] @@ `kitchensink.intList`,
  doubleList: Seq[Double] @@ `kitchensink.doubleList`
) extends KitchenSinkService
{
  def configString: String = {
    return s"""
      stringProp: $stringProp
      booleanProp: $booleanProp
      intProp: $intProp
      doubleProp: $doubleProp
      stringList: $stringList
      booleanList: $booleanList
      intList: $intList
      doubleList: $doubleList
    """
  }
}

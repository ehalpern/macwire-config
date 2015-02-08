package macwire.config

import com.softwaremill.macwire.MacwireMacros._
import com.softwaremill.macwire.Tagging._
import com.typesafe.config.ConfigFactory

import org.specs2.mutable.Specification
import ConfigWiring._


/**
 */
class ConfigValueTypesSpec extends Specification with ConfigWiring.Module
{
  import ConfigWiring._ // import the generated config property tags

  class StringTest(val value: String @@ `types.string`)
  class BooleanTest(val value: Boolean @@ `types.boolean`)
  class IntTest(val value: Int @@ `types.int`)
  class DoubleTest(val value: Double @@ `types.double`)
  class StringListTest(val value: Seq[String] @@ `types.stringList`)
  class BooleanListTest(val value: Seq[Boolean] @@ `types.booleanList`)
  class IntListTest(val value: Seq[Int] @@ `types.intList`)
  class DoubleListTest(val value: Seq[Double] @@ `types.doubleList`)

  lazy val st = wire[StringTest]
  lazy val bt0 = wire[BooleanTest]
  lazy val it = wire[IntTest]
  lazy val dt = wire[DoubleTest]
  lazy val slt = wire[StringListTest]
  lazy val blt = wire[BooleanListTest]
  lazy val ilt = wire[IntListTest]
  lazy val dlt = wire[DoubleListTest]

  "The config injector" should {
    "read a String" in {
      st.value mustEqual "test"
    }
    "read a Boolean" in {
      bt0.value mustEqual false
    }
    "read an Int" in {
      it.value mustEqual 1
    }
    "read a Double" in {
      dt.value mustEqual 1.1
    }
    "read a String List" in {
      slt.value mustEqual Seq("one", "two", "three")
    }
    "read a Boolean List" in {
      blt.value mustEqual Seq(true, false, true)
    }
    "read an Int List" in {
      ilt.value mustEqual Seq(1, 2, 3)
    }
    "read a Double List" in {
      dlt.value mustEqual Seq(1.1, 2.2, 3.3)
    }
  }
}


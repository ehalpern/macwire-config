package macwire.config

import com.softwaremill.macwire.MacwireMacros._
import com.softwaremill.macwire.Tagging._
import org.specs2.mutable.Specification

/**
 */
class ConfigValueTypesSpec extends Specification with ConfigWiring.Module
{ // import the generated config property tags
  import ConfigWiring._
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
    "inject a String" in {
      st.value mustEqual "test"
    }
    "inject a Boolean" in {
      bt0.value mustEqual false
    }
    "inject an Int" in {
      it.value mustEqual 1
    }
    "inject a Double" in {
      dt.value mustEqual 1.1
    }
    "inject a String List" in {
      slt.value mustEqual Seq("one", "two", "three")
    }
    "inject a Boolean List" in {
      blt.value mustEqual Seq(true, false, true)
    }
    "inject an Int List" in {
      ilt.value mustEqual Seq(1, 2, 3)
    }
    "inject a Double List" in {
      dlt.value mustEqual Seq(1.1, 2.2, 3.3)
    }
  }
}


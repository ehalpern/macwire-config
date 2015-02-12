name := "macwire-config-example"

version := "SNAPSHOT"

organization := "com.github.ehalpern"

scalaVersion := "2.11.5"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

// Add external conf directory to the classpath
unmanagedClasspath in Compile += sourceDirectory.value / "main" / "resources"

unmanagedClasspath in Test    += sourceDirectory.value / "test" / "resources"

val ParadiseVersion = "2.0.1"
addCompilerPlugin("org.scalamacros" % "paradise_2.11.5" % ParadiseVersion)

resolvers ++= Seq(
  Resolver.typesafeRepo("releases"),
  Resolver.sonatypeRepo("releases")
)

{
  val MacwireVersion = "0.8.0"
  val ScalaTestVersion = "2.2.1"
  libraryDependencies ++= Seq(
    "com.github.ehalpern" %% "macwire-config-macros" % "SNAPSHOT",
    "com.softwaremill.macwire" %% "macros" % MacwireVersion,
    "com.softwaremill.macwire" %% "runtime" % MacwireVersion,
    //-------------------------------------------------------------------------
    "org.specs2" %% "specs2-core" % "2.4.15" % "test"
  )
}

name := "macwire-config"

version := "SNAPSHOT"

organization := "ehalpern"

scalaVersion := "2.11.5"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

// Add external conf directory to the classpath
unmanagedClasspath in Test += baseDirectory.value / "conf"

unmanagedClasspath in Runtime += baseDirectory.value / "conf"

resolvers ++= Seq(
  "typesafe" at "http://repo.typesafe.com/typesafe/repo/",
  "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases"
)

//"Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",

addCompilerPlugin("org.scalamacros" % "paradise" % "2.0.1" cross CrossVersion.full)

{
  val MacwireVersion = "0.8.0"
  val Log4jVersion = "2.1"
  val ScalaTestVersion = "2.2.1"
  libraryDependencies ++= Seq(
    "com.softwaremill.macwire" %% "macros" % MacwireVersion,
    "com.softwaremill.macwire" %% "runtime" % MacwireVersion,
    "com.typesafe" % "config" % "1.2.1",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
    "org.apache.logging.log4j" % "log4j-api" % Log4jVersion,
    "org.apache.logging.log4j" % "log4j-core" % Log4jVersion,
    "org.apache.logging.log4j" % "log4j-slf4j-impl" % Log4jVersion,
    //-------------------------------------------------------------------------
    "org.scalatest" %% "scalatest" % ScalaTestVersion % "test"
  )
}

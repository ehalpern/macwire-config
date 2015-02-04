import sbt.Keys._
import sbt._

object BuildSettings
{
  val paradiseVersion = "2.0.1"
  val buildSettings = Defaults.coreDefaultSettings ++ Seq(
    organization := "ehalpern",
    version := "1.0.0",
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Ymacro-debug-lite"),
    scalaVersion := "2.11.5",
    resolvers += Resolver.sonatypeRepo("snapshots"),
    resolvers += Resolver.sonatypeRepo("releases"),
    resolvers += "Typesafe" at "http://repo.typesafe.com/typesafe/repo/",
    // Add external conf directory to the classpath
    addCompilerPlugin("org.scalamacros" % "paradise_2.11.5" % paradiseVersion)
  )
}

object MultiBuild extends Build
{
  import BuildSettings._

  lazy val root: Project = Project(
    "root",
    file("."),
    settings = buildSettings ++ Seq(
      run <<= run in Compile in core
    )
  ) aggregate(macros, core)

  lazy val macros: Project = Project(
    "macros",
    file("macros"),
    settings = buildSettings ++ Seq(
      libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-reflect" % _),
      libraryDependencies ++= Seq(
        "com.typesafe" % "config" % "1.2.1",
        // Tests
        "org.specs2" %% "specs2-core" % "2.4.15" % "test"
      ),
      unmanagedClasspath in Compile += baseDirectory.value / ".." / "core" / "main" / "resources"
    )
  )

  val MacwireVersion = "0.8.0"
  val Log4jVersion = "2.1"
  val ScalaTestVersion = "2.2.1"

  lazy val core: Project = Project(
    "core",
    file("core"),
    settings = buildSettings ++ Seq(
      libraryDependencies ++= Seq(
        "org.scalamacros" % "paradise_2.11.5" % paradiseVersion,
        "org.scala-lang" % "scala-library" % scalaVersion.value,
        "org.scala-lang" % "scala-compiler"  % scalaVersion.value,
        "com.softwaremill.macwire" %% "macros" % MacwireVersion,
        "com.softwaremill.macwire" %% "runtime" % MacwireVersion,
        "com.typesafe" % "config" % "1.2.1",
        "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
        "org.apache.logging.log4j" % "log4j-api" % Log4jVersion,
        "org.apache.logging.log4j" % "log4j-core" % Log4jVersion,
        "org.apache.logging.log4j" % "log4j-slf4j-impl" % Log4jVersion,
        //-------------------------------------------------------------------------
        "org.scalatest" %% "scalatest" % ScalaTestVersion % "test"
      ),
      unmanagedClasspath in Compile += baseDirectory.value / "src" / "main" / "resources"
    )
  ) dependsOn(macros)
}

import sbt.Keys._
import sbt._

object BuildSettings
{
  val paradiseVersion = "2.0.1"
  val buildSettings = Defaults.coreDefaultSettings ++ Seq(
    organization := "com.github.ehalpern",
    isSnapshot := true,
    version := "SNAPSHOT",
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature"),
    scalaVersion := "2.11.5",
    resolvers ++= Seq(
      Resolver.sonatypeRepo("snapshots"),
      Resolver.sonatypeRepo("releases"),
      Resolver.typesafeRepo("releases")
    ),
    addCompilerPlugin("org.scalamacros" % "paradise_2.11.5" % paradiseVersion)
  )
}

object MainBuild extends Build
{
  import BuildSettings._

  lazy val root: Project = Project(
    "root",
    file("."),
    settings = buildSettings
  ) aggregate(macros)

  val MacwireVersion = "0.8.0"
  val Slf4jVersion = "1.7.10"
  val Specs2Version = "2.4.15"

  lazy val macros: Project = Project(
    "macwire-config-macros",
    file("macros"),
    settings = buildSettings ++ Seq(
      version := "SNAPSHOT",
      libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-reflect" % _),
      libraryDependencies ++= Seq(
        "com.typesafe" % "config" % "1.2.1",
        "org.scalamacros" % "paradise_2.11.5" % paradiseVersion,
        "org.scala-lang" % "scala-library" % scalaVersion.value,
        "org.scala-lang" % "scala-compiler"  % scalaVersion.value,
        "com.softwaremill.macwire" %% "macros" % MacwireVersion,
        "com.softwaremill.macwire" %% "runtime" % MacwireVersion,
        "org.slf4j" % "slf4j-api" % Slf4jVersion,
        "org.slf4j" % "slf4j-simple" % Slf4jVersion,
        // Tests
        "org.specs2" %% "specs2-core" % "2.4.15" % "test"
      ),
      unmanagedClasspath in Compile += baseDirectory.value / "src" / "test" / "resources"
    )
  )

  lazy val example: Project = Project(
    "macwire-config-example",
    file("example"),
    settings = buildSettings
  )
}

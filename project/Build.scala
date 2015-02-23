import com.typesafe.sbt.pgp.PgpKeys
import sbt.Keys._
import sbt._

object PublishSettings
{
  import sbtrelease.ReleasePlugin.releaseSettings
  import sbtrelease.ReleasePlugin.ReleaseKeys._

  val publishSettings = releaseSettings ++ Seq(
    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (isSnapshot.value)
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases" at nexus + "service/local/staging/deploy/maven2")
    },
    publishMavenStyle := true,
    publishArtifact in Test := false,
    publishArtifactsAction := PgpKeys.publishSigned.value,
    pomIncludeRepository := { _ => false },
    pomExtra := (
      <url>https://github.com/ehalpern/macwire-config</url>
      <licenses>
        <license>
          <name>BSD-style</name>
          <url>http://www.opensource.org/licenses/bsd-license.php</url>
          <distribution>repo</distribution>
        </license>
      </licenses>
      <scm>
        <url>git@github.com:ehalpern/macwire-config.git</url>
        <connection>scm:git:git@github.com:ehalpern/macwire-config.git</connection>
      </scm>
      <developers>
        <developer>
          <id>ehalpern</id>
          <name>Eric Halpern</name>
          <url>https://github.com/ehalpern</url>
        </developer>
      </developers>
    )
  )
}

object BuildSettings
{
  import PublishSettings._

  val paradiseVersion = "2.0.1"
  val buildSettings = Defaults.coreDefaultSettings ++ publishSettings ++ Seq(
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
    settings = buildSettings ++ Seq(
      publishArtifact := false
    )
  ) aggregate(macros)

  val MacwireVersion = "0.8.0"
  val Slf4jVersion = "1.7.10"
  val Specs2Version = "2.4.15"

  lazy val macros: Project = Project(
    "macwire-config",
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

val scala3Version = "3.6.2"

lazy val root = project
  .in(file("."))
  .settings(
    name := "Pong",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies += "org.jline" % "jline" % "3.23.0"
  )

name := "drawing"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
    "org.scala-lang.modules" % "scala-parser-combinators_2.11" % "1.0.4",
    "org.typelevel" %% "cats" % "0.6.0",
    "org.scalatest" %% "scalatest" % "2.2.2" % "test"
)

assemblyJarName in assembly := "drawing.jar"

test in assembly := {}

mainClass in assembly := Some("App")

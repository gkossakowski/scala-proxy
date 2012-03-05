name := "scala-reflection"

organization := "EPFL"

version := "0.1"

scalaVersion := "2.10.0-M1"

resolvers += ScalaToolsSnapshots

libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-compiler" % _)

//we are using virtual pattern matcher as it's seems to be the only way to have
//patterns workin with abstract types
scalacOptions ++= Seq("-deprecation", "-unchecked", "-Yvirtpatmat", "-Xexperimental")

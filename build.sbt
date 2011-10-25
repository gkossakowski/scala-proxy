name := "scala-reflection"

organization := "EPFL"

version := "0.1"

scalaVersion := "2.10.0-SNAPSHOT"

resolvers += ScalaToolsSnapshots

libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-compiler" % _)

scalacOptions ++= Seq("-deprecation", "-unchecked")

name := "scala-reflection"

organization := "EPFL"

version := "0.1"

scalaVersion := "2.10.0-SNAPSHOT"

resolvers += ScalaToolsSnapshots

libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-compiler" % _)

//we are using virtual pattern matcher as it's seems to be the only way to have
//patterns workin with abstract types
scalacOptions ++= Seq("-deprecation", "-unchecked", "-Yvirtpatmat", "-Xexperimental")

//uncomment this one if you want to use local build of scala
scalaHome := Some(file("/Users/grek/scala/scala-trunk/build/pack"))

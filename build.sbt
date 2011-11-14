name := "scala-reflection"

organization := "EPFL"

version := "0.1"

//--- Local Scala

scalaHome <<= baseDirectory { f =>
  val props = new java.util.Properties()
  IO.load(props, f / "local.properties")
  val x = props.getProperty("scala.home")
  if (x == null)
    sys.error("Did you forget to set scala.home property in local.properties file?")
  else Some(file(x))
}

scalaVersion := "2.10.0-SNAPSHOT"

resolvers += ScalaToolsSnapshots

libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-compiler" % _)

//we are using virtual pattern matcher as it's seems to be the only way to have
//patterns workin with abstract types
scalacOptions ++= Seq("-deprecation", "-unchecked", "-Yvirtpatmat", "-Xexperimental")

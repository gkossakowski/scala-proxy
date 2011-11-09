package test;

import scala.reflect.mirror._
import scala.reflect.proxy._

trait Outer {
  
  trait Foo {
    def foo(x: Int, y: String): Unit
    def bar_!(): Unit
    var someVar: Int
  }
}

/**
 * Test of ScalaProxy. Run using trunk version of compiler.
 * Type
 * scalac Proxy.scala && scala test.Test
 */
object Test extends App with Outer {
  
  val h = new InvocationHandler {
    def invoke(proxy: AnyRef, m: Symbol, args: Array[AnyRef]): AnyRef = {
      //TODO: if method has multiple argument list resultType will be MethodType again
      //so we need recursive extraction below
      val MethodType(params, resultType) = m.info
      val paramsStr = {
        val names = params.map(_.name.toString)
        val types = params.map(_.info.toString)
        ((names zip args zip types) map { case ((n, a), t) => n + ": " + t + " = " + a }).mkString("(", ", ", ")")
      }
      println("called " + m.decodedName + paramsStr + ": " + resultType)
      null
    }
  }
  
  val p = ScalaProxy[Foo](h)
  p.foo(1, "str")
  p.bar_!()
  //prints:
  //called foo(x: Int = 1, y: String = str): Unit
  //called bar_!(): Unit
}

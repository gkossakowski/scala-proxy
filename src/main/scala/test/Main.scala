package test;

import scala.reflect.runtime.universe._
import scala.reflect.proxy._

trait Outer {
  trait Foo {
    def foo(x: Int, y: String): Unit
    def bar_!(): Unit
    def concrete: Unit = println("I am Foo concrete.")
    def concrete2(x: Int) = { foo(x, "indirect"); x }
    var someVar: Int = 1
  }
}

trait Bar {
  var someVar: Int = 1
  def concrete: Unit = println("I am Bar concrete.")
}

/**
 * Test of ScalaProxy. Run using trunk version of compiler.
 */
object Test extends App with Outer {
  val h = new InvocationHandler {
    def invoke(proxy: AnyRef, m: Symbol, args: Array[AnyRef]): AnyRef = {
      m.typeSignature match {
        case NullaryMethodType(resultType) =>
          println("called " + m.name.decoded + ": " + resultType)
          scala.reflect.Defaults(resultType)
        //TODO: if method has multiple argument list resultType will be MethodType again
        //so we need recursive extraction below
        case MethodType(params, resultType) =>
          val paramsStr = {
            val names = params.map(_.name.toString)
            val types = params.map(_.typeSignature.toString)
            ((names zip args zip types) map { case ((n, a), t) => n + ": " + t + " = " + a }).mkString("(", ", ", ")")
          }
          println("called " + m.name.decoded + paramsStr + ": " + resultType)
          scala.reflect.Defaults(resultType)
      }
    }
  }
  
  println("ScalaProxy[Foo]")
  val p1 = ScalaProxy[Foo](h)
  p1.foo(1, "str")
  p1.bar_!()
  p1.someVar
  p1.concrete
  println("ScalaProxy[Bar]")
  val p2 = ScalaProxy[Bar](h)
  p2.someVar
  p2.concrete
  println("ScalaAbstractProxy[Foo]")
  val p3 = ScalaAbstractProxy[Foo](h)
  p3.foo(1, "str")
  p3.bar_!()
  p3.someVar
  p3.concrete
  println(p3.concrete2(10))
  println("ScalaAbstractProxy[Bar]")
  val p4 = ScalaAbstractProxy[Bar](h)
  p4.someVar
  p4.concrete
}

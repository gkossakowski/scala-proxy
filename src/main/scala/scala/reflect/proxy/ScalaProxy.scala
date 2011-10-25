package scala.reflect.proxy;

import java.lang.{reflect => jreflect}
import scala.reflect.mirror._

/**
 * Scala counterpart of java.lang.reflect.InvocationHandler
 */
trait InvocationHandler {
  def invoke(proxy: AnyRef, method: Symbol, args: Array[AnyRef]): AnyRef
}

object ScalaProxy {
  def apply[T <: AnyRef](handler: InvocationHandler)(implicit manifest: Manifest[T]): T = {
    val compilerMirror = scala.reflect.runtime.Mirror
    println(compilerMirror)
    println(compilerMirror.getClass())
    val clazz = manifest.erasure
    println(compilerMirror.classToScala(clazz).fullName)
    val h = new ScalaHandler(handler)
    jreflect.Proxy.newProxyInstance(clazz.getClassLoader(), Array(clazz), h).asInstanceOf[T]
  }
  private class ScalaHandler(handler: InvocationHandler) extends jreflect.InvocationHandler {
    def invoke(proxy: Object, method: jreflect.Method, args: Array[Object]): Object = {
      val m = methodToSymbol(method)
      handler.invoke(proxy, m, if (args != null) args else Array.empty)
    }
    /**
      * Maps java.lang.reflect.Method to scala.reflect.api.Symbols.Symbol
      * corresponding to that method.
      *
      * TODO: Improved (complete) implementation of this method should
      *       be in Mirror class similarly to classToType method defined there.
      */
    private def methodToSymbol(m: jreflect.Method): Symbol = {
      val ClassInfoType(_, decls, _) = classToSymbol(m.getDeclaringClass).info
      val jname = m.getName
      //TODO: handle overloaded defs
      decls.find(_.encodedName == jname).get
    }
  }
}

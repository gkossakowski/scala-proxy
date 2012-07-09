package scala.reflect.proxy;

import java.lang.{reflect => jreflect}
import scala.reflect.{ClassTag, classTag}
import scala.reflect.runtime.universe._
import scala.reflect.runtime.{currentMirror => cm}

/**
 * Scala counterpart of java.lang.reflect.InvocationHandler
 */
trait InvocationHandler {
  def invoke(proxy: AnyRef, method: Symbol, args: Array[AnyRef]): AnyRef
}

object ScalaAbstractProxy {
  def apply[T <: AnyRef](handler: InvocationHandler)(implicit manifest: Manifest[T]): T = {
    val clazz = manifest.erasure
    val implClazz = implClass(clazz)
    val implTypeSignature = implClazz.companionSymbol.typeSignature
    lazy val p : T = ScalaProxy[T](new InvocationHandler {
      def invoke(proxy: AnyRef, method: Symbol, args: Array[AnyRef]): AnyRef = {
        val methodImpl = implTypeSignature.declaration(method.name)
        if (methodImpl != NoSymbol) {
          (cm.reflect(p).reflectMethod(methodImpl asMethodSymbol)(p::args.toList : _*)).asInstanceOf[AnyRef]
        } else handler.invoke(proxy, method, args)
      }
    })
    p
  }  
  def implClass(clazz: Class[_]) = {
    val implClass = Class.forName(clazz.getName + "$class")
    cm.classSymbol(implClass)
  }
}

object ScalaProxy {
  def apply[T <: AnyRef](handler: InvocationHandler)(implicit manifest: Manifest[T]): T = {
    val clazz = manifest.erasure
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
      val ClassInfoType(_, decls, _) = cm.classSymbol(m.getDeclaringClass).typeSignature
      val jname = m.getName
      //TODO: handle overloaded defs
      decls.find(_.name.encoded == jname).get
    }
  }
}

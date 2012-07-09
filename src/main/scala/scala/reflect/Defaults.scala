package scala.reflect;

import scala.reflect.runtime.universe._
import definitions._

object Defaults {
  def apply(x: Type): AnyRef = {
    // workaround SI-5959
    if (x.typeSymbol == UnitTpe.typeSymbol) (().asInstanceOf[AnyRef])
    else if (x.typeSymbol == IntTpe.typeSymbol)  (0: java.lang.Integer)
    else ???
  }
}

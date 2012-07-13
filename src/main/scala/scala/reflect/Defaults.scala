package scala.reflect;

import scala.reflect.runtime.universe._
import definitions._

object Defaults {
  def apply(x: Type): AnyRef = {
    if (x =:= UnitTpe) (().asInstanceOf[AnyRef])
    else if (x =:= IntTpe)  (0: java.lang.Integer)
    else ???
  }
}

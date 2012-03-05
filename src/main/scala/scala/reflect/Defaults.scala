package scala.reflect;

import java.lang.{reflect => jreflect}
import scala.reflect.mirror._

object Defaults {
  import definitions._
  private val IntTpe = IntClass.asType
  private val UnitTpe = UnitClass.asType
  def apply(x: scala.reflect.mirror.Type): AnyRef = x match {
    case IntTpe => 0: java.lang.Integer
    case UnitTpe => ().asInstanceOf[AnyRef]
  }
}

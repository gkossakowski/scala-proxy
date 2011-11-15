package scala.reflect;

import java.lang.{reflect => jreflect}
import scala.reflect.mirror._

object Defaults {
  import definitions._
  private val IntTpe = IntClass.tpe
  private val UnitTpe = UnitClass.tpe
  def apply(x: scala.reflect.mirror.Type): AnyRef = x match {
    case IntTpe => 0: java.lang.Integer
    case UnitTpe => ().asInstanceOf[AnyRef]
  }
}
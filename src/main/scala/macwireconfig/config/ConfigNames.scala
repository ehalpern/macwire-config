package macwireconfig.config

import scala.reflect.macros.whitebox.Context
import scala.language.experimental.macros
import scala.annotation.StaticAnnotation
import com.softwaremill.macwire.Tagging._


@ConfigNamesGenerator object ConfigNames
/*
{
  trait `ping.response`
}
*/

class ConfigNamesGenerator extends StaticAnnotation {
  def macroTransform(annottees: Any*) = macro ConfigNamesImpl.impl
}

object ConfigNamesImpl {
  def impl(c: Context)(annottees: c.Expr[Any]*): c.Expr[Any] =
  {
    import c.universe._

    def modifiedClass(classDecl: ClassDef): c.Expr[Any] =
    {
      c.Expr(q"trait `ping.response`")
    }

    annottees.map(_.tree) match {
      case (classDecl: ClassDef) :: Nil => modifiedClass(classDecl)
      case _ => c.abort(c.enclosingPosition, "Annotation can only be used on a trait or class")
    }
  }
}

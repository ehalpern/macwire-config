package macwire.config

import scala.annotation.StaticAnnotation
import scala.language.experimental.macros
import scala.reflect.macros.whitebox.Context

class NamesGenerator extends StaticAnnotation {
  def macroTransform(annottees: Any*) = macro NamesGenerator.impl
}

object NamesGenerator {
  def impl(c: Context)(annottees: c.Expr[Any]*): c.Expr[Any] =
  {
    import c.universe._

    val decl = annottees.map(_.tree)
    decl match {
      case q"object $name extends ..$parents { ..$body }" :: Nil =>
        val result = q"""
          object $name extends ..$parents
          {
            trait `ping.response` {}

            ..$body
          }
        """
        c.Expr[Any](result)
      case _ => c.abort(c.enclosingPosition, "Annotation must be applied to an object, not [" + decl + "]")

    }
  }
}

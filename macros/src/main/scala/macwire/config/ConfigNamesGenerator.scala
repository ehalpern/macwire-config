package macwire.config

import scala.annotation.StaticAnnotation
import scala.language.experimental.macros
import scala.reflect.macros.whitebox.Context

class ConfigNamesGenerator extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro ConfigNamesGenerator.impl
}

object ConfigNamesGenerator
{
  def impl(c: Context)(annottees: c.Expr[Any]*): c.Expr[Any] =
  {
    import c.universe._

    val properties = ConfigReader.readConfig

    val nameTypes = properties.map { case (name, value) =>
      val tname = TypeName(escapeName(name))
      q"""trait $tname"""
    }

    val traits = nameTypes.foldLeft[Tree](q"")((lines, nextLine) => {
      q"""
        ..$lines
        $nextLine
        """
    })

    val decl = annottees.map(_.tree)
    decl match {
      case q"$_ trait $name extends ..$parents { ..$body }" :: Nil =>
        val result = q"""
          trait $name extends ..$parents
          {
            ..$traits

            ..$body
          }
        """
        //System.out.println("Result: " + result)
        c.Expr[Any](result)
      case _ => c.abort(c.enclosingPosition, "Annotation must be applied to a trait, not [" + decl + "]")
    }
  }
}

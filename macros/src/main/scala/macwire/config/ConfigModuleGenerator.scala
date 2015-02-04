package macwire.config

import scala.annotation.StaticAnnotation
import scala.language.experimental.macros
import scala.reflect.macros.whitebox.Context


class ConfigModuleGenerator extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro ConfigModuleGenerator.impl
}

object ConfigModuleGenerator {
  def impl(c: Context)(annottees: c.Expr[Any]*): c.Expr[Any] =
  {
    import c.universe._

    val properties = ConfigReader.readConfig

    val bindingAssignments  = properties.map { case (name, value) =>
      val constant = Constant(value)
      val valName = TermName(dotsToUnderscores(name))
      val tagName = TypeName(escapeName(name))

      q"""val $valName = $constant.taggedWith[$tagName];"""
    }

    val bindings = bindingAssignments.foldLeft[Tree](q"")((lines, nextLine) => {
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
            import com.softwaremill.macwire.Tagging._;

            ..$bindings

            ..$body
          }
        """
        c.Expr[Any](result)
      case _ => c.abort(c.enclosingPosition, "Annotation must be applied to a class, not [" + decl + "]")

    }
  }
}




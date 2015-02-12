package macwire.config

import scala.annotation.StaticAnnotation
import scala.language.experimental.macros
import scala.reflect.macros.whitebox.Context

class ConfigWiringGenerator extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro ConfigWiringGenerator.impl
}

object ConfigWiringGenerator {
  def impl(c: Context)(annottees: c.Expr[Any]*): c.Expr[Any] =
  {
    import c.universe._
    import Util._

    val properties = ConfigReader.readConfig(this.getClass.getClassLoader)
    
    val tags = properties.map { case (name, typeTag) =>
      val tname = TypeName(escapeName(name))
      q"""trait $tname"""
    }.foldLeft[Tree](q"")((lines, nextLine) => {
      q"""
      ..$lines
      $nextLine
      """
    })

    val wiring = properties.map { case (name, ttag) =>
      val valName = TermName(dotsToUnderscores(name))
      val tagName = TypeName(escapeName(name))

      import scala.reflect.runtime.universe.typeTag // disambiguate from c.universe

      val getCall = ttag match {
        case t if t == typeTag[String]       => q"""config.getString($name)"""
        case t if t == typeTag[Boolean]      => q"""config.getBoolean($name)"""
        case t if t == typeTag[Int]          => q"""config.getInt($name)"""
        case t if t == typeTag[Double]       => q"""config.getDouble($name)"""
        case t if t == typeTag[Seq[String]]  => q"""config.getStringList($name).toSeq"""
        case t if t == typeTag[Seq[Boolean]] => q"""config.getBooleanList($name).map(_.booleanValue).toSeq"""
        case t if t == typeTag[Seq[Int]]     => q"""config.getIntList($name).map(_.intValue).toSeq"""
        case t if t == typeTag[Seq[Double]]  => q"""config.getDoubleList($name).map(_.doubleValue).toSeq"""
        case t => c.abort(c.enclosingPosition, s"Config property $name has unexpected element type ${t}")
      }
      q"""def $valName = $getCall.taggedWith[$tagName];"""
    }.foldLeft[Tree](q"")((lines, nextLine) => {
      q"""
        ..$lines
        $nextLine
      """
    })

    val decl = annottees.map(_.tree) 
    
    decl match {
      case q"$mods object $name extends ..$parents { ..$body }" :: Nil =>
        val result = q"""
          $mods object $name extends ..$parents
          {
            ..$tags

            trait Module {
              import scala.collection.JavaConversions._
              import com.softwaremill.macwire.Tagging._
              import com.typesafe.config.{Config, ConfigFactory}

              val config: Config = ConfigFactory.load
              ..$wiring
            }

            ..$body
          }
        """
        System.out.println(result)
        val e = c.Expr[Any](result)
        e
      case _ => c.abort(c.enclosingPosition, "Annotation must be applied to an object, not [" + decl + "]")
    }
  }
}




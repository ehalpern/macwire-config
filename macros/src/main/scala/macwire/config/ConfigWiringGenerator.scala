package macwire.config

import com.typesafe.config.ConfigFactory

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
    
    val tags = properties.map { case (name, value) =>
      val tname = TypeName(escapeName(name))
      q"""trait $tname"""
    }.foldLeft[Tree](q"")((lines, nextLine) => {
      q"""
      ..$lines
      $nextLine
      """
    })

    val wiring = properties.map { case (name, value) =>
      val valName = TermName(dotsToUnderscores(name))
      val tagName = TypeName(escapeName(name))

      val getCall = value match {
        case v: String  => q"""config.getString($name)"""
        case v: Boolean => q"""config.getBoolean($name)"""
        case v: Int     => q"""config.getInt($name)"""
        case v: Double  => q"""config.getDouble($name)"""
        case l: Seq[_] => l.headOption match {
          case Some(h:String)  => q"""config.getStringList($name).toSeq"""
          case Some(h:Boolean) => q"""config.getBooleanList($name).map(_.booleanValue).toSeq"""
          case Some(h:Int)     => q"""config.getIntList($name).map(_.intValue).toSeq"""
          case Some(h:Double)  => q"""config.getDoubleList($name).map(_.doubleValue).toSeq"""
          case Some(h)         => c.abort(c.enclosingPosition, s"Config property $name has unexpected element type ${h.getClass}")
          case None            => q"""config.getStringList($name).toSeq"""
        }
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
        c.Expr[Any](result)
      case _ => c.abort(c.enclosingPosition, "Annotation must be applied to an object, not [" + decl + "]")
    }
  }
}




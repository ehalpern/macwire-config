package twine.macwire.config

import java.net.URLClassLoader

/**
 * Created by eric on 2/6/15.
 */
object Util {
  // Simulates surrounding an identifier with backquotes
  def escapeName(name: String) : String = {
    name.replace(".", "$u002E")
  }

  def dotsToUnderscores(name: String) : String = {
    name.replace(".", "_")
  }

  def printClasspath() {
    val cl = ClassLoader.getSystemClassLoader()
    val urls = cl.asInstanceOf[URLClassLoader].getURLs

    for (url <- urls) {
      System.out.println(url.getFile());
    }
    System.out.println("")
  }
}

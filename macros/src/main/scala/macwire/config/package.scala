package macwire

import java.net.URLClassLoader

/**
 * Created by eric on 2/3/15.
 */
package object config {
  def loadConfig = {

  }

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

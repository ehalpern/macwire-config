package macwire.app

import com.softwaremill.macwire.Tagging._
import macwire.config.ConfigNames._

/**
 */
trait PingService
{
  def ping(str: String): Unit
}

class PingServiceImpl(prefix: String @@ `ping.response`) extends PingService
{
  def ping(str: String) {
    System.out.println(prefix + ": " + str)
  }
}

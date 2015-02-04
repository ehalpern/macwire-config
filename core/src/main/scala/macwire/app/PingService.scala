package macwire.app

import com.softwaremill.macwire.Tagging._
import macwire.config._

/**
 */
trait PingService
{
  def ping(str: String): Unit
}

class PingServiceImpl(
  prefix: String @@ `ping.response`,
  userName: String @@ `user.name`
) extends PingService
{
  def ping(str: String) {
    System.out.println(prefix + ": " + str + ": " + userName)
  }
}

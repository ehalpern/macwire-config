package macwire.config.example

import com.softwaremill.macwire.Tagging._
import ConfigWiring._

/**
 */
trait PingService
{
  def ping(str: String): Unit
}

class PingServiceImpl(
  val prefix: String @@ `ping.response`,
  val userName: String @@ `user.name`
) extends PingService
{
  def ping(str: String) {
    System.out.println(prefix + ": " + str + ": " + userName)
  }
}

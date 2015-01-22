package macwireconfig.app

import com.softwaremill.macwire.Tagging._
import macwireconfig.config.ConfigNames._


trait MainModule
{
  import com.softwaremill.macwire.MacwireMacros._

  lazy val pingService = wire[PingServiceImpl]
  lazy val pingResponse = "Ping Back: ".taggedWith[`ping.response`]
}

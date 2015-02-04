package macwire.app

import macwire.config.ConfigModule

trait MainModule extends ConfigModule
{
  import com.softwaremill.macwire.MacwireMacros._

  lazy val pingService = wire[PingServiceImpl]
}

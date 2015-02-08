package macwire.config.example

trait MainModule extends ConfigWiring.Module
{
  import com.softwaremill.macwire.MacwireMacros._

  lazy val pingService = wire[PingServiceImpl]
}

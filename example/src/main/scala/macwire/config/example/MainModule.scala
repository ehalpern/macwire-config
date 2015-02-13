package macwire.config.example

import com.softwaremill.macwire._

trait MainModule extends Macwire with Config.Wiring
{
  lazy val searchService = wire[RemoteSearchService]
}

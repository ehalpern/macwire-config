package macwire.config.example

import com.softwaremill.macwire._

trait MainModule extends Macwire with ConfigWiring.Module
{
  lazy val searchService = wire[RemoteSearchService]
}

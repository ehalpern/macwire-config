package macwire.config.example

import com.softwaremill.macwire.Tagging._
import Config.Tags._

/**
 */
trait SearchService
{
  def query(query: String): Seq[String]
}

class RemoteSearchService(
  val host: String @@ `search.host`,
  val port: Int @@ `search.port`
) extends SearchService
{
  def query(query: String): Seq[String] = {
    System.out.println(s"""GET $host:$port?query=$query""")
    Seq(
      "result 1",
      "result 2",
      "result 3"
    )
  }
}

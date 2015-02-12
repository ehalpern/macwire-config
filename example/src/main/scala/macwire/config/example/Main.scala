package macwire.config.example

object Main extends App with MainModule
{
  searchService.query("foo")
}

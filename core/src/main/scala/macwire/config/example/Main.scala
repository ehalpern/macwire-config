package macwire.config.example

object Main extends App with MainModule
{
  pingService.ping("foo")
}

package macwire.app

object Main extends App with MainModule
{
  pingService.ping("foo")
}

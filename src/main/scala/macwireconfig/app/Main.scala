package macwireconfig.app

object Main extends App with MainModule
{
  pingService.ping("foo")
}

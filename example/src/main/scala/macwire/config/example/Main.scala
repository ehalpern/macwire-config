package macwire.config.example

object Main extends App with MainModule
{
  import System.out.println

  println(s"""kitchenSinkService.getConfig() returns ${kitchenSinkService.configString}""")
}

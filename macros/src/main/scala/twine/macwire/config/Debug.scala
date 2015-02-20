package twine.macwire.config

private[config] class Debug
{
  private val enabled = System.getProperty("macwire.config.debug") != null
  private var ident = 0

  def apply(msg: => String) {
    if (enabled) {
      val prefix = "   " * ident
      println(s"$prefix[debug] $msg")
    }
  }

  def indent[T](msg: => String)(block: => T): T = {
    apply(msg)
    beginBlock()
    try {
      block
    } finally {
      endBlock()
    }
  }

  def beginBlock() {
    ident += 1
  }

  def endBlock() {
    ident -= 1
  }
}


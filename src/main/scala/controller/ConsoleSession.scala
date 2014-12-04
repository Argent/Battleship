package controller


class ConsoleSession extends Session {
  override def initSession(): Unit = {

    println(boards(0))
    val b = boards(0)
    println(b.ships)

    b.ships.foreach(x => {
      x.foreach(y => { print((y == null : "W" ? "S") + " ")})
      println("")
    }

    )
  }

  override def runSession(): Unit = {

  }
}
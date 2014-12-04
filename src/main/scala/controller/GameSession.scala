package controller


trait GameSession extends Session {

  abstract override def initSession(): Unit = {
    println("SCHIFFE VERSENKEN")
    println("-------------------")
    players(0) = new HumanPlayer()
    players(1) = new AIPlayer()

    super.initSession()
  }

  override def runSession(): Unit = ???
}
package controller

import helper.ConsoleHelper
import models.Ship
import models.ShipTypes.ShipTypes


trait GameSession extends Session {

  abstract override def initSession(ships: List[(ShipTypes, Int)], currentPlayer: Int): Unit = {

    if(players(0) == null) {
      players(0) = new HumanPlayer()
      players(1) = new AIPlayer()
    }

    println("Now setting " + ships.head)
    players(currentPlayer).setShip(ships.head._1, boards(currentPlayer))
    println("")
    println("Board of: " + players(currentPlayer).getClass())
    ConsoleHelper.printArray(boards(currentPlayer).ships)

    ships match {
      case (s: ShipTypes, 1) :: Nil if currentPlayer == 1 => runSession(0)
      case (s: ShipTypes, 1) :: Nil                       => initSession(Ship.generateShipSet(), 1)
      case (s: ShipTypes, 1) :: xs                        => initSession(xs, currentPlayer)
      case (s: ShipTypes, count: Int) :: xs               => initSession((s, count - 1) :: xs, currentPlayer)
    }
  }

  override def runSession(currentPlayer: Int): Unit = {
    println("GAME START")
  }
}
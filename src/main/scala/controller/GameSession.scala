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

    println("Wir setzen jetzt ein " + ships.head)
    val shipCoords = ConsoleHelper.getCoordinatesFromConsole("Koordinaten in der Form Buchstabe Zahl Richtung eingeben: ")
    val ship = Ship(ships.head._1, shipCoords._2, shipCoords._1, shipCoords._3)

    ship.setOnBoard(ship.coords, Nil, boards(currentPlayer)) match {
      case Some(b) => super.initSession(ships.tail, currentPlayer)
      case None => {
        println("Fehler beim Setzen, nochmal bitte")
        super.initSession(ships, currentPlayer)
      }
    }
  }

  override def runSession(): Unit = ???
}
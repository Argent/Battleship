package controller

import helper.ConsoleHelper
import models.{Board, Ship}
import models.ShipTypes.ShipTypes


class HumanPlayer extends Player {
  override def setShip(s: ShipTypes, b: Board): Unit = {

    val shipCoords = ConsoleHelper.getCoordinatesFromConsole("Position in der Form Buchstabe Zahl Richtung eingeben: ")
    val ship = Ship(s, shipCoords._2, shipCoords._1, shipCoords._3)


    if(ship.setOnBoard(ship.coords, Nil, b) == None) {
      println("Fehler beim Setzen. Um Schiffe muss ein Abstand von einem Kästchen bestehen.")
      setShip(s, b)
    }
  }
}
package controller

import models.{Direction, Ship, Board}
import models.ShipTypes.ShipTypes

import scala.util.Random

class AIPlayer extends Player {
  override def setShip(s: ShipTypes, b: Board): Unit = {
    val random = new Random()

    val xCoord = random.nextInt(10)
    val yCoord = random.nextInt(10)
    val direction = random.nextInt(4)

    val ship = Ship(s, xCoord, yCoord, direction match {
      case 0 => Direction.E
      case 1 => Direction.N
      case 2 => Direction.S
      case 3 => Direction.W
    })

    if (ship.setOnBoard(ship.coords, Nil, b) == None) {
      println("Fehler beim Setzen. Um Schiffe muss ein Abstand von einem Kästchen bestehen.")
      setShip(s, b)
    }
  }
}
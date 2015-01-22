package controller

import helper.ConsoleHelper
import models.{WaterTypes, Direction, Ship, Board}

import scala.util.Random

class AIPlayer extends Player {
  val random = new Random()

  override def setShip(s: Ship, b: Board): Unit = {

    val xCoord = random.nextInt(10)
    val yCoord = random.nextInt(10)
    val direction = random.nextInt(4)

    s.translateAndRotate(xCoord, yCoord, direction match {
      case 0 => Direction.E
      case 1 => Direction.N
      case 2 => Direction.S
      case 3 => Direction.W
    })

    if (s.setOnBoard(s.coords, Nil, b) == None) {
      println("Fehler beim Setzen. Um Schiffe muss ein Abstand von einem Kästchen bestehen.")
      setShip(s, b)
    }
  }


  override def doTurn(b: Board): (Int, Int) = {
    val coords = (random.nextInt(10), random.nextInt(10))
    if(!(b.shots(coords._1)(coords._2) == WaterTypes.Water)) {
      doTurn(b: Board)
    }
    (coords._1, coords._2)
  }
}
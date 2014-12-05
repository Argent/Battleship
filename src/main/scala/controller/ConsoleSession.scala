package controller

import helper.ConsoleHelper
import models.{ShipTypes, Ship, AircraftCarrier, Direction}


class ConsoleSession extends Session {
  override def initSession(): Unit = {
    val battleship = Ship.createShip(ShipTypes.Battleship, 5, 5, Direction.N)
    val patrol =  Ship.createShip(ShipTypes.PatrolBoat, 5, 7, Direction.N)

    println(battleship.setOnBoard(battleship.coords, Nil, boards(0)))
    println(patrol.setOnBoard(patrol.coords, Nil, boards(0)))

    ConsoleHelper.printArray(boards(0).ships)


/*

    val carrier = Ship.createShip(ShipTypes.AircraftCarrier)
    val patrol = Ship.createShip(ShipTypes.PatrolBoat)






    val b = boards(0)
    val returnvalue1 = carrier.setOnBoard(0, 0, Direction.E)(b)
    println(returnvalue1)

    println(patrol.setOnBoard(0, 2, Direction.N)(b))


    /*val returnvalue = carrier2.setOnBoard(4, 4, Direction.S)(b)
    println(returnvalue)*/


   // ConsoleHelper.printArray(b.ships)
*/

  }

  override def runSession(): Unit = {

  }
}
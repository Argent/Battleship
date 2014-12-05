package controller

import models.{ShipTypes, Ship, AircraftCarrier, Direction}


class ConsoleSession extends Session {
  override def initSession(): Unit = {



    val carrier = Ship.createShip(ShipTypes.AircraftCarrier)






    val b = boards(0)
    val returnvalue1 = carrier.setOnBoard(0, 0, Direction.E)(b)
    println(returnvalue1)

    /*val returnvalue = carrier2.setOnBoard(4, 4, Direction.S)(b)
    println(returnvalue)*/


    b.ships.foreach(x => {
      x.foreach(y => { print((if(y == null) "W" else "S") + " ")})
      println("")
    }

    )
  }

  override def runSession(): Unit = {

  }
}
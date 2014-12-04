package controller

import models.AircraftCarrier
import models.Direction


class ConsoleSession extends Session {
  override def initSession(): Unit = {



    val carrier = new AircraftCarrier()

    val carrier2 = new AircraftCarrier()




    val b = boards(0)
    val returnvalue1 = carrier.setOnBoard(5, 5, Direction.N)(b)
    println(returnvalue1)

    val returnvalue = carrier2.setOnBoard(4, 4, Direction.N)(b)
    println(returnvalue)


    b.ships.foreach(x => {
      x.foreach(y => { print((if(y == null) "W" else "S") + " ")})
      println("")
    }

    )
  }

  override def runSession(): Unit = {

  }
}
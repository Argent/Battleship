package models

import models.Direction.Direction
import models.ShipTypes.ShipTypes


abstract case class Ship(shipform: List[(Int, Int)], x: Int, y: Int, d: Direction) {
  var parts: List[Shippart] = Nil
  var coords = Ship.rotateAndTranslate(d, shipform, x, y)



  override def toString: String = {
    "Hi, I'm a " + this.getClass() + "! My coords are: " + coords.toString
  }


  def setOnBoard(coords: List[(Int, Int)], setCoords: List[(Int, Int)], b: Board): Option[Board] = {

    coords match {
      case Nil => Some(b)
      case x::xs => {
        parts = new Shippart(this) :: parts
        println("Trying to set " + coords.head)
        if(b.setShippart(x._1, x._2, parts.head)) {
          setOnBoard(xs, x::setCoords, b)
        } else {
          unsetShip(setCoords, b)
        }
      }
    }
  }

  def unsetShip(setCoords: List[(Int, Int)], b: Board): Option[Board] = {
    setCoords match {
      case Nil => None
      case x::xs => {
        b.unsetShippart(x._1, x._2, this)
        unsetShip(xs, b)
      }
    }
  }

/*
  def setOnBoard(x: Int, y: Int, d: Direction.Value): Board => Option[Board] = {
    val rotatedShip = rotate(d)

    (b: Board) => {
      val oldShips = Array.ofDim[Shippart](10, 10)
      Array.copy(b.ships, 0, oldShips, 0, b.ships.size)
      println("After copying:")
      ConsoleHelper.printArray(oldShips)

      var success = true
      var i = 0
      while(i < rotatedShip.length && success) {
        success = b.setShippart(x + rotatedShip(i)._1,
          y + rotatedShip(i)._2,
          new Shippart(this))
        i += 1
      }

      if(!success) {
        println("Before copying:")
        ConsoleHelper.printArray(oldShips)

        Array.copy(oldShips, 0, b.ships, 0, b.ships.size)
        None
      } else {
        Some(b)
      }

      /*
      for (i <- 0 to rotatedShip.length - 1) {
        val success = b.setShippart(x + rotatedShip(i)._1,
          y + rotatedShip(i)._2,
          new Shippart(this))

        println(success)

        if (!success) {
          Array.copy(oldShips, 0, b.ships, 0, b.ships.size)
          return
        }
      }

      Some(b)
      */
    }
  }
*/
  def isDestroyed: Boolean = {
    parts.filter(_.isDestroyed == false).length == 0
  }
  

  
}

object Ship {
  def apply(s: ShipTypes, x: Int, y: Int, d: Direction): Ship = {
    s.construct(x, y, d)

/*
    for(i <- 0 to ship.parts.length - 1) {
      ship.parts(i) = new Shippart(ship)
    }
    ship*/
  }

  def generateShipSet(): List[(ShipTypes, Int)] = {
    (ShipTypes.AircraftCarrier, 1)::(ShipTypes.Battleship, 1)::(ShipTypes.Destroyer, 1)::
      (ShipTypes.Submarine, 1)::(ShipTypes.PatrolBoat, 1)::Nil
  }

  def rotateAndTranslate(d: Direction, shipform: List[(Int, Int)], x: Int, y: Int): List[(Int, Int)] = {
    d match {
      case Direction.N => shipform.map((z: (Int, Int)) => (z._2 + x, (z._1 * (-1)) + y))
      case Direction.W => shipform.map((z: (Int, Int)) => ((z._1 * (-1)) + x, (z._2 * (-1)) + y))
      case Direction.S => shipform.map((z: (Int, Int)) => ((z._2 * (-1)) + x, (z._1 + y)))
      case _ => shipform.map((z:(Int, Int)) => (z._1 + x, z._2 + y))
    }
  }
}




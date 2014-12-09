package models

import models.Direction.Direction


abstract case class Ship(var coords: List[(Int, Int)]) {
  var parts: List[Shippart] = Nil


  override def toString: String = {
    "Hi, I'm a " + this.getClass() + "! My coords are: " + coords.toString
  }

  def translateAndRotate(x: Int, y: Int, d: Direction) {
    coords = d match {
      case Direction.N => coords.map((z: (Int, Int)) => (z._2 + x, (z._1 * (-1)) + y))
      case Direction.W => coords.map((z: (Int, Int)) => ((z._1 * (-1)) + x, (z._2 * (-1)) + y))
      case Direction.S => coords.map((z: (Int, Int)) => ((z._2 * (-1)) + x, (z._1 + y)))
      case _ => coords.map((z: (Int, Int)) => (z._1 + x, z._2 + y))
    }
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
  
  def isDestroyed: Boolean = {
    parts.filter(_.isDestroyed == false).length == 0
  }
}




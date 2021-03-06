package models

import models.Direction.Direction

import scala.util.parsing.json.JSONArray


abstract case class Ship(var coords: List[(Int, Int)]) {
  var parts: List[Shippart] = Nil


  override def toString: String = {
    "Hi, I'm a " + this.getName + "! My coords are: " + coords.toString
  }

  def getName :String = {
    return this.getClass.getSimpleName
  }

  // returns an array of the ships coordinates in list format (needed because tupples serialized to json)
  def formToArray :List[List[Int]] = {
    var retList = List[List[Int]]()
    coords foreach{ x :(Int, Int) => retList = retList ::: List(x._1, x._2) :: Nil}

    retList
  }

  def formToJson :JSONArray = {
    var retList = List[JSONArray]()
    coords foreach{ x :(Int, Int) => retList = retList ::: JSONArray(List(x._1, x._2)) :: Nil}

    return JSONArray(retList)
  }

  // rotates the coordinates of the ship to the given direction
  def translateAndRotate(x: Int, y: Int, d: Direction) {
    println("Direction: " + d)
    println("(" + x + ", " + y + ")")
    val tmpCoords = d match {
      case Direction.N => coords.map((z: (Int, Int)) => (z._2 + x, (z._1 * (-1)) + y))
      case Direction.W => coords.map((z: (Int, Int)) => ((z._1 * (-1)) + x, (z._2 * (-1)) + y))
      case Direction.S => coords.map((z: (Int, Int)) => ((z._2 * (-1)) + x, z._1 + y))
      case _ => coords.map((z: (Int, Int)) => (z._1 + x, z._2 + y))
    }

    println(tmpCoords)
    println(tmpCoords.count((x: (Int, Int)) => x._1 < 0 || x._2 < 0 || x._1 > 9 || x._2 > 9))

    require(tmpCoords.count((x: (Int, Int)) => x._1 < 0 || x._2 < 0 || x._1 > 9 || x._2 > 9) == 0,
      "a ship cannot have negative coordinates")
    coords = tmpCoords
  }

  // sets the ship on the given board (adds shipparts to the board array)
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

  // removes the ship from the given board
  def unsetShip(setCoords: List[(Int, Int)], b: Board): Option[Board] = {
    setCoords match {
      case Nil => None
      case x::xs => {
        b.unsetShippart(x._1, x._2, this)
        unsetShip(xs, b)
      }
    }
  }

  // returns true if all parts of the ship have been destroyed
  def isDestroyed: Boolean = {
    parts.filter(_.isDestroyed == false).length == 0
  }
}




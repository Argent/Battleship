/**
 * Created by Basti on 15.01.15.
 */
package models


import models.HitTypes.HitTypes

class Board {
  val ships = Array.ofDim[Shippart](10, 10)

  def setShippart(x: Int, y: Int, s: Shippart): Boolean = {

    if(x < 0 || y < 0 || x > 9 || y > 9) {
      return false
    }
    for(i <- -1 to 1) {
      for(j <- -1 to 1) {
        val checkY = y + i
        val checkX = x + j
        if(x == 0 || y == 0 || x == 9 || y == 9) {
          if(checkY >= 0 && checkY <= 9 && checkX >= 0 && checkX <= 9) {
            //println("Randfallüberprüfung von (" + checkX + ", " + checkY + ")")
            if (ships(checkY)(checkX) != null && ships(checkY)(checkX).ship != s.ship)
              return false
          }
        } else {
          if(ships(checkY)(checkX) != null && ships(checkY)(checkX).ship != s.ship)
            return false
        }
      }
    }
    println("Setting Shippart on (" + x + ", " + y + ")")
    ships(y)(x) = s
    true
  }

  def unsetShippart(x: Int, y: Int, s: Ship) = {
    if(ships(y)(x).ship == s) {
      ships(y)(x) = null
    }
  }

  def shoot(x: Int, y: Int): HitTypes = {
    if(ships(y)(x) != null) {
      ships(y)(x).isDestroyed = true
      if(ships(y)(x).ship.isDestroyed) {
        HitTypes.HitAndSunk
      } else {
        HitTypes.Hit
      }
    } else {
      HitTypes.Miss
    }
  }

  def getShipAtCoordinates(x: Int, y: Int): Ship = {
    ships(y)(x).ship
  }

  override def toString(): String ={
  /* ships.flatten.collect { part => part match {
     case null => yield 's'
   }}*/

    var stringBoard = ""
    ships.flatten.foreach{ part => part match {
      case null => stringBoard += "."
      case _ => if (part.isDestroyed) { stringBoard += "X" } else  {stringBoard += "O"}
    }}
    stringBoard
  }

  def printArray(printTile: Shippart => String): Unit = {
    val yAxis = Array('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J')
    var i = 0
    println("  0 1 2 3 4 5 6 7 8 9")
    ships.foreach(x => {
      print(yAxis(i) + " ")
      i += 1
      x.foreach(y => { print(printTile(y) + " ")})
      println("")
    })
    println("")
  }

}
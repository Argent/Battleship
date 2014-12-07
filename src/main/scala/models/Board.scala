package models

import models.WaterTypes.WaterTypes

class Board {


  val shots: Array[Array[WaterTypes]] = (for(i <- 0 to 9)
    yield (for(j <- 0 to 9)
      yield WaterTypes.Water).toArray).toArray

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

  def shoot(x: Int, y: Int): Boolean = {
    if(ships(y)(x) != null) {
      ships(y)(x).isDestroyed = true
      //shots(y)(x) = WaterTypes.Hit
      true
    } else {
      //shots(y)(x) = WaterTypes.NoHit
      false
    }
  }

  def setShot(x: Int, y: Int, w: WaterTypes) = {
    shots(y)(x) = w
  }


}
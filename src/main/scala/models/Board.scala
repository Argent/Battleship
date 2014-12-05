package models

class Board {

  val shots = Array.ofDim[Boolean](10, 10)
  val ships = Array.ofDim[Shippart](10, 10)

  def setShippart(x: Int, y: Int, s: Shippart): Boolean = {
    for(i <- -1 to 1) {
      for(j <- -1 to 1) {
        val checkY = y + i
        val checkX = x + j
        if(x == 0 || y == 0 || x == 9 || y == 9) {
          if(checkY >= 0 && checkY <= 9 && checkX >= 0 && checkY <= 9) {
            if (ships(checkY)(checkX) != null && ships(checkY)(checkX).ship != s.ship)
              return false
          }
        } else {
          if(ships(checkY)(checkX) != null && ships(checkY)(checkX).ship != s.ship)
            return false
        }
      }
    }
      
    ships(y)(x) = s
    true
  }

  def unsetShippart(x: Int, y: Int) = {
    ships(y)(x) = null
  }


  /**
   * true -> Treffer, false -> kein Treffer, Null -> noch nicht geschossen
   */
  def shoot(x: Int, y: Int): Boolean = {
    if(ships(x)(y) != null) {
      ships(x)(y).isDestroyed = true
      shots(x)(y) = true
      true
    } else {
      shots(x)(y) = false
      false
    }
  }
  
}
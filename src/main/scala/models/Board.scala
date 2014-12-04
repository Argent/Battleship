package models

class Board {
  val shots = Array.ofDim[Boolean](10, 10)
  val ships = Array.ofDim[Shippart](10, 10)
  
  def setShippart(x: Int, y: Int, s: Shippart): Boolean = {
    for(i <- -1 to 1) {
      for(j <- -1 to 1) {
        if(ships(i)(j) != null) {
          return false
        }
      }
    }
      
    ships(x)(y) = s
    true
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
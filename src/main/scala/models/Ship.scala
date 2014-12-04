package models


trait Ship {
  val parts: Array[Shippart]

  val shipform: Array[(Int, Int)]

  
  def setOnBoard(x: Int, y: Int, d: Direction.Value): Board => Option[Board] = {
    val rotatedShip = rotate(d)
    println(rotatedShip.length)
    
    (b: Board) => {
      val oldShips = Array.ofDim[Shippart](10, 10)
      Array.copy(b.ships, 0, oldShips, 0, b.ships.size)


      for (i <- 0 to rotatedShip.length - 1) {
        val success = b.setShippart(x + rotatedShip(i)._1,
          y + rotatedShip(i)._2,
          new Shippart(this))

        if (!success) {
          Array.copy(oldShips, 0, b.ships, 0, b.ships.size)
          None
        }
      }

      Some(b)
    }
  }
  
  def isDestroyed: Boolean = {
    parts.filter(_.isDestroyed == false).length == 0
  }
  
  def rotate(d: Direction.Value) = {
    d match {
      case Direction.N => shipform.map((x: (Int, Int)) => (x._2, x._1 * (-1)))
      case Direction.W => shipform.map((x: (Int, Int)) => (x._1 * (-1), x._2 * (-1)))
      case Direction.S => shipform.map((x: (Int, Int)) => (x._2 * (-1), x._1))
      case _ => shipform
    }
  }
  
}




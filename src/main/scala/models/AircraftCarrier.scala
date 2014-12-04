package models

class AircraftCarrier extends Ship {
  val parts: Array[models.Shippart] = new Array[Shippart](5)
  val shipform = Array[(Int, Int)]((0, 0), (1, 0), (2, 0), (3, 0), (4, 0))

  for(i <- 0 to parts.length - 1) {
    this.parts(i) = new Shippart(this)
  }
  println(toString)


  override def toString = {
    var result = "Hi, I'm an aircraft carrier! My parts:"

    parts.foreach(x => result += " " + x)
    result
  }
}
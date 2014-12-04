package models

class PatrolBoat extends Ship {
  val parts: Array[Shippart] = new Array[Shippart](2)
  val shipform = Array((0, 0), (1, 0))
}
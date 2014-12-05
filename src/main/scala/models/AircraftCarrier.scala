package models

class AircraftCarrier extends Ship {
  val parts: Array[models.Shippart] = new Array[Shippart](5)
  val shipform = Array[(Int, Int)]((0, 0), (1, 0), (2, 0), (3, 0), (4, 0))
}
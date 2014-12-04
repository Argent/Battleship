package models

class Battleship extends Ship {
  val parts: Array[models.Shippart] = new Array[Shippart](4)
  val shipform = Array[(Int, Int)]((0, 0), (1, 0), (2, 0), (3, 0));
}
package models

import models.Direction.Direction

class Submarine(shipform: List[(Int, Int)], x: Int, y: Int, d: Direction) extends Ship(shipform, x, y, d)
package models

import models.Direction._

class AircraftCarrier(shipform: List[(Int, Int)], x: Int, y: Int, d: Direction) extends Ship(shipform, x, y, d)
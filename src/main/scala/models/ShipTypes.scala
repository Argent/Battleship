package models

import models.Direction.Direction
/*

object ShipTypes extends Enumeration {
  type ShipTypes = Value

  protected  case class Val(val construct: (Int, Int, Direction) => Ship) extends super.Val
  implicit def valueToMyVal(x: Value) = x.asInstanceOf[Val]

  val AircraftCarrier = Val((x: Int, y: Int, d: Direction) =>
    new AircraftCarrier(List((0, 0), (1, 0), (2, 0), (3, 0), (4, 0)), x, y, d)
  )

  val Battleship = Val((x: Int, y: Int, d: Direction) =>
    new Battleship(List((0, 0), (1, 0), (2, 0), (3, 0)), x, y, d)
  )

  val Destroyer = Val((x: Int, y: Int, d: Direction) =>
    new Destroyer(List((0, 0), (1, 0), (2, 0)), x, y, d)
  )

  val PatrolBoat = Val((x: Int, y: Int, d: Direction) =>
    new PatrolBoat(List((0, 0), (1, 0)), x, y, d)
  )

  val Submarine = Val((x: Int, y: Int, d: Direction) =>
    new Submarine(List((0, 0), (1, 0), (2, 0)), x, y, d)
  )

}*/

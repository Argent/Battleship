package models



object ShipTypes extends Enumeration {
  type ShipTypes = Value

  protected  case class Val(val construct: () => Ship) extends super.Val
  implicit def valueToMyVal(x: Value) = x.asInstanceOf[Val]

  val AircraftCarrier = Val(() => new AircraftCarrier())
  val Battleship = Val(() => new Battleship())
  val Destroyer = Val(() => new Destroyer())
  val PatrolBoat = Val(() => new PatrolBoat())
  val Submarine = Val(() => new Submarine())

}

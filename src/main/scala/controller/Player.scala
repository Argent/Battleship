package controller

import models.Board
import models.ShipTypes.ShipTypes

trait Player {
  def setShip(s: ShipTypes, b: Board)
  def doTurn(b: Board): (Int, Int)

}
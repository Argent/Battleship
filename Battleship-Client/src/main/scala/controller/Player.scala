package controller

import models.WaterTypes.WaterTypes
import models.{Ship, Board}


trait Player {
  val shoots: Array[Array[WaterTypes]] = Array.ofDim[WaterTypes](10, 10)
  //val id: Int

  def setShip(s: Ship, b: Board)
  def doTurn(b: Board): (Int, Int)

}
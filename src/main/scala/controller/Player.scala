package controller

import models.{Ship, Board}

trait Player {
  def setShip(s: Ship, b: Board)
  def doTurn(b: Board): (Int, Int)

}
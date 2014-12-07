package controller

import helper.ConsoleHelper
import models.ShipTypes.ShipTypes
import models.{ShipTypes, Ship, AircraftCarrier, Direction}


class ConsoleSession extends Session {
  override def initSession(ships: List[(ShipTypes, Int)], currentPlayer: Int): Unit = {
    println("Board of: " + players(currentPlayer).getClass())
    ConsoleHelper.printArray(boards(currentPlayer).ships)

    if(ships == Nil && currentPlayer == 1) {
      runSession(0)
    } else {
      initSession(ships, currentPlayer)
    }
  }

  override def runSession(currentPlayer: Int): Unit = {

  }
}
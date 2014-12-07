package controller

import helper.ConsoleHelper
import models.ShipTypes.ShipTypes
import models.{ShipTypes, Ship, AircraftCarrier, Direction}


class ConsoleSession extends Session {
  override def initSession(ships: List[(ShipTypes, Int)], currentPlayer: Int): Unit = {
    ConsoleHelper.printArray(boards(currentPlayer).ships)

    initSession(ships, currentPlayer)
  }

  override def runSession(): Unit = {

  }
}
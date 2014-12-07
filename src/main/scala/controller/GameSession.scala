package controller

import helper.ConsoleHelper
import models.WaterTypes.WaterTypes
import models.{Shippart, WaterTypes, Ship}
import models.ShipTypes.ShipTypes
import models.WaterTypes.WaterTypes


trait GameSession extends Session {

  abstract override def initSession(ships: List[(ShipTypes, Int)]): Unit = {

    if(players(0) == null) {
      players(0) = new HumanPlayer()
      players(1) = new AIPlayer()
    }

    println("Now setting " + ships.head)
    players(currentPlayer).setShip(ships.head._1, boards(currentPlayer))
    println("")
    println("Board of: " + players(currentPlayer).getClass())
    ConsoleHelper.printArray(boards(currentPlayer).ships,
      (x: Shippart) =>
        if(x == null)
          "W"
        else
          "S"
    )

    ships match {
      case (s: ShipTypes, 1) :: Nil if currentPlayer == 1 => {
        nextPlayer()
        runSession()
      }
      case (s: ShipTypes, 1) :: Nil => {
        nextPlayer()
        initSession(Ship.generateShipSet())
      }
      case (s: ShipTypes, 1) :: xs => initSession(xs)
      case (s: ShipTypes, count: Int) :: xs => initSession((s, count - 1) :: xs)
    }
  }

  override def runSession(): Unit = {
    println("An der Reihe: " + players(currentPlayer))
    println(boards(0).shots)
    println(boards(1).shots)

    val coords = players(currentPlayer).doTurn(boards(currentPlayer))
    val shootResult = boards(getOtherPlayer()).shoot(coords._1, coords._2)

    if(shootResult) {
      println(players(currentPlayer).getClass() + " - Schuss auf (" + coords._1 + ", " + coords._2 + "): Treffer!")
      println("Setting shot on  " + currentPlayer)
      boards(currentPlayer).setShot(coords._1, coords._2, WaterTypes.Hit)
    } else {
      println(players(currentPlayer).getClass() + " - Schuss auf (" + coords._1 + ", " + coords._2 + "): kein Treffer!")
      println("Setting shot on  " + currentPlayer)
      boards(currentPlayer).setShot(coords._1, coords._2, WaterTypes.NoHit)
      nextPlayer()
    }


    println("Player 1 Board")
    ConsoleHelper.printArray(boards(0).shots,
      (x: WaterTypes) =>
        if(x == WaterTypes.Water)
          "_"
        else if(x == WaterTypes.Hit)
          "X"
        else
          "O"
    )

    println("Player 2 Board")
    ConsoleHelper.printArray(boards(1).shots,
      (x: WaterTypes) =>
        if(x == WaterTypes.Water)
          "_"
        else if(x == WaterTypes.Hit)
          "X"
        else
          "O"
    )

    runSession()
  }
}
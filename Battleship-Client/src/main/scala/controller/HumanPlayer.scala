package controller

import helper.ConsoleHelper
import models.{WaterTypes, Board, Ship}

import scala.util.parsing.json.JSONArray


class HumanPlayer(val id: Int, val shipsLeft: List[Map[String, Any]]) extends Player {
  override def setShip(s: Ship, b: Board): Unit = {
/*
    val shipCoords = ConsoleHelper.getShipCoordinatesFromConsole("Position in der Form Buchstabe Zahl Richtung eingeben: ")
    s.translateAndRotate(shipCoords._2, shipCoords._2, shipCoords._3)

    if(s.setOnBoard(s.coords, Nil, b) == None) {
      println("Fehler beim Setzen. Um Schiffe muss ein Abstand von einem Kästchen bestehen.")
      setShip(s, b)
    }*/
  }

  override def doTurn(b: Board): (Int, Int) = {
   /* val coords = ConsoleHelper.getCoordinatesFromConsole("Wohin schießen? ")
    if(!(b.shots(coords._1)(coords._2) == WaterTypes.Water)) {
      doTurn(b: Board)
    }
    (coords._2, coords._1)*/
    (1, 1)
  }

}
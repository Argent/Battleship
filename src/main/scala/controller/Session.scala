package controller

import models.Board
import models.ShipTypes.ShipTypes

trait Session {
  var currentPlayer: Int = 0
  var players: Array[Player] = new Array[Player](2)
  var boards: Array[Board] = Array[Board](new Board(), new Board())
  
  def initSession(ships: List[(ShipTypes, Int)], currentPlayer: Int)
  def runSession(currentPlayer: Int)

  def isWon: Option[Player] = {
    if(boards(0).ships.flatten.filter(_.isDestroyed != null).length == 0) {
      Some(players(0))
    } else if(boards(1).ships.flatten.filter(_.isDestroyed != null).length == 0) {
      Some(players(1))
    }
    None
  }
  
  def nextPlayer() {
    currentPlayer = (currentPlayer + 1) % 2
  }
}
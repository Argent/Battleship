package shared

import models.{Shippart, Board}

/**
 * Created by Basti on 15.01.15.
 */
object Game {
  var players: Array[Option[Int]] = Array(None, None)
  var currentPlayer: Int = 0
  var boards: Map[Int, Board] = Map()


  def isWon: Option[Int] = {

    // TODO: checken, ob beide Spieler gesetzt sind

    if(boards.get(players(0).get).get.ships.flatten.filter(_.isInstanceOf[Shippart]).filter(_.isDestroyed == false).length == 0) {
      players(0)
    } else if(boards.get(players(1).get).get.ships.flatten.filter(_.isInstanceOf[Shippart]).filter(_.isDestroyed == false).length == 0) {
      players(1)
    } else {
      None
    }
  }

  def nextPlayer() {
    currentPlayer = (currentPlayer + 1) % 2
  }

  def getOtherPlayer(): Int = {
    (currentPlayer + 1) % 2
  }
}

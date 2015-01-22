package controller

import models._

trait Session {
  var currentPlayer: Int = 0
  var players: Array[Player] = new Array[Player](2)
  var boards: Array[Board] = Array[Board](new Board(), new Board())
  
  def initSession(ships: List[Ship])
  def runSession()

  def isWon: Option[Player] = {
    if(boards(0).ships.flatten.filter(_.isInstanceOf[Shippart]).filter(_.isDestroyed == false).length == 0) {
      Some(players(1))
    } else if(boards(1).ships.flatten.filter(_.isInstanceOf[Shippart]).filter(_.isDestroyed == false).length == 0) {
      Some(players(0))
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

object Session {
  def generateShipSet(): List[Ship] = {
    new AircraftCarrier()::Nil /*new Battleship()::new Battleship()::new Destroyer()::new Destroyer()::
      new Destroyer()::new Submarine()::new Submarine()::new Submarine()::new Submarine()::
      new PatrolBoat()::new PatrolBoat()::new PatrolBoat()::new PatrolBoat()::Nil*/
  }
}
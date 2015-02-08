package shared

import models._

/**
 * Created by Basti on 15.01.15.
 */
object Game {
  val players: Array[Option[Int]] = Array(None, None)
  val unplacedShips :scala.collection.mutable.Map[Int, List[Ship]] = scala.collection.mutable.Map()
  var currentPlayer: Int = 0
  val lastActions :scala.collection.mutable.ListBuffer[PlayerAction] = scala.collection.mutable.ListBuffer()
  val boards: scala.collection.mutable.Map[Int, Board] = scala.collection.mutable.Map()

  def generateShipSet(): Map[Ship, Int] = {
    Map(new Submarine() -> 1 /*new Battleship()::new Battleship()::new Destroyer()::new Destroyer()::
      new Destroyer()::new Submarine()::new Submarine()::new Submarine()::new Submarine()::
      new PatrolBoat()::new PatrolBoat()::new PatrolBoat()::new PatrolBoat()::Nil*/)
  }

  def gameStarted: Boolean = {
    return players.filter(_ == None).length == 0 && unplacedShips.values.flatten.toList.length == 0
  }

  def matchUserId(userId :Int): Option[Int] = {
    return players.indexOf(Some(userId)) match {
      case x if x >= 0 => Some(x)
      case _ => None
    }
  }

  def shipAvailable(userId :Int, ship :Ship): Boolean = {
    return unplacedShips.get(userId).get.filter(s => s.getClass.equals(ship.getClass)).length > 0
  }

  def isWon: Option[Int] = {

    if(!gameStarted) {
      None
    } else if(boards.get(players(0).get).get.ships.flatten.filter(_.isInstanceOf[Shippart]).filter(_.isDestroyed == false).length == 0) {
      players(1)
    } else if(boards.get(players(1).get).get.ships.flatten.filter(_.isInstanceOf[Shippart]).filter(_.isDestroyed == false).length == 0) {
      players(0)
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

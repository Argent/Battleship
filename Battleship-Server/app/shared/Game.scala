package shared

import models._

/**
 * Created by Basti on 15.01.15.
 */
object Game {
  val players: Array[Option[Int]] = Array(None, None)
  // saves the ships that each player can place on the board
  val unplacedShips :scala.collection.mutable.Map[Int, List[Ship]] = scala.collection.mutable.Map()
  // index of current player
  var currentPlayer: Int = 0
  // saves the actions the player did in the last round, so his opponent can retrieve tem
  val lastActions :scala.collection.mutable.ListBuffer[PlayerAction] = scala.collection.mutable.ListBuffer()
  // saves a board for each player
  val boards: scala.collection.mutable.Map[Int, Board] = scala.collection.mutable.Map()

  // Set ship types you want to play with
  def generateShipSet(): Map[Ship, Int] = {
    Map(new PatrolBoat() -> 2, new Battleship() -> 1, new Destroyer() -> 1)
  }

  // game is started when players have been registered and both have set all their ships
  def gameStarted: Boolean = {
    return players.filter(_ == None).length == 0 && unplacedShips.values.flatten.toList.length == 0
  }

  // returns index of player if exists
  def matchUserId(userId :Int): Option[Int] = {
    return players.indexOf(Some(userId)) match {
      case x if x >= 0 => Some(x)
      case _ => None
    }
  }

  //returns true if the player is allowed to place a ship of that type
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

  def resetGame() {
    players(0) = None
    players(1) = None
    currentPlayer = 0
    boards.clear()
    unplacedShips.clear()
    lastActions.clear()
  }
}

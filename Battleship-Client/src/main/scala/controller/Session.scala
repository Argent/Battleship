package controller

import java.rmi.UnexpectedException
import java.util.concurrent.TimeoutException

import helper.{ConsoleHelper, ServerHelper}
import model._

class Session {

  // entry point
  def registerWithServer(): Unit = {
    val id = scala.util.Random.nextInt(1000)

    val shipsOption = ServerHelper.registerWithServer(id)

    shipsOption match {
      case None => throw new TimeoutException
      case Some(ships) => placeShips(new Player(id, ships))
    }
  }

  // while there are still ships to place, this function calls itself recursively
  def placeShips(p: Player): Unit = {
    val board = p.placeShip(p.ships.head.ship)
    ConsoleHelper.printArray(Board.parseFromString(board))

    p.ships match {
      case ShipListItem(_, 1) :: Nil => {
        // last ship placed -> game begins
        waitForOpponent(new Player(p.id, Nil))
      }
      case ShipListItem(s: Ship, i: Int) :: Nil => {
        // placed a ship, but not the last of said type
        placeShips(new Player(p.id, ShipListItem(s, i - 1) :: Nil))
      }
      case ShipListItem(_, 1) :: xs  => {
        // placed a ship, there are ships of other types remaining
        placeShips(new Player(p.id, p.ships.tail))
      }
      case ShipListItem(s: Ship, i: Int) :: xs => {
        // placed a ship, there are ships of other types and ships of <<s>>'s type remaining
        placeShips(new Player(p.id, ShipListItem(s, i - 1) :: xs))
      }
      case _ => throw new UnexpectedException("i didn't expect that")
    }
  }

  // there are two reasons for waiting: other player has not placed all of his ships
  // or it's the other player's turn
  def waitForOpponent(p: Player): Unit = {
    ServerHelper.waitForOpponent(p) match {
      case PollResponse(board: Board, Some(winner: Int), actions: List[PlayerAction]) => {
        // the other player has won the game
        ConsoleHelper.printDefeatMessage(board, actions)
      }
      case PollResponse(board: Board, _, Nil) => {
        // the other player finished setting his ships
        ConsoleHelper.printFirstTurnMessage(board, p.shots)
        shoot(p)
      }
      case PollResponse(board: Board, _, actions: List[PlayerAction]) => {
        // the other player finished his turn
        ConsoleHelper.printYourTurnMessage(board, p.shots, actions)
        shoot(p)
      }
    }
  }

  // after waiting the player can shoot as long as he hits ships of the other player
  def shoot(player: Player): Unit = {
    player.shoot() match {
      case (p: Player, ShootResponse(HitType.Hit, _, _)) => {
        // Hit
        ConsoleHelper.printHitMessage(p.shots)
        shoot(p)
      }
      case (p: Player, ShootResponse(_, Some(winner: Int), _)) => {
        // Hit and destroyed all other ships -> Game ends
        ConsoleHelper.printVictoryMessage(p.shots)
      }
      case (p: Player, ShootResponse(HitType.HitAndSunk, _, Some(destroyed: String))) => {
        // Hit and destroyed one of the enemies ships
        ConsoleHelper.printHitAndDestroyedMessage(p.shots, destroyed)
        shoot(p)
      }
      case (p: Player, ShootResponse(HitType.Miss, _, _)) => {
        // Missed -> other player's turn
        ConsoleHelper.printMissMessage(p.shots)
        waitForOpponent(p)
      }
    }
  }

}

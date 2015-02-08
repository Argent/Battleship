package model

import helper.{ServerHelper, ConsoleHelper}

/**
 *  Player represents the second layer of processing of a game action: Inputs
 *  are taken, a new Player generated, if necessary and the the appropriate
 *  Serverfunctions are called
 */


class Player(
              val id: Int,
              val ships: List[ShipListItem],
              val shots: Board = Board.generateEmptyBoard()
              )
{

  // alternate Constructor, to be used, if the player has already placed all of his ships
  def this(id: Int, b: Board) = {
    this(id, Nil, b)
  }


  def placeShip(s: Ship): String = {
    ConsoleHelper.printShip(s)

    // get user input
    val coords = ConsoleHelper.getShipCoordinatesFromConsole()

    // call server
    val result = ServerHelper.setShip(s.name, coords._2, coords._1, coords._3, id)

    // None means placement failed, otherwise send back board to Session for display
    // and further actions
    result match {
      case None => placeShip(s)
      case Some(s: String) => s
    }
  }

  def shoot(): (Player, ShootResponse) = {
    // get user input
    val coords = ConsoleHelper.getCoordinatesFromConsole()
    val xCoord = coords._2
    val yCoord = CharacterCoordinate(coords._1)


    if(!shots.isUnshot(xCoord.toInt, yCoord.toInt)) {
      // check if a location was already shot is client-side
      println("Da hast du schonmal hingeschossen..")
      shoot()
    } else {
      // call server and get instance of ShootResponse
      val shootResponse = ServerHelper.shoot(xCoord, yCoord, this)


      // return tuple of new player and shootresponse to Session
      (new Player(
          this.id,
          shots.setShot(xCoord.toInt, yCoord.toInt, shootResponse.hitType)
        ),
        shootResponse
      )
    }
  }

}

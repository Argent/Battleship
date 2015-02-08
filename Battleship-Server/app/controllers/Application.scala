package controllers

import models._
import play.api.libs.json._
import play.api.mvc._
import shared.Game

import scala.util.parsing.json.{JSONArray, JSONObject}

object Application extends Controller {

  case class ShipPlacement(userid: Int, x :String, y :String, direction :String, shiptype :String)
  case class Shot(userid: Int, x :String, y:String)

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  // this action is called to register a new player
  def registerPlayer = Action(parse.json) { request =>
    request.body\("userid") match {
      case e: JsUndefined => BadRequest("must supply userid")
      case e: JsValue => {
        val userid = request.body.\("userid").asOpt[Int]
        val newPlayerIndex = if (Game.players(0) == None) { 0 } else if (Game.players(1) == None) { 1 } else None

        if (newPlayerIndex != None){
          if (newPlayerIndex == 1 && Game.players(0).get == userid){
            Conflict("userid " + userid + " already registered")
          }else {
            // if a new player can be registered the player and board will be created and a new set of available ships will be generated
            Game.players(newPlayerIndex.asInstanceOf[Int]) = userid
            Game.boards.put(userid.get, new Board())

            var shipList = List[JSONObject]()
            val shipSet = Game.generateShipSet()
            Game.unplacedShips.put(userid.get, shipSet.map {case (k,v) => List.fill(v)(k)}.flatten.toList)
            shipSet.foreach{x => shipList = shipList ::: new JSONObject(Map("name" -> x._1.getName, "form" -> x._1.formToJson, "number" -> x._2)) :: Nil}

            println("Player "+ userid.get + " registered (index: " + newPlayerIndex + ")")

            Ok(new JSONObject(Map("ships" -> JSONArray(shipList), "userid" -> userid.get)).toString())
          }
        }else {
          Conflict("2 players are already registered")
        }
      }
    }
  }

  // this action is called to place a ship on the board
  def placeShip =  Action(parse.json) { implicit request =>

    val placement = new ShipPlacement(request.body.\("userid").as[Int], request.body.\("x").as[String], request.body.\("y").as[String], request.body.\("direction").as[String], request.body.\("shiptype").as[String])

    val ship :Option[Ship] = placement.shiptype match {
      case "Battleship" => Option[Ship] {new Battleship()}
      case "AircraftCarrier" => Option[Ship] {new AircraftCarrier()}
      case "Destroyer" => Option[Ship] {new Destroyer()}
      case "PatrolBoat" =>Option[Ship] {new PatrolBoat()}
      case _ => None
    }

    val board = Game.boards.get(placement.userid)

    if (board.isInstanceOf[Some[Board]] && ship.isInstanceOf[Some[Ship]]){
      if (Game.shipAvailable(placement.userid, ship.get)){
        // if the player is allowed to place the ship at the given coordinates the ship will be set and the ship will be removed from the available ship types of the player
        if (setShip(ship.get,board.get,placement.x,placement.y,placement.direction)) {
          println("New Board:")
          board.get.printArray((x: Shippart) =>
            if (x == null)
              "W"
            else
              "S")

          Game.unplacedShips.put(placement.userid, removeFirst(Game.unplacedShips.get(placement.userid).get) {_.getClass.equals(ship.get.getClass)})
          Ok(new JSONObject(Map[String, Any]("map" -> board.get.toString())).toString())
        }else {
          BadRequest("Could not set ship at these coordinates")
        }
      }else {
        Conflict("No ships of type " + ship.get.getName + " availabe")
      }
    }else {
      BadRequest("Could not parse input data")
    }
  }

  // helper method to remove the first occurence of an element from a list
  def removeFirst[T](list: List[T])(pred: (T) => Boolean): List[T] = {
       val (before, atAndAfter) = list span (x => !pred(x))
       before ::: atAndAfter.drop(1)
  }

  // returns true if the ship can be placed at the given coordinates
  def setShip(s: Ship, b: Board, x: String, y: String, direction: String): Boolean = {

    val shipCoords = (CharacterCoordinate(y),
      Integer.parseInt(x),
      direction match {
        case "E" => Direction.E
        case "N" => Direction.N
        case "S" => Direction.S
        case "W" => Direction.W
      })
    s.translateAndRotate(shipCoords._2, shipCoords._1, shipCoords._3)

    if (s.setOnBoard(s.coords, Nil, b) == None) {
      println("Fehler beim Setzen. Um Schiffe muss ein Abstand von einem Kästchen bestehen.")
      false
    } else {
      true
    }
  }

  // this action is called to check if it's the turn of the player and to get the action history of the opponent
  def checkForOpponent = Action(parse.json) { implicit request =>

    request.body\("userid") match {
      case e: JsUndefined => BadRequest("must supply userid")
      case e: JsValue => {
        val userId = request.body.\("userid").asOpt[Int].get
        val userIndex = Game.matchUserId(userId)
        if (userIndex == None){
          BadRequest("userId not found")
        }else if (!Game.gameStarted){
          NotFound("game has not started yet")
        }else {
          var actionList = List[JSONObject]()
          Game.lastActions.foreach(a => actionList = actionList ::: a.toJSONObject :: Nil)
          // if the game is over, include the id of the wining player in the response
          if (Game.isWon != None){
            // after the losing player has retrieved the game results, reset the game
            val json = new JSONObject(Map("map" -> Game.boards.get(Game.players(userIndex.get).get).get.toString(), "actions" -> JSONArray(actionList), "won" -> Game.isWon.get)).toString()
            if (userId != Game.isWon.get){
              Game.resetGame()
            }
            Ok(json)
          }else if (Game.currentPlayer == userIndex.get){
            Ok(new JSONObject(Map("map" -> Game.boards.get(Game.players(userIndex.get).get).get.toString(), "actions" -> JSONArray(actionList), "won" -> "None")).toString())
          }else {
            NotFound("it's not your turn yet")
          }
        }
      }
    }
  }

  // this action is called to shoot at the given coordinates
  def shoot = Action(parse.json) { implicit request =>

    val shot = new Shot(request.body.\("userid").as[Int], request.body.\("x").as[String], request.body.\("y").as[String])
    val userIndex = Game.matchUserId(shot.userid)
    if (userIndex == None) {
      BadRequest("userId not found")
    }else if (!Game.gameStarted) {
      NotFound("game has not started yet")
    }else if (Game.currentPlayer != userIndex.get){
      NotFound("it's not your turn yet")
    }else {
      val board = Game.boards.get(Game.players(Game.getOtherPlayer()).get)
      val y = CharacterCoordinate(shot.y)
      val x = Integer.parseInt(shot.x)
      if (board.isInstanceOf[Some[Board]] && x >= 0 && x <= 10) {
        if (Game.lastActions.length > 0 && Game.lastActions.last.hitType == HitTypes.Miss){
          Game.lastActions.clear()
        }
        val hit = board.get.shoot(x, y)
        var json = Map[String, Any]("type" -> hit.toString, "won" ->  {if (Game.isWon == None) {"None"} else {Game.isWon.get}})
        val part = board.get.ships(y)(x)
        if (hit == HitTypes.HitAndSunk){
          // if the player sinks a ship, include the type of the ship to action history
          Game.lastActions.append(new PlayerAction((x, y),hit, Some(part.ship.getName)))
        }else {
          Game.lastActions.append(new PlayerAction((x, y),hit))
        }

        if (hit == HitTypes.Miss) {
          // if the player misses, the next player has his turn
          Game.nextPlayer()
        } else if (hit == HitTypes.HitAndSunk) {
          // if the player sinks a ship, tell him which type of ship
          json += "shiptype" -> part.ship.getName
        }

        println("Player: "+ Game.players(Game.getOtherPlayer()).get +", Board:")
        board.get.printArray((x: Shippart) =>
          if (x == null)
            "."
          else if (x.isDestroyed)
            "X"
          else
            "0")

        Ok(new JSONObject(json).toString())
      } else {
        BadRequest("could not parse input data")
      }
    }
  }

}
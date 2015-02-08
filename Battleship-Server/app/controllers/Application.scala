package controllers

import models._
import play.api.libs.json._
import play.api.mvc._
import shared.Game

import scala.util.parsing.json.{JSONArray, JSONObject}

object Application extends Controller {

  case class ShipPlacement(userid: Int, x :String, y :String, direction :String, shiptype :String)
  case class Shot(userid: Int, x :String, y:String)

 /* object ShipPlacement {
    implicit val jsonReads: Reads[ShipPlacement] = (
      (__ \ 'userid).read[Int] and
        (__ \ 'x).read[String] and
        (__ \ 'y).read[String] and
        (__ \ 'direction).read[String] and
        (__ \ 'shiptype).read[String]
      )(ShipPlacement.apply _)
  }*/

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  /*implicit val readTuple: Reads[(Int)] = (
    (JsPath \ "userid").read[Int]// and
      //(JsPath \ "userid2").read[Int]
    )*/

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

  def placeShip =  Action(parse.json) { implicit request =>

//    request.body.asJson.map { json =>
//      json.validate[ShipPlacement].map{
//        case (x) => Ok("Hello " + x.shiptype + ", you're "+ x.userid)
//      }.recoverTotal{
//        e => BadRequest("Detected error:"+ JsError.toFlatJson(e))
//      }
//    }.getOrElse {
//      BadRequest("Expecting Json data")
//    }

    val placement = new ShipPlacement(request.body.\("userid").as[Int], request.body.\("x").as[String], request.body.\("y").as[String], request.body.\("direction").as[String], request.body.\("shiptype").as[String])

    val ship :Option[Ship] = placement.shiptype match {
      case "Battleship" => Option[Ship] {new Battleship()}
      case "AircraftCarrier" => Option[Ship] {new AircraftCarrier()}
      case "Destroyer" => Option[Ship] {new Destroyer()}
      case "PatrolBoat" =>Option[Ship] {new PatrolBoat()}
      case "Submarine" => Option[Ship] {new Submarine()}
      case _ => None
    }

    val board = Game.boards.get(placement.userid)

    if (board.isInstanceOf[Some[Board]] && ship.isInstanceOf[Some[Ship]]){
      if (Game.shipAvailable(placement.userid, ship.get)){
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

  def removeFirst[T](list: List[T])(pred: (T) => Boolean): List[T] = {
       val (before, atAndAfter) = list span (x => !pred(x))
       before ::: atAndAfter.drop(1)
  }

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

  def checkForOpponent = Action(parse.json) { implicit request =>

    request.body\("userid") match {
      case e: JsUndefined => BadRequest("must supply userid")
      case e: JsValue => {
        val userIndex = Game.matchUserId(request.body.\("userid").asOpt[Int].get)
        if (userIndex == None){
          BadRequest("userId not found")
        }else if (!Game.gameStarted){
          NotFound("game has not started yet")
        }else {
          var actionList = List[JSONObject]()
          Game.lastActions.foreach(a => actionList = actionList ::: a.toJSONObject :: Nil)
          if (Game.isWon != None){
            Ok(new JSONObject(Map("map" -> Game.boards.get(Game.players(userIndex.get).get).get.toString(), "actions" -> JSONArray(actionList), "won" -> Game.isWon.get)).toString())
          }else if (Game.currentPlayer == userIndex.get){
            Ok(new JSONObject(Map("map" -> Game.boards.get(Game.players(userIndex.get).get).get.toString(), "actions" -> JSONArray(actionList), "won" -> "None")).toString())
          }else {
            NotFound("it's not your turn yet")
          }
        }
      }
    }
  }

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
          Game.lastActions.append(new PlayerAction((x, y),hit, Some(part.ship.getName)))
        }else {
          Game.lastActions.append(new PlayerAction((x, y),hit))
        }

        if (hit == HitTypes.Miss) {
          Game.nextPlayer()
        } else if (hit == HitTypes.HitAndSunk) {
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
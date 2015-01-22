package controllers

import models._
import play.api.libs.json.{JsUndefined, JsValue}
import play.api.mvc._
import shared.Game

import scala.util.parsing.json.JSONObject

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
        if (Game.players(0) == None){
          Game.players(0) = userid
          Game.boards.put(userid.get, new Board())
          Ok(userid + " has been registered")
        }else if (Game.players(1) == None){
          if (Game.players(0).get == userid.get){
            Conflict("userid " + userid + " already registerd")
          }else {
            Game.players(1) = userid
            Game.boards.put(userid.get, new Board())
            Ok(userid + " has been registered")
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

    if (board.isInstanceOf[Some[Board]] && ship.isInstanceOf[Some[Ship]] && setShip(ship.get,board.get,placement.x,placement.y,placement.direction)){
      println("New Board:")
      board.get.printArray((x: Shippart) =>
        if(x == null)
          "W"
        else
          "S")
      Ok(new JSONObject(Map[String, Any]("map" -> board.get.toString())).toString())
    }else {
      BadRequest("could not parse input data")
    }
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

  def checkForOpponent = Action {
    Ok("hi")
  }

  def shoot = Action(parse.json) { implicit request =>

    val shot = new Shot(request.body.\("userid").as[Int], request.body.\("x").as[String], request.body.\("y").as[String])
    //TODO: check if current player
    val board = Game.boards.get(Game.players(Game.getOtherPlayer()).get)
    val y = CharacterCoordinate(shot.y)
    val x = Integer.parseInt(shot.x)
    if (board.isInstanceOf[Some[Board]] && x >= 0 && x <= 10){

      val hit = board.get.shoot(x, y)
      var json = Map[String, Any]("type" -> hit, "won" -> Game.isWon)
      val part = board.get.ships(y)(x)
      if (hit == HitTypes.Miss){
        Game.nextPlayer()
      }else if (hit == HitTypes.HitAndSunk){
        json += "shiptype" -> part.ship.getName
      }

      Ok(new JSONObject(json).toString())
    }else {
      BadRequest("could not parse input data")
    }
  }

}
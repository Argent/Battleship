package controllers

import play.api._
import play.api.libs.json._
import play.api.mvc._
import play.api.libs.functional.syntax._
import shared.Game

object Application extends Controller {

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
      case e: JsValue => Ok(e.toString())
    }
  }

  def placeShip = Action {
    Ok("hi")
  }

  def checkForOpponent = Action {
    Ok("hi")
  }

  def shoot = Action {
    Ok("hi")
  }
}
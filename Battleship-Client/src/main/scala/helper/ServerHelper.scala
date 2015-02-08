package helper

import java.net.URL

import model._
import uk.co.bigbeeconsultants.http.HttpClient
import uk.co.bigbeeconsultants.http.header.MediaType._
import uk.co.bigbeeconsultants.http.request.RequestBody

import scala.util.parsing.json.{JSON, JSONObject}


object ServerHelper {
  val httpClient = new HttpClient()

  val urls = Map(
    "register" -> new URL("http://localhost:9000/register"),
    "setship"  -> new URL("http://localhost:9000/place"),
    "poll"     -> new URL("http://localhost:9000/opponent"),
    "shoot"    -> new URL("http://localhost:9000/shoot")
  )

  def registerWithServer(id: Int): Option[List[ShipListItem]] = {
    val requestBody = RequestBody(new JSONObject(Map("userid" -> id)).toString(),
      APPLICATION_JSON)
    val response = httpClient.post(urls.get("register").get, Some(requestBody))

    if(!response.status.isSuccess) {
      None
    } else {
      println(response.body.asString)
      val jsonShipList = JSON.parseFull(response.body.asString).get.asInstanceOf[Map[String, List[Map[String, Any]]]].get("ships").get
      println(jsonShipList)
      Some(Ship.parseShiplist(jsonShipList, Nil).reverse)
    }
  }

  def setShip(shipType: String, x: String, y: String, d: String, id: Int): Option[String] = {
    val requestBody = RequestBody(new JSONObject(
      Map(
        "userid" -> id,
        "shiptype" -> shipType,
        "x" -> x,
        "y" -> y,
        "direction" -> d
      )
    ).toString(),
      APPLICATION_JSON)

    val response = httpClient.post(urls.get("setship").get, Some(requestBody))

    if(!response.status.isSuccess) {
      None
    } else {
      Some(JSON.parseFull(response.body.asString).get.asInstanceOf[Map[String, String]].get("map").get)
    }

  }

  def waitForOpponent(p: Player): PollResponse = {
    val requestBody = RequestBody(new JSONObject(Map("userid" -> p.id)).toString(),
      APPLICATION_JSON)

    val response = httpClient.put(urls.get("poll").get, requestBody)

    if(!response.status.isSuccess) {
      Thread.sleep(5000)
      waitForOpponent(p)
    } else {
      println(response.body.asString)
      PollResponse.parseFromJson(JSON.parseFull(response.body.asString).get.asInstanceOf[Map[String, Any]])
    }
  }

  def shoot(x: String, y: String, p: Player): ShootResponse = {
    val requestBody = RequestBody(new JSONObject(
      Map(
        "userid" -> p.id,
        "x" -> x,
        "y" -> y
      )).toString(),
      APPLICATION_JSON
    )

    val response = httpClient.put(urls.get("shoot").get, requestBody)

    if(!response.status.isSuccess) {
      println("Something went wrong, retrying..")
      Thread.sleep(10000)
      shoot(x, y, p)
    } else {
      ShootResponse.parseFromJson(JSON.parseFull(response.body.asString).get.asInstanceOf[Map[String, Any]])
    }
  }
}

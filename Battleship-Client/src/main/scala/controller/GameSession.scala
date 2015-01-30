package controller

import java.net.URL

import helper.ConsoleHelper
import models.WaterTypes.WaterTypes
import scala.util.parsing.json.{JSON, JSONArray, JSONObject}
import models.{HitTypes, Ship}
import models.WaterTypes
import uk.co.bigbeeconsultants.http.HttpClient
import uk.co.bigbeeconsultants.http.request.RequestBody
import uk.co.bigbeeconsultants.http.header.MediaType._



trait GameSession extends Session {
  val httpClient = new HttpClient()

  val urls = Map(
    "register" -> new URL("http://localhost:9000/register"),
    "setship"  -> new URL("http://localhost:9000/place"),
    "poll"     -> new URL("http://localhost:9000/opponent"),
    "shoot"    -> new URL("http://localhost:9000/shoot")
  )


  /**
   * Zu Beginn erstmal dem Server Bescheid sagen, dass ein Spiel gestartet werden
   * soll.
   */
  def registerWithServer(): Unit = {
    val id = scala.util.Random.nextInt(1000)

    println("Now registering with Server. ID -> " + id)

    val requestBody = RequestBody(new JSONObject(Map("userid" -> id)).toString(),
      APPLICATION_JSON)
    val response = httpClient.post(urls.get("register").get, Some(requestBody))

    if(!response.status.isSuccess) {
      println("Something really ugly happened. :(")
    }

    val player = new HumanPlayer(
      id,
      JSON.parseFull(response.body.asString).get.asInstanceOf[Map[String, List[Map[String, String]]]].get("ships").get
    )

    setShips(player)
  }

  def setShips(player: HumanPlayer): Unit = {
    val currentShip = player.shipsLeft.head
    val currentType = currentShip.get("name").get
    val numberLeft = currentShip.get("number").get.asInstanceOf[Double].toInt

    println("You're now setting a " + currentShip.get("name").get)

    val coords = ConsoleHelper.getShipCoordinatesFromConsole("Du setzt jetzt ein " + currentShip.get("name").get +
      " Bitte Koordinaten eingeben: ")

    val requestBody = RequestBody(new JSONObject(
      Map(
        "userid" -> player.id,
        "shiptype" -> currentType,
        "x" -> coords._2.toString,
        "y" -> coords._1.toString,
        "direction" -> coords._3.toString
      )
    ).toString(),
      APPLICATION_JSON)

    val response2 = httpClient.post(urls.get("setship").get, Some(requestBody))

    println("Still " + (numberLeft - 1.0) + " " + currentType + " to set!")

    if(numberLeft == 1 && player.shipsLeft.tail == Nil) {
      println("all ships set, let's go!")
    } else if(numberLeft == 1 && player.shipsLeft.tail != Nil) {
      setShips(new HumanPlayer(
        player.id,
        player.shipsLeft.tail
      ))
    } else {
      setShips(new HumanPlayer(
        player.id,
        Map("name" -> currentType, "form" -> currentShip.get("form").get, "number" -> (numberLeft - 1.0))
          ::player.shipsLeft.tail
      ))
    }
  }

  def waitForOpponent(id: Int): Unit = {
    println("Still waiting for opponent.")

    val requestBody = RequestBody(new JSONObject(Map("userid" -> id)).toString(),
      APPLICATION_JSON)

    val response = httpClient.put(urls.get("poll").get, requestBody)

    if(!response.status.isSuccess) {
      Thread.sleep(5000)
      waitForOpponent(id)
    }

    println("Opponent found. Start placing ships.")
    initSession(Nil)

  }

  abstract override def initSession(ships: List[Ship]): Unit = {

  println("Now placing a  ...")

/*
   val requestBody2 = RequestBody(new JSONObject(
       Map(
         "userid" -> id,
         "shiptype" -> "Battleship",
         "x" -> "0",
         "y" -> "A",
         "direction" -> "E"
       )
      ).toString(),
      APPLICATION_JSON)

    val response2 = httpClient.post(urls.get("setship").get, Some(requestBody2))
    println(response2.status)
    println(response2.body)





    val requestBody3 = RequestBody(new JSONObject(
        Map(
          "userid" -> id,
          "x" -> "0",
          "y" -> "A"
        )).toString(),
      APPLICATION_JSON
    )

    val response3 = httpClient.put(urls.get("shoot").get, requestBody3)
    println(response3.status)
    println(response3.body)


    /*val requestBody2 = RequestBody(new JSONObject(
      Map(
        "userid" -> id,
        "shiptype" -> "Battleship",
        "x" -> "0",
        "y" -> "A",
        "direction" -> "E"
      )
    ).toString(),
      APPLICATION_JSON)

    val response2 = httpClient.post(urls.get("setship").get, Some(requestBody2))
    println(response2.status)
    println(response2.body)*/



    /*
        if(players(0) == null) {
          players(0) = new HumanPlayer()
          players(1) = new AIPlayer()
        }

        println("Now setting " + ships.head)
        players(currentPlayer).setShip(ships.head, boards(currentPlayer))
        println("")
        println("Board of: " + players(currentPlayer).getClass())
        ConsoleHelper.printArray(boards(currentPlayer).ships,
          (x: Shippart) =>
            if(x == null)
              "W"
            else
              "S"
        )

        ships match {
          case x :: Nil if currentPlayer == 1 => {
            nextPlayer()
            runSession()
          }

          case x :: Nil => {
            nextPlayer()
            initSession(Session.generateShipSet())
          }

          case x :: xs => {
            initSession(xs)
          }
        }*/*/
  }

  override def runSession(): Unit = {
    println("An der Reihe: " + players(currentPlayer))
    println(boards(0).shots)
    println(boards(1).shots)

    val coords = players(currentPlayer).doTurn(boards(currentPlayer))
    val shootResult = boards(getOtherPlayer()).shoot(coords._1, coords._2)

    if (shootResult == HitTypes.Hit) {
      println(players(currentPlayer).getClass() + " - Schuss auf (" + coords._1 + ", " + coords._2 + "): Treffer!")
      println("Setting shot on  " + currentPlayer)
      boards(currentPlayer).setShot(coords._1, coords._2, WaterTypes.Hit)
    } else if (shootResult == HitTypes.Miss) {
      println(players(currentPlayer).getClass() + " - Schuss auf (" + coords._1 + ", " + coords._2 + "): kein Treffer!")
      println("Setting shot on  " + currentPlayer)
      boards(currentPlayer).setShot(coords._1, coords._2, WaterTypes.NoHit)
      nextPlayer()
    } else if (shootResult == HitTypes.HitAndSunk) {
      println(players(currentPlayer).getClass() + " - Schuss auf (" + coords._1 + ", " + coords._2 + "): Treffer und versenkt!")
      println("Versenkt wurde: " + boards(getOtherPlayer()).getShipAtCoordinates(coords._1, coords._2))
      println()
      println("Setting shot on  " + currentPlayer)
      boards(currentPlayer).setShot(coords._1, coords._2, WaterTypes.Hit)
    }


    println("Player 1 Board")
    ConsoleHelper.printArray(boards(0).shots,
      (x: WaterTypes) =>
        if (x == WaterTypes.Water)
          "_"
        else if (x == WaterTypes.Hit)
          "X"
        else
          "O"
    )

    println("Player 2 Board")
    ConsoleHelper.printArray(boards(1).shots,
      (x: WaterTypes) =>
        if (x == WaterTypes.Water)
          "_"
        else if (x == WaterTypes.Hit)
          "X"
        else
          "O"
    )

    isWon match {
      case None => runSession()
      case Some(s: Player) => println("Spieler " + s.getClass() + " hat gewonnen!")
    }
  }
}
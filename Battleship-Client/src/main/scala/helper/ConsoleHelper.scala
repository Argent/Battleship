package helper

import model.{Ship, PlayerAction, Board, HitType}
import model.HitType.HitType

import scala.io.StdIn._

object ConsoleHelper {

  def printShip(ship: Ship): Unit = {
    println("Du setzt jetzt ein " + ship.name)
    print("Form: ")
    ship.form.foreach(outer => {
      outer.foreach(inner => if(inner) print("S") else print("W"))
      println()
    })
  }


  def printHitMessage(myShots: Board): Unit = {
    println("Treffer, du bist nochmal dran!")
    printArray(myShots)
  }

  def printMissMessage(myShots: Board): Unit = {
    println("Daneben, der Gegner ist dran!")
    printArray(myShots)
  }

  def printVictoryMessage(myShots: Board): Unit = {
    println("Treffer und versenkt. Du hast die Flotte deines Gegner ausgelöscht!")
    printArray(myShots)
  }

  def printHitAndDestroyedMessage(myShots: Board, destroyed: String): Unit = {
    println("Super, du hast den " + destroyed + " des Gegners zerstört! Du bist nochmal dran!")
    printArray(myShots)

  }

  def printEnemysActions(actions: List[PlayerAction]): Unit = {
    println("Letzter Zug des Gegners:")
    actions.foreach(a => {
      println(
        a.hitType + " auf " + a.coords._2 + " " + a.coords._1 + "!" +
          (if(a.destroyed != None) { " (" + a.destroyed.get + " zerstört!)" } else {""})
      )
    })
  }

  def printBothBoards(myShips: Board, myShots: Board): Unit = {
    println("Dein Spielfeld: ")
    ConsoleHelper.printArray(myShips)
    println("Bisherige Schüsse")
    ConsoleHelper.printArray(myShots)
  }

  def printYourTurnMessage(myShips: Board, myShots: Board, actions: List[PlayerAction]): Unit = {
    println("Du bist dran!")
    printEnemysActions(actions)
    printBothBoards(myShips, myShots);
  }

  def printFirstTurnMessage(myShips: Board, myShots: Board): Unit = {
    println("Du bist dran!")
    printBothBoards(myShips, myShots);
  }

  def printDefeatMessage(board: Board, actions: List[PlayerAction]): Unit = {
    println("Du hast leider verloren. Deine Flotte wurde ausgelöscht.")
    printEnemysActions(actions)
    println("Deine Karte")
    ConsoleHelper.printArray(board)
  }

  def isYCoordinate(s: String): Boolean = {
    s.matches("[A-J]")
  }

  def isDirection(s: String): Boolean = {
    s.matches("[NESW]")
  }

  def isNumber(s: String): Boolean = {
    s.matches("[0-9]")
  }

  def getShipCoordinatesFromConsole(): (String, String, String) = {
    val prompt = "Gewünschte Position für das Schiff eingeben. Syntax (getrennt durch Leerzeichen): Y-Koordinate X-Koordinate Richtung "
    val userInput = getInputFromConsole(prompt)
    val splittedInput = userInput.split(" ")


    if(splittedInput.length != 3 || !isYCoordinate(splittedInput(0)) ||
      !isNumber(splittedInput(1)) || !isDirection(splittedInput(2))) {
      println("Ungültige Eingabe.")
      getShipCoordinatesFromConsole()
    } else {
      (
        splittedInput(0),
        splittedInput(1),
        splittedInput(2)
      )
    }
  }

  def getCoordinatesFromConsole(): (String, String) = {
    val prompt = "Wohin soll geschossen werden? Syntax (getrennt durch Leerzeichen): Y-Koordinate X-Koordinate "
    var userInput = getInputFromConsole(prompt)
    val splittedInput = userInput.split(" ")

    if(splittedInput.length != 2 || !isYCoordinate(splittedInput(0)) ||
      !isNumber(splittedInput(1))) {
      println("Ungültige Eingabe.")
      getCoordinatesFromConsole()
    } else {
      (
        splittedInput(0),
        splittedInput(1)
      )
    }
  }


  def getInputFromConsole(prompt: String): String = {
    print(s"$prompt ")
    readLine().trim match {
      case ""  => getInputFromConsole(prompt)
      case x:String => x
    }
  }


  def printArray(b: Board): Unit = {
    val topValues = Vector(" ", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
    val sideValues = List("A", "B", "C", "D", "E", "F", "G", "H", "I", "J")

    def getRepresentation(s: HitType): String = {
      s match {
        case HitType.None => "."
        case HitType.Hit => "X"
        case HitType.HitAndSunk => "X"
        case HitType.Miss => "O"
      }
    }

    def prependSideValues(left: List[String], right: IndexedSeq[IndexedSeq[String]]): List[IndexedSeq[String]] = {
      left match {
        case Nil => Nil
        case x::xs => (x +: right.head) :: prependSideValues(xs, right.tail)
      }
    }

    val stringRepresentation = for(i <- 0 until b.board.length)
                                  yield for(j <- 0 until b.board(0).length)
                                    yield getRepresentation(b.board(i)(j))

    val printString = topValues +: prependSideValues(sideValues, stringRepresentation)

    printString.foreach(item => {
      item.foreach(innerItem => print(innerItem + " "))
      println()
    })
  }

}

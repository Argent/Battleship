package helper

import models.Direction

import scala.io.StdIn.readLine

object ConsoleHelper {
  def getCoordinatesFromConsole(s: String) = {
    val userInput = getInputFromConsole(s)
    val splittedInput = userInput.split(" ")

    (splittedInput(0) match {
      case "A" => 0
      case "B" => 1
      case "C" => 2
      case "D" => 3
      case "E" => 4
      case "F" => 5
      case "G" => 6
      case "H" => 7
      case "I" => 8
      case "J" => 9
    },
    Integer.parseInt(splittedInput(1)),
    splittedInput(2) match {
      case "E" => Direction.E
      case "N" => Direction.N
      case "S" => Direction.S
      case "W" => Direction.W
    })
  }


  def getInputFromConsole(prompt: String): String = {
    print(s"$prompt ")
    readLine().trim match {
      case ""  => getInputFromConsole(prompt)
      case x:String => x
    }
  }

  def printArray[T](a: Array[Array[T]]): Unit = {
    println("0 1 2 3 4 5 6 7 8 9")
    a.foreach(x => {
      x.foreach(y => { print((if(y == null) "W" else "S") + " ")})
      println("")
    })
    println("")
  }
}
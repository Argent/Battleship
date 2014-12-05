package helper

import scala.io.StdIn.readLine

object ConsoleHelper {

  def getInputFromConsole(prompt: String): String = {
    print(s"$prompt ")
    readLine().trim match {
      case ""  => getInputFromConsole(prompt)
      case x:String => x
    }
  }

  def printArray[T](a: Array[Array[T]]): Unit = {
    a.foreach(x => {
      x.foreach(y => { print((if(y == null) "W" else "S") + " ")})
      println("")
    })
    println("")
  }
}
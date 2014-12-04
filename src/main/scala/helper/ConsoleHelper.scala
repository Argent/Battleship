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
}
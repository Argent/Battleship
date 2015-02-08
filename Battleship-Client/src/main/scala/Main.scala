import controller.Session
import helper.{ConsoleHelper, ServerHelper}
import model.{Board, Player}

/**
 * Created by Basti on 04.02.15.
 */
object Main extends App {

  new Session().registerWithServer()

  //ConsoleHelper.getShipCoordinatesFromConsole("hi ")

 // new Player(1, Nil).shoot()

 // Board.parseFromString(".......................XXXXX........................................................................")

}

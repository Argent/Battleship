package model

import model.HitType.HitType

case class Board(board: IndexedSeq[IndexedSeq[HitType]]) {

  def setShot(x: Int, y: Int, hitType: HitType): Board = {
    // creates a new two-dimensional array with <<hitType>> at <<x>>, <<y>>
    // and returns a new instance of Board
    Board(
      for(i <- 0 until board.length)
        yield for(j <- 0 until board(0).length)
          yield if(y == i && j == x) hitType else board(i)(j)
    )
  }

  def isUnshot(x: Int, y: Int): Boolean = {
    board(y)(x) == HitType.None
  }
}

object Board {
  val topValues = List(" ", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
  val sideValues = List('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J')

  // The server sends the board as a string. This method converts the string into an
  // instance of Board
  def parseFromString(s: String): Board = {

    val stringAsArray = s.toCharArray()

    def charToHitType(s: Char): HitType = {
      s match {
        case '.' => HitType.None
        case 'X' => HitType.Hit
        case 'O' => HitType.Miss
      }
    }

    // creates the array and replaces the strings with the appropriate HitType
    val asVector = for(i <- 0 until stringAsArray.length / 10)
                    yield for(j <- i*10 until (i*10 + 10))
                      yield charToHitType(stringAsArray(j))

    Board(asVector)
  }

  def generateEmptyBoard(): Board = {
    Board(
      for(i <- 0 to 9)
        yield for(j <- 0 to 9)
          yield HitType.None
    )
  }
}


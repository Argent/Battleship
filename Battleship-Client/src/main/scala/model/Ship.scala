package model

// an abstract representation of a ship, only consisting of a name and
// a form
// form is required, because the game supports arbitrary forms of ships
// which can be defined on server-side without client modifications

case class Ship(name: String, form: IndexedSeq[IndexedSeq[Boolean]])

object Ship {
  def parseShiplist(l: List[Map[String, Any]], s: List[ShipListItem]): List[ShipListItem] = {
    l match {
      case Nil
        => s
      case x::xs
        => parseShiplist(xs,
            ShipListItem(
              new Ship(
                x.get("name").get.asInstanceOf[String],
                coordinateListToArray(x.get("form").get.asInstanceOf[List[List[Double]]])
              ),
              x.get("number").get.asInstanceOf[Double].toInt
            )::s)
    }
  }

  // translates to coordinates sent by the server to a two-dimensional array for easy printing
  def coordinateListToArray(l: List[List[Double]]): IndexedSeq[IndexedSeq[Boolean]] = {
    val xMax = l.map(l => l.head.toInt).reduceLeft((x, y) => if (x > y) x else y)
    val yMax = l.map(l => l.tail.head.toInt).reduceLeft((x, y) => if (x > y) x else y)

    for (i <- 0 to yMax)
      yield for (j <- 0 to xMax)
        yield if (l.count(item => item.head.toInt == j && item.tail.head.toInt == i) != 0) { true } else { false }
  }
}

package model

import scala.collection.immutable.HashMap

/**
 *  This class allows for seamless conversion between character coordinates (y-Axis, A-J) and
 *  the associated int-Values (0-9)
 */

class CharacterCoordinate(val s: String)

object CharacterCoordinate {
  val translator = HashMap(("A", 0), ("B", 1), ("C", 2), ("D", 3),
    ("E", 4), ("F", 5), ("G", 6), ("H", 7), ("I", 8), ("J", 9))

  def apply(s: String) = {
    require(translator.map(_._1).toList.contains(s), "only characters from A-J are valid CharacterCoordinates")
    new CharacterCoordinate(s)
  }

  implicit def CharacterCoordinate2Int(c: CharacterCoordinate): Int = {
    translator.get(c.s).get
  }

  implicit def Int2CharacterCoordinate(i: Int): CharacterCoordinate = {
    CharacterCoordinate(translator.filter(_._2 == i).toList.head._1)
  }

  implicit def CharacterCoordinate2String(c: CharacterCoordinate): String = {
    c.s
  }

  implicit def String2CharacterCoordinate(s: String): CharacterCoordinate = {
    CharacterCoordinate(s)
  }
}

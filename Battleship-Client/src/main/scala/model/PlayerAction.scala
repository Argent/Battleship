package model

import model.HitType.HitType

// represents a PlayerAction (e.g. Hit on A 3). If HitAndSunk the name of the sunken Ship
// is provided
case class PlayerAction(coords: (String, String), hitType: HitType, destroyed: Option[String])

object PlayerAction {
  def parseFromJSON(j: List[Map[String, Any]]): List[PlayerAction] = {
    println(j)
    j match {
      case Nil => Nil
      case x::xs => {
        val coords = x.get("coordinates").get.asInstanceOf[List[String]]
        PlayerAction(
          (coords.head, coords.tail.head),
          HitType.withName(x.get("type").get.asInstanceOf[String]),
          if(x.contains("shiptype")) Some(x.get("shiptype").get.asInstanceOf[String]) else None
        ) :: parseFromJSON(xs)

      }
    }
  }
}
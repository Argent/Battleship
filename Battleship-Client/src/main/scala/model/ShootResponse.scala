package model

import model.HitType.HitType

// models the response to a shootaction, in case the action ws the winning action won
// is the player's id
case class ShootResponse(hitType: HitType, won: Option[Int], destroyed: Option[String])

object ShootResponse {
  def parseFromJson(j: Map[String, Any]): ShootResponse = {
    new ShootResponse(
      HitType.withName(j.get("type").get.asInstanceOf[String]),
      if(j.get("won").get.toString == "None") { None } else { Some(j.get("won").get.asInstanceOf[Double].toInt) },
      if(j.contains("shiptype")) Some(j.get("shiptype").get.asInstanceOf[String]) else None
    )
  }
}

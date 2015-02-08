package models

import models.HitTypes.HitTypes

import scala.util.parsing.json.{JSONArray, JSONObject}

/**
 * Created by ben on 06.02.15.
 */
class PlayerAction(val coords :(Int, Int), val hitType :HitTypes, val shipType :Option[String]) {
    def this(coords :(Int, Int), hitType :HitTypes) = this(coords, hitType, None)

    def toJSONObject :JSONObject = {
      shipType match{
        case None => JSONObject(Map("coordinates" -> new JSONArray(List[String](coords._1.toString, (coords._2 + 65).asInstanceOf[Char].toString)), "type" -> hitType.toString))
        case _ => new JSONObject(Map("coordinates" -> new JSONArray(List[String](coords._1.toString, (coords._2 + 65).asInstanceOf[Char].toString)), "type" -> hitType.toString, "shiptype" -> shipType.get))
      }
    }
}

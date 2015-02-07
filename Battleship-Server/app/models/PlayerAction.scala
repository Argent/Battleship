package models

import models.HitTypes.HitTypes

import scala.util.parsing.json.{JSONArray, JSONObject}

/**
 * Created by ben on 06.02.15.
 */
class PlayerAction(val coords :List[Int], val hitType :HitTypes, val shipType :Option[String]) {
    def this(coords :List[Int], hitType :HitTypes) = this(coords, hitType, None)

    def toJSONObject :JSONObject = {
      if (shipType == None){
        new JSONObject(Map("coordinates" -> new JSONArray(List(coords.head, (coords.tail.head + 65).asInstanceOf[Char])), "type" -> hitType.toString))
      }else {
        new JSONObject(Map("coordinates" -> new JSONArray(coords), "type" -> hitType.toString, "shiptype" -> shipType.get))
      }
    }
}

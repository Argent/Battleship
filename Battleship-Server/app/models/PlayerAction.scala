package models

import models.HitTypes.HitTypes

import scala.util.parsing.json.JSONObject

/**
 * Created by ben on 06.02.15.
 */
class PlayerAction(val coords :(Int,Int), val hitType :HitTypes, val shipType :Option[String]) {
    def this(coords :(Int,Int), hitType :HitTypes) = this(coords, hitType, None)

    def toJSONObject :JSONObject = {
      if (shipType == None){
        new JSONObject(Map("coordinates" -> (coords._1, (coords._2 + 65).asInstanceOf[Char]), "type" -> hitType))
      }else {
        new JSONObject(Map("coordinates" -> coords, "type" -> hitType, "shiptype" -> shipType.get))
      }
    }
}

package model

// represents a response to a poll
// There are two distinct types: with and without actions. Without actions represents
// the answer, the player gets, if the other player has set all of his ships (-> first turn)
// as soon as there are actions, it's not a "first turn"

case class PollResponse(board: Board, winner: Option[Int], actions: List[PlayerAction])

object PollResponse {
  def parseFromJson(j: Map[String, Any]): PollResponse = {
    new PollResponse(
      Board.parseFromString(j.get("map").get.asInstanceOf[String]),
      if(j.get("won").get.toString == "None") None else Some(j.get("won").get.asInstanceOf[Double].toInt),
      if(j.contains("actions"))
        PlayerAction.parseFromJSON(j.get("actions").get.asInstanceOf[List[Map[String, Any]]])
      else
        Nil
    )
  }
}

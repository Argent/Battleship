package models

/**
 * Created by Basti on 08.12.14.
 */
object HitTypes extends Enumeration {
  type HitTypes = Value

  val None, Hit, Miss, HitAndSunk = Value
}

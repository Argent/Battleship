package model

object HitType extends Enumeration {
  type HitType = Value

  val None = Value("None")
  val Hit = Value("Hit")
  val Miss = Value("Miss")
  val HitAndSunk = Value("HitAndSunk")

}

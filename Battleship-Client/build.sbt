name := "Battleship"

version := "1.0"

scalaVersion := "2.11.5"

libraryDependencies ++= Seq(
  "uk.co.bigbeeconsultants" %% "bee-client" % "0.28.+",
  "org.slf4j" % "slf4j-api" % "1.7.+",
  "ch.qos.logback" % "logback-core"    % "1.0.+",
  "ch.qos.logback" % "logback-classic" % "1.0.+"
)

libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.3"

resolvers += "Big Bee Consultants" at "http://repo.bigbeeconsultants.co.uk/repo"

    
package de.thm.arsnova.tutor

import de.thm.arsnova.models.Session

import io.gatling.core.Predef._ // 2
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spray.json._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import java.util.Calendar

object BasicTutorSimulation {
  import de.thm.arsnova.mappings.SessionJsonProtocol._

  val now = Calendar.getInstance.getTime.toString
  val newSession = Session(None, "12312312", 1, "A new Session", "ans", now, now, true, false, false, None)

  val createSession = exec(http("Tutor creates session")
    .post("/session/")
    .header("Content-Type", "application/json")
    .body(StringBody(newSession.toJson.toString)).asJSON)
}
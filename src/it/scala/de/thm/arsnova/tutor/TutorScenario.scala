package de.thm.arsnova.tutor

import de.thm.arsnova.models.{AnswerOption, Question, Session}

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spray.json._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import java.util.Calendar

trait TutorScenario {
  import de.thm.arsnova.mappings.QuestionJsonProtocol._
  import de.thm.arsnova.mappings.SessionJsonProtocol._

  val now = Calendar.getInstance.getTime.toString
  val basicNewSession = Session(None, "12312312", 1, "A new Session", "ans", now, now, true, false, false, None)

  def createSession(session: Session) = exec(
    http("Tutor creates session")
      .post("/session/")
      .header("Content-Type", "application/json")
      .body(StringBody(session.toJson.toString)).asJSON
      .check(bodyString.saveAs("sessionId"))
  ).pause(4)

  def createQuestion(question: Question, name: String) = exec(
    http(s"Tutor creates ${name} question")
      .post("/session/${sessionId}/question")
      .header("Content-Type", "application/json")
      .body(StringBody(question.toJson.toString)).asJSON
  ).pause(4)
}
package de.thm.arsnova.tutor

import de.thm.arsnova.models.{AnswerOption, Question, Session}

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spray.json._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import java.util.Calendar

object FlashcardTutorScenario {
  import de.thm.arsnova.mappings.QuestionJsonProtocol._
  import de.thm.arsnova.mappings.SessionJsonProtocol._

  val now = Calendar.getInstance.getTime.toString
  val newSession = Session(None, "12312312", 1, "A new Session", "ans", now, now, true, false, false, None)

  val mcAnswerOptions = Seq(
    AnswerOption(None, None, false, "12", -10),
    AnswerOption(None, None, true, "13", 10),
    AnswerOption(None, None, false, "14", -10),
    AnswerOption(None, None, true, "thirteen", 10)
  )

  val newMCQuestion = Question(None, 0, "new Question Subject", "This is an MC question for stress testing",
    "preparation", "mc", Some("This is the hint!"), Some("The answer is 13"), true, false, true, true, false, None, Some(mcAnswerOptions))

  val scn = scenario("Flashcard Tutor").exec(
    http("Tutor creates session")
      .post("/session/")
      .header("Content-Type", "application/json")
      .body(StringBody(newSession.toJson.toString)).asJSON
      .check(bodyString.saveAs("sessionId"))
  ).pause(4).exec(
    http("Tutor creates mc question")
      .post("/session/${sessionId}/question")
      .header("Content-Type", "application/json")
      .body(StringBody(newMCQuestion.toJson.toString)).asJSON
  )
}
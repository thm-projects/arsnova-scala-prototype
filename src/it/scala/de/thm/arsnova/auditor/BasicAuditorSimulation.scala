package de.thm.arsnova.auditor

import de.thm.arsnova.models.ChoiceAnswer

import io.gatling.core.Predef._ // 2
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spray.json._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

object BasicAuditorSimulation {
  import de.thm.arsnova.mappings.ChoiceAnswerJsonProtocol._

  val joinSession = exec(http("Auditor joins session")
    .get("/session/1"))

  val getAllPrepQuestions = exec(http("Auditor gets all preparation questions")
    .get("/question/")
    .queryParam("sessionid", "1")
    .queryParam("variant", "preparation"))

  val newAnswer = ChoiceAnswer(None, 5, 1, 2)

  val answerToMCQuestion = exec(http("Auditor answers mc question")
    .post("/question/5/choiceAnswer")
    .header("Content-Type", "application/json")
    .body(StringBody(newAnswer.toJson.toString)).asJSON)
}
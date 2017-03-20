package de.thm.arsnova

import de.thm.arsnova.services.{BaseService, SessionService}
import de.thm.arsnova.models._
import de.thm.arsnova.api.FreetextAnswerApi

import akka.http.scaladsl.model.{HttpEntity, MediaTypes, StatusCode}
import org.scalatest.concurrent.ScalaFutures
import spray.json._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.Matchers
import org.scalatest._
import scala.concurrent.{Future, Await}
import scala.concurrent.duration._
import akka.http.scaladsl.server.MissingQueryParamRejection

trait FreetextAnswerApiSpec extends FunSpec with Matchers with ScalaFutures with BaseService with ScalatestRouteTest with Routes
    with TestData with FreetextAnswerApi {
  import de.thm.arsnova.mappings.FreetextAnswerJsonProtocol._
  describe("FreetextAnswer api") {
    it("retrieve freetext answers for question") {
      Get("/question/1/freetextAnswer") ~> freetextAnswerApi ~> check {
        responseAs[JsObject] should be(freetextAnswerAdapter.toResources(testFreetextAnswersForQuestionOne))
      }
    }
    it("return empty seq when question is not a choicequestion") {
      Get("/question/5/freetextAnswer") ~> freetextAnswerApi ~> check {
        responseAs[JsObject] should be(JsObject(List(
          "data" -> JsArray(),
          "links" -> JsArray()
        )))
      }
    }
    it("create freetext answer correctly") {
      val questionId = testFreetextAnswers.head.questionId
      val sessionId = testFreetextAnswers.head.sessionId
      val subject = "postSubject"
      val text = "postText"
      val newFreetextAnswer = FreetextAnswer(None, questionId, sessionId, subject, text)
      val requestEntity = HttpEntity(MediaTypes.`application/json`, newFreetextAnswer.toJson.toString)
      Post("/question/" + questionId.toString + "/freetextAnswer", requestEntity) ~> freetextAnswerApi ~> check {
        response.status should be(OK)
        val newId: String =  Await.result(Unmarshal(response.entity).to[String], 1.second)
        Get("/question/" + questionId.toString + "/freetextAnswer/" + newId) ~> freetextAnswerApi ~> check {
          val checkFreetextAnswer = newFreetextAnswer.copy(id = Some(newId.toLong))
          responseAs[JsObject] should be(freetextAnswerAdapter.toResource(checkFreetextAnswer))
        }
      }
    }
    it("update freetext answer") {
      val freetextAnswer = testFreetextAnswers.drop(1).head
      val updatedSubject = "updatedSubject"
      val updatedText = "updatedText"
      val updatedFreetextAnswer = freetextAnswer.copy(subject = updatedSubject, text = updatedText)
      val requestEntity = HttpEntity(MediaTypes.`application/json`, updatedFreetextAnswer.toJson.toString)
      Put("/question/" + freetextAnswer.questionId + "/freetextAnswer/" + freetextAnswer.id.get, requestEntity) ~> freetextAnswerApi ~> check {
        response.status should be(OK)
        Get("/question/" + freetextAnswer.questionId.toString + "/freetextAnswer/" + freetextAnswer.id.get) ~> freetextAnswerApi ~> check {
          responseAs[JsObject] should be(freetextAnswerAdapter.toResource(updatedFreetextAnswer))
        }
      }
    }
    it("delete freetext answer") {
      val freetextAnswerId = testFreetextAnswers.head.id.get
      val questionId = testFreetextAnswers.head.questionId
      Delete("/question/" + questionId + "/freetextAnswer/" + freetextAnswerId) ~> freetextAnswerApi ~> check {
        response.status should be(OK)
        Get("/question/" + questionId + "/freetextAnswer/" + freetextAnswerId) ~> freetextAnswerApi ~> check {
          response.status should be(NotFound)
        }
      }
    }

    // last test
    it("delete all freetext answers by questionid") {
      val questionId = testFreetextAnswersForQuestionOne.head.questionId
      Delete("/question/" + questionId + "/freetextAnswer/") ~> freetextAnswerApi ~> check {
        response.status should be(OK)
        Get("/question/" + questionId + "/freetextAnswer/") ~> freetextAnswerApi ~> check {
          responseAs[JsObject] should be(JsObject(List(
            "data" -> JsArray(),
            "links" -> JsArray()
          )))
        }
      }
    }
  }
}

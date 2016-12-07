import akka.http.scaladsl.model.{HttpEntity, MediaTypes, StatusCode}
import services.{BaseService, SessionService}
import models._
import org.scalatest.concurrent.ScalaFutures
import spray.json._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import models._
import api.ChoiceAnswerApi
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.unmarshalling.Unmarshal

import scala.concurrent.Future
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.Matchers
import services.BaseService
import org.scalatest._
import scala.concurrent.Await
import scala.concurrent.duration._
import akka.http.scaladsl.server.MissingQueryParamRejection

trait ChoiceAnswerApiSpec extends FunSpec with Matchers with ScalaFutures with BaseService with ScalatestRouteTest with Routes
    with TestData with ChoiceAnswerApi {
  import mappings.ChoiceAnswerJsonProtocol._
  describe("ChoiceAnswer api") {
    it("retrieve choice answers for question") {
      Get("/question/5/choiceAnswer") ~> choiceAnswerApi ~> check {
        responseAs[JsObject] should be(choiceAnswerAdapter.toResources(testChoiceAnswers))
      }
    }
    it("return empty data set when question is no choicequestion") {
      Get("/question/1/choiceAnswer") ~> choiceAnswerApi ~> check {
        responseAs[JsObject] should be(JsObject(List(
          "data" -> JsArray(),
          "links" -> JsArray()
        )))
      }
    }
    it("create choice answer correctly") {
      val questionId = testChoiceAnswers.head.questionId
      val sessionId = testChoiceAnswers.head.sessionId
      val answerOptionId = testAnswerOptions.head.id.get
      val newChoiceAnswer = ChoiceAnswer(None, questionId, sessionId, answerOptionId)
      val requestEntity = HttpEntity(MediaTypes.`application/json`, newChoiceAnswer.toJson.toString)
      Post("/question/" + questionId.toString + "/choiceAnswer", requestEntity) ~> choiceAnswerApi ~> check {
        response.status should be(OK)
        val newId: String =  Await.result(Unmarshal(response.entity).to[String], 1.second)
        Get("/question/" + questionId.toString + "/choiceAnswer/" + newId) ~> choiceAnswerApi ~> check {
          val checkChoiceAnswer = newChoiceAnswer.copy(id = Some(newId.toLong))
          responseAs[JsObject] should be(choiceAnswerAdapter.toResource(checkChoiceAnswer))
        }
      }
    }
    it("update choice answer") {
      val choiceAnswer = testChoiceAnswers.drop(1).head
      val newAnswerOptionId = testAnswerOptions.drop(1).head.id.get
      val updatedChoiceAnswer = choiceAnswer.copy(answerOptionId = newAnswerOptionId)
      val requestEntity = HttpEntity(MediaTypes.`application/json`, updatedChoiceAnswer.toJson.toString)
      Put("/question/" + choiceAnswer.questionId + "/choiceAnswer/" + choiceAnswer.id.get, requestEntity) ~> choiceAnswerApi ~> check {
        response.status should be(OK)
        Get("/question/" + choiceAnswer.questionId.toString + "/choiceAnswer/" + choiceAnswer.id.get) ~> choiceAnswerApi ~> check {
          responseAs[JsObject] should be(choiceAnswerAdapter.toResource(updatedChoiceAnswer))
        }
      }
    }
    it("delete choice answer") {
      val choiceAnswerId = testChoiceAnswers.head.id.get
      val questionId = testChoiceAnswers.head.questionId
      Delete("/question/" + questionId + "/choiceAnswer/" + choiceAnswerId) ~> choiceAnswerApi ~> check {
        response.status should be(OK)
        Get("/question/" + questionId + "/choiceAnswer/" + choiceAnswerId) ~> choiceAnswerApi ~> check {
          response.status should be(NotFound)
        }
      }
    }

    // last test
    it("delete all choice answers by questionid") {
      val questionId = testChoiceAnswers.head.questionId
      Delete("/question/" + questionId + "/freetextAnswer/") ~> freetextAnswerApi ~> check {
        response.status should be(OK)
        Get("/question/" + questionId + "/freetextAnswer/") ~> freetextAnswerApi ~> check {
          responseAs[JsArray] should be(JsArray())
        }
      }
    }
  }
}

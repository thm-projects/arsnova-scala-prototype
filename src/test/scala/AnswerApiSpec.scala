import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json._
import akka.http.scaladsl.model.{HttpEntity, MediaTypes, StatusCode}
import services.{BaseService, SessionService}
import models._
import org.scalatest.concurrent.ScalaFutures
import models._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.unmarshalling.Unmarshal

import scala.concurrent.Future
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.Matchers
import services.BaseService
import org.scalatest._
import akka.http.scaladsl.server.MissingQueryParamRejection

trait AnswerApiSpec extends FunSpec with Matchers with ScalaFutures with BaseService with ScalatestRouteTest with Routes with TestData {
  import mappings.FreetextAnswerJsonProtocol._
  import mappings.ChoiceAnswerJsonProtocol._
  describe("Answer api") {
    it("retrieve freetext answers for question") {
      Get("/question/1/freetextAnswer") ~> freetextAnswerApi ~> check {
        responseAs[JsArray] should be(testFreetextAnswers.toJson)
      }
    }
    it("retrieve choice answers for question") {
      Get("/question/5/choiceAnswer") ~> choiceAnswerApi ~> check {
        responseAs[JsArray] should be(testChoiceAnswers.toJson)
      }
    }
    it("block request for choice answer when question is not a choicequestion") {
      Get("/question/5/freetextAnswer") ~> freetextAnswerApi ~> check {
        handled shouldBe false
        //rejection shouldBe MissingQueryParamRejection("user")
      }
    }
    it("block request for freetext answer when question is a choicequestion") {
      Get("/question/1/choiceAnswer") ~> choiceAnswerApi ~> check {
        handled shouldBe false
        //rejection shouldBe MissingQueryParamRejection("user")
      }
    }
  }
}

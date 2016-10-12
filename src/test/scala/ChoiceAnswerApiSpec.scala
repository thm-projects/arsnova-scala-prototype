import akka.http.scaladsl.model.{HttpEntity, MediaTypes, StatusCode}
import services.{BaseService, SessionService}
import models._
import org.scalatest.concurrent.ScalaFutures
import spray.json._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
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

trait ChoiceAnswerApiSpec extends FunSpec with Matchers with ScalaFutures with BaseService with ScalatestRouteTest with Routes with TestData {
  import mappings.ChoiceAnswerJsonProtocol._
  describe("ChoiceAnswer api") {
    it("retrieve choice answers for question") {
      Get("/question/5/choiceAnswer") ~> choiceAnswerApi ~> check {
        responseAs[JsArray] should be(testChoiceAnswers.toJson)
      }
    }
    it("return empty seq when question is a choicequestion") {
      Get("/question/1/choiceAnswer") ~> choiceAnswerApi ~> check {
        responseAs[JsArray] should be(JsArray())
      }
    }
  }
}

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

trait FreetextAnswerApiSpec extends FunSpec with Matchers with ScalaFutures with BaseService with ScalatestRouteTest with Routes with TestData {
  import mappings.FreetextAnswerJsonProtocol._
  describe("FreetextAnswer api") {
    it("retrieve freetext answers for question") {
      Get("/question/1/freetextAnswer") ~> freetextAnswerApi ~> check {
        responseAs[JsArray] should be(testFreetextAnswersForQuestionOne.toJson)
      }
    }
    it("return empty seq when question is not a choicequestion") {
      Get("/question/5/freetextAnswer") ~> freetextAnswerApi ~> check {
        responseAs[JsArray] should be(JsArray())
      }
    }
  }
}

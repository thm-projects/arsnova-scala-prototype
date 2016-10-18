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
import scala.concurrent.Await
import scala.concurrent.duration._
import akka.http.scaladsl.server.MissingQueryParamRejection

trait CommentApiSpec extends FunSpec with Matchers with ScalaFutures with BaseService with ScalatestRouteTest with Routes with TestData {
  import mappings.CommentJsonProtocol._

  describe("Comment api") {
    it("retrieve comment by id") {
      Get("/comment/1") ~> commentApi ~> check {
        responseAs[JsObject] should be(testComments.head.toJson)
      }
    }
    it("retrieve all comments by session id") {
      Get("/session/1/comment") ~> commentApi ~> check {
        responseAs[JsArray] should be(testComments.toJson)
      }
    }
    it
  }
}
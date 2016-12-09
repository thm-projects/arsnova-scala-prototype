import akka.http.scaladsl.model.{HttpEntity, MediaTypes, StatusCode}
import services.{BaseService, SessionService}
import models._
import api.SessionApi
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

trait SessionApiSpec extends FunSpec with Matchers with ScalaFutures with BaseService with ScalatestRouteTest with Routes with TestData with SessionApi {
  import mappings.SessionJsonProtocol._

  describe("Session api") {
    it("retrieve sessions for user 1") {
      Get("/session/?user=1") ~> sessionApi ~> check {
        responseAs[JsObject] should be(sessionAdapter.toResources(testSessionsForUser1))
      }
    }
    it("retrieve session by id") {
      Get("/session/1/") ~> sessionApi ~> check {
        responseAs[JsObject] should be(sessionAdapter.toResource(testSessions.head))
      }
    }
    it("should deny invalid route") {
      Get("/session/") ~> sessionApi ~> check {
        handled shouldBe false
        rejection shouldBe MissingQueryParamRejection("user")
      }
      Get("/session/einself") ~> sessionApi ~> check {
        handled shouldBe false
      }
    }
    it("create session properly") {
      val newSessionTitle = "newTitle"
      val newSessionShortTitle = "newShortTitle"
      val requestEntity = HttpEntity(MediaTypes.`application/json`,
        JsObject(
          "key" -> JsString("55555555"),
          "userId" -> JsNumber(testUsers.head.id.get),
          "title" -> JsString(newSessionTitle),
          "shortTitle" -> JsString(newSessionShortTitle)
        ).toString())
      Post("/session", requestEntity) ~> sessionApi ~> check {
        response.status should be(OK)
        val newSessionId: Future[String] = Unmarshal(response.entity).to[String]
        newSessionId.onSuccess { case id =>
          Get("/session/" + id.toString) ~> sessionApi ~> check {
            val checkSession: Future[Session] = Unmarshal(response.entity).to[Session]
            checkSession.onSuccess { case session =>
              session.title should be(newSessionTitle)
            }
          }
        }
      }
    }
    it("update session by id") {
      val updatedTitle = "UpdatedTitle"
      val session = testSessions.head
      val updatedSession = session.copy(title = updatedTitle)
      val requestEntity = HttpEntity(MediaTypes.`application/json`, updatedSession.toJson.toString)
      Put("/session/" + session.id.get.toString, requestEntity) ~> sessionApi ~> check {
        response.status should be(OK)
        Get("/session/" + session.id.get.toString) ~> sessionApi ~> check {
          val checkSession: Future[Session] = Unmarshal(response.entity).to[Session]
          checkSession.onSuccess { case session =>
            session should be(updatedSession)
          }
        }
      }
    }
    it("delete session by id") {
      val userSessionId = testSessionsForUser2.head.id.get
      Delete("/session/" + userSessionId.toString) ~> sessionApi ~> check {
        response.status should be(OK)
        Get("/session/" + userSessionId) ~> sessionApi ~> check {
          response.status should be(NotFound)
        }
      }
    }
  }
}

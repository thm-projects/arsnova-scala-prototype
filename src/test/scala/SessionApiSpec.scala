import akka.http.scaladsl.model.{StatusCode, MediaTypes, HttpEntity}
import services.SessionService
import models._
import org.scalatest.concurrent.ScalaFutures
import spray.json._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import models._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.unmarshalling.Unmarshal
import scala.concurrent.Future
import akka.http.scaladsl.model.StatusCodes._

class SessionApiSpec extends BaseServiceSpec with ScalaFutures {
  import mappings.SessionJsonProtocol._
  "Session api" should {
    "retrieve sessions for user 1" in {
      Get("/session/?user=1") ~> sessionApi ~> check {
        responseAs[JsArray] should be(testSessionsForUser1.toJson)
      }
    }
    "retrieve session by id" in {
      Get("/session/1/") ~> sessionApi ~> check {
        responseAs[JsObject] should be(testSessions.head.toJson)
      }
    }
    "create session properly" in {
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
            responseAs[JsObject].asInstanceOf[Session].title should be(JsString(newSessionTitle))
          }
        }
      }
    }
    "update session by id" in {
      val updatedTitle = "UpdatedTitle"
      val session = testSessions.head
      val updatedSession = session.copy(title = updatedTitle)
      val requestEntity = HttpEntity(MediaTypes.`application/json`, updatedSession.toJson.toString)
      Put("/session/" + session.id.get.toString, requestEntity) ~> sessionApi ~> check {
        response.status should be(OK)
        Get("/session/" + session.id.get.toString) ~> sessionApi ~> check {
          val checkSession: Future[Session] = Unmarshal(response.entity).to[Session]
          println(response.toString)
          checkSession.onSuccess { case session =>
            session should be(updatedSession)
          }
        }
      }
    }
    "delete session by id" in {
      val userSessionId = testSessionsForUser2.head.id.get
      Delete("/session/" + userSessionId.toString) ~> sessionApi ~> check {
        response.status should be(OK)
        Get("/session/?user=2") ~> sessionApi ~> check {
          responseAs[Seq[Session]] should have length 1
        }
      }
    }
  }
}

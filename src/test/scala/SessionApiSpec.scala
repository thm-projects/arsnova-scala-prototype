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

class SessionApiSpecSpec extends BaseServiceSpec with ScalaFutures {
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
          "key" -> JsString("44444444"),
          "userId" -> JsNumber(testUsers.head.id.get),
          "title" -> JsString(newSessionTitle),
          "shortTitle" -> JsString(newSessionShortTitle)
        ).toString())
      Post("/session", requestEntity) ~> sessionApi ~> check {
        val httpOkCode = 200
        response.status should be(StatusCode.int2StatusCode(httpOkCode))
        val newSessionId: Future[String] = Unmarshal(response.entity).to[String]
        newSessionId.onSuccess { case id =>
          val newSession = SessionService.findById(id.toLong)
          newSession.onSuccess {
            case session => session.title should be(newSessionTitle)
          }
        }
      }
    }
    /*"update comment by id" in {
      val newContent = "UpdatedContent"
      val requestEntity = HttpEntity(MediaTypes.`application/json`,
        JsObject(
          "postId" -> JsNumber(testPosts.head.id.get),
          "userId" -> JsNumber(testUsers.head.id.get),
          "content" -> JsString(newContent)
        ).toString())
      Put("/users/1/posts/1/comments/1", requestEntity) ~> commentsApi ~> check {
        response.status should be(StatusCode.int2StatusCode(200))
        whenReady(CommentsDao.findById(1,1, 1)) { result =>
          result.content should be(newContent)
        }
      }
    }
    "delete comment by id" in {
      Delete("/comments/1") ~> commentsApi ~> check {
        response.status should be(StatusCode.int2StatusCode(200))
        Get("/users/1/posts/1/comments") ~> commentsApi ~> check {
          responseAs[Seq[Comment]] should have length 1
        }
      }
    }*/
  }
}

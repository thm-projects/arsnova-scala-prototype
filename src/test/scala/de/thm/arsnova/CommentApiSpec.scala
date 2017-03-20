package de.thm.arsnova

import de.thm.arsnova.services.{BaseService, SessionService}
import de.thm.arsnova.models._
import de.thm.arsnova.api.CommentApi

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

trait CommentApiSpec extends FunSpec with Matchers with ScalaFutures with BaseService with ScalatestRouteTest with Routes
    with TestData with CommentApi {
  import de.thm.arsnova.mappings.CommentJsonProtocol._

  describe("Comment api") {
    it("retrieve comment by id") {
      Get("/comment/1") ~> commentApi ~> check {
        responseAs[JsObject] should be(commentAdapter.toResource(testComments.head))
      }
    }
    it("retrieve all comments by session id") {
      Get("/session/1/comment") ~> commentApi ~> check {
        responseAs[JsObject] should be(commentAdapter.toResources(testComments))
      }
    }
    it("create comment") {
      val subject = "postSubject"
      val text = "postText"
      val postComment = Comment(None, 1, 1, false, subject, text, "111111111")
      val requestEntity = HttpEntity(MediaTypes.`application/json`, postComment.toJson.toString)
      Post("/comment/", requestEntity) ~> commentApi ~> check {
        response.status should be(OK)
        val newId = Await.result(Unmarshal(response.entity).to[String], 1.second).toLong
        val checkComment = postComment.copy(id = Some(newId))
        Get("/comment/" + newId) ~> commentApi ~> check {
          responseAs[JsObject] should be(commentAdapter.toResource(checkComment))
        }
      }
    }
    it("update comment") {
      val comment = testComments.head
      val updateComment = comment.copy(subject = "updateSubject", text = "updateText")
      val requestEntity = HttpEntity(MediaTypes.`application/json`, updateComment.toJson.toString)
      Put("/comment/" + comment.id.get, requestEntity) ~> commentApi ~> check {
        response.status should be(OK)
        Get("/comment/" + comment.id.get) ~> commentApi ~> check {
          responseAs[JsObject] should be(commentAdapter.toResource(updateComment))
        }
      }
    }
    it("delete comment") {
      val commentId = testComments.head.id.get
      Delete("/comment/" + commentId) ~> commentApi ~> check {
        response.status should be(OK)
        Get("/comment/" + commentId) ~> commentApi ~> check {
          response.status should be(NotFound)
        }
      }
    }
  }
}

package api

import services.CommentService

import scala.concurrent.ExecutionContext.Implicits.global
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import models._
import akka.http.scaladsl.server.Directives._
import spray.json._

trait CommentApi {
  import mappings.CommentJsonProtocol._

  val commentApi = pathPrefix("comment") {
    pathEndOrSingleSlash {
      post {
        entity(as[Comment]) { comment =>
          complete (CommentService.create(comment).map(_.toJson))
        }
      }
    } ~
    pathPrefix(IntNumber) { commentId =>
      pathEndOrSingleSlash {
        get {
          complete (CommentService.getById(commentId).map(_.toJson))
        } ~
        put {
          entity(as[Comment]) { comment =>
            complete (CommentService.update(comment).map(_.toJson))
          }
        } ~
        delete {
          complete (CommentService.delete(commentId).map(_.toJson))
        }
      }
    }
  } ~
  pathPrefix("session") {
    pathPrefix(IntNumber) { sessionId =>
      path("comment") {
        get {
          complete (CommentService.getBySessionId(sessionId).map(_.toJson))
        }
      }
    }
  }
}

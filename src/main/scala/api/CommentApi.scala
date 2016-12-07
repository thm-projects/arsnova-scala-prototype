package api

import services.CommentService

import scala.concurrent.ExecutionContext.Implicits.global
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import models._
import akka.http.scaladsl.server.Directives._
import spray.json._

import hateoas.{ApiRoutes, ResourceAdapter, Link}

trait CommentApi {
  import mappings.CommentJsonProtocol._

  def commentSelfLink(comment: Comment): Link = {
    Link("self", s"/comment/${comment.id.get}")
  }

  val commentAdapter = new ResourceAdapter[Comment](commentFormat, commentSelfLink)

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
          complete (CommentService.getById(commentId).map(commentAdapter.toResource(_)))
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
          complete (CommentService.getBySessionId(sessionId).map(commentAdapter.toResources(_)))
        }
      }
    }
  }
}

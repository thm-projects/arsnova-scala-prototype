package api

import services.CommentService

import scala.concurrent.ExecutionContext.Implicits.global
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import models._
import akka.http.scaladsl.server.Directives._
import spray.json._

import hateoas.{ApiRoutes, ResourceAdapter, Link}

/*
The API Interface regarding comments (formerly known as "interposed question"), made by participants.
 */
trait CommentApi {
  // protocol for serializing data
  import mappings.CommentJsonProtocol._

  // add the "top level" endpoint to ApiRoutes
  ApiRoutes.addRoute("comment", "comment")

  // function to generate the model links
  def commentLinks(comment: Comment): Seq[Link] = {
    Seq(
      Link("self", s"/${ApiRoutes.getRoute("comment")}/${comment.id.get}")
    )
  }

  // the HATEOAS Adapter
  val commentAdapter = new ResourceAdapter[Comment](commentFormat, commentLinks)

  val commentApi = pathPrefix(ApiRoutes.getRoute("comment")) {
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
  pathPrefix(ApiRoutes.getRoute("session")) {
    pathPrefix(IntNumber) { sessionId =>
      path(ApiRoutes.getRoute("comment")) {
        get {
          complete (CommentService.getBySessionId(sessionId).map(commentAdapter.toResources(_)))
        }
      }
    }
  }
}

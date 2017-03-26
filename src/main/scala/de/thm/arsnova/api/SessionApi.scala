package de.thm.arsnova.api

import de.thm.arsnova.services.SessionService
import de.thm.arsnova.models._
import de.thm.arsnova.hateoas.{ApiRoutes, ResourceAdapter, Link}

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import spray.json._


/*
The API Interface regarding sessions, the core component for arsnova.voting.
 */
trait SessionApi {
  import de.thm.arsnova.Context.executor

  // protocol for serializing data
  import de.thm.arsnova.mappings.SessionJsonProtocol._

  // add the "top level" endpoint to ApiRoutes
  ApiRoutes.addRoute("session", "session")

  // function to generate the model links
  def sessionLinks(session: Session): Seq[Link] = {
    Seq(
      Link("self", s"/${ApiRoutes.getRoute("session")}/${session.id.get}"),
      Link("features", s"/${ApiRoutes.getRoute("session")}/${session.id.get}/${ApiRoutes.getRoute("features")}"),
      Link("comments", s"/${ApiRoutes.getRoute("session")}/${session.id.get}/${ApiRoutes.getRoute("comment")}")
    )
  }

  // the HATEOAS Adapter
  val sessionAdapter = new ResourceAdapter[Session](sessionFormat, sessionLinks)

  val sessionApi = pathPrefix(ApiRoutes.getRoute("session")) {
    pathEndOrSingleSlash {
      get {
        parameter("user".as[UserId]) { (userId) =>
          ApiRoutes.addRoute("getUserSession", "/session/?user=<username>")
          complete (SessionService.findUserSessions(userId).map(sessionAdapter.toResources(_)))
        }
      } ~
      post {
        entity(as[Session]) { session =>
          complete (SessionService.create(session).map(_.toJson))
        }
      }
    } ~
    pathPrefix(IntNumber) { sessionId =>
      pathEndOrSingleSlash {
        get {
          ApiRoutes.addRoute("getSession", "/session/<id>")
          complete (SessionService.findById(sessionId).map(sessionAdapter.toResource(_)))
        } ~
          put {
            entity(as[Session]) { session =>
              complete (SessionService.update(session, sessionId).map(_.toJson))
            }
          } ~
          delete {
            complete (SessionService.delete(sessionId).map(_.toJson))
          }
      }
    }
  }
}

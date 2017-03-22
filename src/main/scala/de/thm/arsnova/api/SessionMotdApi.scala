package de.thm.arsnova.api

import de.thm.arsnova.services.SessionMotdService
import de.thm.arsnova.models._
import de.thm.arsnova.hateoas.{ApiRoutes, ResourceAdapter, Link}

import scala.concurrent.ExecutionContext.Implicits.global
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import spray.json._

/*
The API Interface regarding session messages.
 */
trait SessionMotdApi {
  // protocol for serializing data
  import de.thm.arsnova.mappings.SessionMotdJsonProtocol._

  // add the "top level" endpoint to ApiRoutes
  ApiRoutes.addRoute("sessionmotd", "motd")

  // function to generate the model links
  def sessionMotdLinks(motd: SessionMotd): Seq[Link] = {
    Seq(
      Link("self", s"/${ApiRoutes.getRoute("session")}/${motd.sessionId}" +
        s"/${ApiRoutes.getRoute("sessionmotd")}/${motd.id.get}"),
      Link("session", s"/${ApiRoutes.getRoute("session")}/${motd.sessionId}")
    )
  }

  // the HATEOAS Adapter
  val sessionMotdAdapter = new ResourceAdapter[SessionMotd](sessionMotdFormat, sessionMotdLinks)

  val sessionMotdApi = pathPrefix(ApiRoutes.getRoute("session")) {
    pathPrefix(IntNumber) { sessionId =>
      pathPrefix(ApiRoutes.getRoute("sessionmotd")) {
        pathEndOrSingleSlash {
          get {
            complete (SessionMotdService.getBySessionId(sessionId).map(sessionMotdAdapter.toResources(_)))
          } ~
          post {
            entity(as[SessionMotd]) { motd =>
              complete (SessionMotdService.create(motd).map(_.toJson))
            }
          }
        } ~
        pathPrefix(IntNumber) { motdId =>
          pathEndOrSingleSlash {
            get {
              complete (SessionMotdService.getById(motdId).map(sessionMotdAdapter.toResource(_)))
            } ~
            put {
              entity(as[SessionMotd]) { motd =>
                complete (SessionMotdService.update(motd).map(_.toJson))
              }
            } ~
            delete {
              complete (SessionMotdService.delete(motdId).map(_.toJson))
            }
          }
        }
      }
    }
  }
}

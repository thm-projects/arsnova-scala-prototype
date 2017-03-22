package de.thm.arsnova.api

import de.thm.arsnova.services.GlobalMotdService
import de.thm.arsnova.models._
import de.thm.arsnova.hateoas.{ApiRoutes, ResourceAdapter, Link}

import scala.concurrent.ExecutionContext.Implicits.global
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import spray.json._

/*
The API Interface regarding global messages.
Only an admin should post these motd
 */
trait GlobalMotdApi {
  // protocol for serializing data
  import de.thm.arsnova.mappings.GlobalMotdJsonProtocol._

  // add the "top level" endpoint to ApiRoutes
  ApiRoutes.addRoute("globalmotd", "globalmotd")

  // function to generate the model links
  def globalMotdLinks(motd: GlobalMotd): Seq[Link] = {
    Seq(
      Link("self", s"/${ApiRoutes.getRoute("globalmotd")}/${motd.id.get}")
    )
  }

  // the HATEOAS Adapter
  val globalMotdAdapter = new ResourceAdapter[GlobalMotd](globalMotdFormat, globalMotdLinks)

  val globalMotdApi = pathPrefix(ApiRoutes.getRoute("globalmotd")) {
    pathEndOrSingleSlash {
      get {
        parameter("audience".as[String]) { audience =>
          complete (GlobalMotdService.getByAudience(audience).map(globalMotdAdapter.toResources(_)))
        }
      } ~
      post {
        entity(as[GlobalMotd]) { motd =>
          complete (GlobalMotdService.create(motd).map(_.toJson))
        }
      }
    } ~
    pathPrefix(IntNumber) { motdId =>
      pathEndOrSingleSlash {
        get {
          complete (GlobalMotdService.getById(motdId).map(globalMotdAdapter.toResource(_)))
        } ~
        put {
          entity(as[GlobalMotd]) { motd =>
            complete (GlobalMotdService.update(motd).map(_.toJson))
          }
        } ~
        delete {
          complete (GlobalMotdService.delete(motdId).map(_.toJson))
        }
      }
    }
  }
}

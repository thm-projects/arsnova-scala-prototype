package de.thm.arsnova.api

import de.thm.arsnova.services.FeaturesService
import de.thm.arsnova.models._
import de.thm.arsnova.hateoas.{ApiRoutes, ResourceAdapter, Link}

import scala.concurrent.ExecutionContext.Implicits.global
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import spray.json._


/*
The API Interface regarding session features.
 */
trait FeaturesApi {
  // protocol for serializing data
  import de.thm.arsnova.mappings.FeatureJsonProtocol._

  // add the "top level" endpoint to ApiRoutes
  ApiRoutes.addRoute("features", "features")

  // function to generate the model links
  def featuresLinks(features: Features): Seq[Link] = {
    Seq(
      Link("self", s"/${ApiRoutes.getRoute("features")}/${features.id.get}")
    )
  }

  // the HATEOAS Adapter
  val featuresAdapter = new ResourceAdapter[Features](featuresFormat, featuresLinks)

  val featuresApi = pathPrefix(ApiRoutes.getRoute("features")) {
    pathEndOrSingleSlash {
      post {
        entity(as[Features]) { features =>
          complete (FeaturesService.create(features).map(_.toJson))
        }
      }
    } ~
    pathPrefix(IntNumber) { featuresId =>
      pathEndOrSingleSlash {
        get {
          complete (FeaturesService.getById(featuresId).map(featuresAdapter.toResource(_)))
        } ~
        put {
          entity(as[Features]) { features =>
            complete (FeaturesService.update(features).map(_.toJson))
          }
        } ~
        delete {
          complete (FeaturesService.delete(featuresId).map(_.toJson))
        }
      }
    }
  } ~
  pathPrefix(ApiRoutes.getRoute("session")) {
    pathPrefix(IntNumber) { sessionId =>
      path(ApiRoutes.getRoute("features")) {
        get {
          complete (FeaturesService.getBySessionid(sessionId).map(featuresAdapter.toResource(_)))
        }
      }
    }
  }
}
package api

import services.FeaturesService

import scala.concurrent.ExecutionContext.Implicits.global
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import models._
import akka.http.scaladsl.server.Directives._
import spray.json._

import hateoas.{ApiRoutes, ResourceAdapter, Link}

trait FeaturesApi {
  import mappings.FeatureJsonProtocol._

  def featuresSelfLink(features: Features): Link = {
    Link("self", s"/features/${features.id.get}")
  }

  val featuresAdapter = new ResourceAdapter[Features](featuresFormat, featuresSelfLink)

  val featuresApi = pathPrefix("features") {
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
  pathPrefix("session") {
    pathPrefix(IntNumber) { sessionId =>
      path("features") {
        get {
          complete (FeaturesService.getBySessionid(sessionId).map(featuresAdapter.toResource(_)))
        }
      }
    }
  }
}
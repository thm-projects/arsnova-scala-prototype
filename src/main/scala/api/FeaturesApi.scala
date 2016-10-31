package api

import services.FeaturesService

import scala.concurrent.ExecutionContext.Implicits.global
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import models._
import akka.http.scaladsl.server.Directives._
import spray.json._

trait FeaturesApi {
  import mappings.FeatureJsonProtocol._

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
          complete (FeaturesService.getById(featuresId).map(_.toJson))
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
          complete (FeaturesService.getBySessionid(sessionId).map(_.toJson))
        }
      }
    }
  }
}
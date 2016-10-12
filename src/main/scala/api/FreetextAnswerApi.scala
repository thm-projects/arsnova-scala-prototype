package api

import services.FreetextAnswerService

import scala.concurrent.ExecutionContext.Implicits.global
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import models._
import akka.http.scaladsl.server.Directives._
import spray.json._

trait FreetextAnswerApi {
  import mappings.FreetextAnswerJsonProtocol._

  val freetextAnswerApi = pathPrefix("question") {
    pathPrefix(IntNumber) { id =>
      pathPrefix("freetextAnswer") {
        pathEndOrSingleSlash {
          get {
            complete (FreetextAnswerService.getByQuestionId(id))
          } ~
          post {
            entity(as[FreetextAnswer]) { answer =>
              complete (FreetextAnswerService.create(answer).map(_.toJson))
            }
          }
        } ~
        pathPrefix(IntNumber) { freetextAnswerId =>
          pathEndOrSingleSlash {
            get {
              complete (FreetextAnswerService.getById(freetextAnswerId))
            } ~
              put {
                entity(as[FreetextAnswer]) { freetextAnswer =>
                  complete (FreetextAnswerService.update(freetextAnswer).map(_.toJson))
                }
              } ~
              delete {
                complete (FreetextAnswerService.delete(freetextAnswerId).map(_.toJson))
              }
          }
        }
      }
    }
  }
}

package api

import services.FreetextAnswerService

import scala.concurrent.ExecutionContext.Implicits.global
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import models._
import akka.http.scaladsl.server.Directives._
import spray.json._

import hateoas.{ApiRoutes, ResourceAdapter, Link}

trait FreetextAnswerApi {
  import mappings.FreetextAnswerJsonProtocol._

  def freetextAnswerSelfLink(freetextAnswer: FreetextAnswer): Link = {
    Link("self", s"/session/${freetextAnswer.questionId}/freetextAnswer/${freetextAnswer.id.get}")
  }

  val freetextAnswerAdapter = new ResourceAdapter[FreetextAnswer](freetextAnswerFormat, freetextAnswerSelfLink)

  val freetextAnswerApi = pathPrefix("question") {
    pathPrefix(IntNumber) { questionId =>
      pathPrefix("freetextAnswer") {
        pathEndOrSingleSlash {
          get {
            complete (FreetextAnswerService.getByQuestionId(questionId)
              .map(freetextAnswerAdapter.toResources(_)))
          } ~
          post {
            entity(as[FreetextAnswer]) { answer =>
              complete (FreetextAnswerService.create(answer).map(_.toJson))
            }
          } ~
          delete {
            complete (FreetextAnswerService.deleteAllByQuestionId(questionId).map(_.toJson))
          }
        } ~
        pathPrefix(IntNumber) { freetextAnswerId =>
          pathEndOrSingleSlash {
            get {
              complete (FreetextAnswerService.getById(freetextAnswerId)
                .map(freetextAnswerAdapter.toResource(_)))
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

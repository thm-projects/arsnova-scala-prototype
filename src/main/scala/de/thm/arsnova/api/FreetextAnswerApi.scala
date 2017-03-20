package de.thm.arsnova.api

import de.thm.arsnova.services.FreetextAnswerService
import de.thm.arsnova.models._
import de.thm.arsnova.hateoas.{ApiRoutes, ResourceAdapter, Link}

import scala.concurrent.ExecutionContext.Implicits.global
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import spray.json._


/*
The API Interface regarding answers for the question type "freetext".
 */
trait FreetextAnswerApi {
  // protocol for serializing data
  import de.thm.arsnova.mappings.FreetextAnswerJsonProtocol._

  // add the "top level" endpoint to ApiRoutes
  ApiRoutes.addRoute("freetextAnswer", "freetextAnswer")

  // function to generate the model links
  def freetextAnswerLinks(freetextAnswer: FreetextAnswer): Seq[Link] = {
    Seq(
      Link("self", s"/${ApiRoutes.getRoute("question")}/${freetextAnswer.questionId}" +
        s"/${ApiRoutes.getRoute("freetextAnswer")}/${freetextAnswer.id.get}"),
      Link("question", s"/${ApiRoutes.getRoute("question")}/${freetextAnswer.questionId}")
    )
  }

  // the HATEOAS Adapter
  val freetextAnswerAdapter = new ResourceAdapter[FreetextAnswer](freetextAnswerFormat, freetextAnswerLinks)

  val freetextAnswerApi = pathPrefix(ApiRoutes.getRoute("question")) {
    pathPrefix(IntNumber) { questionId =>
      pathPrefix(ApiRoutes.getRoute("freetextAnswer")) {
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

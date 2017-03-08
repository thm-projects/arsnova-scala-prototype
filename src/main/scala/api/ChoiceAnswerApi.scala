package api

import services.{ChoiceAnswerService, FreetextAnswerService}

import scala.concurrent.ExecutionContext.Implicits.global
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import models._
import akka.http.scaladsl.server.Directives._
import spray.json._

import hateoas.{ApiRoutes, ResourceAdapter, Link}

/*
The API Interface regarding answers to choice questions (e.g. MC, SC).
 */
trait ChoiceAnswerApi {
  // protocol for serializing data
  import mappings.ChoiceAnswerJsonProtocol._

  // add the "top level" endpoint to ApiRoutes
  ApiRoutes.addRoute("choiceAnswer", "choiceAnswer")

  // function to generate the model links
  def choiceAnswerLinks(choiceAnswer: ChoiceAnswer): Seq[Link] = {
    Seq(
      Link("self", s"/${ApiRoutes.getRoute("question")}/${choiceAnswer.questionId}/" +
        s"${ApiRoutes.getRoute("choiceAnswer")}/${choiceAnswer.id.get}"),
      Link("question", s"/${ApiRoutes.getRoute("question")}/${choiceAnswer.questionId}")
    )
  }

  // the HATEOAS Adapter
  val choiceAnswerAdapter = new ResourceAdapter[ChoiceAnswer](choiceAnswerFormat, choiceAnswerLinks)

  val choiceAnswerApi = pathPrefix(ApiRoutes.getRoute("question")) {
    pathPrefix(IntNumber) { questionId =>
      pathPrefix(ApiRoutes.getRoute("choiceAnswer")) {
        pathEndOrSingleSlash {
          get {
            complete (ChoiceAnswerService.getByQuestionId(questionId).map(choiceAnswerAdapter.toResources(_)))
          } ~
          post {
            entity(as[ChoiceAnswer]) { answer =>
              complete (ChoiceAnswerService.create(answer).map(_.toJson))
            }
          } ~
            delete {
              complete (ChoiceAnswerService.deleteAllByQuestionId(questionId).map(_.toJson))
            }
        } ~
        pathPrefix(IntNumber) { choiceAnswerId =>
          pathEndOrSingleSlash {
            get {
              complete (ChoiceAnswerService.getById(choiceAnswerId).map(choiceAnswerAdapter.toResource(_)))
            } ~
            put {
              entity(as[ChoiceAnswer]) { choiceAnswer =>
                complete (ChoiceAnswerService.update(choiceAnswer).map(_.toJson))
              }
            } ~
            delete {
              complete (ChoiceAnswerService.delete(choiceAnswerId).map(_.toJson))
            }
          }
        }
      }
    }
  }
}

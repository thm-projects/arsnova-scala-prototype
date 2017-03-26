package de.thm.arsnova.api

import de.thm.arsnova.services.{ChoiceAnswerService, FreetextAnswerService}
import de.thm.arsnova.models._
import de.thm.arsnova.hateoas.{ApiRoutes, ResourceAdapter, Link}

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import spray.json._

/*
The API Interface regarding answers to choice questions (e.g. MC, SC).
 */
trait ChoiceAnswerApi {
  import de.thm.arsnova.Context.executor

  // protocol for serializing data
  import de.thm.arsnova.mappings.ChoiceAnswerJsonProtocol._

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

package api

import services.{ChoiceAnswerService, FreetextAnswerService}

import scala.concurrent.ExecutionContext.Implicits.global
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import models._
import akka.http.scaladsl.server.Directives._
import spray.json._

trait ChoiceAnswerApi {
  import mappings.ChoiceAnswerJsonProtocol._

  val choiceAnswerApi = pathPrefix("question") {
    pathPrefix(IntNumber) { questionId =>
      pathPrefix("choiceAnswer") {
        pathEndOrSingleSlash {
          get {
            complete (ChoiceAnswerService.getByQuestionId(questionId))
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
              complete (ChoiceAnswerService.getById(choiceAnswerId))
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

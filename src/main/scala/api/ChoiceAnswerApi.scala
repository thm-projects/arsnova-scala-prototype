package api

import services.AnswerService

import scala.concurrent.ExecutionContext.Implicits.global
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import models._
import akka.http.scaladsl.server.Directives._
import spray.json._

trait ChoiceAnswerApi {
  import mappings.ChoiceAnswerJsonProtocol._

  val choiceAnswerApi = pathPrefix("question") {
    pathPrefix(IntNumber) { id =>
      pathPrefix("choiceAnswer") {
        pathEndOrSingleSlash {
          post {
            entity(as[ChoiceAnswer]) { answer =>
              complete (AnswerService.createChoiceAnswer(answer).map(_.toJson))
            }
          }
        }
      }
    }
  }
}

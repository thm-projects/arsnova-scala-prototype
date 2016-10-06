package api

import services.AnswerService

import scala.concurrent.ExecutionContext.Implicits.global
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import models._
import akka.http.scaladsl.server.Directives._
import spray.json._

trait FreetextAnswerApi {
  import mappings.FreetextAnswerJsonProtocol._
  //import mappings.ChoiceAnswerJsonProtocol._

  val freetextAnswerApi = pathPrefix("question") {
    pathPrefix(IntNumber) { id =>
      pathPrefix("freetextAnswer") {
        pathEndOrSingleSlash {
          post {
            entity(as[FreetextAnswer]) { answer =>
              complete (AnswerService.createFreetextAnswer(answer).map(_.toJson))
            }
          }
        }
      }
    }
  }
}

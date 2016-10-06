package api

import services.QuestionService

import scala.concurrent.ExecutionContext.Implicits.global
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import models._
import akka.http.scaladsl.server.Directives._
import spray.json._

trait QuestionApi {
  import mappings.QuestionJsonProtocol._

  val questionApi = pathPrefix("question") {
    pathEndOrSingleSlash {
      get {
        parameters("sessionid".as[SessionId], "variant".as[String]) { (sessionId, variant) =>
          complete(QuestionService.findQuestionsBySessionIdAndVariant(sessionId, variant))
        }
        parameter("sessionid".as[SessionId]) { sessionId =>
          complete {QuestionService.findAllBySessionId(sessionId)}
        }
      } ~
      post {
        entity(as[Question]) { question =>
          complete (QuestionService.create(question).map(_.toJson))
        }
      }
    } ~
    pathPrefix(IntNumber) { id =>
      pathEndOrSingleSlash {
        get {
          complete (QuestionService.getById(id))
        } ~
        put {
          entity(as[Question]) { question =>
            complete (QuestionService.update(question, id).map(_.toJson))
          }
        } ~
        delete {
          complete (QuestionService.delete(id).map(_.toJson))
        }
      }
    }
  }
}

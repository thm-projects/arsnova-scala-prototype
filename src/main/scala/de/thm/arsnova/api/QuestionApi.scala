package de.thm.arsnova.api

import de.thm.arsnova.services.QuestionService
import de.thm.arsnova.models._
import de.thm.arsnova.hateoas.{ApiRoutes, ResourceAdapter, Link}

import scala.concurrent.ExecutionContext.Implicits.global
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import spray.json._


/*
The API Interface regarding questions.
 */
trait QuestionApi {
  // protocol for serializing data
  import de.thm.arsnova.mappings.QuestionJsonProtocol._

  // add the "top level" endpoint to ApiRoutes
  ApiRoutes.addRoute("question", "question")

  // function to generate the model links
  def questionLinks(question: Question): Seq[Link] = {
    Seq(
      Link("self", s"/${ApiRoutes.getRoute("question")}/${question.id.get}")
    )
  }

  // the HATEOAS Adapter
  val questionAdapter = new ResourceAdapter[Question](questionFormat, questionLinks)

  val questionApi = pathPrefix(ApiRoutes.getRoute("question")) {
    pathEndOrSingleSlash {
      get {
        parameters("sessionid".as[SessionId], "variant".?) { (sessionId, variant) =>
          variant match {
            case Some(v) => complete(QuestionService.findQuestionsBySessionIdAndVariant(sessionId, v)
              .map(questionAdapter.toResources(_)))
            case None => complete {QuestionService.findAllBySessionId(sessionId)
              .map(questionAdapter.toResources(_))}
          }
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
          complete (QuestionService.findById(id).map(questionAdapter.toResource(_)))
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

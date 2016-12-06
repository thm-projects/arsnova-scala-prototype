package api

import scala.concurrent.ExecutionContext.Implicits.global
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import models._
import akka.http.scaladsl.server.Directives._
import spray.json._

trait FeedbackApi {
  val feedbackApi = pathPrefix("feedback") {
    pathEndOrSingleSlash {
      parameter("session".as[SessionId]) { sessionId =>
        handleWebSocketMessages(FeedbackWrapper.findOrCreate(sessionId).websocketFlow())
      }
    }
  }
}

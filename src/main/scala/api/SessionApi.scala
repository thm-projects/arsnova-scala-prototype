package api

import services.SessionService

import scala.concurrent.ExecutionContext.Implicits.global
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import models._
import akka.http.scaladsl.server.Directives._
import spray.json._

trait SessionApi {
  import mappings.SessionJsonProtocol._

  val sessionApi = pathPrefix("session") {
    pathEndOrSingleSlash {
      get {
        parameter("user".as[UserId]) { (userId) =>
          complete (SessionService.findUserSessions(userId))
        }
      } ~
      post {
        entity(as[Session]) { session =>
          complete (SessionService.create(session).map(_.toJson))
        }
      }
    } ~
    pathPrefix(IntNumber) { sessionId =>
      pathEndOrSingleSlash {
        get {
          complete (SessionService.findById(sessionId))
        } ~
          put {
            entity(as[Session]) { session =>
              complete (SessionService.update(session, sessionId).map(_.toJson))
            }
          } ~
          delete {
            complete (SessionService.delete(sessionId).map(_.toJson))
          }
      }
    }
  }
}

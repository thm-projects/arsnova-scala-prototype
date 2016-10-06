package api

import services.UserService

import scala.concurrent.ExecutionContext.Implicits.global
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import models._
import akka.http.scaladsl.server.Directives._
import spray.json._

trait UserApi {
  import mappings.UserJsonProtocol._

  val userApi = pathPrefix("") {
    pathEndOrSingleSlash {
      get {
        complete (UserService.findAll)
      } ~
      post {
        entity(as[User]) { user =>
          complete (UserService.create(user).map(_.toJson))
        }
      }
    } ~
    pathPrefix(IntNumber) { userId =>
      pathEndOrSingleSlash {
        get {
          complete (UserService.findById(userId))
        } ~
        put {
          entity(as[User]) { user =>
            complete (UserService.update(user, userId).map(_.toJson))
          }
        } ~
        delete {
          complete (UserService.delete(userId).map(_.toJson))
        }
      }
    }
  }
}

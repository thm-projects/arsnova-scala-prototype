package de.thm.arsnova.api

import de.thm.arsnova.services.UserService
import de.thm.arsnova.models._

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import spray.json._

trait UserApi {
  import de.thm.arsnova.Context.executor

  import de.thm.arsnova.mappings.UserJsonProtocol._

  val userApi = pathPrefix("") {
    pathEndOrSingleSlash {
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

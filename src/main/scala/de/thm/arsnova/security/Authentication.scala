package de.thm.arsnova.security

import de.thm.arsnova.models.User
import de.thm.arsnova.services.UserService

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes.Unauthorized
import akka.http.scaladsl.server.{Directive1, Directives}
import spray.json.{JsObject, JsString}

object Authentication extends Directives with SprayJsonSupport {
  val authenticate: Directive1[User] = {
    optionalHeaderValueByName("Authorization") flatMap {
      case Some(authHeader) =>
        val accessToken = authHeader.split(' ').last
        onSuccess(UserService.getByLoginTokenString(accessToken)).flatMap {
          case user: User => provide(user)
          case _ => complete(Unauthorized,  JsObject(Map("status" -> JsString("Wrong Authorization header"))))
        }
      case _ => complete(Unauthorized,  JsObject(Map("status" -> JsString("Missing Authorization header"))))
    }
  }
}

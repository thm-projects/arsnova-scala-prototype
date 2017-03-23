package de.thm.arsnova.api

import de.thm.arsnova.services.AuthService
import de.thm.arsnova.models._
import de.thm.arsnova.hateoas.{ApiRoutes, ResourceAdapter, Link}

import scala.concurrent.ExecutionContext.Implicits.global
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import spray.json._

/*
The API Interface regarding user auth.
 */
trait AuthApi {
  // protocol for serializing data
  import de.thm.arsnova.mappings.LoginTokenJsonProtocol._

  // add the "top level" endpoint to ApiRoutes
  ApiRoutes.addRoute("auth", "auth")

  val authApi = pathPrefix(ApiRoutes.getRoute("auth")) {
    get {
      parameters("username".as[String], "password".as[String]) { (username, password) =>
        complete (AuthService.login(username, password).map(_.toJson))
      }
    }
  }
}

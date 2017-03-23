package de.thm.arsnova.mappings



import de.thm.arsnova.models.LoginToken
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object LoginTokenJsonProtocol extends DefaultJsonProtocol {
  implicit val loginTokenFormat: RootJsonFormat[LoginToken] = jsonFormat5(LoginToken)
}
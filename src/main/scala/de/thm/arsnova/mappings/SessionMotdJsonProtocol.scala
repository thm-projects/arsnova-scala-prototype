package de.thm.arsnova.mappings

import de.thm.arsnova.models.SessionMotd
import spray.json._

object SessionMotdJsonProtocol extends DefaultJsonProtocol {
  implicit val commentFormat: RootJsonFormat[SessionMotd] = jsonFormat6(SessionMotd)
}

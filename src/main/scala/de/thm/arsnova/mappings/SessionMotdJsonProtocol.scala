package de.thm.arsnova.mappings

import de.thm.arsnova.models.SessionMotd
import spray.json._

object SessionMotdJsonProtocol extends DefaultJsonProtocol {
  implicit val sessionMotdFormat: RootJsonFormat[SessionMotd] = jsonFormat6(SessionMotd)
}

package de.thm.arsnova.mappings

import de.thm.arsnova.models.GlobalMotd
import spray.json._

object GlobalMotdJsonProtocol extends DefaultJsonProtocol {
  implicit val globalMotdFormat: RootJsonFormat[GlobalMotd] = jsonFormat6(GlobalMotd)
}

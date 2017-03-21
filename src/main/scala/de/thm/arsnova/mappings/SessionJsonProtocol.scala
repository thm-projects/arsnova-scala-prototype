package de.thm.arsnova.mappings

import de.thm.arsnova.models.Session
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object SessionJsonProtocol extends DefaultJsonProtocol {
  import FeatureJsonProtocol._
  implicit val sessionFormat: RootJsonFormat[Session] = jsonFormat11(Session)
}

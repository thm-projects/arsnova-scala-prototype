package mappings

import models.Session
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object SessionJsonProtocol extends DefaultJsonProtocol {
  implicit val sessionFormat: RootJsonFormat[Session] = jsonFormat5(Session)
}

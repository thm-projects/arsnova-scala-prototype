package mappings

import models.User
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object UserJsonProtocol extends DefaultJsonProtocol {
  implicit val userFormat: RootJsonFormat[User] = jsonFormat3(User)
}

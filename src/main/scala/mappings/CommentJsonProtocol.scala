package mappings

import models.Comment
import spray.json.{DefaultJsonProtocol, RootJsonFormat}


object CommentJsonProtocol extends DefaultJsonProtocol {
  implicit val commentFormat: RootJsonFormat[Comment] = jsonFormat7(Comment)
}

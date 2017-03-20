package de.thm.arsnova.mappings

import de.thm.arsnova.models.Comment
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object CommentJsonProtocol extends DefaultJsonProtocol {
  implicit val commentFormat: RootJsonFormat[Comment] = jsonFormat7(Comment)
}

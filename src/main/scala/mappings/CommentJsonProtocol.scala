package mappings

import models.Comment
import spray.json.{DefaultJsonProtocol, RootJsonFormat}
import java.sql.Timestamp
import spray.json._


object CommentJsonProtocol extends DefaultJsonProtocol {
  implicit object TimestampFormat extends JsonFormat[Timestamp] {
    def write(obj: Timestamp) = JsNumber(obj.getTime)

    def read(json: JsValue) = json match {
      case JsNumber(time) => new Timestamp(time.toLong)

      case _ => throw new Exception("Date expected")
    }
  }
  implicit val commentFormat: RootJsonFormat[Comment] = jsonFormat7(Comment)
}

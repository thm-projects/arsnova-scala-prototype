package mappings

import models.FreetextAnswer
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object FreetextAnswerJsonProtocol extends DefaultJsonProtocol {
  implicit val freetextAnswerFormat: RootJsonFormat[FreetextAnswer] = jsonFormat5(FreetextAnswer)
}

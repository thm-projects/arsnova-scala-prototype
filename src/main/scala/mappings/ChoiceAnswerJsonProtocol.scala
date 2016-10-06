package mappings

import models.ChoiceAnswer
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object ChoiceAnswerJsonProtocol extends DefaultJsonProtocol {
  implicit val choiceAnswerFormat: RootJsonFormat[ChoiceAnswer] = jsonFormat4(ChoiceAnswer)
}

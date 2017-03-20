package de.thm.arsnova.mappings

import de.thm.arsnova.models.AnswerOption
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object AnswerOptionJsonProtocol extends DefaultJsonProtocol {
  implicit val answerOptionFormat: RootJsonFormat[AnswerOption] = jsonFormat5(AnswerOption)
}

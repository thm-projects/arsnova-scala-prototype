package mappings

import models.{ChoiceQuestion, Flashcard, Freetext, Question}
import spray.json._

object QuestionJsonProtocol extends DefaultJsonProtocol {
  import AnswerOptionJsonProtocol._

  implicit object questionFormat extends RootJsonFormat[Question] {
    implicit val choiceQuestionFormat: RootJsonFormat[ChoiceQuestion] = jsonFormat7(ChoiceQuestion)
    implicit val freetextFormat: RootJsonFormat[Freetext] = jsonFormat6(Freetext)
    implicit val flashcardFormat: RootJsonFormat[Flashcard] = jsonFormat7(Flashcard)
    def write(q: Question): JsValue = q match {
      case Freetext(_, _, _, _, _, _) => q.asInstanceOf[Freetext].toJson
      case Flashcard(_, _, _, _, _, "flashcard", _) => q.asInstanceOf[Flashcard].toJson
      case ChoiceQuestion(_, _, _, _, _, _, _) => q.asInstanceOf[ChoiceQuestion].toJson
    }
    def read(json: JsValue) = {
      json.asJsObject.getFields(
        "format"
      ) match {
        case Seq(JsString(format)) => format match {
          case "flashcard" => json.convertTo[Flashcard]
          case "mc" => json.convertTo[ChoiceQuestion]
          case "freetext" => json.convertTo[Freetext]
        }
      }
    }
  }
}

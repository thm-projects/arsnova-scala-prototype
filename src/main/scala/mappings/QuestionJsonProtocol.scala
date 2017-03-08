package mappings

import models.{FormatAttributes, Question}
import spray.json._

object QuestionJsonProtocol extends DefaultJsonProtocol {
  import AnswerOptionJsonProtocol._

  /*
   FormatAttributes JSON protocol must be done manually since the map isn't serialized in a "spray.json default way"
   */
  implicit object formatAttributesFormat extends RootJsonFormat[FormatAttributes] {
    def write(fA: FormatAttributes): JsValue = {
      val keyVals = fA.attributes.map {
        case (key, value) => key -> JsString(value)
      }
      JsObject(keyVals)
    }

    def read(json: JsValue): FormatAttributes = {
      json match {
        case js: JsObject => {
          val wat: Map[String, JsValue] = json.asJsObject.fields
          val attributes = wat.map {
            case (key, value) => key -> value.convertTo[String]
          }
          FormatAttributes(attributes = attributes)
        }
        case _ => FormatAttributes(Map())
      }
    }
  }

  // questin JSON protocol utilizes the above format. No need to manually do things here
  implicit val questionFormat: RootJsonFormat[Question] = jsonFormat8(Question)
}

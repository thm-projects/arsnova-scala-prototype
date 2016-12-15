package mappings

import models.{FormatAttributes, Question}
import spray.json._

object QuestionJsonProtocol extends DefaultJsonProtocol {
  import AnswerOptionJsonProtocol._

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

  implicit val questionFormat: RootJsonFormat[Question] = jsonFormat8(Question)
}

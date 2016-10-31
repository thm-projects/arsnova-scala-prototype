package mappings

import models.Features
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object FeatureJsonProtocol extends DefaultJsonProtocol {
  implicit val featuresFormat: RootJsonFormat[Features] = jsonFormat12(Features)
}

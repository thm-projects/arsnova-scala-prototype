package de.thm.arsnova.mappings

import de.thm.arsnova.models.Features
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object FeatureJsonProtocol extends DefaultJsonProtocol {
  implicit val featuresFormat: RootJsonFormat[Features] = jsonFormat12(Features)
}

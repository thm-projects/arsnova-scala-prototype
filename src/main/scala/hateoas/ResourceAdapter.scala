package hateoas

import spray.json._

class ResourceAdapter[T](jsonFormat: RootJsonFormat[T], modelLink: T => Seq[Link]) {
  import hateoas.LinkJsonProtocol._

  /** This function maps a sequence of models to a sequence of models with their links.
    *
    * @param models the models as a sequence
    * @param links additional links to add to the collection
    * @return the HATEOS-style JSON object
    */
  def toResources(models: Seq[T], links: Seq[Link] = Seq()): JsValue = {
    val jsonModels = models.map(toResource(_, Seq())).toJson
    jsonModels match {
      case obj: JsObject => addLinks(obj, links)
      case _ => addLinks(JsObject("data" -> jsonModels), links)
    }
  }

  /**
    *
    * @param model the single model
    * @param links additional links to add
    * @return the HATEOS-style JSON object
    */
  def toResource(model: T, links: Seq[Link] = Seq()): JsValue = {
    val linksWithSelf = links ++ modelLink(model)
    val json: JsValue = jsonFormat.write(model)
    json match {
      case obj: JsObject => addLinks(obj, linksWithSelf)
      case _ => addLinks(JsObject("data" -> json), linksWithSelf)
    }
  }

  /**
    *
    * @param obj the JSON object to add the links to
    * @param links the links to add
    * @return the JSON object with links
    */
  def addLinks(obj: JsObject, links: Seq[Link]): JsObject = {
    JsObject(obj.fields + ("links" -> links.toJson))
  }
}
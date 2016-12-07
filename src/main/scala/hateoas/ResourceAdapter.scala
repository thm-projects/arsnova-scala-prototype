package hateoas

import spray.json._

class ResourceAdapter[T](jsonFormat: RootJsonFormat[T], selfLink: T => Link) {
  import hateoas.LinkJsonProtocol._

  def toResources(models: Seq[T], links: Seq[Link] = Seq()): JsValue = {
    val jsonModels = models.map(toResource(_, Seq())).toJson
    jsonModels match {
      case obj: JsObject => addLinks(obj, links)
      case _ => addLinks(JsObject("data" -> jsonModels), links)
    }
  }

  def toResource(model: T, links: Seq[Link] = Seq()): JsValue = {
    val linksWithSelf = links :+ selfLink(model)
    val json: JsValue = jsonFormat.write(model)
    json match {
      case obj: JsObject => addLinks(obj, linksWithSelf)
      case _ => addLinks(JsObject("data" -> json), linksWithSelf)
    }
  }

  def addLinks(obj: JsObject, links: Seq[Link]): JsObject = {
    JsObject(obj.fields + ("links" -> links.toJson))
  }
}
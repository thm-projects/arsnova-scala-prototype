package hateoas

import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object LinkJsonProtocol extends DefaultJsonProtocol {
  implicit val linkFormat: RootJsonFormat[Link] = jsonFormat2(Link)
}

case class Link(name: String, href: String)

package hateoas

object ApiRoutes {
  val routes = scala.collection.mutable.Map[String, String]()

  def addRoute(name: String, url: String): Unit = {
    routes += name -> url
  }

  def getRoute(name: String): String = {
    val url: Option[String] = routes.get(name)
    url match {
      case Some(v) => v
      case None => "no route for this name"
    }
  }
}
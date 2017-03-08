package hateoas

/*
This object stores every "routepiece" (e.g. "session") to reduce redundancy in HATEOAS impl.
 */
object ApiRoutes {
  val routes = scala.collection.mutable.Map[String, String]()

  def addRoute(name: String, url: String): Unit = {
    routes += name -> url
  }

  /*
  This doesn't return a Try since it's used for API-declaration.
  Correct behaviour of HATEOAS needs to be checked with tests!
   */
  def getRoute(name: String): String = {
    val url: Option[String] = routes.get(name)
    url match {
      case Some(v) => v
      case None => "no route for this name"
    }
  }
}

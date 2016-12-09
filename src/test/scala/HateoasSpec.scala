import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{FunSpec, Matchers}
import org.scalatest.concurrent.ScalaFutures
import spray.json._
import services.BaseService
import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.unmarshalling.Unmarshal
import hateoas._
import api._

trait HateoasSpec extends FunSpec with Matchers with ScalaFutures with BaseService with ScalatestRouteTest with Routes {
  import hateoas.LinkJsonProtocol._

  describe("hateoas") {
    it("check session model links") {
      Get("/session/1/") ~> sessionApi ~> check {
        val linksJson: Seq[JsValue] = Await.result(Unmarshal(response.entity).to[JsObject], 1.second).getFields("links")
        linksJson.map(_ match {
          case JsArray(links) => { links.map( link =>
            Get(linkFormat.read(link).href) ~> routes ~> check {
              handled shouldBe true
            }
          )}
        })
      }
    }
  }
}

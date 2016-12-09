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
import akka.http.scaladsl.server.Route
import hateoas._
import api._

trait HateoasSpec extends FunSpec with Matchers with ScalaFutures with BaseService with ScalatestRouteTest with Routes {
  import hateoas.LinkJsonProtocol._

  def checkLinksForRoute(url: String, api: Route): Seq[scala.collection.immutable.Vector[org.scalatest.Assertion]] = {
    Get(url) ~> api ~> check {
      val linksJson: Seq[JsValue] = Await.result(Unmarshal(response.entity).to[JsObject], 1.second).getFields("links")
      checkLinks(linksJson)
    }
  }

  def checkLinks(linksJson: Seq[JsValue]): Seq[scala.collection.immutable.Vector[org.scalatest.Assertion]] = {
    linksJson.map(_ match {
      case JsArray(links) => { links.map( link =>
        Get(linkFormat.read(link).href) ~> routes ~> check {
          handled shouldBe true
        }
      )}
      case _ => fail("hateoas links are no array")
    })
  }

  describe("HATEOAS session api") {
    it("check model links") {
      checkLinksForRoute("/session/1", sessionApi)
    }
  }
  describe("HATEOAS question api") {
    it("check model links") {
      checkLinksForRoute("/question/1", questionApi)
    }
  }
  describe("HATEOAS freetextanswer api") {
    it("check model links") {
      checkLinksForRoute("/question/1/freetextAnswer/1", freetextAnswerApi)
    }
  }
  describe("HATEOAS choiceanswer api") {
    it("check model links") {
      checkLinksForRoute("/question/5/choiceAnswer/1/", choiceAnswerApi)
    }
  }
  describe("HATEOAS comment api") {
    it("check model links") {
      checkLinksForRoute("/session/1/comment", commentApi)
    }
  }
  describe("HATEOAS features api") {
    it("check model links") {
      checkLinksForRoute("/session/1/features", featuresApi)
    }
  }
}

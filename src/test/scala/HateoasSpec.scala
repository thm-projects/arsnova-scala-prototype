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

  describe("HATEOAS session api") {
    it("check model links") {
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
  describe("HATEOAS question api") {
    it("check model links") {
      Get("/question/1") ~> questionApi ~> check {
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
  describe("HATEOAS freetextanswer api") {
    it("check model links") {
      Get("/question/1/freetextAnswer/1") ~> freetextAnswerApi ~> check {
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
  describe("HATEOAS choiceanswer api") {
    it("check model links") {
      Get("/question/5/choiceAnswer/1/") ~> choiceAnswerApi ~> check {
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
  describe("HATEOAS comment api") {
    it("check model links") {
      Get("/session/1/comment") ~> commentApi ~> check {
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
  describe("HATEOAS features api") {
    it("check model links") {
      Get("/session/1/features") ~> featuresApi ~> check {
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

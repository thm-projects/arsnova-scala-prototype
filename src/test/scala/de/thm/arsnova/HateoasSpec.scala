package de.thm.arsnova

import de.thm.arsnova.services.BaseService
import de.thm.arsnova.hateoas._
import de.thm.arsnova.api._

import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{FunSpec, Matchers}
import org.scalatest.concurrent.ScalaFutures
import spray.json._
import scala.concurrent.{Future, Await}
import scala.concurrent.duration._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.http.scaladsl.server.Route

trait HateoasSpec extends FunSpec with Matchers with ScalaFutures with BaseService with ScalatestRouteTest with Routes {
  import hateoas.LinkJsonProtocol._

  /**
    * Makes a GET-Request to the url, extracts the links in the result and checks them by verifying the handling
    *
    * @param url the url to check
    * @param api the api interface to send the request to
    * @return sequence assertions
    */
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

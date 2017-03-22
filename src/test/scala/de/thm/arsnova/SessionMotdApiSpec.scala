package de.thm.arsnova

import de.thm.arsnova.services.BaseService
import de.thm.arsnova.models._
import de.thm.arsnova.api.SessionMotdApi

import scala.concurrent.Await
import scala.concurrent.duration._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.model.{HttpEntity, MediaTypes}
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import org.scalatest.{FunSpec, Matchers}
import org.scalatest.concurrent.ScalaFutures
import spray.json._
import java.util.Calendar

trait SessionMotdApiSpec extends FunSpec with Matchers with ScalaFutures with BaseService with ScalatestRouteTest with Routes
  with TestData with SessionMotdApi {
  import de.thm.arsnova.mappings.SessionMotdJsonProtocol._

  val ENDDATEDAYSFROMNOW2 = 7

  describe("Session motd api") {
    it("retrieve motd by id") {
      Get("/session/1/motd/1") ~> sessionMotdApi ~> check {
        responseAs[JsObject] should be(sessionMotdAdapter.toResource(testSessionMotds.head))
      }
    }
    it("create motd") {
      val now = Calendar.getInstance
      val startdate = now.getTime.toString
      now.add(Calendar.DAY_OF_MONTH, ENDDATEDAYSFROMNOW2)
      val enddate = now.getTime.toString
      val newText = "a new session motd for session w/ id 1"
      val newMotd = SessionMotd(None, 1, startdate, enddate, "newMotd", newText)
      val requestEntity = HttpEntity(MediaTypes.`application/json`, newMotd.toJson.toString)
      Post("/session/1/motd/", requestEntity) ~> sessionMotdApi ~> check {
        response.status should be(OK)
        val newId = Await.result(Unmarshal(response.entity).to[String], 1.second).toLong
        val checkMotd = newMotd.copy(id = Some(newId))
        Get(s"/session/1/motd/${newId}") ~> sessionMotdApi ~> check {
          responseAs[JsObject] should be(sessionMotdAdapter.toResource(checkMotd))
        }
      }
    }
    it("update motd") {
      val motd = testSessionMotds.head
      val updateMotd = motd.copy(text = "this in an updated text")
      val requestEntity = HttpEntity(MediaTypes.`application/json`, updateMotd.toJson.toString)
      Put(s"/session/1/motd/${motd.id.get}", requestEntity) ~> sessionMotdApi ~> check {
        response.status should be(OK)
        Get(s"/session/1/motd/${motd.id.get}") ~> sessionMotdApi ~> check {
          responseAs[JsObject] should be(sessionMotdAdapter.toResource(updateMotd))
        }
      }
    }
    it("delete motd") {
      val motdId = testSessionMotds.head.id.get
      Delete(s"/session/1/motd/$motdId") ~> sessionMotdApi ~> check {
        response.status should be(OK)
        Get(s"/session/1/motd/$motdId") ~> sessionMotdApi ~> check {
          response.status should be(NotFound)
        }
      }
    }
  }
}

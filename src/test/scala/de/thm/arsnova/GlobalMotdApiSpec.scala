package de.thm.arsnova

import de.thm.arsnova.services.BaseService
import de.thm.arsnova.models._
import de.thm.arsnova.api.GlobalMotdApi

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

trait GlobalMotdApiSpec extends FunSpec with Matchers with ScalaFutures with BaseService with ScalatestRouteTest with Routes
    with TestData with GlobalMotdApi {
  import de.thm.arsnova.mappings.GlobalMotdJsonProtocol._

  val ENDDATEDAYSFROMNOW = 7

  describe("Global motd api") {
    it("retrieve motd by id") {
      Get("/globalmotd/1") ~> globalMotdApi ~> check {
        responseAs[JsObject] should be(globalMotdAdapter.toResource(testGlobalMotds.head))
      }
    }
    it("create motd") {
      val now = Calendar.getInstance
      val startdate = now.getTime.toString
      now.add(Calendar.DAY_OF_MONTH, ENDDATEDAYSFROMNOW)
      val enddate = now.getTime.toString
      val newText = "a new global motd for all"
      val newMotd = GlobalMotd(None, startdate, enddate, "newMotd", newText, "all")
      val requestEntity = HttpEntity(MediaTypes.`application/json`, newMotd.toJson.toString)
      Post("/globalmotd/", requestEntity) ~> globalMotdApi ~> check {
        response.status should be(OK)
        val newId = Await.result(Unmarshal(response.entity).to[String], 1.second).toLong
        val checkMotd = newMotd.copy(id = Some(newId))
        Get(s"/globalmotd/${newId}") ~> globalMotdApi ~> check {
          responseAs[JsObject] should be(globalMotdAdapter.toResource(checkMotd))
        }
      }
    }
  }
}

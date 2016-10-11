import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.unmarshalling._
import akka.http.scaladsl.model.{HttpEntity, MediaTypes, StatusCode}
import services.{BaseService, SessionService}
import models._
import org.scalatest.concurrent.ScalaFutures
import spray.json._
import akka.http.scaladsl.server.Directives._

import scala.concurrent.Future
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.server.MissingQueryParamRejection
import org.scalatest.{FunSpec, Matchers}

trait QuestionApiSpec extends FunSpec with Matchers with ScalaFutures with BaseService with ScalatestRouteTest with Routes with TestData {
  import mappings.QuestionJsonProtocol._
  // you need to call unmarshal because of the question trait
  describe("Question api") {
    it("retrieve question by id 1") {
      Get("/question/1") ~> questionApi ~> check {
        responseAs[JsObject] should be(testQuestions.head.asInstanceOf[Question].toJson)
      }
    }
    it("retrieve preparation questions for session with id 1") {
      Get("/question/?sessionid=1&variant=preparation") ~> questionApi ~> check {
        /*val question = Unmarshal(response.entity).to[Seq[Question]]
        question.onSuccess { case q =>
          q should be(preparationQuestions)
        }*/
        val prepQuestionsJson = preparationQuestions.map(_.asInstanceOf[Question].toJson)
        responseAs[JsArray] should be(prepQuestionsJson)
        //responseAs[JsArray] should be(preparationQuestions.asInstanceOf[Seq[Question]].toJson)
      }
    }
    it("retrieve live questions for session with id 1") {
      Get("/question/?sessionid=1&variant=live") ~> questionApi ~> check {
        val liveQuestionsJson = liveQuestions.map(_.asInstanceOf[Question].toJson)
        responseAs[JsArray] should be(liveQuestionsJson)
        /*val question = Unmarshal(response.entity).to[Question]
        question.onSuccess { case q =>
          q should be(preparationQuestions)
        }*/
      }
    }
    it("should deny invalid route") {
      Get("/question/") ~> questionApi ~> check {
        handled shouldBe false
        rejection shouldBe MissingQueryParamRejection("sessionid")
      }
    }
    it("create freetext question properly") {
      val sessionId = 1
      val subject = "postSubject"
      val content = "postContent"
      val variant = "preparation"
      val format = "freetext"
      val requestEntity = HttpEntity(MediaTypes.`application/json`,
        JsObject(
          "sessionId" -> JsNumber(sessionId),
          "subject" -> JsString(subject),
          "content" -> JsString(content),
          "variant" -> JsString(variant),
          "format" -> JsString(format)
        ).toString())
      Post("/question", requestEntity) ~> questionApi ~> check {
        response.status should be(OK)
        val newQuestionId: Future[String] = Unmarshal(response.entity).to[String]
        newQuestionId.onSuccess { case id =>
          Get("/question/" + id.toString) ~> questionApi ~> check {
            //responseAs[JsObject].asInstanceOf[Freetext].subject should be(JsString(subject))
          }
        }
      }
    }
    it("create flashcard question properly") {
      val sessionId = 1
      val subject = "postSubject"
      val content = "postContent"
      val variant = "preparation"
      val format = "flashcard"
      val backside = "backside"
      val requestEntity = HttpEntity(MediaTypes.`application/json`,
        JsObject(
          "sessionId" -> JsNumber(sessionId),
          "subject" -> JsString(subject),
          "content" -> JsString(content),
          "variant" -> JsString(variant),
          "format" -> JsString(format),
          "backside" -> JsString(backside)
        ).toString())
      Post("/question", requestEntity) ~> questionApi ~> check {
        response.status should be(OK)
        val newQuestionId: Future[String] = Unmarshal(response.entity).to[String]
        newQuestionId.onSuccess { case id =>
          Get("/question/" + id.toString) ~> questionApi ~> check {
            //responseAs[JsObject].asInstanceOf[Flashcard].subject should be(JsString(subject))
          }
        }
      }
    }
    it("should create mc question properly") {
      val sessionId = 1
      val subject = "postSubject"
      val content = "postContent"
      val variant = "preparation"
      val format = "mc"
      val requestEntity = HttpEntity(MediaTypes.`application/json`,
        JsObject(
          "sessionId" -> JsNumber(sessionId),
          "subject" -> JsString(subject),
          "content" -> JsString(content),
          "variant" -> JsString(variant),
          "format" -> JsString(format)
        ).toString())
      Post("/question", requestEntity) ~> questionApi ~> check {
        response.status should be(OK)
        val newQuestionId: Future[String] = Unmarshal(response.entity).to[String]
        newQuestionId.onSuccess { case id =>
          Get("/question/" + id.toString) ~> questionApi ~> check {
            responseAs[JsObject].asInstanceOf[Question].subject should be(JsString(subject))
          }
        }
      }
    }
    it("should update a question by id") {
      val updatedSubject = "UpdatedSubject"
      val question: Freetext = testQuestions.head.asInstanceOf[Freetext]
      val updatedQuestion: Freetext = question.copy(subject = updatedSubject)
      val requestEntity = HttpEntity(MediaTypes.`application/json`, updatedQuestion.asInstanceOf[Question].toJson.toString)
      Put("/question/" + question.id.get.toString, requestEntity) ~> questionApi ~> check {
        response.status should be(OK)
        Get("/question/" + question.id.get.toString) ~> questionApi ~> check {
          val checkQuestion: Future[Question] = Unmarshal(response.entity).to[Question]
          checkQuestion.onSuccess { case question =>
            question should be(updatedQuestion)
          }
        }
      }
    }
    it("should delete session by id") {
      val questionId = testQuestions.last.id.get
      Delete("/question/" + questionId.toString) ~> questionApi ~> check {
        response.status should be(OK)
        Get("/question/" + questionId.toString) ~> questionApi ~> check {
          response.status should be(NotFound)
        }
      }
    }
  }
}

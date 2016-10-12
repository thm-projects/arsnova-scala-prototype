import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.unmarshalling._
import akka.http.scaladsl.model.{HttpEntity, MediaTypes, StatusCode}
import services.{BaseService, SessionService}
import models._
import org.scalatest.concurrent.ScalaFutures
import spray.json._
import akka.http.scaladsl.server.Directives._

import scala.concurrent.Future
import scala.util.{Failure, Success}
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
    it("retrieve all questions for session with id 1") {
      Get("/question/?sessionid=1") ~> questionApi ~> check {
        val questionsJson = testQuestions.asInstanceOf[Seq[Question]].toJson
        responseAs[JsArray] should be(questionsJson)
      }
    }
    it("retrieve preparation questions for session with id 1") {
      Get("/question/?sessionid=1&variant=preparation") ~> questionApi ~> check {
        val prepQuestionsJson = preparationQuestions.asInstanceOf[Seq[Question]].toJson
        responseAs[JsArray] should be(prepQuestionsJson)
      }
    }
    it("retrieve live questions for session with id 1") {
      Get("/question/?sessionid=1&variant=live") ~> questionApi ~> check {
        val liveQuestionsJson = liveQuestions.asInstanceOf[Seq[Question]].toJson
        responseAs[JsArray] should be(liveQuestionsJson)
      }
    }
    it("should deny invalid route") {
      Get("/question/") ~> questionApi ~> check {
        handled shouldBe false
        rejection shouldBe MissingQueryParamRejection("sessionid")
      }
    }
    it("create freetext question properly") {
      val sessionId: SessionId = 1
      val subject = "postSubject"
      val content = "postContent"
      val variant = "preparation"
      val format = "freetext"
      val newFreetext = Freetext(None, sessionId, subject, content, variant, format)
      val requestEntity = HttpEntity(MediaTypes.`application/json`,newFreetext.asInstanceOf[Question].toJson.toString)
      Post("/question", requestEntity) ~> questionApi ~> check {
        response.status should be(OK)
        val newQuestionId: Future[String] = Unmarshal(response.entity).to[String]
        newQuestionId.onSuccess { case newId =>
          val checkFreetext = newFreetext.copy(id = Some(newId.toLong))
          Get("/question/" + newId.toString) ~> questionApi ~> check {
            responseAs[JsObject] should be(checkFreetext.asInstanceOf[Question].toJson)
          }
        }
      }
    }
    it("create flashcard question properly") {
      val sessionId: SessionId = 1
      val subject = "postSubject"
      val content = "postContent"
      val variant = "preparation"
      val format = "flashcard"
      val backside = "backside"
      val newFlashcard = Flashcard(None, sessionId, subject, content, variant, format, backside)
      val requestEntity = HttpEntity(MediaTypes.`application/json`, newFlashcard.asInstanceOf[Question].toJson.toString)
      Post("/question", requestEntity) ~> questionApi ~> check {
        response.status should be(OK)
        val newQuestionId: Future[String] = Unmarshal(response.entity).to[String]
        newQuestionId.onSuccess { case newId =>
          val checkFlashcard = newFlashcard.copy(id = Some(newId.toLong))
          Get("/question/" + newId) ~> questionApi ~> check {
            responseAs[JsObject] should be(checkFlashcard.asInstanceOf[Question].toJson)
          }
        }
      }
    }
    it("create mc question properly") {
      val sessionId = 1
      val subject = "postSubject"
      val content = "postContent"
      val variant = "preparation"
      val format = "mc"
      val answerOptions = Seq(AnswerOption(None, None, false, "false", -1), AnswerOption(None, None, true, "true", 1))
      val newMC = ChoiceQuestion(None, sessionId, subject, content, variant, format, answerOptions)
      val requestEntity = HttpEntity(MediaTypes.`application/json`, newMC.asInstanceOf[Question].toJson.toString)
      Post("/question", requestEntity) ~> questionApi ~> check {
        response.status should be(OK)
        val newQuestionId: Future[String] = Unmarshal(response.entity).to[String]
        newQuestionId.onSuccess { case newId =>
          Get("/question/" + newId) ~> questionApi ~> check {
            // can't check question due to answerOptions (don't have ids)
            val checkQuestionFuture = Unmarshal(response.entity).to[Question]
            checkQuestionFuture.onComplete {
              case Success(checkQuestion) => {
                val checkAnswerOptions = checkQuestion.asInstanceOf[ChoiceQuestion].answerOptions
                checkAnswerOptions.length should be(answerOptions.length)
                checkAnswerOptions.map(_.questionId should be(Some(newId.toLong)))
                val questionWithoutAnswerOptions = checkQuestion.asInstanceOf[ChoiceQuestion].copy(answerOptions = Seq())
                val postedQuestionWithoutAnswerOptions = newMC.copy(answerOptions = Seq(), id = Some(newId.toLong))
                questionWithoutAnswerOptions should be(postedQuestionWithoutAnswerOptions)
              }
            }
          }
        }
      }
    }
    it("update answer options properly") {
      val question = preparationQuestions.last.asInstanceOf[ChoiceQuestion]
      val updatedAnswerOptions: Seq[AnswerOption] = question.answerOptions
      val updatedQuestion: ChoiceQuestion = question.copy(answerOptions = updatedAnswerOptions)
      val questionId = updatedQuestion.id.get
      val requestEntity = HttpEntity(MediaTypes.`application/json`, updatedQuestion.asInstanceOf[Question].toJson.toString)
      Put("/question/" + questionId.toString, requestEntity) ~> questionApi ~> check {
        response.status should be(OK)
        Get("/question/" + questionId.toString) ~> questionApi ~> check {
          val checkQuestionFuture: Future[Question] = Unmarshal(response.entity).to[Question]
          checkQuestionFuture.onComplete {
            case Success(checkQuestion) => checkQuestion.asInstanceOf[ChoiceQuestion].answerOptions should be(updatedAnswerOptions)
            case Failure(t) => fail("couldn't get updated question with error: " + t)
          }
        }
      }
    }
    it("update a question by id") {
      val updatedSubject = "UpdatedSubject"
      val question: Freetext = testQuestions.head.asInstanceOf[Freetext]
      val updatedQuestion: Freetext = question.copy(subject = updatedSubject)
      val requestEntity = HttpEntity(MediaTypes.`application/json`, updatedQuestion.asInstanceOf[Question].toJson.toString)
      Put("/question/" + question.id.get.toString, requestEntity) ~> questionApi ~> check {
        response.status should be(OK)
        Get("/question/" + question.id.get.toString) ~> questionApi ~> check {
          val checkQuestionFuture: Future[Question] = Unmarshal(response.entity).to[Question]
          checkQuestionFuture.onComplete {
            case Success(checkQuestion) => checkQuestion should be(updatedQuestion)
            case Failure(t) => fail("couldn't get updated question with error: " + t)
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

import services.BaseService
import models.User

import akka.event.{ NoLogging, LoggingAdapter }
import akka.http.scaladsl.testkit.ScalatestRouteTest
import utils.MigrationConfig
import org.scalatest._
import scala.concurrent.Await
import scala.concurrent.duration._
import services.BaseService
import models._

trait BaseServiceSpec extends WordSpec with Matchers with ScalatestRouteTest with Routes with MigrationConfig with BaseService {
  protected val log: LoggingAdapter = NoLogging

  import driver.api._

  val testUsers = Seq(
    User(Some(1), "user1", "password1"),
    User(Some(2), "user2", "password2"),
    User(Some(3), "user3", "password3")
  )

  val testSessionsForUser1 = Seq(
    Session(Some(1), "11111111", 1, "session1", "s1"),
    Session(Some(2), "22222222", 1, "session2", "s2")
  )

  val testSessions = testSessionsForUser1 ++ Seq(
    Session(Some(3), "33333333", 2, "session3", "s3")
  )

  val questionId = 5
  val pointsForRightAnswer: Int = 10
  val pointsForWrongAnswer: Int = -10
  val FOUR = 4
  val FIVE = 5


  val testAnswerOptions = Seq(
    AnswerOption(Some(1), questionId, true, "rightAnswer1", pointsForRightAnswer),
    AnswerOption(Some(2), questionId, true, "rightAnswer2", pointsForRightAnswer),
    AnswerOption(Some(3), questionId, false, "falseAnswer1", pointsForWrongAnswer),
    AnswerOption(Some(FOUR), questionId, false, "falseAnswer2", pointsForWrongAnswer),
    AnswerOption(Some(FIVE), questionId, false, "falseAnswer3", pointsForWrongAnswer)
  )

  val testQuestions = Seq(
    Freetext(Some(1), 1, "subject1", "content1", "preparation", "freetext"),
    Freetext(Some(2), 1, "subject2", "content2", "preparation", "freetext"),
    Flashcard(Some(3), 1, "subject3", "content3", "preparation", "flashcard", "backside3"),
    Flashcard(Some(FOUR), 1, "subject4", "content4", "preparation", "flashcard", "backside4"),
    ChoiceQuestion(Some(FIVE), 1, "subject5", "content5", "preparation", "mc", testAnswerOptions)
  )

  val testFreetextAnswers = Seq(
    FreetextAnswer(Some(1), 1, 1, "freetextAnswer1", "freetextText1"),
    FreetextAnswer(Some(2), 1, 1, "freetextAnswer2", "freetextText2"),
    FreetextAnswer(Some(3), 2, 1, "freetextAnswer3", "freetextText3")
  )

  val testChoiceAnswers = Seq(
    ChoiceAnswer(Some(1), questionId, 1, 1),
    ChoiceAnswer(Some(2), questionId, 1, 2),
    ChoiceAnswer(Some(3), questionId, 1, 3),
    ChoiceAnswer(Some(FOUR), questionId, 1, 3),
    ChoiceAnswer(Some(FIVE), questionId, 1, FOUR)
  )

  reloadSchema()
  Await.result(usersTable ++= testUsers, 10.seconds)
  Await.result(sessionsTable ++= testSessions, 10.seconds)
  Await.result(questionsTable ++= testQuestions, 10.seconds)
  Await.result(answerOptionsTable ++= testAnswerOptions, 10.seconds)
  Await.result(freetextAnswersTable ++= testFreetextAnswers, 10.seconds)
  Await.result(choiceAnswersTable ++= testChoiceAnswers, 10.seconds)
}

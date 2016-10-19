import services.BaseService
import models.User

import akka.event.{ NoLogging, LoggingAdapter }
import utils.MigrationConfig
import org.scalatest._
import scala.concurrent.Await
import scala.concurrent.duration._
import services.BaseService
import models._
import scala.language.existentials
import org.scalatest.BeforeAndAfterEach
import utils.DatabaseConfig

class BaseServiceSpec extends FunSpec with Matchers with MigrationConfig with BeforeAndAfterAll with DatabaseConfig
  with BaseService with SessionApiSpec with QuestionApiSpec with FreetextAnswerApiSpec with ChoiceAnswerApiSpec with CommentApiSpec with TestData {
  protected val log: LoggingAdapter = NoLogging

  import driver.api._


  protected override def beforeAll() = {
    reloadSchema
    Await.result(usersTable ++= testUsers, 10.seconds)
    Await.result(sessionsTable ++= testSessions, 10.seconds)
    Await.result(questionsTable ++= testQuestions, 10.seconds)
    Await.result(answerOptionsTable ++= testAnswerOptions, 10.seconds)
    Await.result(freetextAnswersTable ++= testFreetextAnswers, 10.seconds)
    Await.result(choiceAnswersTable ++= testChoiceAnswers, 10.seconds)
    Await.result(commentsTable ++= testComments, 10.seconds)
  }

  /*Await.result(usersTable ++= testUsers, 10.seconds)
  Await.result(sessionsTable ++= testSessions, 10.seconds)
  Await.result(questionsTable ++= testQuestions, 10.seconds)
  Await.result(answerOptionsTable ++= testAnswerOptions, 10.seconds)
  Await.result(freetextAnswersTable ++= testFreetextAnswers, 10.seconds)
  Await.result(choiceAnswersTable ++= testChoiceAnswers, 10.seconds)*/
}

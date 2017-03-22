package de.thm.arsnova

import de.thm.arsnova.services.BaseService
import de.thm.arsnova.models._
import de.thm.arsnova.utils.{DatabaseConfig, MigrationConfig}

import akka.event.{ NoLogging, LoggingAdapter }
import org.scalatest._
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.existentials
import org.scalatest.BeforeAndAfterEach

class BaseServiceSpec extends FunSpec with Matchers with MigrationConfig with BeforeAndAfterAll with DatabaseConfig
  with BaseService with HateoasSpec with SessionApiSpec with QuestionApiSpec with FreetextAnswerApiSpec with ChoiceAnswerApiSpec with CommentApiSpec
  with FeaturesApiSpec with GlobalMotdApiSpec with TestData {
  protected val log: LoggingAdapter = NoLogging

  import driver.api._


  // triggered by a hook that's triggered once (befor the tests are executed)
  protected override def beforeAll() = {
    reloadSchema
    Await.result(usersTable ++= testUsers, 10.seconds)
    Await.result(sessionsTable ++= testSessions, 10.seconds)
    Await.result(questionsTable ++= testQuestions, 10.seconds)
    Await.result(answerOptionsTable ++= testAnswerOptions, 10.seconds)
    Await.result(freetextAnswersTable ++= testFreetextAnswers, 10.seconds)
    Await.result(choiceAnswersTable ++= testChoiceAnswers, 10.seconds)
    Await.result(commentsTable ++= testComments, 10.seconds)
    Await.result(featuresTable ++= testFeatures, 10.seconds)
    Await.result(globalMotdsTable ++= testGlobalMotds, 10.seconds)
    Await.result(sessionMotdsTable ++= testSessionMotds, 10.seconds)
  }
}

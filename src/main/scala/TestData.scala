import models._
import services._

import scala.util.{Success, Failure}
import scala.concurrent.ExecutionContext.Implicits.global
import slick.driver.MySQLDriver.api._

trait TestData extends BaseService {
  val setup = DBIO.seq(
    usersTable += User(None, "user1", "adfsie324"),
    usersTable += User(None, "user2", "320948492304"),
    usersTable += User(None, "user3", "iae90898988"),

    sessionsTable += Session(None, "12345678", 1, "session1", "s1"),
    sessionsTable += Session(None, "11111111", 1, "session2", "s2"),
    sessionsTable += Session(None, "87654321", 1, "session3", "s2"),

    questionsTable += Freetext(None, 1, "subject1", "First Test Question \\o/", "preparation", "freetext"),
    questionsTable += Freetext(None, 1, "subject2", "Second Question. Isn't that nice?", "preparation", "freetext"),
    questionsTable += Freetext(None, 1, "subject3", "First lecture question", "lecture", "freetext"),
    questionsTable += Flashcard(None, 1, "subject4", "Test Data isn't my thing", "lecture", "flashcard", "this is a backside. I'm making a note here: huge success!"),
    questionsTable += ChoiceQuestion(None, 1, "subject5", "Last but not least", "lecture", "mc", Nil),
    questionsTable += ChoiceQuestion(None, 1, "subject1", "First Question on second session", "lecture", "mc", Nil),

    answerOptionsTable += AnswerOption(None, 5, true, "FirstAnswerOption", 10),
    answerOptionsTable += AnswerOption(None, 5, false, "FirstAnswerOption", -10),
    answerOptionsTable += AnswerOption(None, 5, false, "FirstAnswerOption", -10),
    answerOptionsTable += AnswerOption(None, 5, false, "FirstAnswerOption", -10),
    answerOptionsTable += AnswerOption(None, 5, true, "FirstAnswerOption", 10),
    answerOptionsTable += AnswerOption(None, 6, false, "FirstAnswerOption", -10),
    answerOptionsTable += AnswerOption(None, 6, false, "FirstAnswerOption", -10),
    answerOptionsTable += AnswerOption(None, 6, true, "FirstAnswerOption", 10)

  )
  def populateDB: Unit = {
    val setupFuture = db.run(setup)
    setupFuture.onComplete {
      case Success(dunno) => println("testdata imported")
      case Failure(t) => println("An error has occured: " + t.getMessage)
    }
  }
}

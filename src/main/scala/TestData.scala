package main

import models._
import services._

import scala.util.{Success, Failure}
import scala.concurrent.ExecutionContext.Implicits.global
import slick.driver.MySQLDriver.api._
import scala.concurrent.Await
import scala.concurrent.duration._

trait TestData extends BaseService {
  val pointsForRightAnswer: Int = 10
  val pointsForWrongAnswer: Int = -10
  val answerOptionId1: AnswerOptionId = 5
  val answerOptionId2: AnswerOptionId = 6
  val setup = DBIO.seq(
    usersTable += User(None, "user1", "adfsie324"),
    usersTable += User(None, "user2", "320948492304"),
    usersTable += User(None, "user3", "iae90898988"),

    sessionsTable += Session(None, "12345678", 1, "session1", "s1"),
    sessionsTable += Session(None, "11111111", 1, "session2", "s2"),
    sessionsTable += Session(None, "87654321", 1, "session3", "s2"),

    questionsTable += Question(None, 1, "subject1", "First Test Question \\o/", "preparation", "freetext", None, None),
    questionsTable += Question(None, 1, "subject2", "Second Question. Isn't that nice?", "preparation", "freetext", None, None),
    questionsTable += Question(None, 1, "subject3", "First lecture question", "lecture", "freetext", None, None),
    questionsTable += Question(None, 1, "subject4", "Test Data isn't my thing", "lecture", "flashcard",
      Some(FormatAttributes(Map("backside" -> "this is a backside. I'm making a note here: huge success!"))), None),
    questionsTable += Question(None, 1, "subject5", "Last but not least", "lecture", "mc", None, None),
    questionsTable += Question(None, 1, "subject1", "First Question on second session", "lecture", "mc", None, None),

    answerOptionsTable += AnswerOption(None, Some(answerOptionId1), true, "FirstAnswerOption", pointsForRightAnswer),
    answerOptionsTable += AnswerOption(None, Some(answerOptionId1), false, "FirstAnswerOption", pointsForWrongAnswer),
    answerOptionsTable += AnswerOption(None, Some(answerOptionId1), false, "FirstAnswerOption", pointsForWrongAnswer),
    answerOptionsTable += AnswerOption(None, Some(answerOptionId1), false, "FirstAnswerOption", pointsForWrongAnswer),
    answerOptionsTable += AnswerOption(None, Some(answerOptionId1), true, "FirstAnswerOption", pointsForRightAnswer),
    answerOptionsTable += AnswerOption(None, Some(answerOptionId1), false, "FirstAnswerOption", pointsForWrongAnswer),
    answerOptionsTable += AnswerOption(None, Some(answerOptionId1), false, "FirstAnswerOption", pointsForWrongAnswer),
    answerOptionsTable += AnswerOption(None, Some(answerOptionId1), true, "FirstAnswerOption", pointsForRightAnswer)

  )
  def populateDB: Unit = {
    val setupFuture = db.run(setup)
    Await.result(setupFuture, 10.seconds)
  }
}

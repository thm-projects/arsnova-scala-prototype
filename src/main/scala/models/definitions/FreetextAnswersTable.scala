package models.definitions

import models.{FreetextAnswer, FreetextAnswerId, QuestionId, SessionId}
import slick.driver.MySQLDriver.api._

class FreetextAnswersTable(tag: Tag) extends Table[FreetextAnswer](tag, "freetext_answers") {
  def id = column[FreetextAnswerId]("id", O.PrimaryKey, O.AutoInc)
  def questionId = column[QuestionId]("question_id")
  def sessionId = column[SessionId]("session_id")
  def subject = column[String]("subject")
  def content = column[String]("content")

  def * = (id.?, questionId, sessionId, subject, content) <> ((FreetextAnswer.apply _).tupled, FreetextAnswer.unapply)

  def session = foreignKey("freetext_answer_session_fk", sessionId, TableQuery[QuestionsTable])(_.id)
  def question = foreignKey("freetext_answer_question_fk", questionId, TableQuery[QuestionsTable])(_.id)
}

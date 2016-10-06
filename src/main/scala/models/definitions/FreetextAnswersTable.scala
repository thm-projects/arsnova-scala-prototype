package models.definitions

import models.{FreetextAnswer, FreetextAnswerId, QuestionId, SessionId}
import slick.driver.MySQLDriver.api._
import models.{Question, Session}
import slick.lifted.ForeignKeyQuery

class FreetextAnswersTable(tag: Tag) extends Table[FreetextAnswer](tag, "freetext_answers") {
  def id: Rep[FreetextAnswerId] = column[FreetextAnswerId]("id", O.PrimaryKey, O.AutoInc)
  def questionId: Rep[QuestionId] = column[QuestionId]("question_id")
  def sessionId: Rep[SessionId] = column[SessionId]("session_id")
  def subject: Rep[String] = column[String]("subject")
  def content: Rep[String] = column[String]("content")

  def * = (id.?, questionId, sessionId, subject, content) <> ((FreetextAnswer.apply _).tupled, FreetextAnswer.unapply)

  def session: ForeignKeyQuery[SessionsTable, Session] = foreignKey("freetext_answer_session_fk", sessionId, TableQuery[SessionsTable])(_.id)
  def question: ForeignKeyQuery[QuestionsTable, Question] = foreignKey("freetext_answer_question_fk", questionId, TableQuery[QuestionsTable])(_.id)
}

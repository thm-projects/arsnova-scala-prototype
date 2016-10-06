package models.definitions

import models._
import slick.driver.MySQLDriver.api._
import slick.lifted.ForeignKeyQuery
import models.Session

class QuestionsTable(tag: Tag) extends Table[Question](tag, "questions"){
  def id: Rep[QuestionId] = column[QuestionId]("id", O.PrimaryKey, O.AutoInc)
  def sessionId: Rep[Long] = column[Long]("session_id")
  def subject: Rep[String] = column[String]("subject")
  def content: Rep[String] = column[String]("content")
  def variant: Rep[String] = column[String]("variant")
  def format: Rep[String] = column[String]("format")
  def backside: Rep[String] = column[String]("backside")

  def * = (id.?, sessionId, subject, content, variant, format, backside.?) <> (
    { t: (Option[QuestionId], SessionId, String, String, String, String, Option[String]) => t match {
      case (id, sessionId, subject, content, variant, "flashcard", Some(backside)) =>
        new Flashcard(id, sessionId, subject, content, variant, "flashcard", backside):Question
      case (id, sessionId, subject, content, variant, "mc", _) =>
        new ChoiceQuestion(id, sessionId, subject, content, variant, "mc", Nil):Question
      case (id, sessionId, subject, content, variant, "freetext", _) =>
        new Freetext(id, sessionId, subject, content, variant, "freetext")
    }}, { k: Question => k match {
      case Flashcard(id, sessionId, subject, content, variant, format, backside) =>
        Some((id, sessionId, subject, content, variant, "flashcard", Some(backside))):
          Option[(Option[QuestionId], SessionId, String, String, String, String, Option[String])]
      case ChoiceQuestion(id, sessionId, subject, content, variant, format, answerOptions) =>
        Some((id, sessionId, subject, content, variant, "mc", None)):
          Option[(Option[QuestionId], SessionId, String, String, String, String, Option[String])]
      case Freetext(id, sessionId, subject, content, variant, format) =>
        Some((id, sessionId, subject, content, variant, "freetext", None)):
          Option[(Option[QuestionId], SessionId, String, String, String, String, Option[String])]
  }})

  def session: ForeignKeyQuery[SessionsTable, Session] = foreignKey("question_session_fk", sessionId, TableQuery[SessionsTable])(_.id)
}


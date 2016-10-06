package models.definitions

import models._
import slick.driver.MySQLDriver.api._
import slick.profile.SqlProfile.ColumnOption.Nullable

class QuestionsTable(tag: Tag) extends Table[Question](tag, "questions"){
  def id = column[QuestionId]("id", O.PrimaryKey, O.AutoInc)
  def sessionId = column[Long]("session_id")
  def subject = column[String]("subject")
  def content = column[String]("content")
  def variant = column[String]("variant")
  def format = column[String]("format")
  def backside = column[String]("backside")
  //def * = (id.?, sessionId, subject, content, variant, format, backside.?, hasCorrectAnswer.?) <> ((Question.apply _).tupled, Question.unapply)
  /*def * = format <> ({t: String => t match {
    case "flashcard" => (id.?, sessionId, subject, content, variant, format, backside) <> ((Flashcard.apply _).tupled, Flashcard.unapply)
  }
  })*/
  def * = (id.?, sessionId, subject, content, variant, format, backside.?) <> (
    { t: (Option[QuestionId], SessionId, String, String, String, String, Option[String]) => t match {
      case (id, sessionId, subject, content, variant, "flashcard", Some(backside)) => new Flashcard(id, sessionId, subject, content, variant, "flashcard", backside):Question
      case (id, sessionId, subject, content, variant, "mc", _) => new ChoiceQuestion(id, sessionId, subject, content, variant, "mc", Nil):Question
      case (id, sessionId, subject, content, variant, "freetext", _) => new Freetext(id, sessionId, subject, content, variant, "freetext")
    }}, { k: Question => k match {
      case Flashcard(id, sessionId, subject, content, variant, format, backside) => Some((id, sessionId, subject, content, variant, "flashcard", Some(backside))): Option[(Option[QuestionId], SessionId, String, String, String, String, Option[String])]
      case ChoiceQuestion(id, sessionId, subject, content, variant, format, answerOptions) => Some((id, sessionId, subject, content, variant, "mc", None)): Option[(Option[QuestionId], SessionId, String, String, String, String, Option[String])]
      case Freetext(id, sessionId, subject, content, variant, format) => Some((id, sessionId, subject, content, variant, "freetext", None)): Option[(Option[QuestionId], SessionId, String, String, String, String, Option[String])]
  }})

  def session = foreignKey("question_session_fk", sessionId, TableQuery[SessionsTable])(_.id)
}


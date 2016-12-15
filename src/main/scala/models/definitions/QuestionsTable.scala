package models.definitions

import models._
import slick.driver.MySQLDriver.api._
import slick.lifted.ForeignKeyQuery
import models.Session
import spray.json._

class QuestionsTable(tag: Tag) extends Table[Question](tag, "questions"){
  import mappings.QuestionJsonProtocol._
  def id: Rep[QuestionId] = column[QuestionId]("id", O.PrimaryKey, O.AutoInc)
  def sessionId: Rep[Long] = column[Long]("session_id")
  def subject: Rep[String] = column[String]("subject")
  def content: Rep[String] = column[String]("content")
  def variant: Rep[String] = column[String]("variant")
  def format: Rep[String] = column[String]("format")
  def formatAttributes: Rep[String] = column[String]("format_attributes")

  def * = (id.?, sessionId, subject, content, variant, format, formatAttributes) <> (
    { q: (Option[QuestionId], SessionId, String, String, String, String, String) => q match {
      case (id, sessionId, subject, content, variant, format, formatAttributes) => {
        formatAttributes match {
          case "null" => new Question(id, sessionId, subject, content, variant, format, None, None)
          case _ => val fA = formatAttributes.substring(1, formatAttributes.length - 1)
            .split(",")
            .map(_.split(":"))
            .map { case Array(k, v) => (k.substring(1, k.length-1), v.substring(1, v.length-1))}
            .toMap
            new Question(id, sessionId, subject, content, variant, format, Some(FormatAttributes(fA)), None)
        }
      }
    }}, { q: Question =>
      Some((q.id, q.sessionId, q.subject, q.content, q.variant, q.format, q.formatAttributes.toJson.toString)):
        Option[(Option[QuestionId], SessionId, String, String, String, String, String)]
    })

  def session: ForeignKeyQuery[SessionsTable, Session] = foreignKey("question_session_fk", sessionId, TableQuery[SessionsTable])(_.id)
}


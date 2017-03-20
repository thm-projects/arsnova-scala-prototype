package de.thm.arsnova.models.definitions

import de.thm.arsnova.models._
import slick.driver.MySQLDriver.api._
import slick.lifted.ForeignKeyQuery
import spray.json._

class QuestionsTable(tag: Tag) extends Table[Question](tag, "questions"){
  import de.thm.arsnova.mappings.QuestionJsonProtocol._
  def id: Rep[QuestionId] = column[QuestionId]("id", O.PrimaryKey, O.AutoInc)
  def sessionId: Rep[Long] = column[Long]("session_id")
  def subject: Rep[String] = column[String]("subject")
  def content: Rep[String] = column[String]("content")
  def variant: Rep[String] = column[String]("variant")
  def format: Rep[String] = column[String]("format")
  def formatAttributes: Rep[String] = column[String]("format_attributes")

  def * = (id.?, sessionId, subject, content, variant, format, formatAttributes) <> (
    // first comes the part for fetching
    { q: (Option[QuestionId], SessionId, String, String, String, String, String) => q match {
      case (id, sessionId, subject, content, variant, format, formatAttributes) => {
        formatAttributes match {
          // some question formats don't have formatAttributes
          case "null" => new Question(id, sessionId, subject, content, variant, format, None, None)
          // formatAttributes are stored as JSON strings. This parsese them into the map
          // using the formatAttributes JSON protocol doesn't work since it returns a JsObject
          case _ => val fA = formatAttributes.substring(1, formatAttributes.length - 1)
            .split(",")
            .map(_.split(":"))
            .map { case Array(k, v) => (k.substring(1, k.length-1), v.substring(1, v.length-1))}
            .toMap
            new Question(id, sessionId, subject, content, variant, format, Some(FormatAttributes(fA)), None)
        }
      }
    }}, {
    // part for storing questions in the table
    q: Question =>
      Some((q.id, q.sessionId, q.subject, q.content, q.variant, q.format, q.formatAttributes.toJson.toString)):
        Option[(Option[QuestionId], SessionId, String, String, String, String, String)]
    })

  def session: ForeignKeyQuery[SessionsTable, de.thm.arsnova.models.Session] = foreignKey("question_session_fk", sessionId, TableQuery[SessionsTable])(_.id)
}


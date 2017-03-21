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
  def hint: Rep[String] = column[String]("hint")
  def solution: Rep[String] = column[String]("solution")
  def active: Rep[Boolean] = column[Boolean]("active")
  def votingDisabled: Rep[Boolean] = column[Boolean]("voting_disabled")
  def showStatistic: Rep[Boolean] = column[Boolean]("show_statistic")
  def showAnswer: Rep[Boolean] = column[Boolean]("show_answer")
  def abstentionAllowed: Rep[Boolean] = column[Boolean]("abstention_allowed")
  def formatAttributes: Rep[String] = column[String]("format_attributes")

  // TODO: please clean this up.
  def * = (id.?, sessionId, subject, content, variant, format, hint.?, solution.?, active, votingDisabled, showStatistic,
      showAnswer, abstentionAllowed, formatAttributes) <> (
    // first comes the part for fetching
    { q: (Option[QuestionId], SessionId, String, String, String, String, Option[String], Option[String],
        Boolean, Boolean, Boolean, Boolean, Boolean, String) =>
      q match {
        case (id, sessionId, subject, content, variant, format, hint, solution, active, vD, sS, sA, aA, formatAttributes) => {
          formatAttributes match {
            // some question formats don't have formatAttributes
            case "null" => new Question(id, sessionId, subject, content, variant, format, hint, solution, active, vD, sS, sA, aA, None, None)
            // formatAttributes are stored as JSON strings. This parsese them into the map
            // using the formatAttributes JSON protocol doesn't work since it returns a JsObject
            case _ => val fA = formatAttributes.substring(1, formatAttributes.length - 1)
              .split(",")
              .map(_.split(":"))
              .map { case Array(k, v) => (k.substring(1, k.length-1), v.substring(1, v.length-1))}
              .toMap
              new Question(id, sessionId, subject, content, variant, format, hint, solution, active, vD, sS, sA, aA, Some(FormatAttributes(fA)), None)
          }
        }
    }}, {
    // part for storing questions in the table
    q: Question =>
      Some((q.id, q.sessionId, q.subject, q.content, q.variant, q.format, q.hint, q.solution,
          q.active, q.votingDisabled, q.showStatistic, q.showAnswer, q.abstentionAllowed, q.formatAttributes.toJson.toString)):
        Option[(Option[QuestionId], SessionId, String, String, String, String, Option[String], Option[String],
            Boolean, Boolean, Boolean, Boolean, Boolean, String)]
    })

  def session: ForeignKeyQuery[SessionsTable, de.thm.arsnova.models.Session] = foreignKey("question_session_fk", sessionId, TableQuery[SessionsTable])(_.id)
}


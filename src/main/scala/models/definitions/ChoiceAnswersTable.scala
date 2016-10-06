package models.definitions

import models.{ChoiceAnswer, ChoiceAnswerId, QuestionId, SessionId, AnswerOptionId}
import slick.driver.MySQLDriver.api._

class ChoiceAnswersTable(tag: Tag) extends Table[ChoiceAnswer](tag, "choice_answers") {
  def id = column[ChoiceAnswerId]("id", O.PrimaryKey, O.AutoInc)
  def questionId = column[QuestionId]("question_id")
  def sessionId = column[SessionId]("session_id")
  def answerOptionId = column[AnswerOptionId]("answer_option_id")

  def * = (id.?, questionId, sessionId, answerOptionId) <> ((ChoiceAnswer.apply _).tupled, ChoiceAnswer.unapply)
}

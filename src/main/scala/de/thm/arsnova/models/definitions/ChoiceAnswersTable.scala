package de.thm.arsnova.models.definitions

import de.thm.arsnova.models.{ChoiceAnswer, ChoiceAnswerId, QuestionId, SessionId, AnswerOptionId}
import slick.driver.MySQLDriver.api._

class ChoiceAnswersTable(tag: Tag) extends Table[ChoiceAnswer](tag, "choice_answers") {
  def id: Rep[ChoiceAnswerId] = column[ChoiceAnswerId]("id", O.PrimaryKey, O.AutoInc)
  def questionId: Rep[QuestionId] = column[QuestionId]("question_id")
  def sessionId: Rep[SessionId] = column[SessionId]("session_id")
  def answerOptionId: Rep[AnswerOptionId] = column[AnswerOptionId]("answer_option_id")

  def * = (id.?, questionId, sessionId, answerOptionId) <> ((ChoiceAnswer.apply _).tupled, ChoiceAnswer.unapply)
}

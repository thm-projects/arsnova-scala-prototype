package de.thm.arsnova.models.definitions

import de.thm.arsnova.models.{AnswerOption, AnswerOptionId, QuestionId, Question}
import slick.driver.MySQLDriver.api._
import slick.lifted.ForeignKeyQuery

class AnswerOptionsTable(tag: Tag) extends Table[AnswerOption](tag, "answer_options") {
  def id: Rep[AnswerOptionId] = column[AnswerOptionId]("id", O.PrimaryKey, O.AutoInc)
  def questionId: Rep[QuestionId] = column[QuestionId]("question_id")
  def correct: Rep[Boolean] = column[Boolean]("correct")
  def text: Rep[String] = column[String]("content")
  def value: Rep[Int] = column[Int]("points")

  def * = (id.?, questionId.?, correct, text, value) <> ((AnswerOption.apply _).tupled, AnswerOption.unapply)

  def question: ForeignKeyQuery[QuestionsTable, Question] = foreignKey("answer_option_question_fk", questionId, TableQuery[QuestionsTable])(_.id)
}

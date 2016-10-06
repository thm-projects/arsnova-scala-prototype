package models.definitions

import models.{AnswerOption, AnswerOptionId, QuestionId}
import slick.driver.MySQLDriver.api._

class AnswerOptionsTable(tag: Tag) extends Table[AnswerOption](tag, "answer_options") {
  def id = column[AnswerOptionId]("id", O.PrimaryKey, O.AutoInc)
  def questionId = column[QuestionId]("question_id")
  def correct = column[Boolean]("correct")
  def text = column[String]("content")
  def value = column[Int]("points")

  def * = (id.?, questionId, correct, text, value) <> ((AnswerOption.apply _).tupled, AnswerOption.unapply)

  def question = foreignKey("answer_option_question_fk", questionId, TableQuery[QuestionsTable])(_.id)
}
package models

case class AnswerOption(id: Option[AnswerOptionId], questionId: Option[QuestionId], correct: Boolean, text: String, value: Int)

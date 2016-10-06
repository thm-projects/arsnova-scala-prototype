package models

case class AnswerOption(id: Option[AnswerOptionId], questionId: QuestionId, correct: Boolean, text: String, value: Int)
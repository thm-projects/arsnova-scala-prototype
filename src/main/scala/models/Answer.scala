package models

case class FreetextAnswer(id: Option[FreetextAnswerId], questionId: QuestionId, sessionId: SessionId, subject: String, text: String)
case class ChoiceAnswer(id: Option[ChoiceAnswerId], questionId: QuestionId, sessionId: SessionId, answerOptionId: AnswerOptionId)

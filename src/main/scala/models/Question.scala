package models

trait Question {
  val id: Option[QuestionId]
  val sessionId: SessionId
  val subject: String
  val content: String
  val variant: String
  val format: String
}

/*trait ChoiceQuestion {
  val answerOptions: Seq[AnswerOption]
}*/

case class Freetext(id: Option[QuestionId], sessionId: SessionId, subject: String, content: String, variant: String,
                    format: String) extends Question
case class Flashcard(id: Option[QuestionId], sessionId: SessionId, subject: String, content: String, variant: String,
                     format: String, backside: String) extends Question
//case class MC(id: Option[QuestionId], sessionId: SessionId, subject: String, content: String, variant: String,
// format: String, answerOptions: Seq[AnswerOption], hasCorrectAnswer: Boolean) extends Question with ChoiceQuestion

case class ChoiceQuestion(id: Option[QuestionId], sessionId: SessionId, subject: String, content: String,
                          variant: String, format: String, answerOptions: Seq[AnswerOption]) extends Question

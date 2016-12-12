package models

case class Attribute(key: String, value: String)

case class FormatAttributes(attributes: Map[String, String])
case class Question(
                     id: Option[QuestionId],
                     sessionId: SessionId,
                     subject: String,
                     content: String,
                     variant: String,
                     format: String,
                     formatAttributes: FormatAttributes,
                     answerOptions: Option[Seq[AnswerOption]]
                   )

/*case class Freetext(id: Option[QuestionId], sessionId: SessionId, subject: String, content: String, variant: String,
                    format: String) extends Question
case class Flashcard(id: Option[QuestionId], sessionId: SessionId, subject: String, content: String, variant: String,
                     format: String, backside: String) extends Question
case class ChoiceQuestion(id: Option[QuestionId], sessionId: SessionId, subject: String, content: String,
                          variant: String, format: String, answerOptions: Seq[AnswerOption]) extends Question
*/

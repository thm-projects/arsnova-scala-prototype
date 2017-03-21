package de.thm.arsnova.models

case class FormatAttributes(attributes: Map[String, String])

case class Question(
                     id: Option[QuestionId],
                     sessionId: SessionId,
                     subject: String,
                     content: String,
                     variant: String,
                     format: String,
                     hint: Option[String],
                     solution: Option[String],
                     active: Boolean,
                     votingDisabled: Boolean,
                     showStatistic: Boolean,
                     showAnswer: Boolean,
                     abstentionAllowed: Boolean,
                     formatAttributes: Option[FormatAttributes],
                     answerOptions: Option[Seq[AnswerOption]]
                   )

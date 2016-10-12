package services

import models._
import slick.driver.MySQLDriver.api._
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object QuestionService extends BaseService {
  def findQuestionsBySessionIdAndVariant(sessionId: SessionId, variant: String): Future[Seq[Question]] = {
    db.run(questionsTable.filter(q => q.sessionId === sessionId && q.variant === variant).result).map(qSeq =>
      Future.traverse(qSeq) { q: Question => q match {
        case ChoiceQuestion(id, _, _, _, _, _, _) => {
          AnswerOptionService.findByQuestionId(id.get).map(a => q.asInstanceOf[ChoiceQuestion].copy(answerOptions = a))
        }
        case _ => Future(q)
      }}
    ).flatMap(identity)
  }

  def findAllBySessionId(sessionId: SessionId): Future[Seq[Question]] = {
    db.run(questionsTable.filter(_.sessionId === sessionId).result).map(qSeq =>
      Future.traverse(qSeq) { q: Question => q match {
        case ChoiceQuestion(id, _, _, _, _, _, _) => {
          AnswerOptionService.findByQuestionId(id.get).map(a => q.asInstanceOf[ChoiceQuestion].copy(answerOptions = a))
        }
        case _ => Future(q)
      }}
    ).flatMap(identity)
  }
  def findById(questionId: QuestionId): Future[Question] = {
    db.run(questionsTable.filter(_.id === questionId).result.head).map(q => q match {
        case ChoiceQuestion(id, _, _, _, _, _, _) => {
          AnswerOptionService.findByQuestionId(id.get).map(a => q.asInstanceOf[ChoiceQuestion].copy(answerOptions = a))
        }
        case _ => Future(q)
      }
    ).flatMap(identity)
  }
  def create(newQuestion: Question): Future[QuestionId] = {
    newQuestion match {
      case ChoiceQuestion(_, _, _, _, _, _, answerOptions) => {
        (db.run(questionsTable returning questionsTable.map(_.id) += newQuestion)).map(
          qId => {
            val answerOptionsWithQId = answerOptions.map(_.copy(questionId = Some(qId)))
            AnswerOptionService.create(answerOptionsWithQId)
            qId
          })
        }
      case _ => questionsTable returning questionsTable.map(_.id) += newQuestion
    }
  }
  def update(question: Question, questionId: QuestionId): Future[Int] = questionsTable.filter(_.id ===  questionId)
    .map(q => (q.subject, q.content))
    .update(question.subject, question.content)

  def delete(questionId: QuestionId): Future[Int] = questionsTable.filter(_.id === questionId).delete
}

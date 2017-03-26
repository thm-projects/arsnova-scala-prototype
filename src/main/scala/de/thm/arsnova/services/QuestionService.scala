package de.thm.arsnova.services

import de.thm.arsnova.models._
import slick.driver.MySQLDriver.api._
import scala.concurrent.{Future, Await}
import scala.concurrent.duration._

object QuestionService extends BaseService {
  import de.thm.arsnova.Context.executor

  def findQuestionsBySessionIdAndVariant(sessionId: SessionId, variant: String): Future[Seq[Question]] = {
    val questionSeqFuture: Future[Seq[Question]] = db.run(questionsTable.filter(q => q.sessionId === sessionId && q.variant === variant).result)
    questionSeqFuture.map((qSequence: Seq[Question]) =>
      qSequence.map((q: Question) =>
        q.format match {
          case "mc" => Await.result(AnswerOptionService.findByQuestionId(q.id.get).map(a => q.copy(answerOptions = Some(a))), 5.seconds)
          case _ => q
        }
      )
    )
  }

  def findAllBySessionId(sessionId: SessionId): Future[Seq[Question]] = {
    val questionSeqFuture: Future[Seq[Question]] = db.run(questionsTable.filter(_.sessionId === sessionId).result)
    questionSeqFuture.map((qSequence: Seq[Question]) =>
      qSequence.map((q: Question) =>
        q.format match {
          case "mc" => Await.result(AnswerOptionService.findByQuestionId(q.id.get).map(a => q.copy(answerOptions = Some(a))), 5.seconds)
          case _ => q
        }
      )
    )
  }
  def findById(questionId: QuestionId): Future[Question] = {
    val questionFuture: Future[Question] = db.run(questionsTable.filter(_.id === questionId).result.head)
    questionFuture.map((q: Question) =>
      q.format match {
        case "mc" => Await.result(AnswerOptionService.findByQuestionId(q.id.get).map(a => q.copy(answerOptions = Some(a))), 5.seconds)
        case _ => q
      }
    )
  }
  def create(newQuestion: Question): Future[QuestionId] = {
    newQuestion.format match {
      case "mc" => {
        (db.run(questionsTable returning questionsTable.map(_.id) += newQuestion)).map(
          qId => {
            val answerOptionsWithQId = newQuestion.answerOptions.get.map(_.copy(questionId = Some(qId)))
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

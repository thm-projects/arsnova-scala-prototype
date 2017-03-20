package de.thm.arsnova.services

import de.thm.arsnova.models._
import slick.driver.MySQLDriver.api._

import scala.concurrent.Future

object ChoiceAnswerService extends BaseService {
  def getById(choiceAnswerId: ChoiceAnswerId): Future[ChoiceAnswer] = {
    choiceAnswersTable.filter(_.id === choiceAnswerId).result.head
  }
  def getByQuestionId(questionId: QuestionId): Future[Seq[ChoiceAnswer]] = {
    choiceAnswersTable.filter(_.questionId === questionId).result
  }

  def create(choiceAnswer: ChoiceAnswer): Future[ChoiceAnswerId] = {
    choiceAnswersTable returning choiceAnswersTable.map(_.id) += choiceAnswer
  }

  def update(choiceAnswer: ChoiceAnswer): Future[Int] = {
    choiceAnswersTable.filter(_.id === choiceAnswer.id.get).map(a => a.answerOptionId).update(choiceAnswer.answerOptionId)
  }

  def delete(choiceAnswerId: ChoiceAnswerId): Future[Int] = {
    choiceAnswersTable.filter(_.id === choiceAnswerId).delete
  }

  def deleteAllByQuestionId(questionId: QuestionId): Future[Int] = {
    choiceAnswersTable.filter(_.questionId === questionId).delete
  }
}

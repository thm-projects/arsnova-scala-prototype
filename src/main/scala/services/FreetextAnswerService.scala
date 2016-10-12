package services

import models._
import slick.driver.MySQLDriver.api._
import scala.concurrent.Future

object FreetextAnswerService extends BaseService {
  def getById(freetextAnswerId: FreetextAnswerId): Future[FreetextAnswer] = {
    freetextAnswersTable.filter(_.id === freetextAnswerId).result.head
  }

  def getByQuestionId(questionId: QuestionId): Future[Seq[FreetextAnswer]] = {
    freetextAnswersTable.filter(_.questionId === questionId).result
  }

  def create(freetextAnswer: FreetextAnswer): Future[FreetextAnswerId] = {
    println(freetextAnswer)
    freetextAnswersTable returning freetextAnswersTable.map(_.id) += freetextAnswer
  }

  def update(freetextAnswer: FreetextAnswer): Future[Int] = {
    freetextAnswersTable.filter(_.id === freetextAnswer.id.get).map(a => (a.subject, a.content)).update(freetextAnswer.subject, freetextAnswer.text)
  }

  def delete(freetextAnswerId: FreetextAnswerId): Future[Int] = {
    freetextAnswersTable.filter(_.id === freetextAnswerId).delete
  }

  def deleteAllByQuestionId(questionId: QuestionId): Future[Int] = {
    freetextAnswersTable.filter(_.questionId === questionId).delete
  }
}

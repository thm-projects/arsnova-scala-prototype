package de.thm.arsnova.services

import de.thm.arsnova.models._
import slick.driver.MySQLDriver.api._
import scala.concurrent.Future

object AnswerOptionService extends BaseService {
  def findByQuestionId(questionId: QuestionId): Future[Seq[AnswerOption]] = {
    (for {
      answerOption <- answerOptionsTable.filter(_.questionId === questionId)
    } yield answerOption).result
  }

  def create(answerOptions: Seq[AnswerOption]): Future[Seq[AnswerOptionId]] = {
    answerOptionsTable returning answerOptionsTable.map(_.id) ++= answerOptions
  }
}

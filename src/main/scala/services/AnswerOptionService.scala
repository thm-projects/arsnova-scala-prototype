package services

import models._
import slick.driver.MySQLDriver.api._
import scala.concurrent.Future

object AnswerOptionService extends BaseService {
  def findByQuestionId(questionId: QuestionId): Future[Seq[AnswerOption]] = {
    (for {
      answerOption <- answerOptionsTable.filter(_.questionId === questionId)
    } yield answerOption).result
  }
}

package services

import models._
import slick.driver.MySQLDriver.api._
import scala.concurrent.Future

object AnswerService extends BaseService {
  def createFreetextAnswer(freetextAnswer: FreetextAnswer): Future[FreetextAnswerId] = {
    freetextAnswersTable returning freetextAnswersTable.map(_.id) += freetextAnswer
  }
  def createChoiceAnswer(choiceAnswer: ChoiceAnswer): Future[ChoiceAnswerId] = {
    choiceAnswersTable returning choiceAnswersTable.map(_.id) += choiceAnswer
  }
}

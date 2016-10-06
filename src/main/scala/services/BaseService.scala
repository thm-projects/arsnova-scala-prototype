package services

import models.ChoiceAnswer
import models.definitions._
import slick.dbio.{Effect, NoStream}
import slick.lifted.TableQuery
import slick.profile.{FixedSqlAction, FixedSqlStreamingAction, SqlAction}
import utils.DatabaseConfig

import scala.concurrent.Future

trait BaseService extends DatabaseConfig {
  val usersTable = TableQuery[UsersTable]
  val sessionsTable = TableQuery[SessionsTable]
  val questionsTable = TableQuery[QuestionsTable]
  val answerOptionsTable = TableQuery[AnswerOptionsTable]
  val freetextAnswersTable = TableQuery[FreetextAnswersTable]
  val choiceAnswersTable = TableQuery[ChoiceAnswersTable]

  protected implicit def executeFromDb[A](action: SqlAction[A, NoStream, _ <: slick.dbio.Effect]): Future[A] = {
    db.run(action)
  }
  protected implicit def executeReadStreamFromDb[A](action: FixedSqlStreamingAction[Seq[A], A, _ <: slick.dbio.Effect]): Future[Seq[A]] = {
    db.run(action)
  }
}

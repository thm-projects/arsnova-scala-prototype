package de.thm.arsnova.services

import com.sun.org.apache.xml.internal.security.c14n.implementations.Canonicalizer11_OmitComments
import de.thm.arsnova.models.ChoiceAnswer
import de.thm.arsnova.models.definitions._
import slick.dbio.{Effect, NoStream}
import slick.lifted.TableQuery
import slick.sql.{FixedSqlAction, FixedSqlStreamingAction, SqlAction}
import de.thm.arsnova.utils.DatabaseConfig

import scala.concurrent.Future

trait BaseService extends DatabaseConfig {
  val usersTable = TableQuery[UsersTable]
  val sessionsTable = TableQuery[SessionsTable]
  val questionsTable = TableQuery[QuestionsTable]
  val answerOptionsTable = TableQuery[AnswerOptionsTable]
  val freetextAnswersTable = TableQuery[FreetextAnswersTable]
  val choiceAnswersTable = TableQuery[ChoiceAnswersTable]
  val commentsTable = TableQuery[CommentsTable]
  val featuresTable = TableQuery[FeaturesTable]
  val globalMotdsTable = TableQuery[GlobalMotdsTable]
  val sessionMotdsTable = TableQuery[SessionMotdsTable]

  protected implicit def executeFromDb[A](action: SqlAction[A, NoStream, _ <: slick.dbio.Effect]): Future[A] = {
    db.run(action)
  }
  protected implicit def executeReadStreamFromDb[A](action: FixedSqlStreamingAction[Seq[A], A, _ <: slick.dbio.Effect]): Future[Seq[A]] = {
    db.run(action)
  }
}

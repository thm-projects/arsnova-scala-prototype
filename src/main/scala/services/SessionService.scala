package services

import models.{UserId, Session, SessionId}
import slick.driver.MySQLDriver.api._
import scala.concurrent.Future

object SessionService extends BaseService{
  def findUserSessions(userId: UserId): Future[Seq[Session]] = {
    (for{
      user <- usersTable.filter(_.id === userId)
      sessions <- sessionsTable.filter(_.userId === user.id)
    } yield sessions).result
  }
  def findById(sessionId: SessionId): Future[Session] = {
    sessionsTable.filter(_.id === sessionId).result.head
  }
  def findByUserIdAndId(userId: UserId, sessionId: SessionId): Future[Session] = {
    (for{
      user <- usersTable.filter(_.id === userId)
      post <- sessionsTable.filter(_.id === sessionId)
    } yield post).result.head
  }
  def create(session: Session): Future[SessionId] = sessionsTable returning sessionsTable.map(_.id) += session
  def update(newSession: Session, sessionId: SessionId): Future[Int] = sessionsTable.filter(_.id === sessionId)
    .map(newSession => (newSession.title, newSession.shortTitle))
    .update((newSession.title, newSession.shortTitle))

  def delete(sessionId: SessionId): Future[Int] = sessionsTable.filter(_.id === sessionId).delete

}

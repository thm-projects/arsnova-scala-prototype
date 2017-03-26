package de.thm.arsnova.services

import de.thm.arsnova.models.{UserId, Session, SessionId, Features}
import slick.driver.MySQLDriver.api._
import scala.concurrent.Future

object SessionService extends BaseService {
  import de.thm.arsnova.Context.executor

  def findUserSessions(userId: UserId): Future[Seq[Session]] = {
    val resultTupleQry = for {
      sessions <- sessionsTable.filter(_.userId === userId)
      sessionFeatures <- featuresTable.filter(_.sessionId === sessions.id)
    } yield (sessions, sessionFeatures)
    val resultTuple: Future[Seq[Tuple2[Session, Features]]]  = db.run(resultTupleQry.result)
    resultTuple.map(_.map(tuple =>
      tuple._1.copy(features = Some(tuple._2))
    ))
  }

  def findById(sessionId: SessionId): Future[Session] = {
    val futureTupleQry = (for {
      session <- sessionsTable.filter(_.id === sessionId)
      sessionFeatures <- featuresTable.filter(_.sessionId === session.id)
    } yield (session, sessionFeatures)).result.head
    val futureTuple = db.run(futureTupleQry)
    futureTuple.map(tuple => tuple._1.copy(features = Some(tuple._2)))
  }

  def findByUserIdAndId(userId: UserId, sessionId: SessionId): Future[Session] = {
    val futureTupleQry = (for {
      session <- sessionsTable.filter(s => (s.id === sessionId) && (s.userId === userId))
      sessionFeatures <- featuresTable.filter(_.sessionId === session.id)
    } yield (session, sessionFeatures)).result.head
    val futureTuple = db.run(futureTupleQry)
    futureTuple.map(tuple => tuple._1.copy(features = Some(tuple._2)))
  }

  def create(session: Session): Future[SessionId] = {
    val sessionIdFuture: Future[SessionId] = sessionsTable returning sessionsTable.map(_.id) += session
    sessionIdFuture.map { sessionId =>
      session.features match {
        case Some(features) => FeaturesService.create(features)
        case None => FeaturesService.create(
          Features(None, sessionId, true, true, true, true, true, true, true, true, true, true)
        )
      }
      sessionId
    }
  }

  def update(newSession: Session, sessionId: SessionId): Future[Int] = {
    newSession.features match {
      case Some(features) => FeaturesService.create(features)
      case None => FeaturesService.create(
        Features(None, sessionId, true, true, true, true, true, true, true, true, true, true)
      )
    }
    sessionsTable.filter(_.id === sessionId)
      .map(newSession => (newSession.title, newSession.shortName))
      .update((newSession.title, newSession.shortName))
  }

  def delete(sessionId: SessionId): Future[Int] = sessionsTable.filter(_.id === sessionId).delete

}

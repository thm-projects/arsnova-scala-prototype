package de.thm.arsnova.services

import de.thm.arsnova.models.{SessionMotd, SessionMotdId, SessionId}
import slick.driver.MySQLDriver.api._
import scala.concurrent.Future

object SessionMotdService extends BaseService {
  def getById(motdId: SessionMotdId): Future[SessionMotd] = {
    sessionMotdsTable.filter(_.id === motdId).result.head
  }

  def getBySessionId(sessionId: SessionId): Future[Seq[SessionMotd]] = {
    sessionMotdsTable.filter(_.sessionId === sessionId).result
  }

  def create(motd: SessionMotd): Future[SessionMotdId] = {
    sessionMotdsTable.returning(sessionMotdsTable.map(_.id)) += motd
  }

  def update(motd: SessionMotd): Future[Int] = {
    sessionMotdsTable.filter(_.id === motd.id.get).map(m =>
      (m.title, m.text))
        .update(motd.title, motd.text)
  }

  def delete(motdId: SessionMotdId): Future[Int] = {
    sessionMotdsTable.filter(_.id === motdId).delete
  }
}

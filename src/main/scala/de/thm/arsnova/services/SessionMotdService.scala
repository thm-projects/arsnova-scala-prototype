package de.thm.arsnova.services

import de.thm.arsnova.models.{SessionMotd, SessionMotdId}
import slick.driver.MySQLDriver.api._
import scala.concurrent.Future

object SessionMotdService extends BaseService {
  def getById(motdId: SessionMotdId): Future[SessionMotd] = {
    globalMotdsTable.filter(_.id === motdId).result.head
  }

  def getByAudience(audience: String): Future[Seq[SessionMotd]] = {
    globalMotdsTable.filter(_.audience === audience).result
  }

  def create(motd: SessionMotd): Future[SessionMotdId] = {
    globalMotdsTable.returning(globalMotdsTable.map(_.id)) += motd
  }

  def update(motd: SessionMotd): Future[Int] = {
    globalMotdsTable.filter(_.id === motd.id.get).map(m =>
      (m.title, m.text))
        .update(motd.title, motd.text)
  }

  def delete(motdId: SessionMotdId): Future[Int] = {
    globalMotdsTable.filter(_.id === motdId).delete
  }
}

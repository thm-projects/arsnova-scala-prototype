package de.thm.arsnova.services

import de.thm.arsnova.models.{GlobalMotd, GlobalMotdId}
import slick.driver.MySQLDriver.api._
import scala.concurrent.Future

object GlobalMotdService extends BaseService {
  def getById(motdId: GlobalMotdId): Future[GlobalMotd] = {
    globalMotdsTable.filter(_.id === motdId).result.head
  }

  def getByAudience(audience: String): Future[Seq[GlobalMotd]] = {
    globalMotdsTable.filter(_.audience === audience).result
  }

  def create(motd: GlobalMotd): Future[GlobalMotdId] = {
    globalMotdsTable.returning(globalMotdsTable.map(_.id)) += motd
  }

  def update(motd: GlobalMotd): Future[Int] = {
    globalMotdsTable.filter(_.id === motd.id.get).map(m =>
      (m.audience, m.title, m.text))
        .update(motd.audience, motd.title, motd.text)
  }

  def delete(motdId: GlobalMotdId): Future[Int] = {
    globalMotdsTable.filter(_.id === motdId).delete
  }
}

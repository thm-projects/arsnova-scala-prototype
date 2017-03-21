package de.thm.arsnova.models.definitions

import de.thm.arsnova.models.{SessionMotd, SessionMotdId, SessionId}
import slick.driver.MySQLDriver.api._
import slick.lifted.ForeignKeyQuery

class SessionMotdsTable(tag: Tag) extends Table[SessionMotd](tag, "session_motds") {
  def id: Rep[SessionMotdId] = column[SessionMotdId]("id", O.PrimaryKey, O.AutoInc)
  def sessionId: Rep[SessionId] = column[SessionId]("session_id")
  def startdate: Rep[String] = column[String]("startdate")
  def enddate: Rep[String] = column[String]("enddate")
  def title: Rep[String] = column[String]("title")
  def text: Rep[String] = column[String]("content")

  def * = (id.?, sessionId, startdate, enddate, title, text) <> ((SessionMotd.apply _).tupled, SessionMotd.unapply)
}

package de.thm.arsnova.models.definitions

import de.thm.arsnova.models.{GlobalMotd, GlobalMotdId}
import slick.driver.MySQLDriver.api._
import slick.lifted.ForeignKeyQuery

class GlobalMotdsTable(tag: Tag) extends Table[GlobalMotd](tag, "global_motds") {
  def id: Rep[GlobalMotdId] = column[GlobalMotdId]("id", O.PrimaryKey, O.AutoInc)
  def startdate: Rep[String] = column[String]("startdate")
  def enddate: Rep[String] = column[String]("enddate")
  def title: Rep[String] = column[String]("title")
  def text: Rep[String] = column[String]("content")
  def audience: Rep[String] = column[String]("audience")

  def * = (id.?, startdate, enddate, title, text, audience) <> ((GlobalMotd.apply _).tupled, GlobalMotd.unapply)
}

package de.thm.arsnova.models.definitions

import de.thm.arsnova.models.{SessionId, UserId, Session, User}
import slick.driver.MySQLDriver.api._
import slick.lifted.ForeignKeyQuery

class SessionsTable(tag: Tag) extends Table[Session](tag, "sessions"){
  def id: Rep[SessionId] = column[SessionId]("id", O.PrimaryKey, O.AutoInc)
  def key: Rep[String] = column[String]("sessionkey")
  def userId: Rep[UserId] = column[UserId]("user_id")
  def title: Rep[String] = column[String]("title")
  def shortTitle: Rep[String] = column[String]("short_title")

  def * = (id.?, key, userId, title, shortTitle) <> ((Session.apply _).tupled, Session.unapply)

  def author: ForeignKeyQuery[UsersTable, User] = foreignKey("session_user_fk", userId, TableQuery[UsersTable])(_.id)
}

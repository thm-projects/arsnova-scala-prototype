package models.definitions

import models.{SessionId, UserId, Session}
import slick.driver.MySQLDriver.api._
import slick.lifted.ForeignKeyQuery
import models.User

class SessionsTable(tag: Tag) extends Table[Session](tag, "sessions"){
  def id: Rep[SessionId] = column[SessionId]("id", O.PrimaryKey, O.AutoInc)
  def key: Rep[String] = column[String]("sessionkey")
  def userId: Rep[UserId] = column[UserId]("user_id")
  def title: Rep[String] = column[String]("title")
  def shortTitle: Rep[String] = column[String]("short_title")

  def * = (id.?, key, userId, title, shortTitle) <> ((Session.apply _).tupled, Session.unapply)

  def author: ForeignKeyQuery[UsersTable, User] = foreignKey("session_user_fk", userId, TableQuery[UsersTable])(_.id)
}

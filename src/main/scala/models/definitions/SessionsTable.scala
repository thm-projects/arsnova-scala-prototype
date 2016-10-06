package models.definitions

import models.{SessionId, UserId, Session}
import slick.driver.MySQLDriver.api._

class SessionsTable(tag: Tag) extends Table[Session](tag, "sessions"){
  def id = column[SessionId]("id", O.PrimaryKey, O.AutoInc)
  def key = column[String]("sessionkey")
  def userId = column[UserId]("user_id")
  def title = column[String]("title")
  def shortTitle = column[String]("short_title")
  def * = (id.?, key, userId, title, shortTitle) <> ((Session.apply _).tupled, Session.unapply)

  def author = foreignKey("session_user_fk", userId, TableQuery[UsersTable])(_.id)
}

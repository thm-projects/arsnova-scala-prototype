package models.definitions

import models.{UserId, User}
import slick.driver.MySQLDriver.api._

class UsersTable(tag: Tag) extends Table[User](tag, "users"){
  def id = column[UserId]("id", O.PrimaryKey, O.AutoInc)
  def username = column[String]("username")
  def password = column[String]("pwd")
  def * = (id.?, username, password) <> ((User.apply _).tupled, User.unapply)
}


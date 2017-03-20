package de.thm.arsnova.models.definitions

import de.thm.arsnova.models.{UserId, User}
import slick.driver.MySQLDriver.api._

class UsersTable(tag: Tag) extends Table[User](tag, "users"){
  def id: Rep[UserId] = column[UserId]("id", O.PrimaryKey, O.AutoInc)
  def username: Rep[String] = column[String]("username")
  def password: Rep[String] = column[String]("pwd")

  def * = (id.?, username, password) <> ((User.apply _).tupled, User.unapply)
}


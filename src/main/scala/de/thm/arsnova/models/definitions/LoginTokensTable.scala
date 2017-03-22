package de.thm.arsnova.models.definitions

import de.thm.arsnova.models.LoginToken
import slick.driver.MySQLDriver.api._

class LoginTokensTable(tag: Tag) extends Table[LoginToken](tag, "login_tokens") {
  def token: Rep[String] = column[String]("token", O.PrimaryKey, O.AutoInc)
  def userId: Rep[Long] = column[Long]("user_id")
  def created: Rep[String] = column[String]("created")
  def modified: Rep[String] = column[String]("modified")
  def lastUsed: Rep[String] = column[String]("last_used")

  def * = (token, userId, created, modified.?, lastUsed) <> ((LoginToken.apply _).tupled, LoginToken.unapply)
}
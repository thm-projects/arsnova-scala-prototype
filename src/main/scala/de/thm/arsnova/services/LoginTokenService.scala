package de.thm.arsnova.services

import slick.driver.MySQLDriver.api._
import de.thm.arsnova.models.{LoginToken, User}
import scala.concurrent.Future
import java.util.Date
import java.util.UUID.randomUUID

object LoginTokenService extends BaseService {
  def create(user: User): Future[LoginToken] = {
    val now = new Date().getTime
    val token = LoginToken(randomUUID.toString, user.id.get, now.toString, None, now.toString)
    loginTokensTable.returning(loginTokensTable) += token
  }
}

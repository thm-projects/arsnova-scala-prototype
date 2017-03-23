package de.thm.arsnova.services

import slick.driver.MySQLDriver.api._
import de.thm.arsnova.models.{LoginToken, User}
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object AuthService extends BaseService {
  def login(username: String, password: String): Future[Option[LoginToken]] = {
    db.run(usersTable.filter((user) => user.username === username && user.password === password).result.head).map {
      case u: User => Some(Await.result(LoginTokenService.create(u), 5.seconds))
      case _ => None
    }
  }
}

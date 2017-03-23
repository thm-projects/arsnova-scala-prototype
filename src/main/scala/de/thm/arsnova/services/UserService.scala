package de.thm.arsnova.services

import de.thm.arsnova.models.{User, UserId}
import slick.driver.MySQLDriver.api._

import scala.concurrent.Future

object UserService extends BaseService {
  def findById(userId: UserId): Future[User] = {
    usersTable.filter(_.id === userId).result.head
  }

  def create(user: User): Future[UserId] = {
    usersTable returning usersTable.map(_.id) += user
  }
  def update(newUser: User, userId: UserId): Future[Int] = {
    usersTable.filter(_.id === userId)
      .map(user => (user.username, user.password))
      .update((newUser.userName, newUser.password))
  }

  def delete(userId: UserId): Future[Int] = {
    usersTable.filter(_.id === userId).delete
  }

  def getByLoginTokenString(loginTokenString: String): Future[User] = {
    val qry = for {
      token <- loginTokensTable filter(_.token === loginTokenString)
      user <- usersTable if (token.userId === user.id)
    } yield (user)
    db.run(qry.result.head)
  }

}

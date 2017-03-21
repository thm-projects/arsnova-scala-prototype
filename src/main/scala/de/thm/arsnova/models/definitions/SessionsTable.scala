package de.thm.arsnova.models.definitions

import de.thm.arsnova.models.{SessionId, UserId, Session, User}
import slick.driver.MySQLDriver.api._
import slick.lifted.ForeignKeyQuery

class SessionsTable(tag: Tag) extends Table[Session](tag, "sessions"){
  def id: Rep[SessionId] = column[SessionId]("id", O.PrimaryKey, O.AutoInc)
  def key: Rep[String] = column[String]("keyword")
  def userId: Rep[UserId] = column[UserId]("user_id")
  def title: Rep[String] = column[String]("title")
  def shortName: Rep[String] = column[String]("short_name")
  def lastOwnerActivity: Rep[String] = column[String]("last_owner_activity")
  def creationTime: Rep[String] = column[String]("creation_time")
  def active: Rep[Boolean] = column[Boolean]("active")
  def feedbackLock: Rep[Boolean] = column[Boolean]("feedback_lock")
  def flipFlashcards: Rep[Boolean] = column[Boolean]("flip_flashcards")

  def * = (id.?, key, userId, title, shortName, lastOwnerActivity, creationTime, active, feedbackLock, flipFlashcards) <> ({
    s: (Option[SessionId], String, UserId, String, String, String, String, Boolean, Boolean, Boolean) => s match {
      case (id, key, userId, title, shortName, lastOwnerActivity, creationTime, active, feedbackLock, flipFlashcards) =>
        Session (id, key, userId, title, shortName, lastOwnerActivity, creationTime, active, feedbackLock, flipFlashcards, None)
    }}, {
    s: Session =>
      Some((s.id, s.keyword, s.userId, s.title, s.shortName, s.lastOwnerActivity, s.creationTime, s.active, s.feedbackLock, s.flipFlashcards))
  })

  def author: ForeignKeyQuery[UsersTable, User] = foreignKey("session_user_fk", userId, TableQuery[UsersTable])(_.id)
}

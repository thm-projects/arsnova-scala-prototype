package models.definitions

import models.{CommentId, SessionId, Session, UserId, User, Comment}
import slick.driver.MySQLDriver.api._
import slick.lifted.ForeignKeyQuery
import java.sql.Timestamp

class CommentsTable(tag: Tag) extends Table[Comment](tag, "comments"){
  def id: Rep[CommentId] = column[CommentId]("id", O.PrimaryKey, O.AutoInc)
  def userId: Rep[UserId] = column[UserId]("user_id")
  def sessionId: Rep[SessionId] = column[SessionId]("session_id")
  def isRead: Rep[Boolean] = column[Boolean]("is_read")
  def subject: Rep[String] = column[String]("subject")
  def text: Rep[String] = column[String]("content")
  def createdAt: Rep[String] = column[String]("created_at")

  def * = (id.?, userId, sessionId, isRead, subject, text, createdAt) <> ((Comment.apply _).tupled, Comment.unapply)

  def author: ForeignKeyQuery[UsersTable, User] = foreignKey("comment_user_fk", userId, TableQuery[UsersTable])(_.id)
  def session: ForeignKeyQuery[SessionsTable, Session] = foreignKey("comment_session_fk", sessionId, TableQuery[SessionsTable])(_.id)
}

package services

import models.{Comment, CommentId, SessionId}
import slick.driver.MySQLDriver.api._

import scala.concurrent.Future

object CommentService extends BaseService {
  def getById(commentId: CommentId): Future[Comment] = {
    commentsTable.filter(_.id === commentId).result.head
  }

  def getBySessionId(sessionId: SessionId): Future[Seq[Comment]] = {
    commentsTable.filter(_.sessionId === sessionId).result
  }

  def create(comment: Comment): Future[Int] = {
    commentsTable returning commentsTable.map(_.id) += comment
  }

  def update(comment: Comment): Future[Int] = {
    commentsTable.filter(_.id === comment.id.get).map(c =>(c.isRead, c.subject, c.text))
      .update(comment.isRead, comment.subject, comment.text)
  }

  def delete(commentId: CommentId): Future[Int] = {
    commentsTable.filter(_.id === commentId).delete
  }
}

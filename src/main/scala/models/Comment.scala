package models

import java.sql.Timestamp

case class Comment(id: Option[CommentId], userId: UserId, sessionId: SessionId, isRead: Boolean, subject: String, text: String, createdAt: Timestamp)
package de.thm.arsnova.models

case class Comment(id: Option[CommentId], userId: UserId, sessionId: SessionId, isRead: Boolean, subject: String, text: String, createdAt: String)

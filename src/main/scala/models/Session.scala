package models

case class Session(id: Option[SessionId], key: String, userId: UserId, title: String, shortTitle: String)

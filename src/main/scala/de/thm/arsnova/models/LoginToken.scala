package de.thm.arsnova.models

case class LoginToken(token: String, userId: Long, created: String, modified: Option[String], lastUsed: String)

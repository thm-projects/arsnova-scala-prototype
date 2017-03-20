package de.thm.arsnova.models.definitions

import de.thm.arsnova.models.{FeaturesId, Features, SessionId, Session}
import slick.driver.MySQLDriver.api._
import slick.lifted.ForeignKeyQuery

class FeaturesTable(tag: Tag) extends Table[Features](tag, "features") {
  def id: Rep[FeaturesId] = column[FeaturesId]("id", O.PrimaryKey, O.AutoInc)
  def sessionId: Rep[SessionId] = column[SessionId]("session_id")
  def slides: Rep[Boolean] = column[Boolean]("slides")
  def flashcards: Rep[Boolean] = column[Boolean]("flashcards")
  def peerGrading: Rep[Boolean] = column[Boolean]("peer_grading")
  def peerInstruction: Rep[Boolean] = column[Boolean]("peer_instruction")
  def comments: Rep[Boolean] = column[Boolean]("comments")
  def tileView: Rep[Boolean] = column[Boolean]("tile_view")
  def jitt: Rep[Boolean] = column[Boolean]("jitt")
  def learningProgress: Rep[Boolean] = column[Boolean]("learning_progress")
  def feedback: Rep[Boolean] = column[Boolean]("feedback")
  def liveQuestions: Rep[Boolean] = column[Boolean]("live_questions")

  def * = (id.?, sessionId, slides, flashcards, peerGrading, peerInstruction, comments, tileView, jitt, learningProgress, feedback,
    liveQuestions) <> ((Features.apply _).tupled, Features.unapply)

  def session: ForeignKeyQuery[SessionsTable, Session] = foreignKey("features_session_fk", sessionId, TableQuery[SessionsTable])(_.id)
}

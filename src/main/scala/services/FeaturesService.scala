package services

import models._
import slick.driver.MySQLDriver.api._
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object FeaturesService extends BaseService {
  def getById(featuresId: FeaturesId): Future[Features] = {
    featuresTable.filter(_.id === featuresId).result.head
  }

  def getBySessionid(sessionId: SessionId): Future[Features] = {
    featuresTable.filter(_.sessionId === sessionId).result.head
  }

  def create(features: Features): Future[FeaturesId] = {
    featuresTable.returning(featuresTable.map(_.id)) += features
  }

  def update(features: Features): Future[Int] = {
    featuresTable.filter(_.id === features.id.get)
      .map(f => (f.slides, f.flashcards, f.peerGrading, f.peerInstruction, f.comments, f.tileView, f.jitt, f.learningProgress,
        f.feedback, f.liveQuestions))
      .update(features.slides, features.flashcards, features.peerGrading, features.peerInstruction, features.comments,
        features.tileView, features.jitt, features.learningProgress, features.feedback, features.liveQuestions)
  }

  def delete(featuresId: FeaturesId): Future[Int] = {
    featuresTable.filter(_.id === featuresId).delete
  }
}

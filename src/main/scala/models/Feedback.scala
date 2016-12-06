package models

import akka.actor._
import akka.stream.scaladsl.Flow
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.stream.OverflowStrategy
import akka.stream.scaladsl._

class Feedback(sessionId: SessionId, actorSystem: ActorSystem) {
  private[this] val feedbackActor = actorSystem.actorOf(Props(classOf[FeedbackActor], sessionId))

  def websocketFlow(): Flow[Message, Message, Any] =
    Flow(Source.actorRef[FeedbackMessage](500, OverflowStrategy.fail)) { implicit builder =>
      chatSource =>
        val fromWebsocket = builder.add(
          Flow[Message].collect {
            case TextMessage.Strict(txt) => IncomingFeedback(txt.toInt)
          }
        )

        val backToWebsocket = builder.add(
          Flow[FeedbackMessage].map {
            case FeedbackMessage(fb) => TextMessage(fb.toString)
          }
        )

        val merge = builder.add(Merge[FeedbackEvent](2))
    }

  //def sendMessage(msg: )
}

object Feedback {
  def apply(sessionId: SessionId)(implicit actorSystem: ActorSystem): Feedback =
    new Feedback(sessionId, actorSystem)
}

object FeedbackWrapper {
  var feedbackSessionMap: Map[SessionId, Feedback] = Map.empty[SessionId, Feedback]

  def findOrCreate(sessionId: SessionId)(implicit actorSystem: ActorSystem): Feedback =
    feedbackSessionMap.getOrElse(sessionId, createFeedbackForSession(sessionId))

  private def createFeedbackForSession(sessionId: SessionId)(implicit actorSystem: ActorSystem): Feedback = {
    val feedback = Feedback(sessionId)
    feedbackSessionMap += sessionId -> feedback
    feedback
  }
}

class FeedbackActor(sessionId: SessionId) extends Actor {
  val differentFeedbackOptions: Int = 5
  var participants: Map[String, ActorRef] = Map.empty[String, ActorRef]
  var feedback: Array[Int] = new Array[Int](differentFeedbackOptions)

  def sendFeedback(): Unit = {
    participants.values.foreach(_ ! FeedbackMessage(feedback))
  }

  override def receive: Receive = {
    case UserJoined(actorRef: ActorRef) =>
      participants += "" -> actorRef
  }
}

case class FeedbackMessage(feedback: Array[Int])

sealed trait FeedbackEvent

case class UserJoined(actorRef: ActorRef) extends FeedbackEvent

case class IncomingFeedback(feedback: Int) extends FeedbackEvent

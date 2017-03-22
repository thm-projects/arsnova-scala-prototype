package de.thm.arsnova

import akka.http.scaladsl.server.Directives._
import de.thm.arsnova.api._

trait Routes extends ApiErrorHandler with UserApi with SessionApi with QuestionApi with FreetextAnswerApi with ChoiceAnswerApi
  with CommentApi with FeaturesApi with SessionMotdApi with GlobalMotdApi {
  val routes = {
      userApi ~
      sessionApi ~
      questionApi ~
      freetextAnswerApi ~
      choiceAnswerApi ~
      commentApi ~
      featuresApi ~
      sessionMotdApi ~
      globalMotdApi
    } ~ path("")(getFromResource("public/index.html"))
}

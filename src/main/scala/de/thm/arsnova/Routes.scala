package de.thm.arsnova

import akka.http.scaladsl.server.Directives._
import de.thm.arsnova.api._

trait Routes extends ApiErrorHandler with UserApi with SessionApi with QuestionApi with FreetextAnswerApi with ChoiceAnswerApi
  with CommentApi with FeaturesApi {
  val routes = {
      userApi ~
      sessionApi ~
      questionApi ~
      freetextAnswerApi ~
      choiceAnswerApi ~
      commentApi ~
      featuresApi
    } ~ path("")(getFromResource("public/index.html"))
}

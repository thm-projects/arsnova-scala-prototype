import akka.http.scaladsl.server.Directives._
import api._

trait Routes extends ApiErrorHandler with UserApi with SessionApi with QuestionApi with FreetextAnswerApi with ChoiceAnswerApi {
  val routes =
    pathPrefix("api") {
      userApi ~
      sessionApi ~
      questionApi ~
      freetextAnswerApi ~
      choiceAnswerApi
    } ~ path("")(getFromResource("public/index.html"))
}

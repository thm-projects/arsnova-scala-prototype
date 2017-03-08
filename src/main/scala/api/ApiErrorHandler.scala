package api

import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.ExceptionHandler

/*
This interface catches Exceptions that are thrown by any api route.
All exception handlers musst be implicit to automatically handle these.
*/
trait ApiErrorHandler {
  implicit def myExceptionHandler: ExceptionHandler = ExceptionHandler {
    case e: NoSuchElementException =>
      extractUri { uri =>
        complete(HttpResponse(NotFound, entity = s"Invalid id: ${e.getMessage}"))
      }
  }
}

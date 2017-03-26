package de.thm.arsnova

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import de.thm.arsnova.utils.{MigrationConfig, Config}

import scala.concurrent.ExecutionContext

object WebServer extends App with Config with MigrationConfig with Routes {
  import de.thm.arsnova.Context._
  protected val log: LoggingAdapter = Logging(system, getClass)

  if (args.contains("migrate")) {
    migrate
  }
  if (args.contains("cleanDB")) {
    reloadSchema
  }
  // if (args.contains("mockupData"))

  Http().bindAndHandle(handler = logRequestResult("log")(routes), interface = httpInterface, port = httpPort)
}

package main

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import utils.{MigrationConfig, Config}
import scala.concurrent.Await
import scala.concurrent.duration._

import scala.concurrent.ExecutionContext

object Main extends App with Config with MigrationConfig with Routes with TestData {
  import Shared._

  //migrate()
  //reloadSchema()
  //populateDB

  Http().bindAndHandle(handler = logRequestResult("log")(routes), interface = httpInterface, port = httpPort)
  //Await.ready(system.terminate(), 5.seconds)
}

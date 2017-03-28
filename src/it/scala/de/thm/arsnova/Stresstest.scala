package de.thm.arsnova

import de.thm.arsnova.auditor.BasicAuditorScenario
import de.thm.arsnova.tutor.BasicTutorScenario

import io.gatling.core.Predef._ // 2
import io.gatling.http.Predef._
import scala.concurrent.duration._

class Stresstest extends Simulation {
  println("starting webserver")
  WebServer.main(Array())

  val httpProtocol = http
    .baseURL("http://localhost:9000")
    .inferHtmlResources(BlackList(""".*\.css""", """.*\.js""", """.*\.ico"""), WhiteList())
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .doNotTrackHeader("1")
    .userAgentHeader("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:50.0) Gecko/20100101 Firefox/50.0")

  setUp(
    BasicTutorScenario.scn.inject(rampUsers(100) over (5 seconds)),
    BasicAuditorScenario.scn.inject(rampUsers(1000) over (5 seconds))
  ).protocols(httpProtocol)
}
name := "arsnova-3-backend"

version := "0.0.1"

scalaVersion := "2.12.1"

enablePlugins(GatlingPlugin)

libraryDependencies ++= {
  val akkaVersion = "2.4.17"
  val akkaHTTPVersion = "10.0.1"
  val scalaTestVersion = "3.0.0"
  val scalaMockVersion = "3.2.2"
  val slickVersion = "3.2.0"
  val gatlingVersion = "2.2.2"

  Seq(
    "com.typesafe.akka"     %% "akka-actor"                           % akkaVersion,
    "com.typesafe.akka"     %% "akka-stream"                          % akkaVersion,
    "com.typesafe.akka"     %% "akka-testkit"                         % akkaVersion % "test",
    "com.typesafe.akka"     %% "akka-http"                            % akkaHTTPVersion,
    "com.typesafe.akka"     %% "akka-http-testkit"                    % akkaHTTPVersion,
    "com.typesafe.akka"     %% "akka-http-spray-json"                 % akkaHTTPVersion,
    "com.typesafe.slick"    %% "slick"                                % slickVersion,
    "com.typesafe.slick"    %% "slick-hikaricp"                       % slickVersion,
    "org.slf4j"             %  "slf4j-nop"                            % "1.7.21",
    "mysql"                 %  "mysql-connector-java"                 % "6.0.3",
    "org.flywaydb"          %  "flyway-core"                          % "3.2.1",
    "org.scalatest"         %% "scalatest"                            % scalaTestVersion,
    "io.gatling.highcharts" % "gatling-charts-highcharts"             % gatlingVersion % "test,it",
    "io.gatling"            % "gatling-test-framework"                % gatlingVersion % "test,it"
  )
}

// skip Tests in assembly job
test in assembly := {}
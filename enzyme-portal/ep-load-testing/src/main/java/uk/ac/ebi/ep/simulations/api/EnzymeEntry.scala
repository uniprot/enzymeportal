package uk.ac.ebi.ep.simulations.api
import io.gatling.core.Predef.{atOnceUsers, _}
import io.gatling.http.Predef._

import scala.concurrent.duration._
import scala.language.postfixOps

/**
 * Performance testing for the Enzyme Portal REST API - Enzyme entry.
 * @author joseph <joseph@ebi.ac.uk>
 *
 */
class EnzymeEntry extends Simulation {


  val feeder = Array(
    Map("ec" -> "1.1.1.1", "ec" -> "2.1.1.1"),
    Map("ec" -> "3.1.1.1", "ec" -> "4.1.1.1"),
    Map("ec" -> "5.1.1.1", "ec" -> "6.1.1.1"),
    Map("ec" -> "7.1.1.1", "ec" -> "7.1.1.3")
  ).random


  val enzymeEndPointScenario = scenario("Enzyme Entry Endpoints")
    .feed(feeder)
    .exec(
      http("Enzyme by EC ")
        .get("/enzyme/${ec}").check(status.is(200))
    )
    .pause(5)
    .exec(
      http("Associated Protein by EC")
        .get("/enzyme/${ec}/proteins")
        .queryParam("limit","10")
        .check(status.is(200))
    )
    .pause(5)
    .exec(
      http("Reaction Parameters by EC")
        .get("/enzyme/${ec}/kineticParameters")
        .queryParam("limit","10")
        .check(status.is(200))
    )
    .pause(5)
    .exec(
      http("Mechanisms by EC")
        .get("/enzyme/{ec}/mechanisms")
        .check(status.is(200))
    )



  val httpProtocol = http
    .baseUrl("https://wwwdev.ebi.ac.uk/enzymeportal/rest")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-UK,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")
    .disableCaching

  setUp(
    enzymeEndPointScenario.inject(
      nothingFor(2 seconds),
      atOnceUsers(1),
      rampUsers(10) during (10 seconds),
      constantUsersPerSec(1) during (10 seconds))
      .protocols(httpProtocol)
  ).assertions(global.responseTime.percentile(50).lte(200))
    .assertions(forAll.failedRequests.percent.lte(40))
    .assertions(global.failedRequests.count.lte(10))
}




package uk.ac.ebi.ep.simulations.website

import io.gatling.core.Predef.{atOnceUsers, _}
import io.gatling.http.Predef._

import scala.concurrent.duration._
import scala.language.postfixOps

class EcPage extends Simulation {

  val feeder = Array(
    Map("ec" -> "1.1.1.1", "ec" -> "2.1.1.1"),
    Map("ec" -> "3.1.1.1", "ec" -> "4.1.1.1"),
    Map("ec" -> "5.1.1.1", "ec" -> "6.1.1.1"),
    Map("ec" -> "7.1.1.1", "ec" -> "7.1.1.3")
  ).random


  val searchWithRandomECScenario = scenario("Concrete Enzyme Page")
    .feed(feeder)
    .exec(
      http("search by ec")
        .get("/ec/${ec}").check(status.is(200))
    )




  val httpProtocol = http
    .baseUrl("https://wwwdev.ebi.ac.uk/enzymeportal")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-UK,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")
    .disableCaching

  setUp(
    searchWithRandomECScenario.inject(
      nothingFor(2 seconds),
      atOnceUsers(10),
            incrementUsersPerSec(1)
              .times(5)
              .eachLevelLasting(5 seconds)
              .separatedByRampsLasting(5 seconds)
              .startingFrom(10)
    ).protocols(httpProtocol)
  ).assertions(global.responseTime.percentile(50).lte(20000))
    .assertions(forAll.failedRequests.percent.lte(40))
    .assertions(global.failedRequests.count.lte(10))
}


package uk.ac.ebi.ep.simulations.website
import io.gatling.core.Predef.{atOnceUsers, _}
import io.gatling.http.Predef._

import scala.concurrent.duration._
import scala.language.postfixOps

class ProteinPage extends Simulation {

  val feeder = Array(
    Map("accession" -> "P43235", "accession" -> "O76074"),
    Map("accession" -> "P39610", "accession" -> "Q8W1X2"),
    Map("accession" -> "A7UQU8", "accession" -> "P40191")
  ).random


  val searchWithAccessionScenario = scenario("Protein entry pages")
    .feed(feeder)
    .exec(
      http("Protein function page")
        .get("/search/${accession}/enzyme").check(status.is(200))
    )
    .pause(5)
    .exec(
      http("Protein Structure page")
        .get("/search/${accession}/proteinStructure").check(status.is(200))
    )
    .pause(5)
    .exec(
      http("Reaction & Mechanism page")
        .get("/search/${accession}/reactionsMechanisms").check(status.is(200))
    )
    .pause(5)
    .exec(
      http("Pathways page")
        .get("/search/${accession}/pathways").check(status.is(200))
    )
    .pause(5)
    .exec(
      http("Small Molecules page")
        .get("/search/${accession}/molecules").check(status.is(200))
    )
    .pause(5)
    .exec(
      http("Disease page")
        .get("/search/${accession}/diseaseDrugs").check(status.is(200))
    )
    .pause(5)
    .exec(
      http("Literature page")
        .get("/search/${accession}/literature").check(status.is(200))
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
    searchWithAccessionScenario.inject(
      nothingFor(2 seconds),
      atOnceUsers(1),
      rampUsers(10) during (10 seconds),
      constantUsersPerSec(1) during (10 seconds))
      .protocols(httpProtocol)
  ).assertions(global.responseTime.percentile(50).lte(200))
    .assertions(forAll.failedRequests.percent.lte(40))
    .assertions(global.failedRequests.count.lte(10))
}

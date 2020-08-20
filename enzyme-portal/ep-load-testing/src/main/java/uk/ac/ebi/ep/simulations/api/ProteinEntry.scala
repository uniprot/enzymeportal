package uk.ac.ebi.ep.simulations.api

import io.gatling.core.Predef.{atOnceUsers, _}
import io.gatling.http.Predef._

import scala.concurrent.duration._
import scala.language.postfixOps
/**
 * Performance testing for the Enzyme Portal REST API - Protein entry.
 * @author joseph <joseph@ebi.ac.uk>
 *
 */
class ProteinEntry extends Simulation {


  val feeder = Array(
    Map("accession" -> "P43235", "accession" -> "O76074"),
    Map("accession" -> "P39610", "accession" -> "Q8W1X2"),
    Map("accession" -> "A7UQU8", "accession" -> "P40191")
  ).random


  val proteinEndPointScenario = scenario("Protein Entry Endpoints")
    .feed(feeder)
    .exec(
      http("Protein Entry by UniProt Accession")
        .get("/protein/${accession}").check(status.is(200))
    )
    .pause(5)
    .exec(
      http("Protein Structure by UniProt Accession")
        .get("/protein/${accession}/proteinStructure").check(status.is(200))
    )
    .pause(5)
    .exec(
      http("Reaction by UniProt Accession")
        .get("/protein/${accession}/reaction").check(status.is(200))
    )
    .pause(5)
    .exec(
      http("Pathways by UniProt Accession")
        .get("/protein/${accession}/pathways").check(status.is(200))
    )
    .pause(5)
    .exec(
      http("Small Molecules by UniProt Accession")
        .get("/protein/${accession}/smallmolecules").check(status.is(200))
    )
    .pause(5)
    .exec(
      http("Disease by UniProt Accession")
        .get("/protein/${accession}/diseases").check(status.is(200))
    )
    .pause(5)
    .exec(
      http("Citation by UniProt Accession ")
        .get("/protein/${accession}/citation").check(status.is(200))
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
    proteinEndPointScenario.inject(
      nothingFor(2 seconds),
      atOnceUsers(1),
      rampUsers(10) during (10 seconds),
      constantUsersPerSec(1) during (10 seconds))
      .protocols(httpProtocol)
  ).assertions(global.responseTime.percentile(50).lte(200))
    .assertions(forAll.failedRequests.percent.lte(40))
    .assertions(global.failedRequests.count.lte(10))
}



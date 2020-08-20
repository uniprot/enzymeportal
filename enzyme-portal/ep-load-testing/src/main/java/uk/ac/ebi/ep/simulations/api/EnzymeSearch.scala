package uk.ac.ebi.ep.simulations.api
import scala.language.postfixOps
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import scala.language.postfixOps
/**
 * Performance testing for the Enzyme Portal REST API - Search.
 * @author joseph <joseph@ebi.ac.uk>
 *
 */
class EnzymeSearch extends Simulation {

  object EnzymeCentricResult{
    val feeder = csv("search.csv").random
    val search = exec(
      http("Enzyme-centric search ")
        .get("/enzymes/search")
        .queryParam("dataType","json")
        .queryParam("page","0")
        .queryParam("pageSize","10")
        .queryParam("query","sildenafil")
        .check(status.is(200))
        .check(jsonPath("$..enzymeName").is("3',5'-cyclic-GMP phosphodiesterase")))
      .pause(5 seconds)
      .feed(feeder)
      .exec(
        http("Enzyme-centric search - Mixed search terms ")
          .get("/enzymes/search")
          .queryParam("dataType","json")
          .queryParam("page","0")
          .queryParam("pageSize","10")
          .queryParam("query","${Name}")
          .check(status.is(200)))
  }
  object ProteinCentricResult{
    val feeder = csv("search.csv").random
    val search = exec(
      http("Protein-centric search ")
        .get("/search/proteins")
        .queryParam("dataType","json")
        .queryParam("page","0")
        .queryParam("pageSize","10")
        .queryParam("query","pyruvate kinase")
        .queryParam("ec","2.7.1.40")
        .check(status.is(200))
        .check(jsonPath("$..accession").is("P53657")))

  }

  val httpProtocol = http
    .baseUrl("https://wwwdev.ebi.ac.uk/enzymeportal/rest")
    .disableCaching
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-UK,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  val searchScenario = scenario("Searches ...").exec(EnzymeCentricResult.search,ProteinCentricResult.search)
  setUp(
    searchScenario.inject(
      nothingFor(2 seconds),
      atOnceUsers(1),
      rampUsersPerSec(100) to(500) during(10 seconds)
    ).protocols(httpProtocol)
  ).maxDuration(1 minute)
    .assertions(global.responseTime.max.lte(60000))
    .assertions(global.responseTime.percentile(75).lte(40000))
    .assertions(global.successfulRequests.percent.gte(80))
    .assertions(forAll.failedRequests.percent.lte(50))
    .assertions(global.failedRequests.count.lte(80))

}

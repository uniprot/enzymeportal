package uk.ac.ebi.ep.simulations.website

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import scala.language.postfixOps

/**
 * Performance testing for the search feature in the Enzyme Portal website.
 * @author joseph <joseph@ebi.ac.uk>
 *
 */
class Search extends Simulation {

  object KeywordSearch {
    val feeder = csv("search.csv").random
    val search = feed(feeder).exec(
      http("Search by keyword")
        .get("/enzymes?searchparams.type=KEYWORD&searchparams.text=${Name}&keywordType=KEYWORD")
        .check(status.is(200))
    ).pause(5 seconds)
      .exec(
        http("Autocomplete Search - Search term")
          .get("/service/search")
          .queryParam("dataType","json")
          .queryParam("name","huma")
          .check(status.is(200))
          .check(jsonPath("$..suggestion").is("human"))
      ).pause(5 seconds)
      .exec(
        http("Autocomplete Search (POST) - Search term")
          .post("/service/auto")
          .formParam("name","pyruva")
          .check(status.is(200))
          .check(jsonPath("$..suggestion").is("pyruvate"))
    )
  }
  object ResourceIdSearch {
    val feeder = csv("search.csv").shuffle
    val search = exec(
      http("Search By ChEBI ID or RHEA ID")
        .get("/enzymes?searchparams.type=KEYWORD&searchparams.text=${Id}&keywordType=KEYWORD")
        .check(status.is(200))
    ).pause(5 seconds)
  }

  object AssocitedProteinSearch {
    val search = exec(
      http("Incomplete EC Search")
        .post("/enzymes")
        .formParam("searchKey","1.1.1.-")
        .formParam("filterFacet","")
        .formParam("servicePage","1")
        .formParam("keywordType","KEYWORD")
        .formParam("searchId","1.1.1.-")
        .formParam("searchparams.text","1.1.1.-")
        .check(status.is(200))
    ).pause(5)
      .exec(
        http("Associated Protein Search")
          .post("/search")
          .formParam("ec", "3.4.23.50")
          .formParam("searchKey","human")
          .formParam("filterFacet","")
          .formParam("servicePage","1")
          .formParam("keywordType","KEYWORD")
          .formParam("searchId","human")
          .formParam("searchparams.text","human")
          .check(status.is(200))
      )
  }

  val httpProtocol = http
    //.baseUrl("http://localhost:8080/enzymeportal")
    .baseUrl("https://wwwdev.ebi.ac.uk/enzymeportal")
    .disableCaching
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-UK,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  //  scenario as a composition
  val searchScenario = scenario("Searches ...").exec(KeywordSearch.search,ResourceIdSearch.search,AssocitedProteinSearch.search)
  setUp(
    searchScenario.inject(
      nothingFor(2 seconds),
      atOnceUsers(10),
      rampUsersPerSec(10) to(200) during(10 seconds)
    ).protocols(httpProtocol)
  ).maxDuration(1 minute)
    .assertions(global.responseTime.max.lte(60000))
    .assertions(global.responseTime.percentile(75).lte(40000))
    .assertions(global.successfulRequests.percent.gte(80))
    .assertions(forAll.failedRequests.percent.lte(50))
    .assertions(global.failedRequests.count.lte(70))
}

package uk.ac.ebi.ep.simulations.website


import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import scala.language.postfixOps

/**
 * Performance testing for the browse by pages in the Enzyme Portal website.
 * @author joseph <joseph@ebi.ac.uk>
 *
 */
class BrowseBy extends Simulation {

  object Diseases {

    val browse = exec(
      http("Browse By Diseases")
        .get("/browse/disease")
    ).pause(5 seconds)
      .exec(
        http("Autocomplete Search - disease name")
          .get("/service/diseases?name=canc")
      )
      .pause(1)
      .exec(
        http("Search Enzymes by Disease")
          .get("/enzymes?searchKey=Breast%20cancer%20(BC)%20&searchparams.type=KEYWORD&searchparams.previoustext=Breast%20cancer%20(BC)%20&searchparams.start=0&searchparams.text=Breast%20cancer%20(BC)%20&keywordType=DISEASE&searchId=114480")
      )
      .pause(10)
  }
  object Taxonomy {
    val browse = exec(http("Browse By Taxonomy").get("/browse/taxonomy"))
      .pause(1)
      .exec(http("Autocomplete Search - scientific name").get("/service/taxonomy-autocomplete-service?name=homo%20sap"))
      .pause(1)
      .exec(http("Search Enzymes by Taxonomy")
        .get("/enzymes?searchKey=Homo%20sapiens&searchparams.type=KEYWORD&searchparams.previoustext=Homo%20sapiens&searchparams.start=0&searchparams.text=Homo%20sapiens&keywordType=TAXONOMY&searchId=9606"))
      .pause(10)
  }
  object Pathways {
    val browse = exec(
      http("Browse By Pathways")
        .get("/browse/pathways")
    ).pause(1)
      .exec(
        http("Autocomplete Search - Pathway name")
          .get("/service/pathways?name=xeno")
      )
      .pause(1)
      .exec(
        http("Search Enzymes by Pathways")
          .get("/enzymes?searchKey=R-211981&searchparams.type=KEYWORD&searchparams.previoustext=Xenobiotics&searchparams.start=0&searchparams.text=Xenobiotics&keywordType=PATHWAYS&searchId=R-211981")
      )
      .pause(10)
  }
  object ProteinFamilies {
    val browse = exec(
      http("Browse By Protein Families")
        .get("/browse/families")
    ).pause(1)
      .exec(
        http("Autocomplete Search - protein family name")
          .get("/service/families?name=cap")
      )
      .pause(1)
      .exec(
        http("Search Enzymes by Protein Family")
          .get("/enzymes?searchKey=FG9QDS&searchparams.type=KEYWORD&searchparams.previoustext=CAP%20family&searchparams.start=0&searchparams.text=CAP%20family&keywordType=FAMILIES&searchId=FG9QDS")
      )
      .pause(10)
  }
  object Cofactors {
    val browse = exec(
      http("Browse By Cofactors")
        .get("/browse/cofactors")
    ).pause(1)
      .exec(
        http("Autocomplete Search - cofactor name")
          .get("/service/cofactors?name=mg")
      )
      .pause(1)
      .exec(
        http("Search Enzymes by Cofactor")
          .get("/enzymes?searchKey=CHEBI:18420&searchparams.type=KEYWORD&searchparams.previoustext=Mg(2+)&searchparams.start=0&searchparams.text=Mg(2+)&keywordType=COFACTORS&searchId=CHEBI:18420")
      )
      .pause(10)
  }
  object Metabolites {
    val browse = exec(
      http("Browse By Metabolites")
        .get("/browse/metabolites")
    ).pause(1)
      .exec(
        http("Autocomplete Search - metabolite name")
          .get("/service/metabolites?name=glut")
      )
      .pause(1)
      .exec(
        http("Search Enzymes by Metabolite")
          .get("/enzymes?searchKey=CHEBI:57925&searchparams.type=KEYWORD&searchparams.previoustext=glutathione&searchparams.start=0&searchparams.text=glutathione&keywordType=METABOLITES&searchId=CHEBI:57925")
      )
      .pause(10)
  }



  val httpProtocol = http
    .baseUrl("https://wwwdev.ebi.ac.uk/enzymeportal")
    .disableCaching
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-UK,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  //  scenario as a composition
  val browseByScenario = scenario("Browse by pages").exec(Diseases.browse,Taxonomy.browse,Cofactors.browse, Pathways.browse, ProteinFamilies.browse,Metabolites.browse)

  setUp(
    browseByScenario.inject(
      nothingFor(2 seconds),
      atOnceUsers(1),
      rampUsersPerSec(1) to(20) during(10 seconds)
    ).protocols(httpProtocol)
  ).assertions(global.responseTime.max.lte(50000))//Assert that the max response time of all requests is between 3600 - 10000 ms
    .assertions(global.responseTime.percentile(75).lte(20000))
    .assertions(global.successfulRequests.percent.gte(80))
    .assertions(forAll.failedRequests.percent.lte(20)) // Assert that every request has no more than 50% of failing requests
    .assertions(global.failedRequests.count.lte(20))
}

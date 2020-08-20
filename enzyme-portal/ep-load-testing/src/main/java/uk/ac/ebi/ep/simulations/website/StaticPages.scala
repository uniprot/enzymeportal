package uk.ac.ebi.ep.simulations.website
import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._
import scala.language.postfixOps


/**
 * Performance testing for the static pages (home, about, FAQ etc.) in the Enzyme Portal website.
 * @author joseph <joseph@ebi.ac.uk>
 *
 */
class StaticPages extends Simulation {

  val pageScenario = scenario("Static landing pages")
    .exec(
      http("home page")
        .get("/").check(status.is(200))
    )
    .pause(1)
    .exec(
      http("about us page")
        .get("/about").check(status.is(200))
    )
    .pause(1)
    .exec(
      http("FAQ page")
        .get("/faq").check(status.is(200))
    )
    .pause(1)
    .exec(
      http("basket information page")
        .get("/basket").check(status.is(200))
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
    pageScenario.inject(
      nothingFor(2 seconds),
      atOnceUsers(10),
     //rampUsers(500) during(10 seconds),
      //constantUsersPerSec(100) during (20 seconds)
      //heavisideUsers(1000) during(20 seconds)
      //new test
//      incrementUsersPerSec(10)
//        .times(5)
//        .eachLevelLasting(10 seconds)
//        .separatedByRampsLasting(10 seconds)
//        .startingFrom(10),
      rampUsers(3000) during(30 seconds)
      //concurrent
//      incrementConcurrentUsers(500)
//        .times(5)
//        .eachLevelLasting(10 seconds)
//        .separatedByRampsLasting(10 seconds)
//        .startingFrom(10)
    ).protocols(httpProtocol)
  )
    .assertions(global.responseTime.max.lte(50000))//Assert that the max response time of all requests is less than 50000 ms
    .assertions(global.responseTime.percentile(99).gte(60))
    .assertions(forAll.failedRequests.percent.lte(40)) // Assert that every request has no more than 5% of failing requests
    .assertions(global.failedRequests.count.lte(20))

}

package uk.ac.ebi.ep.simulations.website

import io.gatling.core.Predef.{Simulation, _}
import io.gatling.http.Predef._

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps
class EnzymeEndPoint extends Simulation  {

  val httpProtocol = http
    //.baseUrl("https://computer-database.gatling.io")
    .baseUrl("https://www.computingfacts.com/")
    .inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-GB,en-US;q=0.9,en;q=0.8")
    .upgradeInsecureRequestsHeader("1")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

  //val csvFeeder = csv("data/computerCsvFile.csv").circular


  val scn = scenario("EnzymeEndPoint")
    //.feed(csvFeeder)
    .exec(http("LoadHomePage")
      //.get("/computers"))
    .get("/").check(status.is(200)))
    .pause(5)
    .exec(http("PostPage")
      .get("/posts").check(status.is(200)))
    .exec(http("tutorials - Spring ").get("/tutorials/Spring").check(status.is(200)))
    .exec(http("tutorials Java ").get("/tutorials/Core-Java").check(status.is(200)))
    .exec(http("User").get("/@joe").check(status.is(200)))
    //.get("/post/Dependency-Injection-in-Spring"))
      //.get("/computers/new"))
//    .pause(5)
//    .exec(http("CreateNewComputer")
//      .post("/computers")
//      .formParam("name", "${computerName}")
//      .formParam("introduced", "${introduced}")
//      .formParam("discontinued", "${discontinued}")
//      .formParam("company", "${companyId}"))
//    .pause(5)
//    .exec(http("FilterComputer")
//      .get("/computers?f=GatlingComputer")
//      .check(regex("""computers\/([0-9]{3,5})""").exists.saveAs("topComputerInList")))
//    .exec(http("GetSingleComputer")
//      .get("/computers/${topComputerInList}"))
//    .exec(http("DeleteComputer")
//      .post("/computers/${topComputerInList}/delete"))

  setUp(
    scn.inject(
      nothingFor(5 seconds),
      atOnceUsers(10),
      rampUsersPerSec(500) to(2000) during(10 seconds)
    ).protocols(httpProtocol)
  ).assertions(global.responseTime.max.lt(100000))//Assert that the max response time of all requests is less than 100 ms
    .assertions(global.responseTime.percentile(75).lte(90))
    .assertions(forAll.failedRequests.percent.lte(80)) // Assert that every request has no more than 5% of failing requests
    .assertions(global.failedRequests.count.lte(80))

}

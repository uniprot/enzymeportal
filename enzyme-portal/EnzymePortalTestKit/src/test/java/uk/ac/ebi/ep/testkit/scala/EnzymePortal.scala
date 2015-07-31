/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.ep.testkit.scala

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class EnzymePortal extends Simulation {

  val httpConf = http //.baseURLs("http://ves-pg-94:8080/enzymeportal", "http://ves-pg-95:8080/enzymeportal", "http://ves-oy-94:8080/enzymeportal", "http://ves-oy-95:8080/enzymeportal")
    //val httpConf = http.baseURLs("http://wwwdev.ebi.ac.uk/enzymeportal","http://ves-hx-95:8080/enzymeportal")
    //.baseURL("http://localhost:8080/enzymeportal")
    //.baseURL("http://ves-pg-95:8080/enzymeportal")
    .baseURL("http://wwwdev.ebi.ac.uk/enzymeportal")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")
//    .disableWarmUp
//    .maxConnectionsPerHostLikeFirefoxOld
//    .maxConnectionsPerHostLikeFirefox
//    .maxConnectionsPerHostLikeOperaOld
//    .maxConnectionsPerHostLikeOpera
//    .maxConnectionsPerHostLikeSafariOld
//    .maxConnectionsPerHostLikeSafari
//    .maxConnectionsPerHostLikeIE7
//    .maxConnectionsPerHostLikeIE8
//    .maxConnectionsPerHostLikeIE10
//    .maxConnectionsPerHostLikeChrome
//    .maxConnectionsPerHost(1000)

  val headers_10 = Map("Content-Type" -> """application/x-www-form-urlencoded""") // Note the headers specific to a given request

  //http.warmUp("http://wwwdev.ebi.ac.uk/enzymeportal")
  // disable warm up
//  http.disableWarmUp
//
//  http.maxConnectionsPerHostLikeFirefoxOld
//  http.maxConnectionsPerHostLikeFirefox
//  http.maxConnectionsPerHostLikeOperaOld
//  http.maxConnectionsPerHostLikeOpera
//  http.maxConnectionsPerHostLikeSafariOld
//  http.maxConnectionsPerHostLikeSafari
//  http.maxConnectionsPerHostLikeIE7
//  http.maxConnectionsPerHostLikeIE8
//  http.maxConnectionsPerHostLikeIE10
//  http.maxConnectionsPerHostLikeChrome

  val homePageSenario = scenario("HomePage Scenario").exec(http("home_request_1").get("/")).pause(1)

  val browseDiseaseSenario = scenario("Browse Disease Scenario").exec(http("browse_disease").get("/browse/disease")).pause(1)

  val browsePathwaysSenario = scenario("Browse Pathways Scenario").exec(http("browse_pathways").get("/browse/pathways")).pause(1)

  val browseTaxonomySenario = scenario("Browse Taxonomy Scenario").exec(http("browse_taxonomy").get("/browse/taxonomy")).pause(1)

  val searchSenario = scenario("Search Scenario").exec(http("search").get("/search?searchparams.type=KEYWORD&searchparams.previoustext=&searchparams.start=0&searchparams.text=sildenafil")).pause(1)
  //setUp(homePageSenario.inject(atOnceUsers(6)).protocols(httpConf), browseDiseaseSenario.inject(atOnceUsers(6)).protocols(httpConf),browsePathwaysSenario.inject(atOnceUsers(6)).protocols(httpConf))

 


  //setUp(scn.inject(atOnceUsers(1)).protocols(httpConf))
  //setUp(senario.inject(atOnceUsers(6)).protocols(httpConf), senario1.inject(atOnceUsers(6)).protocols(httpConf))
  setUp(homePageSenario.inject(atOnceUsers(1)).protocols(httpConf))
  //setUp(browseDiseaseSenario.inject(atOnceUsers(500)).protocols(httpConf))
   //setUp(searchSenario.inject(atOnceUsers(99)).protocols(httpConf))
  //setUp(homePageSenario.inject(atOnceUsers(90)).protocols(httpConf), browseDiseaseSenario.inject(atOnceUsers(90)).protocols(httpConf))
  //setUp(homePageSenario.inject(atOnceUsers(50)).protocols(httpConf), browseDiseaseSenario.inject(atOnceUsers(50)).protocols(httpConf), browsePathwaysSenario.inject(atOnceUsers(50)).protocols(httpConf), browseTaxonomySenario.inject(atOnceUsers(50)).protocols(httpConf))

}

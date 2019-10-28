<%--
    Document   : enzymePage
    Created on : Feb 21, 2017, 3:05:34 PM
    Author     : Joseph <joseph@ebi.ac.uk>
--%>


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars" %>
--%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="Fn" uri="/WEB-INF/epTagLibray.tld" %>
<%@taglib prefix="ep" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
    <head>

        <%@include file="head.jspf" %>
        <title>Enzyme Portal Enzyme Page</title>
    </head>
    <body>



        <div id="skip-to">
            <ul>
                <li><a href="#content">Skip to main content</a></li>
                <li><a href="#local-nav">Skip to local navigation</a></li>
                <li><a href="#global-nav">Skip to EBI global navigation menu</a></li>
                <li><a href="#global-nav-expanded">Skip to expanded EBI global navigation menu (includes all sub-sections)</a>
                </li>
            </ul>
        </div>
        <!--        <script>
                    var speciesAutocompleteDataSource = [];
                    var compoundsAutoCompleteDataSource = [];
                    var diseaseAutoCompleteDataSource = [];
                    var ecAutoCompleteDataSource = [];
                </script>-->

        <div id="wrapper" class="container_24">
            <%@include file="header.jspf" %>

            <div id="content" role="main" class="clearfix" >

                <div class="row" id="title-row">
                    <div class="large-2 columns">
                        <img src="/enzymeportal/resources/images/enzyme_page_logo.jpg">
                    </div>

                    <section class="large-10 columns">
                        <h1>
                            Enzyme
                            <c:if test="${link eq true}">
                                <a href="ec/${enzymePage.ec}" class="icon icon-generic" data-icon="L" style="font-size: medium;" title="permalink to this enzyme"></a>        
                            </c:if>

                        </h1>
                        <h2>${enzymePage.ec} - ${enzymePage.enzymeName}</h2>
                    </section>

                </div>

                <div class="row" id="title-row">
                    <div class="large-2 columns"></div>
                    <div class="large-10 columns">
                        <c:choose>
                            <c:when test="${not empty enzymePage.altNames}">
                                <h5>Alternative Name(s)</h5>
                            </c:when>
                            <c:otherwise>
                                <h5 class="noResults">Alternative Name(s)</h5>
                            </c:otherwise>
                        </c:choose>

                        <div id="alternative-names">
                            <c:choose>
                                <c:when test="${not empty enzymePage.altNames}">
                                    <ul>
                                        <c:forEach items="${enzymePage.altNames}" var="altName">
                                            <li>${altName}</li>
                                            </c:forEach>
                                    </ul>
                                </c:when>
                                <c:otherwise>
                                    <p class="noResults">There are no alternative names for this Enzyme</p>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>

                <section class="row enzymeContent">
                    <div class="large-2 columns"></div>
                    <div class="large-10 columns">

                        <c:choose>
                            <c:when test="${not empty enzymePage.catalyticActivities}">
                                <h3>Catalytic Activity</h3>
                                <p>${enzymePage.catalyticActivities}</p>
                            </c:when>
                            <c:otherwise>
                                <h3 class="noResults">Catalytic Activity</h3>
                                <p class="noResults">No Catalytic Activity was found for this Enzyme</p>
                            </c:otherwise>
                        </c:choose>      



                        <section>
                            <c:choose>
                                <c:when test="${not empty enzymePage.cofactors }">
                                    <h3>Cofactors</h3>
                                    <c:forEach items="${enzymePage.cofactors}" var="c">
                                        <p>${c}</p>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <h3 class="noResults">Cofactors</h3>
                                    <p class="noResults"> There are no Cofactors for this Enzyme</p>
                                </c:otherwise>
                            </c:choose>

                        </section>
                        <section>
                            <h3>Kinetics Parameters </h3>
                            <c:choose>
                                <c:when test="${not empty brendaList }">
                                    <c:set var="count" value="0" scope="page" />
                                    <div class="tableFixHead tableFix2">
                                        <table id="kinetics">
                                            <thead>
                                            <tr>
                                                <th>kcat/KM Value [1/mMs-1]</th>
                                                <th>Substrate</th>
                                                <th>Organism</th>
                                                <!--  <th>ph Range</th>
                                                  <th>Temp Range</th>-->
                                                <th>Comment</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <c:forEach items="${brendaList}" var="b" varStatus="brendaCounter">
                                                <c:if test="${brendaCounter.count <= 50}">
                                                    <tr>
                                                        <td>${b.kcatKmValue}</td>
                                                        <td>${b.substrate}</td>
                                                        <td>${b.organism}</td>
                                                        <td>${b.comment}</td>
                                                    </tr>
                                                </c:if>
                                            </c:forEach>


                                            <tr>

                                                <td colspan="3">
                                                    <c:if test="${fn:length(brendaList) > 50}">
                                                        <button id="full-view" class="full-view icon icon-functional btn" data-icon="F" type="submit"> View all ${fn:length(brendaList)} in Brenda</button>
                                                    </c:if>
                                                </td>

                                            </tr>

                                            </tbody>
                                        </table>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    There are no Functional Parameters information for this Enzyme
                                </c:otherwise>
                            </c:choose>
                        </section>                         
                        <section>
                            <h3>Kinetics - Temperature </h3>
                            <c:choose>
                                <c:when test="${not empty tempList }">
                                    <c:set var="count" value="0" scope="page" />
                                    <div class="tableFixHead">
                                        <table>
                                             <thead>
                                            <tr>
                                                <th>Organism</th>
                                                <th>TemperatureRange</th>
                                                <th>temperatureRangeMaximum</th>
                                                <th>Comment</th>
                                            </tr>
                                             </thead>
                                             <tbody>
                                            <c:forEach items="${tempList}" var="b" varStatus="brendaCounter">
                                                <c:if test="${brendaCounter.count <= 50}">
                                                    <tr>
                                                        <td>${b.organism}</td>
                                                        <td>${b.temperatureRange}</td>
                                                        <td>${b.temperatureRangeMaximum}</td>
                                                        <td>${b.comment}</td>
                                                    </tr>
                                                </c:if>
                                            </c:forEach>


                                            <tr>

                                                <td colspan="3">
                                                    <c:if test="${fn:length(tempList) > 50}">
                                                        <button id="full-view" class="full-view icon icon-functional btn" data-icon="F" type="submit"> View all ${fn:length(tempList)} in Brenda</button>
                                                    </c:if>
                                                </td>

                                            </tr>

                                             </tbody>
                                        </table>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    There are no Functional Parameters information for this Enzyme
                                </c:otherwise>
                            </c:choose>
                        </section> 
                        <section>
                            <h3>Kinetics Ph </h3>
                            <c:choose>
                                <c:when test="${not empty phList }">
                                    <c:set var="count" value="0" scope="page" />
                                    <div class="tableFixHead">
                                        <table>
                                             <thead>
                                            <tr>
                                                <th>Organism</th>
                                                <th>PhRange</th>
                                                <th>PhRangeMaximum</th>
                                                <th>Comment</th>
                                            </tr>
                                             </thead>
                                             <tbody>
                                            <c:forEach items="${phList}" var="b" varStatus="brendaCounter">
                                                <c:if test="${brendaCounter.count <= 50}">
                                                    <tr>
                                                        <td>${b.organism}</td>
                                                        <td>${b.phRange}</td>
                                                        <td>${b.phRangeMaximum}</td>
                                                        <td>${b.comment}</td>
                                                    </tr>
                                                </c:if>
                                            </c:forEach>


                                            <tr>

                                                <td colspan="3">
                                                    <c:if test="${fn:length(phList) > 50}">
                                                        <button id="full-view" class="full-view icon icon-functional btn" data-icon="F" type="submit"> View all ${fn:length(phList)} in Brenda</button>
                                                    </c:if>
                                                </td>

                                            </tr>

                                             </tbody>
                                        </table>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    There are no Functional Parameters information for this Enzyme
                                </c:otherwise>
                            </c:choose>
                        </section>               

                        <section>
                            <h3>Associated Proteins </h3>
                            <c:choose>
                                <c:when test="${not empty enzymePage.associatedProteins }">
                                    <c:set var="count" value="0" scope="page" />



                                    <form id="proteinViewForm-${enzymePage.ec}" action="${pageContext.request.contextPath}/search" modelAttribute="searchModel" method="POST">
                                        <input type="hidden" name="keywordType" value="${keywordType}">
                                        <input type="hidden" id="searchId" name="searchId" value="${searchId}"/>
                                        <input type="hidden" id="searchKey" name="searchKey" value="${searchKey}"/>


                                        <input name="ec" type="hidden" value="${enzymePage.ec}">
                                        <%--
                                        <form:hidden path="searchparams.previoustext" />
                                        <form:hidden path="searchparams.text" value="${enzymePage.ec}-${enzymePage.ec}" />
                                        <form:hidden path="searchparams.type" value="${keywordType}"/>
                                        --%>
                                        <input name="searchparams.previoustext" type="hidden" value="">
                                        <input name="searchparams.text" type="hidden" value="${enzymePage.ec}-${enzymePage.ec}">
                                        <input name="searchparams.type" type="hidden" value="${keywordType}">


                                        <table id="associatedProteins">
                                            <tr>
                                                <th>Protein name</th>
                                                <th>Organism</th>
                                            </tr>
                                            <%--
                                               <c:forEach items="${enzymePage.proteins.entries}" var="p" varStatus="proteinsCounter">
                                            --%>
                                            <c:forEach items="${enzymePage.associatedProteins}" var="p" varStatus="proteinsCounter">
                                                <c:if test="${proteinsCounter.count <= 5}">
                                                    <tr>
                                                        <td><a href="${pageContext.request.contextPath}/search/${p.primaryAccession}/enzyme">${p.proteinName}</td>
                                                        <td>${p.primaryOrganism}</td>
                                                    </tr>
                                                </c:if>
                                            </c:forEach>


                                            <tr>

                                                <td colspan="3">
                                                    <c:if test="${enzymePage.numProteins > 5}">
                                                        <button id="full-view" class="full-view icon icon-functional btn" data-icon="F" type="submit"> View all ${enzymePage.numProteins} Proteins</button>
                                                    </c:if>
                                                </td>

                                            </tr>


                                        </table>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    There are no Associated Proteins for this Enzyme
                                </c:otherwise>
                            </c:choose>
                        </section>

                        <section>

                            <c:choose>
                                <c:when test="${ reactionMechanism.count == 0}">
                                    <h3 class="noResults">Reaction Mechanism</h3>
                                    <p class="noResults"> There are no Reaction Mechanism for this Enzyme</p>     
                                </c:when>
                                <c:otherwise>
                                    <c:set var="result" value="${reactionMechanism.results[0]}"/>

                                    <c:if test="${reactionMechanism.count == 1}">
                                        <h3>Reaction Mechanism</h3> 
                                        <ul class="tabs" data-active-collapse="true"  data-tabs id="mechanism-tabs-ec">
                                            <%@ include file="mcsa.jsp"%>
                                            <div style="padding-left: 2em;">
                                                <a   href="https://www.ebi.ac.uk/thornton-srv/m-csa/search/?s=${enzymePage.ec}" target="_blank">
                                                    <button style="margin-bottom: 1em;" id="all-proteins" class="full-view icon icon-functional btn" data-icon="F" type="submit"> View Reaction Mechanisms in M-CSA</button>
                                                </a>
                                            </div>
                                        </ul>
                                    </c:if>
                                    <c:if test="${reactionMechanism.count > 1}">
                                        <h3>Reaction Mechanisms</h3> 
                                        <ul class="tabs" data-active-collapse="true"  data-tabs id="mechanism-tabs-ec">
                                            <%@ include file="mcsa.jsp"%>
                                            <div style="padding-left: 2em;">
                                                <a   href="https://www.ebi.ac.uk/thornton-srv/m-csa/search/?s=${enzymePage.ec}" target="_blank">
                                                    <button style="margin-bottom: 1em;" id="all-proteins" class="full-view icon icon-functional btn" data-icon="F" type="submit"> View All ${reactionMechanism.count} Reaction Mechanisms in M-CSA</button>
                                                </a>
                                            </div>
                                        </ul>
                                    </c:if>

                                </c:otherwise>
                            </c:choose> 




                        </section> 
                        <%--    
                            <section>

                            <c:if test="${empty results}">
                                <h3 class="noResults">Reaction Mechanism</h3>
                                <p class="noResults"> There are no Reaction Mechanism for this Enzyme</p>
                            </c:if>
                            <c:if test="${not empty results}">
                                <h3>Reaction Mechanism</h3> 
                                <c:choose>
                                    <c:when test="${fn:length(results) > 1}">
                                        <ul class="tabs" data-active-collapse="true"  data-tabs id="mechanism-tabs-ec">

                                            <li class="tabs-title is-active">
                                                <a href="#ec${firstResult.mcsaId}" aria-selected="true">M-CSA ID:${firstResult.mcsaId}</a>
                                            </li>        


                                            <c:forEach var="r" items="${results}">

                                                <li class="tabs-title ">
                                                    <a data-tabs-target="ec${r.mcsaId}" href="#ec${r.mcsaId}">M-CSA ID:${r.mcsaId}</a>
                                                </li> 

                                            </c:forEach>
                                        </ul>        
                                    </c:when>
                                    <c:otherwise>

                                        <ul class="tabs" data-active-collapse="true"  data-tabs id="mechanism-tabs-ec">

                                            <c:forEach var="r" items="${results}">

                                                <li class="tabs-title is-active">
                                                    <a data-tabs-target="ec${r.mcsaId}" href="#ec${r.mcsaId}">M-CSA ID:${r.mcsaId}</a>
                                                </li> 

                                            </c:forEach>
                                        </ul>           
                                    </c:otherwise>
                                </c:choose>



                                <div class="tabs-content" data-tabs-content="mechanism-tabs-ec">
                                    <c:choose>
                                        <c:when test="${fn:length(results) > 1}">
                                            <c:if test="${not empty firstResult}">
                                                <div class="tabs-panel is-active" id="ec${firstResult.mcsaId}">
                                 
                                                    <c:set var="result" value="${firstResult}"/>
                                                    <%@ include file="mcsa.jsp"%> 
                                                </div> 

                                            </c:if>
                                            <c:if test="${not empty results}">
                                                <c:forEach var="result" items="${results}">
                                                    <div class="tabs-panel" id="ec${result.mcsaId}">

                                                        <%@ include file="mcsa.jsp"%> 
                                                    </div> 
                                                </c:forEach>    
                                            </c:if>        
                                        </c:when> 
                                        <c:otherwise>
                                            <c:if test="${not empty results}">
                                                <c:forEach var="result" items="${results}">
                                                    <div class="tabs-panel is-active" id="ec${result.mcsaId}">

                                                        <%@ include file="mcsa.jsp"%> 
                                                    </div> 
                                                </c:forEach>    
                                            </c:if>      
                                        </c:otherwise>
                                    </c:choose>

                                    <div style="padding-left: 2em;">
                                        <a   href="https://www.ebi.ac.uk/thornton-srv/m-csa/search/?s=${enzymePage.ec}" target="_blank">
                                            <button style="margin-bottom: 1em;" id="all-proteins" class="full-view icon icon-functional btn" data-icon="F" type="submit"> View All Reaction Mechanisms in M-CSA</button>
                                        </a>
                                    </div>
                                </div>  
                            </c:if>



                        </section> 

                        --%>
                        <section>

                            <c:choose>
                                <c:when test="${not empty enzymePage.citations }">
                                    <h3>Citations</h3>
                                    <ul id="citations">
                                        <c:forEach items="${enzymePage.citations}" var="citation">

                                            <li>

                                                <a href="https://europepmc.org/abstract/${citation.source}/${citation.id}"
                                                   title="View ${citation.source} ${citation.id} in Europe PubMed Central"
                                                   target="_blank" class="extLink ${citation.source}">${citation.title}</a>
                                            </li>

                                        </c:forEach>
                                    </ul>
                                    <c:if test="${enzymePage.numCitations > 10}">
                                        <a href="https://europepmc.org/search?page=1&query=${enzymePage.enzymeName}" target="_blank">
                                            <button id="all-proteins" class="full-view icon icon-functional btn" data-icon="F" type="submit"> View more in Europe PMC</button>
                                        </a>        
                                    </c:if>

                                </c:when>
                                <c:otherwise>
                                    <h3 class="noResults">Citations</h3>
                                    <p class="noResults">There are no citations for this Enzyme</p>
                                </c:otherwise>
                            </c:choose>

                        </section>

                    </div>

                </section>

            </div>

            <%@include file="footer.jspf" %>
        </div>






    </body>
</html>

<%-- 
    Document   : enzymes
    Created on : May 6, 2016, 11:39:04 AM
    Author     : Joseph <joseph@ebi.ac.uk>
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="Fn" uri="/WEB-INF/epTagLibray.tld" %>
<%@taglib prefix="ep" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
    <c:set var="pageTitle" value="Enzymes"/>
    <%@include file="head.jspf" %>


</head>

<body class="level2"><!-- add any of your classes or IDs -->
<div id="skip-to">
    <ul>
        <li><a href="#content">Skip to main content</a></li>
        <li><a href="#local-nav">Skip to local navigation</a></li>
        <li><a href="#global-nav">Skip to EBI global navigation menu</a></li>
        <li><a href="#global-nav-expanded">Skip to expanded EBI global navigation menu (includes all sub-sections)</a>
        </li>
    </ul>
</div>

<div id="wrapper" class="container_24">
    <%@include file="header.jspf" %>

    <div id="content" role="main" class="grid_24 clearfix" >

        <h1>Enzyme Results</h1>


        <section class="grid_6 alpha" id="search-filters">

            <div class="filter">
                <div class="title">
                    Search Filters
                </div>
                <div class="line"></div>
                <form:form id="filtersForm" name="filtersForm" modelAttribute="searchModel" action="${pageContext.request.contextPath}/enzymes/filter" method="POST">

                    <form:hidden path="searchparams.type" />
                    <form:hidden path="searchparams.text" />
                    <form:hidden path="searchparams.sequence" />
                    <form:hidden path="searchparams.previoustext" />
                    <input type="hidden" id="filtersFormStart" name="searchparams.start" value="0"/>
                    <input id="pageNumber" type="hidden" name="pageNumber" value="0"/>
                    <input type="hidden" name="ec" value="${ec}"/>
                    <input type="hidden" name="ecname" value="${ecname}"/>
                    <%@ include file="filter-species.jspf"%>
                    <br/>
                    <%@ include file="filter-compounds.jspf"%>
                    <br/>
                    <%@ include file="filter-diseases.jspf"%>
                    <br/>
                    <%@ include file="filter-family.jsp"%>

                </form:form>
            </div>
            <%--filter --%>

        </section>




        <section class="grid_18 alpha" id="search-results">





         <c:if test="${not empty enzymes}">
            <div>

                   <table id="enzymeResults" cellpadding="60px">
                       <tr>
                           <th>Name</th>
                           <th>Hits</th>
                           <th>Enzyme Family</th>
                           <th>EC</th>
                           <th>Catalytic Activity</th>
                           <th>Species</th>
                       </tr>

                       <c:forEach var="enzyme" items="${enzymes}">
                           <tr class="enzymeRow">
                               <td class="enzymeName sideTwizzle">${enzyme.enzymeName}</td>
                               <td>${fn:length(enzyme.dummyProteinSet)}</td>
                               <td>Enzyme Family Name here</td>
                               <td>${enzyme.ecNumber}</td>
                               <td>${enzyme.catalyticActivity}</td>
                               <td>Human, cow, Gazelle etc</td>

                           </tr>
                           <tr id="proteinList" style="display: none">
                               <td colspan="6">
                                <table id="enzymeResultsProteins">
                                    <tr>
                                        <th> </th>
                                        <th>Associated Proteins:</th>
                                    </tr>
                                    <c:forEach var="protein" items="${enzyme.dummyProteinSet}">
                                    <tr class="proteinRow">
                                        <td> </td>
                                        <td width="50%">

                                            <a href="${pageContext.request.contextPath}/search/${protein.accession}/enzyme">
                                            [Pyruvate dehydrogenase (acetyl-transferring)] kinase isozyme 2, mitochondrial</a>
                                        </td>
                                        <td>

                                            <c:set var="proteinSpeciesSize" value="${fn:length(protein.speciesSet)}"/>
                                            <c:set var="proteinSpecies" value="${protein.speciesSet}"/>
                                            <c:set var="speciesMaxDisplay" value="${5}"/>

                                            ${proteinSpeciesSize} species found<br />

                                            <c:if test="${proteinSpeciesSize gt 0}">


                                                <c:if test="${proteinSpeciesSize <= speciesMaxDisplay}">


                                                  <c:forEach var="sp" items="${proteinSpecies}" varStatus="bingo">
                                                    ${sp.commonName},



                                                  </c:forEach>

                                                </c:if>

                                                <c:if test="${proteinSpeciesSize > speciesMaxDisplay}">



                                                    <c:forEach var="i" begin="0" end="${speciesMaxDisplay-1}">
                                                        thing2
                                                          <%--${proteinSpecies[i]}--%>
                                                          <%--${proteinSpecies[i].commonName},--%>
                                                          <%--${proteinSpecies[i].commonName}--%>


                                                    </c:forEach>

                                                    **** More link***

                                                </c:if>


                                            </c:if>


                                        </td>

                                        <%--<td>
                                        <c:set var="relSpeciesMaxDisplay" value="${5}"/>
                                         <c:set var="relspecies" value="${protein.speciesSet}"/>
                                         <c:set var="relSpeciesSize" value="${fn:length(protein.speciesSet)}"/>
                                         <c:if test="${relSpeciesSize gt 0}">
                                             <b>Species:</b>
                                             <c:if test="${relSpeciesSize <= relSpeciesMaxDisplay}">
                                                 <c:set var="relSpeciesMaxDisplay" value="${relSpeciesSize}"/>
                                             </c:if>
                                             <c:forEach var="i" begin="0" end="${relSpeciesMaxDisplay-1}">
                                                 <c:choose>
                                                     <c:when test="${empty relspecies[i].species.commonname}">

                                                         [<a class="popup" href='search/${relspecies[i].uniprotaccessions[0]}/enzyme'>${relspecies[i].species.scientificname}<span>${relspecies[i].species.scientificname}</span></a>
                                                         <c:if test="${relspecies[i].scoring eq true}">
                                                             <span class="score S${fn:substringBefore(relspecies[i].identity/10 , '.') }" title="Score = ${relspecies[i].score}"> ${relspecies[i].identity}&#37;</span>
                                                         </c:if>]

                                                     </c:when>
                                                     <c:otherwise>

                                                         [<a class="popup" href='search/${relspecies[i].uniprotaccessions[0]}/enzyme'>${relspecies[i].species.commonname}<span>${relspecies[i].species.scientificname}</span></a>
                                                         <c:if test="${ relspecies[i].scoring eq true}">
                                                             <span class="score S${fn:substringBefore(relspecies[i].identity/10 , '.') }" title="Score = ${relspecies[i].score}"> ${relspecies[i].identity}&#37;</span>
                                                         </c:if>]

                                                     </c:otherwise>
                                                 </c:choose>
                                             </c:forEach>


                                             <c:if test="${relSpeciesSize > relSpeciesMaxDisplay}">
                                                 <span id="relSpecies_${resultItemId}" style="display: none">
                                                     <c:forEach var = "i" begin="${relSpeciesMaxDisplay}" end="${relSpeciesSize-1}">
                                                         <c:choose>
                                                             <c:when test="${empty relspecies[i].species.commonname}">
                                                                 [<a class="popup" href='search/${relspecies[i].uniprotaccessions[0]}/enzyme'>${relspecies[i].species.scientificname}<span>${relspecies[i].species.scientificname}</span></a>
                                                                 <c:if test="${relspecies[i].scoring eq true}">
                                                                     <span class="score S${fn:substringBefore(relspecies[i].identity/10 , '.') }" title="Score = ${relspecies[i].score}"> ${relspecies[i].identity}&#37;</span>
                                                                 </c:if>]
                                                             </c:when>
                                                             <c:otherwise>
                                                                 [<a class="popup" href='search/${relspecies[i].uniprotaccessions[0]}/enzyme'>${relspecies[i].species.commonname}<span>${relspecies[i].species.scientificname}</span></a>
                                                                 <c:if test="${relspecies[i].scoring eq true}">
                                                                     <span class="score S${fn:substringBefore(relspecies[i].identity/10 , '.') }" title="Score = ${relspecies[i].score}"> ${relspecies[i].identity}&#37;</span>
                                                                 </c:if>]
                                                             </c:otherwise>
                                                         </c:choose>
                                                     </c:forEach>
                                                 </span>
                                                 <a class="showLink" id="<c:out value='relSpecies_link_${resultItemId}'/>">Show more species</a>
                                             </c:if>
                                         </c:if>


                                        </td>

                                           --%>










                                    </tr>
                                    </c:forEach>
                                    <tr class="proteinRow">
                                        <td></td>
                                        <td><a id="full-view" href="#" class="icon icon-functional btn" data-icon="F">Full View</a></td>
                                    </tr>
                                </table>
                                </td>

                           </tr>
                       </c:forEach>

                   </table>



                </ul>

            </div>

        </c:if>

        </section>
    </div>



    <%@include file="footer.jspf" %>
</div>
<!--! end of #wrapper -->
</body>
</html>

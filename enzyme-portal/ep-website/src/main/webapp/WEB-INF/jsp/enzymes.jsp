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

    <script src="${pageContext.request.contextPath}/resources/javascript/jquery.easy-autocomplete.min.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/easy-autocomplete.min.css" type="text/css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/easy-autocomplete.themes.min.css" type="text/css" />




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
<script>
    var speciesAutocompleteDataSource = [];
    var compoundsAutoCompleteDataSource = [];
    var diseaseAutoCompleteDataSource = [];
    var ecAutoCompleteDataSource = [];
</script>

<div id="wrapper" class="container_24">
    <%@include file="header.jspf" %>

    <div id="content" role="main" class="grid_24 clearfix" >

        <section class="grid_18 alpha">
            <h1>Enzyme Results</h1>
            <h2>${ebiResult.hitCount} Enzymes found for "${searchKey}"</h2>
        </section>

        <form id="facetFilterForm" action="${pageContext.request.contextPath}/enzymes" method="POST">


            <section class="grid_6 alpha">

                <input id="paginationPage" name="servicePage" type="hidden" value="">
                <c:if test="${page.totalElements gt page.size}">
                    <nav class="paginationContainer">
                          <ul class="pagination">
                            <c:if test="${currentIndex >= 2}">
                                <li><a class="paginationLink" id="${currentIndex - 1}" href="#" aria-label="Previous"><span>&laquo;</span></a></li>
                            </c:if>
                            <li class="paginationText">Page ${page.number + 1} of ${page.totalPages}</li>
                            <c:if test="${currentIndex < page.totalPages}">
                                <li><a class="paginationLink" id="${currentIndex + 1}" href="#" aria-label="Next"><span>&raquo;</span></a></li>
                            </c:if>
                          </ul>
                    </nav>
                </c:if>

            </section>

            <section class="grid_24">

                <section class="grid_6 alpha" id="search-filters">

                    <div class="filter">

                        <input type="hidden" id="searchKey" name="searchKey" value="${searchKey}"></input>
                        <c:forEach var="facet" items="${enzymeFacet}">
                             <div>

                                 <input type="hidden" id="filtersApplied" value="${filtersApplied}"></input>
                                 <c:if test="${facet.id eq 'enzyme_family'}">
                                 <div class="sublevel1">
                                     <div class="subTitle">Enzyme Family</div>
                                     <ul>
                                         <c:forEach var="v" items="${facet.facetValues}">
                                             <li><input id="enzyme_family_${v.value}" name="filterFacet" value="enzyme_family:${v.value}" type="checkbox" onChange="this.form.submit()"> ${v.label} (${v.count})</li>
                                         </c:forEach>
                                     </ul>
                                 </div>
                                 </c:if>

                                <c:if test="${facet.id eq 'TAXONOMY'}">
                                     <div class="sublevel1">
                                        <div class="subTitle">Organism</div>
                                        <ul>
                                            <li>
                                                <input id="organismsSearch" />
                                                <script>
                                                    var options = {
                                                        data:[
                                                            <c:forEach var="org" items="${facet.facetValues}">
                                                            {"value": "${org.label}", "taxId": "TAXONOMY:${org.value}"},
                                                            </c:forEach>
                                                              ],
                                                        placeholder: "Search for Organism",
                                                        getValue: "value",
                                                        list: {
                                                                match: {
                                                                    enabled: true
                                                                },
                                                                onClickEvent: function() {
                                                                    var value = $("#organismsSearch").getSelectedItemData().taxId;
                                                                    $("#auto-complete-holder").val(value).trigger("change");
                                                                    $("#facetFilterForm").submit();
                                                                }
                                                            }
                                                    };
                                                    $("#organismsSearch").easyAutocomplete(options);
                                                </script>

                                                <div class="filterContent">
                                                     <c:set var="speciesList" value="${facet.facetValues}"/>
                                                     <c:set var="speciesListSize" value="${fn:length(facet.facetValues)}"/>
                                                     <c:set var="speciesParams" value="${searchModel.searchparams.species}"/>
                                                </div>
                                                <input id="auto-complete-holder" name="filterFacet" type="hidden" />

                                            </li>

                                            <c:set var="facetSize" value="${fn:length(facet.facetValues)}"/>
                                            <c:choose>
                                                 <c:when test='${facetSize > 10}'>
                                                     <c:forEach begin="0" end="9" var="v" items="${facet.facetValues}">
                                                        <li><input id="TAXONOMY_${v.value}" name="filterFacet" value="TAXONOMY:${v.value}" type="checkbox" onChange="this.form.submit()"> ${v.label} (${v.count})</li>
                                                     </c:forEach>
                                                     <li class="toggleOrganismList">${facetSize - 10} more...</li>
                                                         <div class="organismFullList">
                                                             <c:forEach begin="10" var="vMore" items="${facet.facetValues}">
                                                                <li><input id="TAXONOMY_${vMore.value}" name="filterFacet" value="TAXONOMY:${vMore.value}" type="checkbox" onChange="this.form.submit()"> ${vMore.label} (${vMore.count})</li>
                                                             </c:forEach>
                                                         </div>
                                                 </c:when>
                                                 <c:otherwise>
                                                     <c:forEach var="v" items="${facet.facetValues}">
                                                        <li><input id="TAXONOMY_${v.value}" name="filterFacet" value="TAXONOMY:${v.value}" type="checkbox" onChange="this.form.submit()"> ${v.label} (${v.count})</li>
                                                     </c:forEach>
                                                 </c:otherwise>
                                             </c:choose>
                                         </ul>
                                     </div>
                                 </c:if>

                             </div>
                         </c:forEach>
                    </div>

                </section>

        </form>


            <section class="grid_18 alpha" id="search-results">

                 <c:if test="${not empty enzymeView}">
                    <div>

                       <table id="enzymeResults" cellpadding="60" cellspacing="60">
                           <tr>
                               <th width="20%">Name</th>
                               <th width="5%">Hits</th>
                               <th width="15%">Enzyme Family</th>
                               <th width="10%">EC</th>
                               <th width="30%">Catalytic Activity</th>
                               <th width="20%">Species</th>
                           </tr>

                           <c:forEach var="enzyme" items="${enzymeView}">
                               <tr class="enzymeRow">
                                   <c:choose>
                                        <c:when test='${enzyme.numEnzymeHits > 0}'>
                                            <td class="enzymeName sideTwizzle">${enzyme.enzymeName}</td>
                                        </c:when>
                                       <c:otherwise>
                                           <td>${enzyme.enzymeName}</td>
                                       </c:otherwise>
                                   </c:choose>
                                   <td>${enzyme.numEnzymeHits}</td>
                                   <td>${enzyme.enzymeFamily}</td>
                                   <td>${enzyme.ec}</td>
                                   <td>${enzyme.catalyticActivities}</td>
                                   <td>
                                       <c:set var="enzymeSpeciesSize" value="${fn:length(enzyme.species)}"/>
                                       <c:set var="enzymeSpecies" value="${enzyme.species}"/>
                                       <c:set var="enzymeSpeciesMaxDisplay" value="${5}"/>

                                       <c:if test="${enzymeSpeciesSize gt 0}">
                                           <c:if test="${enzymeSpeciesSize <= enzymeSpeciesMaxDisplay}">
                                               <ul>
                                                   <c:forEach var="i" begin="0" end="${enzymeSpeciesSize-1}">
                                                     <li>${enzyme.species[i]}</li>
                                                   </c:forEach>
                                               </ul>
                                           </c:if>
                                           <c:if test="${enzymeSpeciesSize > enzymeSpeciesMaxDisplay}">
                                               <ul>
                                                   <c:forEach var="i" begin="0" end="${enzymeSpeciesMaxDisplay-1}">
                                                     <li>${enzyme.species[i]}</li>
                                                   </c:forEach>
                                                    <li>${enzymeSpeciesSize-5} others</li>
                                               </ul>

                                           </c:if>
                                       </c:if>
                                   </td>
                               </tr>
                               <tr id="proteinList" style="display: none">
                                   <td colspan="6">


                                       <form:form id="proteinViewForm-${enzyme.ec}" action="${pageContext.request.contextPath}/search" modelAttribute="searchModel" method="POST">

                                       <input name="searchTerm" type="hidden" value="${searchKey}">
                                        <input name="ec" type="hidden" value="${enzyme.ec}">
                                       <form:hidden path="searchparams.previoustext" />
                                       <form:hidden path="searchparams.text" value="${enzyme.ec}-${searchKey}" />
                                       <form:hidden path="searchparams.type" value="KEYWORD"/>


                                    <table id="enzymeResultsProteins">
                                        <tr>
                                            <th width="3%"> </th>
                                            <th width="40%">Associated Proteins:</th>
                                            <th width="57%"> </th>
                                        </tr>
                                        <c:set var="proteinSize" value="${fn:length(enzyme.protein)}"/>


                                            <c:forEach var="p" begin="0" end="4">
                                                <tr class="proteinRow">
                                                    <td> </td>
                                                    <td width="50%"><a href="${pageContext.request.contextPath}/search/${enzyme.protein[p].accession}/enzyme">${enzyme.protein[p].proteinName}</a></td>
                                                    <td>${enzyme.protein[p].species}</td>
                                                </tr>
                                            </c:forEach>
                                            <c:if test="${proteinSize >= 5}">
                                            <tr class="proteinRow">

                                                <td> </td>
                                                <td><button id="full-view" class="full-view icon icon-functional btn" data-icon="F" type="submit">View all ${enzyme.numEnzymeHits} Proteins</button></td>
                                                <td> </td>

                                            </tr>
                                            </c:if>

                                    </table>
                                       </form:form>
                                    </td>
                               </tr>
                           </c:forEach>

                       </table>

                    </div>

                </c:if>

            </section>
        </section>
    </div>



    <%@include file="footer.jspf" %>
</div>
<!--! end of #wrapper -->
</body>
</html>

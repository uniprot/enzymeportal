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

<body class="level2 full-width"><!-- add any of your classes or IDs -->
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

<div id="wrapper" class="">
    <%@include file="header.jspf" %>

    <div id="content" role="main" class="clearfix" >

<!-- Feedback widget code  -->

        <%--<img id="trigger" src="${pageContext.request.contextPath}/resources/images/feedback.png" />--%>
        <%--<div id='slider'>--%>
            <%--<iframe src="https://docs.google.com/forms/d/e/1FAIpQLScvxggSYUe_6rmsLmJ-wBEm8BLz0SBH_RmHD3Bvp7DiN4G4JA/viewform?embedded=true" width="300" height="100%" frameborder="0" marginheight="0" marginwidth="0">Loading...</iframe>--%>
        <%--</div>--%>
        <%--<script>--%>
            <%--$('#slider').slideReveal({--%>
              <%--trigger: $("#trigger"),--%>
                <%--position: "right",--%>
                <%--width: "300"--%>
            <%--});--%>
        <%--</script>--%>

<!-- END of Feedback widget code  -->

        <div class="row" id="title-row">
            <section class="large-2 columns">
                <img src="/enzymeportal/resources/images/protein_page_logo.jpg">
            </section>

            <section class="large-10 columns">
                <h2>Protein</h2>
                <h4>${ebiResult.hitCount} enzymatic activities found for "${Fn:splitHyphen(searchKey)}"</h4>
            </section>
        </div>

            <section class="pagination-container">

                <c:if test="${page.totalElements gt page.size}">
                    <nav class="paginationContainer">
                          <ul class="pagination" role="navigation" aria-label="Pagination">

                            <c:if test="${currentIndex >= 2}">
                                <li class="pagination-previous"><a class="paginationLink" id="${currentIndex - 1}" href="#" aria-label="Page ${page.number}"></a></li>
                            </c:if>

                            <li>Page ${page.number + 1} of ${page.totalPages}</li>

                            <c:if test="${currentIndex < page.totalPages}">
                                <li class="pagination-next"><a class="paginationLink" id="${currentIndex + 1}" href="#" aria-label="Next"></a></li>
                            </c:if>

                          </ul>
                    </nav>
                </c:if>
            </section>

            <div class="row">

                <section id="search-filters" class="large-3 columns">

                    <form id="facetFilterForm" action="${pageContext.request.contextPath}/enzymes" method="POST">
                        <input type="hidden" id="paginationPage" name="servicePage" value="">
                        <input type="hidden" name="keywordType" value="${keywordType}">
                        <input type="hidden" id="searchId" name="searchId" value="${searchId}"/>
                        <div class="filter">
                            <input type="hidden" id="searchKey" name="searchKey" value="${searchKey}"></input>
                            <div id="activeFilters" class="sublevel1">
                                <div class="subTitle">Active filters</div>
                                <ul>
                                    <li id="activeOrganisms"></li>
                                </ul>
                            </div>

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

                                                <div id="organismList">
                                                <c:set var="facetSize" value="${fn:length(facet.facetValues)}"/>
                                                   <c:forEach var="v" items="${facet.facetValues}">
                                                      <li><input id="TAXONOMY_${v.value}" name="filterFacet" value="TAXONOMY:${v.value}" type="checkbox" onChange="this.form.submit()"> ${v.label} (${v.count})</li>
                                                   </c:forEach>
                                                </div>
                                             </ul>
                                         </div>
                                     </c:if>
                                 </div>
                             </c:forEach>
                        </div>
                    </form>


                </section>

                <section class="large-9 columns" id="search-results">
                    <c:if test="${not empty enzymeView}">
                        <div>
                           <table id="enzymeResults" cellpadding="60" cellspacing="60">
                               <tr>
                                   <th width="20%">Name</th>
                                   <th>Alternative Names</th>
                                   <th width="5%">Protein Hits</th>
                                   <th width="15%">Enzyme Family</th>
                                   <th width="10%">EC</th>
                                   <th width="30%">Catalytic Activity</th>
                                   <th width="20%">Cofactor</th>
                               </tr>
                               <c:forEach var="enzyme" items="${enzymeView}" varStatus="theIndex">
                                   <tr class="enzymeRow">
                                       <c:choose>
                                            <c:when test='${enzyme.numProteins > 0}'>
                                                <td data-toggle="proteinList${theIndex.index}" class="enzymeName sideTwizzle">${enzyme.enzymeName}</td>
                                            </c:when>
                                           <c:otherwise>
                                               <td>${enzyme.enzymeName}</td>
                                           </c:otherwise>
                                       </c:choose>
                                       <td>
                                           <div class="alternative-names">
                                           <ul>
                                                <c:forEach var="altname" items="${enzyme.altNames}">
                                                      <li>${altname}</li>
                                                </c:forEach>
                                           </ul>
                                           </div>
                                       </td>
                                       <td>${enzyme.numProteins}</td>
                                       <td>${enzyme.enzymeFamily}</td>
                                       <td><a href="search/ec/${enzyme.ec}?enzymeName=${enzyme.enzymeName}">${enzyme.ec}</a></td>
                                       <td>${enzyme.catalyticActivities}</td>
                                       <td>
                                          <ul>
                                               <c:forEach var="cofactor" items="${enzyme.intenzCofactors}">
                                                     <li>${cofactor}</li>
                                               </c:forEach>
                                          </ul>
                                       </td>
                                   </tr>
                                   <tr id="proteinList${theIndex.index}" data-toggler data-animate="hinge-in-from-top hinge-out-from-top" style="display: none">
                                       <td colspan="7">
                                           <form:form id="proteinViewForm-${enzyme.ec}" action="${pageContext.request.contextPath}/search" modelAttribute="searchModel" method="POST">
                                               <input name="keywordType" type="hidden" value="${keywordType}">
                                               <input name="searchTerm" type="hidden" value="${searchKey}">
                                               <input name="searchId" type="hidden" value="${searchId}">
                                               <input name="ec" type="hidden" value="${enzyme.ec}">
                                               <form:hidden path="searchparams.previoustext" />
                                               <form:hidden path="searchparams.text" value="${searchKey}-${enzyme.ec}" />
                                               <form:hidden path="searchparams.type" value="${searchType}"/>
                                               <h4>Associated Proteins:</h4>
                                               <ul id="enzymeResultsProteins">
                                                    <c:set var="proteinSize" value="${enzyme.numProteins}"/>
                                                    <c:if test="${proteinSize >= 5}">
                                                       <c:set var="proteinSize" value="5"/>
                                                    </c:if>
                                                    <c:forEach var="p" begin="0" end="${proteinSize -1}">
                                                        <li><a href="${pageContext.request.contextPath}/search/${enzyme.proteinGroupEntry[p].primaryAccession}/enzyme">${enzyme.proteinGroupEntry[p].proteinName} </a> - (${enzyme.proteinGroupEntry[p].primaryOrganism})</li>
                                                    </c:forEach>
                                                    <c:if test="${enzyme.numProteins >= 5}">
                                                          <button id="full-view" class="full-view icon icon-functional btn" data-icon="F" type="submit"> View all ${enzyme.numProteins} Proteins</button>
                                                    </c:if>
                                               </ul>
                                           </form:form>
                                        </td>
                                   </tr>
                               </c:forEach>
                           </table>
                        </div>
                    </c:if>
                </section>
            </div>

        <section>
            <c:if test="${page.totalElements gt page.size}">
                <nav class="paginationContainer">
                      <ul class="pagination">
                        <c:if test="${currentIndex >= 2}">
                            <li><a class="paginationLink" id="${currentIndex - 1}" aria-label="Previous"><span>&laquo;</span></a></li>
                        </c:if>
                        <li class="paginationText">Page ${page.number + 1} of ${page.totalPages}</li>
                        <c:if test="${currentIndex < page.totalPages}">
                            <li><a class="paginationLink" id="${currentIndex + 1}" aria-label="Next"><span>&raquo;</span></a></li>
                        </c:if>
                      </ul>
                </nav>
            </c:if>
        </section>
    </div>

    <%@include file="footer.jspf" %>
</div>
<!--! end of #wrapper -->
</body>
</html>

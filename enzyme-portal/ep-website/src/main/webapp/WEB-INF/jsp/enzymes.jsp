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
        <section class="grid_6 alpha">
            <c:if test="${page.totalElements gt page.size}">
        <nav class="paginationContainer">
          <ul class="pagination">
           

                
        <c:choose>
         <c:when test="${currentIndex == 1}">
             <li>
             <a href="#" aria-label="Previous">
                <span aria-hidden="true">&laquo;</span>
              </a>
            </li>
         </c:when>
         <c:otherwise>
             <li>
             <a href="javascript:void(0);" aria-label="Previous">
                <span>&laquo;</span>
              </a>
             </li>
              <input id="prevPage" type="hidden"value="${currentIndex - 1}">
         </c:otherwise>
     </c:choose>             

                
       
            
               Page ${page.number + 1} of ${page.totalPages} 
          
           

    <c:choose>
        <c:when test="${currentIndex == page.totalPages}">
              <li>
            <a href="#" aria-label="Next">
                <span aria-hidden="true">&raquo;</span>
              </a>
            </li>

    </c:when>
    <c:otherwise>
       <li>
      <a id="nextButton" href="javascript:void(0);" aria-label="Next">
                <span aria-hidden="true">&raquo;</span>
      </a>   
       </li>
        <input id="nextPage" type="hidden"value="${currentIndex + 1}">
        </c:otherwise>
    </c:choose>     
     
          </ul>
        </nav>
        </c:if>
        </section>

        <section class="grid_24">

        <section class="grid_6 alpha" id="search-filters">

            <div class="filter">

                    <form id="facetFilterForm" action="${pageContext.request.contextPath}/search/filter?searchKey=${searchKey}" method="POST">
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



                                 <c:if test="${facet.id eq 'compound_type'}">
                                 <div class="sublevel1">
                                     <div class="subTitle">Compound Type</div>
                                     <ul>
                                         <c:forEach var="v" items="${facet.facetValues}">
                                             <li><input id="compound_type_${v.value}" name="filterFacet" value="compound_type:${v.value}" type="checkbox" onChange="this.form.submit()"> ${v.label} (${v.count})</li>
                                         </c:forEach>
                                     </ul>
                                 </div>
                                 </c:if>



                                 <c:if test="${facet.id eq 'compound_name'}">
                                 <div class="sublevel1">
                                     <div class="subTitle">Compounds</div>
                                     <ul>
                                         <c:forEach var="v" items="${facet.facetValues}">
                                             <li><input id="compound_name_${Fn:replaceSpacesWithUnderScore(v.value)}" name="filterFacet" value="compound_name:${v.value}" type="checkbox" onChange="this.form.submit()"> ${v.label} (${v.count})</li>
                                         </c:forEach>
                                     </ul>
                                 </div>
                                 </c:if>



                                  <c:if test="${facet.id eq 'disease_name'}">
                                 <div class="sublevel1">
                                      <div class="subTitle">Diseases</div>
                                     <ul>
                                         <c:forEach var="v" items="${facet.facetValues}">
                                             <li><input id="disease_name_${Fn:replaceSpacesWithUnderScore(v.value)}" name="filterFacet" value="disease_name:${v.value}" type="checkbox" onChange="this.form.submit()"> ${v.label} (${v.count})</li>
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
                                                	//url: "resources/colors.js"

                                                                  ${facet.facetValues}


                                                };

                                                console.log("options: ",options);

                                                $("#organismsSearch").easyAutocomplete(options);





                                            </script>


                                            <%----%>
                                        <%--<div class="ui-widget grid_12zzz">--%>
                                            <%--<input id="specieAT" itemtype="text" name="speciefilter" autocomplete="off" class="filterSearchBox" placeholder="Enter Species to filter" />--%>
                                            <%--<input id="_ctempList_selected" name="_ctempList_selected" type="hidden" value=""/>--%>
                                        <%--</div>--%>
                                            <%----%>
                                            <%----%>


                                            <div class="filterContent">
                                                 <c:set var="speciesList" value="${facet.facetValues}"/>
                                                 <c:set var="speciesListSize" value="${fn:length(facet.facetValues)}"/>
                                                 <c:set var="speciesParams" value="${searchModel.searchparams.species}"/>





                                                <%--${speciesParams}--%>

                                                 <%--<div id="species_filters_y"--%>
                                                     <%--style="${empty searchModel.searchparams.species?--%>
                                                     <%--'border-bottom: none' : 'border-bottom: thin solid #ddd' }"></div>--%>
                                                 <%--<div id="species_filters_n"></div>--%>

                                                 <%--<script>--%>
                                               			<%--$(document).ready(function () {--%>
                                               				<%--// (See search.js if in doubt)--%>
                                                             <%--// code copied from  filter-species.jspf--%>

                                     <%--//                            <input id="specieAT" itemtype="text" name="speciefilter" autocomplete="off" class="filterSearchBox" placeholder="Enter Species to filter" />--%>
                                     <%--//                            <input id="_ctempList_selected" name="_ctempList_selected" type="hidden" value=""/>--%>

                                               				<%--// Initialise variables:--%>
                                               				<%--var group = 'species';--%>
                                               				<%--checkedFilters[group] = [];--%>
                                               				<%--uncheckedFilters[group] = [];--%>
                                               				<%--displayedFilters[group] = 0;--%>

                                               				<%--// Populate js variables with data from server:--%>
                                               				<%--<c:forEach var="specie" items="${facet.facetValues}">--%>
                                               					<%--var sp = { "id": "${specie.label}", "name": "${specie.label}","taxId":"${specie.value}" };--%>
                                               					<%--<c:choose>--%>
                                               						<%--<c:when test="${Fn:contains(speciesParams, specie.label)}">--%>
                                               							<%--checkedFilters[group][checkedFilters[group].length] = sp;--%>
                                               						<%--</c:when>--%>
                                               						<%--<c:otherwise>--%>
                                               							<%--uncheckedFilters[group][uncheckedFilters[group].length] = sp;--%>
                                               						<%--</c:otherwise>--%>
                                               					<%--</c:choose>--%>

                                                            <%--&lt;%&ndash;&ndash;%&gt;--%>
                                                            <%--&lt;%&ndash;speciesAutocompleteDataSource.push({&ndash;%&gt;--%>
                                                                    <%--&lt;%&ndash;label: "${not empty specie.commonname? specie.commonname : specie.scientificname}",&ndash;%&gt;--%>
                                                       			    <%--&lt;%&ndash;value: "${specie.scientificname}"});&ndash;%&gt;--%>

                                               					<%--speciesAutocompleteDataSource.push({--%>
                                               						<%--label: "${specie.label}",--%>
                                               						<%--value: "${specie.label}"--%>
                                                                <%--});--%>
                                               				<%--</c:forEach>--%>

                                                            <%--console.log("speciesAutocompleteDataSource: ",speciesAutocompleteDataSource);--%>

                                               				<%--// Display filters:--%>
                                               				<%--&lt;%&ndash;for (var i = 0; i < checkedFilters[group].length; i++){&ndash;%&gt;--%>
                                               					<%--&lt;%&ndash;addCheckbox(group, checkedFilters[group][i], true);&ndash;%&gt;--%>
                                               				<%--&lt;%&ndash;}&ndash;%&gt;--%>
                                               				<%--&lt;%&ndash;for (var i = 0; displayedFilters[group] < ${filterSizeDefault}&ndash;%&gt;--%>
                                               				<%--&lt;%&ndash;&& displayedFilters[group] < ${speciesListSize}; i++){&ndash;%&gt;--%>
                                               					<%--&lt;%&ndash;addCheckbox(group, uncheckedFilters[group][i], false);&ndash;%&gt;--%>
                                               				<%--&lt;%&ndash;}&ndash;%&gt;--%>
                                               			<%--});--%>
                                               		<%--</script>--%>

                                                     </div>




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
                    </form>

            </div>
            <%--filter --%>

        </section>



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
                                <table id="enzymeResultsProteins">
                                    <tr>
                                        <th width="3%"> </th>
                                        <th width="40%">Associated Proteins:</th>
                                        <th width="57%"> </th>
                                    </tr>
                                    <%--<c:forEach var="protein" begin="0" end="4" items="${enzyme.proteins}">--%>
                                    <c:set var="proteinSize" value="${fn:length(enzyme.protein)}"/>

                                    <c:forEach var="p" begin="0" end="4">
                                        <tr class="proteinRow">
                                            <td> </td>
                                            <td width="50%"><a href="${pageContext.request.contextPath}/search/${enzyme.protein[p].accession}/enzyme">${enzyme.protein[p].proteinName}</a></td>
                                            <td>${enzyme.protein[p].species}</td>
                                        </tr>
                                    </c:forEach>
                                    <tr class="proteinRow">
                                        <td> </td>
                                        <td><a id="full-view" href="#" class="icon icon-functional btn" data-icon="F">Full View</a></td>
                                        <td> </td>
                                    </tr>
                                </table>
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

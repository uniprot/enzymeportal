<%-- 
    Document   : search
    Created on : Mar 31, 2011, 7:57:06 PM
    Author     : hongcao
--%>
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib  prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
    <head>
        <title>Enzyme Portal</title>
        <link rel="stylesheet"  href="http://www.ebi.ac.uk/inc/css/contents.css"     type="text/css" />
        <link media="screen" href="resources/lib/spineconcept/css/960gs-fluid/grid.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="resources/lib/spineconcept/css/common.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="resources/lib/spineconcept/css/identification.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="resources/lib/spineconcept/css/species.css" type="text/css" rel="stylesheet" />
        <script src="resources/lib/spineconcept/javascript/jquery-1.5.1.min.js" type="text/javascript"></script>
        <script src="resources/lib/spineconcept/javascript/identification.js" type="text/javascript"></script>
        <link href="resources/css/search.css" type="text/css" rel="stylesheet" />
        <script src="resources/javascript/search.js" type="text/javascript"></script>
    </head>
    <body>
    <jsp:include page="header.jsp"/>
    <div class="contents">
        <div class="page container_12">            
            <form:form id="searchForm" modelAttribute="searchModel" action="search" method="POST">
            <jsp:include page="subHeader.jsp"/>
            <jsp:include page="searchBox.jsp"/>
            <!--Global variables-->
            <c:set var="showButton" value="Show more"/>
            <c:set var="searchText" value="${searchModel.searchparams.text}"/>
            <c:set var="startRecord" value="${searchModel.searchparams.start}"/>
            <c:set var="searchresults" value="${searchModel.searchresults}"/>
             <c:set var="searchFilter" value="${searchresults.searchfilters}"/>
            <c:set var="summaryentries" value="${searchresults.summaryentries}"/>
            <c:set var="summaryentriesSize" value="${fn:length(summaryentries)}"/>
            <c:set var="totalfound" value="${searchresults.totalfound}"/>
            <c:set var="filterSizeDefault" value="${10}"/>
            <div class="grid_12 content">
                <c:if test="${summaryentries!=null && searchresults.totalfound>0}">
                <div class="filter">                    
                    <div class="title">
                        Search Filters
                    </div>
                    <div class="line"></div>
                    <div class="sublevel1">
                        <div class="subTitle">
                            Species
                        </div>
                        <div class="filterContent">
                            <c:set var="speciesList" value="${searchFilter.species}"/>
                            <c:set var="speciesListSize" value="${fn:length(speciesList)}"/>
                            <c:set var="limitedDisplay" value="${filterSizeDefault}"/>
                            <c:if test="${speciesListSize > 0}">
                                <c:if test="${speciesListSize <= filterSizeDefault}">
                                    <c:set var="limitedDisplay" value="${speciesListSize}"/>
                                </c:if>
                                <c:forEach var="i" begin="0" end="${limitedDisplay-1}">
                                    <c:set var="speciesSciName" value="${speciesList[i].scientificname}"/>
                                    <c:set var="speciesName" value="${speciesList[i].commonname}"/>
                                    <c:if test='${empty speciesName}'>
                                        <c:set var="speciesName" value="${speciesSciName}"/>
                                    </c:if>
                                    <c:if test='${not empty speciesSciName}'>
                                    <div class="filterLine">
                                    <div class="text">
                                    <span>
                                        <c:out value="${speciesName}"/>
                                    </span>
                                    </div>
                                    <div class="checkItem">
                                        <form:checkbox path="searchparams.species" value="${speciesSciName}"/>
                                     </div>
                                    <div class="clear"></div>
                                    </div>
                                    </c:if>
                                </c:forEach>
                            </c:if>
                            <c:if test="${speciesListSize > filterSizeDefault}">
                                <div id="species_0" style="display: none">
                                <c:forEach var="i" begin="${filterSizeDefault}" end="${speciesListSize-1}">
                                    <c:set var="speciesSciName" value="${speciesList[i].scientificname}"/>
                                    <c:set var="speciesName" value="${speciesList[i].commonname}"/>
                                    <c:if test='${speciesName ==""}'>
                                        <c:set var="speciesName" value="${speciesSciName}"/>
                                    </c:if>
                                    <c:if test='${speciesSciName != ""}'>
                                    <div class="filterLine">
                                    <div class="text">
                                    <span>
                                        <c:out value="${speciesName}"/>
                                    </span>
                                    </div>
                                    <div class="checkItem">
                                        <form:checkbox path="searchparams.species" value="${speciesSciName}"/>
                                     </div>
                                    <div class="clear"></div>
                                    </div>
                                    </c:if>
                                </c:forEach>
                                <c:set var="speciesMoreSize" value="${speciesListSize-filterSizeDefault}"/>
                            </div>
                                <a class="showLink" id="<c:out value='species_link_0'/>"><c:out value="See ${speciesMoreSize} more"/></a> <br/>
                            </c:if>
                        </div>
                    </div>
                    <div class="sublevel1">
                        <div class="subTitle">
                            Chemical Compounds
                        </div>
                        <div class="filterContent">
                            <c:set var="compoundList" value="${searchFilter.compounds}"/>
                            <c:set var="compoundListSize" value="${fn:length(compoundList)}"/>
                            <c:set var="limitedDisplay" value="${filterSizeDefault}"/>
                            <c:if test="${compoundListSize > 0}">
                                <c:if test="${compoundListSize <= filterSizeDefault}">
                                    <c:set var="limitedDisplay" value="${compoundListSize}"/>
                                </c:if>
                            <c:forEach var="i" begin="0" end="${limitedDisplay-1}">
                                <div class="filterLine">
                                <div class="text">
                                    <xchars:translate>
                                        <c:out value="${compoundList[i].name}" escapeXml="false"/>
                                    </xchars:translate>
                                </div>
                                <div class="checkItem">
                                    <form:checkbox path="searchparams.compounds" value="${compoundList[i].id}"/>
                                 </div>
                                <div class="clear"></div>
                                </div>
                            </c:forEach>
                            </c:if>
                            <c:if test="${compoundListSize > filterSizeDefault}">
                             <div id="compound_0" style="display: none">
                                <c:forEach var="i" begin="${filterSizeDefault}" end="${compoundListSize-1}">
                                    <div class="filterLine">
                                    <div class="text">
                                        <xchars:translate>
                                            <c:out value="${compoundList[i].name}" escapeXml="false"/>
                                        </xchars:translate>
                                    </div>
                                    <div class="checkItem">
                                        <form:checkbox path="searchparams.compounds" value="${compoundList[i].id}"/>
                                     </div>
                                    <div class="clear"></div>
                                    </div>
                                </c:forEach>
                              </div>
                             <c:set var="compoundMoreSize" value="${compoundListSize-filterSizeDefault}"/>
                             <a class="showLink" id="<c:out value='compound_link_0'/>"><c:out value="See ${compoundMoreSize} more"/></a> <br/>
                            </c:if>
                        </div>
                    </div>
                    <div class="floatRight">
                        <input id ="filterButton" type="submit" value="Filter Selected" class="filterButton"/>                        
                    </div>
                </div>
                </c:if>
                <div id="keywordSearchResult" class="result">
                    <c:if test="${totalfound==0}">
                        <spring:message code="label.search.empty"/>
                    </c:if>
                    <c:if test="${summaryentries!=null && searchresults.totalfound>0}">
                        <form:form modelAttribute="pagination">
                        <c:set var="totalPages" value="${pagination.totalPages}"/>
                        <c:set var="maxPages" value="${totalPages}"/>
                        <div class="resultText">
                            About <c:out value="${totalfound}"/> results found for <c:out value="${searchText}"/>,
                            displaying <c:out value="${startRecord+1}"/> - <c:out value="${startRecord+summaryentriesSize}"/>

                        </div>
                        <div id="pagination">                                    
                                <c:if test="${totalPages>pagination.maxDisplayedPages}">
                                    <c:set var="maxPages" value="${pagination.maxDisplayedPages}"/>
                                    <c:set var="showNextButton" value="${true}"/>
                                </c:if>
                                <input id ="prevStart" type="hidden" value="${startRecord-summaryentriesSize}">
                                <c:if test="${pagination.currentPage==1}">
                                    <c:set var="prevDisplay" value="none"/>
                                </c:if>
                                <a id="prevButton" href="javascript:void(0);" style="display:${prevDisplay}">
                                    Previous
                                </a>
                                Page <c:out value="${pagination.currentPage}"/> of <c:out value="${totalPages}"/>

                                <c:if test="${showNextButton==true}">
                                    <input id ="nextStart" type="hidden" value="${startRecord+summaryentriesSize}">                                    
                                    <a id="nextButton" href="javascript:void(0);">
                                        Next
                                    </a>
                                </c:if>                         
                        </div>
                        </form:form>
                    <div class="clear"></div>
                        <div class="line"></div>
                        <div class="resultContent">
                            <c:set var="resultItemId" value="${0}"/>
                            <c:forEach items="${summaryentries}" var="enzyme" varStatus="vsEnzymes">
                            <c:set var="uniprotId" value="${enzyme.uniprotid}"/>                            
                             <c:set var="primAcc" value="${enzyme.uniprotaccessions[0]}"/>
                            <div class="resultItem">
                            
                                <div class="proteinImg">
                                    <c:set var="imgFile" value='${enzyme.pdbeaccession[0]}'/>
                                   	<c:set var="imgFooter" value=""/>
                                    <c:if test="${empty imgFile}">
                                    	<c:forEach var="relSp" items="${enzyme.relatedspecies}">
                                    	<c:if test="${empty imgFile and not empty relSp.pdbCodes}">
                                    		<c:set var="imgFile" value="${relSp.pdbCodes[0]}"/>
                                    		<c:set var="imgFooter">
	                                    		<spring:message code="label.entry.proteinStructure.other.species"/>
                                    			${empty relSp.species.commonname?
                                    				relSp.species.scientificname : relSp.species.commonname}
                                   			</c:set>
                                    	</c:if>
                                    	</c:forEach>
                                    </c:if>
                                    <c:choose>
                                    	<c:when test="${empty imgFile}">
                                    		<div style="position: absolute; width: 110px; height: 90px;
                                    			background-color: #eee; opacity: 0.6; vertical-align: middle;
                                    			margin-top: 0px; padding: 0px;">No structure available</div>
                                    		<img src="resources/images/noStructure-light.png"
                                    			width="110" height="90"
                                            	alt="No structure available"
                                            	title="No structure available"/>
                                    	</c:when>
                                    	<c:otherwise>
                                        	<c:set var="imgLink"
                                        		value="http://www.ebi.ac.uk/pdbe-srv/view/images/entry/${imgFile}_cbc600.png"/>
	                                        <a target="blank" href="${imgLink}">
	                                            <img src="${imgLink}" width="110" height="90"
	                                            	alt="PDB ${imgFile}"/>
	                                        </a>
	                                        <div class="imgFooter">${imgFooter}</div>
                                    	</c:otherwise>
                                    </c:choose>
                                    <c:if test='${imgFile != "" && imgFile != null}'>
                                    </c:if>
                                </div>
                                
                                <div class="desc">
                                    <a href="search/${primAcc}/enzyme">
                                        <c:set var="showName" value="${fn:substring(enzyme.name, 0, 100)}"/>
                                        <c:out value="${showName}"/>
                                       <!-- [<c:out value="${enzyme.uniprotid}"/>]-->
                                       [${empty enzyme.species.commonname?
                                       		enzyme.species.scientificname :
                                       		enzyme.species.commonname}]
                                    </a>

                                    <c:set var="function" value="${enzyme.function}"/>
                                    <c:if test='${function != null && function != ""}'>
                                        <div>
                                            <b>Function</b>:
                                        <c:out value="${enzyme.function}"/>.<br/>
                                        </div>
                                    </c:if>
                                    <c:set var="synonym" value="${enzyme.synonym}"/>
                                    <c:set var="synonymSize" value="${fn:length(synonym)}"/>
                                    <c:set var="synLimitedDisplayDefault" value="${5}"/>
                                    <c:set var="synLimitedDisplay" value="${synLimitedDisplayDefault}"/>
                                    <c:if test='${synonymSize>0}'>
                                        <div id ="synonym">
                                            <b>Synonyms</b>:
                                        <c:if test="${synonymSize > 0 && synonymSize <= synLimitedDisplay}">
                                            <c:set var="synLimitedDisplay" value="${synonymSize}"/>
                                        </c:if>

                                        <c:set var="hiddenSyns" value=""/>                                        
                                        <c:forEach var="i" begin="0" end="${synLimitedDisplay-1}">
                                            <c:out value="${synonym[i]}"/>;
                                        </c:forEach>                                        
                                        <c:if test="${synonymSize>synLimitedDisplay}">
                                        <span id='syn_${resultItemId}' style="display: none">
                                            <c:forEach var="i" begin="${synLimitedDisplay}" end="${synonymSize-1}">
                                                <c:out value="${synonym[i]}"/>;
                                            </c:forEach>
                                            </span>
                                            <a class="showLink" id="<c:out value='syn_link_${resultItemId}'/>">Show more synonyms</a>
                                        </c:if>
                                        </div>
                                    </c:if>

                                <div>
                                    <!-- div id="in">in</div -->
                                    <div>
                                    	<b>Species:</b>
                                    	<%--
                                        <a href="search/${primAcc}/enzyme">
	                                        [${empty enzyme.species.commonname?
	                                       		enzyme.species.scientificname :
	                                       		enzyme.species.commonname}]
                                       		<!-- ${enzyme.pdbeaccession} -->
                                        </a>
                                    	 --%>
                                        <!--display = 3 = 2 related species + 1 default species -->
                                        <c:set var="relSpeciesMaxDisplay" value="${5}"/>
                                        <c:set var="relspecies" value="${enzyme.relatedspecies}"/>                                        
                                        <c:set var="relSpeciesSize" value="${fn:length(relspecies)}"/>
                                        <c:if test="${relSpeciesSize gt 0}">
                                        <c:if test="${relSpeciesSize <= relSpeciesMaxDisplay}">
                                            <c:set var="relSpeciesMaxDisplay" value="${relSpeciesSize}"/>
                                        </c:if>
                                        <c:forEach var="i" begin="0" end="${relSpeciesMaxDisplay-1}">
                                            <!-- c:if test="${relspecies[i].species.scientificname ne enzyme.species.scientificname}" -->
                                            <a href="search/${relspecies[i].uniprotaccessions[0]}/enzyme">
                                                [${empty relspecies[i].species.commonname?
		                                       		relspecies[i].species.scientificname :
		                                       		relspecies[i].species.commonname}]
		                                       	<!-- ${relspecies[i].pdbCodes} -->
                                            </a>
                                            <!-- /c:if -->                                             
                                        </c:forEach>
                                        <c:if test="${relSpeciesSize > relSpeciesMaxDisplay}">
                                            <span id="relSpecies_${resultItemId}" style="display: none">
                                            <c:forEach var = "i" begin="${relSpeciesMaxDisplay}" end="${relSpeciesSize-1}">                                                
                                                <a href="search/${relspecies[i].uniprotaccessions[0]}/enzyme">
	                                                [${empty relspecies[i].species.commonname?
			                                       		relspecies[i].species.scientificname :
			                                       		relspecies[i].species.commonname}]
		                                       		<!-- ${relspecies[i].pdbCodes} -->
                                                </a>
                                            </c:forEach>
                                         	</span>
                                         	<a class="showLink" id="<c:out value='relSpecies_link_${resultItemId}'/>">Show more species</a>
                                         </c:if>
                                       </c:if>
                                </div>
                            </div>




                                </div>
                            </div>
                            <div class="clear"></div>
                            <c:set var="resultItemId" value="${resultItemId+1}"/>
                            </c:forEach>
                        </div>
                    </c:if>

                </form:form>
            </div>
        </div>
        </div>
    <jsp:include page="footer.jsp"/>
    </div>    
    </body>
</html>
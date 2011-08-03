<%-- 
    Document   : search
    Created on : Mar 31, 2011, 7:57:06 PM
    Author     : hongcao
--%>
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars"%>

<html>
    <head>
        <title>Enzyme Portal</title>        
        <link media="screen" href="resources/lib/spineconcept/css/960gs/reset.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="resources/lib/spineconcept/css/960gs/text.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="resources/lib/spineconcept/css/960gs/960.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="resources/lib/spineconcept/css/common.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="resources/lib/spineconcept/css/identification.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="resources/lib/spineconcept/css/species.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="resources/lib/spineconcept/javascript/jquery-ui/css/custom-theme/jquery-ui-1.8.11.custom.css" type="text/css" rel="stylesheet" />
        <link href="resources/css/search.css" type="text/css" rel="stylesheet" />
        <script src="resources/lib/spineconcept/javascript/jquery-1.5.1.min.js" type="text/javascript"></script>
        <script src="resources/lib/spineconcept/javascript/jquery-ui/js/jquery-ui-1.8.11.custom.min.js" type="text/javascript"></script>
        <script src="resources/lib/spineconcept/javascript/identification.js" type="text/javascript"></script>
        <script src="resources/javascript/search.js" type="text/javascript"></script>
        <!--
       <link media="screen" href="resources/spineconcept/css/common.css" type="text/css" rel="stylesheet" />
        <link href="resources/lib/layout-default-latest.css" type="text/css" rel="stylesheet" />
        <link href="resources/spineconcept/css/epHome.css" type="text/css" rel="stylesheet" />
        <script type="text/javascript" src="resources/lib/jquery-latest.js"></script>
        <script type="text/javascript" src="resources/lib/jquery-ui-latest.js"></script>
        <script type="text/javascript" src="resources/lib/jquery.layout-latest.js"></script>
	<script type="text/javascript" src="resources/lib/js/debug.js"></script>

-->
        <!--
    <link rel="stylesheet" type="text/css" href="resources/lib/extjs4.0.0/ext-all.css" />
    <script type="text/javascript" src="resources/lib/extjs4.0.0/bootstrap.js"></script>
    <script type="text/javascript" src="resources/lib/extjs4.0.0/border.js"></script>
-->
    </head>
    <body>        
        <div class="page container_12">
            <div  class="grid_12">
                <!--
<div class="headerdiv" id="headerdiv" style="height: 60px;">
    <iframe src="http://www.ebi.ac.uk/inc/homepage_head.html" name="head" id="head" marginwidth="0" marginheight="0" style="height: 125px;" frameborder="0" scrolling="no" width="100%">
</iframe> </div>
-->
            </div>
            <div class="clear"></div>

            <div class="grid_12">
                <div class="breadcrumbs" id="breadcrumbs">
                    <ul>
                        <li class="first"><a href="">EBI</a></li>
                        <li><a href="">Databases</a></li>
                        <li><a href="">Enzymes</a></li>
                        <li><a href="">Search Results</a></li>
                    </ul>
                </div>
                <div class="basket">
                    <input id ="compareButton" type="button" value="Compare & Download (0)" />
                </div>
            </div>
            <div class="clear"></div>
            <form:form id="searchForm" modelAttribute="searchModel" action="showResults" method="get">
            <div class="grid_12">
                <div  id="keywordSearch" class="search">
                    <p>
                        <form:input path="searchparams.text" cssClass="field"/>
                        <form:hidden path="searchparams.start" />
                        <form:hidden path="searchparams.previoustext" />
                        <input type="submit" value="Search" class="button" />
                    </p>
                 </div>
            </div>
            <!--Global variables-->
            <c:set var="showButton" value="Show more"/>
            <c:set var="searchresults" value="${searchModel.searchresults}"/>
             <c:set var="searchFilter" value="${searchresults.searchfilters}"/>
            <c:set var="summaryentries" value="${searchresults.summaryentries}"/>
            <c:set var="totalfound" value="${searchresults.totalfound}"/>
            <c:set var="filterSizeDefault" value="${5}"/>
            <div class="grid_12 content">
                <c:if test="${summaryentries!=null && searchresults.totalfound>0}">
                <div class="filter">                    
                    <div class="title">
                        Search Filters
                    </div>
                    <div class="line"></div>

                    <div class="sublevel1">
                        <div class="subTitle">
                            Chemical Compounds
                        </div>
                        <div class="filterContent">
                            <c:set var="compoundList" value="${searchFilter.compounds}"/>
                            <c:set var="compoundListSize" value="${fn:length(compoundList)}"/>
                            <c:set var="limitedDisplay" value="${filterSizeDefault}"/>

                            <c:if test="${compoundListSize > 0 && compoundListSize <= filterSizeDefault}">
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
                            <c:if test="${compoundListSize > filterSizeDefault}">                          
                             <div id="compound_0" style="display: none">
                                <c:forEach var="i" begin="${filterSizeDefault}" end="${compoundListSize-1}">
                                    <div class="filterLine">
                                    <div class="text">
                                        <xchars:translate>
                                            <c:out value="${compoundList[i].id}" escapeXml="false"/>
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
                             <a class="link" id="<c:out value='compound_link_0'/>"><c:out value="See ${compoundMoreSize} more"/></a> <br/>
                            </c:if>
                        </div>
                    </div>

                    <div class="sublevel1">
                        <div class="subTitle">
                            Species
                        </div>
                        <div class="filterContent">
                            <c:set var="speciesList" value="${searchFilter.species}"/>
                            <c:set var="speciesListSize" value="${fn:length(speciesList)}"/>
                            <c:set var="limitedDisplay" value="${filterSizeDefault}"/>
                            <c:if test="${speciesListSize > 0 && speciesListSize < filterSizeDefault}">
                                <c:set var="limitedDisplay" value="${speciesListSize}"/>
                            </c:if>
                            <c:forEach var="i" begin="0" end="${limitedDisplay-1}">
                                <c:set var="speciesName" value="${speciesList[i].commonname}"/>
                                <c:if test='${speciesName==null || speciesName ==""}'>
                                    <c:set var="speciesName" value="${speciesList[i].scientificname}"/>
                                </c:if>
                                <div class="filterLine">
                                <div class="text">
                                <span>
                                    <c:out value="${speciesName}"/>
                                </span>
                                </div>
                                <div class="checkItem">
                                    <form:checkbox path="searchparams.species" value="${speciesList[i].scientificname}"/>
                                 </div>
                                <div class="clear"></div>
                                </div>
                            </c:forEach>
                            <c:if test="${speciesListSize > filterSizeDefault}">
                                <div id="species_0" style="display: none">
                                <c:forEach var="j" begin="${filterSizeDefault}" end="${speciesListSize-1}">
                                    <c:set var="speciesName" value="${speciesList[j].commonname}"/>
                                    <c:if test='${speciesName==null || speciesName ==""}'>
                                        <c:set var="speciesName" value="${speciesList[j].scientificname}"/>
                                    </c:if>
                                    <div class="filterLine">
                                    <div class="text">
                                    <span>
                                        <c:out value="${speciesName}"/>
                                    </span>
                                    </div>
                                    <div class="checkItem">
                                        <form:checkbox path="searchparams.species" value="${speciesList[j].scientificname}"/>
                                     </div>
                                    <div class="clear"></div>
                                    </div>
                                </c:forEach>
                                <c:set var="speciesMoreSize" value="${speciesListSize-filterSizeDefault}"/>
                            </div>
                                <a class="link" id="<c:out value='species_link_0'/>"><c:out value="See ${speciesMoreSize} more"/></a> <br/>
                            </c:if>
                        </div>
                    </div>
                </div>
                </c:if>
                <div id="keywordSearchResult" class="result">
                    <c:if test="${totalfound==0}">
                        No results found!
                    </c:if>
                    <c:if test="${summaryentries!=null && searchresults.totalfound>0}">    
                        <div class="resultText">
                            About <c:out value="${totalfound}"/> results found

                                                     </div>
                        <div id="tnt_pagination">
                             <form:form modelAttribute="pagination">        
                                <c:set var="totalPages" value="${pagination.totalPages}"/>
                                <c:set var="maxPages" value="${totalPages}"/>
                                <c:if test="${totalPages>pagination.maxDisplayedPages}">
                                    <c:set var="maxPages" value="${pagination.maxDisplayedPages}"/>
                                    <c:set var="showNextButton" value="${true}"/>
                                </c:if>
                                <c:forEach var="i" begin="1" end="${maxPages}">
                                    <c:set var="start" value="${(i-1)*pagination.numberResultsPerPage}"/>
                                    <a href="showResults?keywords=${searchParameters.keywords}&start=${start}">
                                        <c:out value="${i}"/>
                                    </a>
                                </c:forEach>
                                <c:if test="${showNextButton==true}">
                                    <a href="showResults?keywords=${searchParameters.keywords}&start=${searchParameters.start+pagination.numberResultsPerPage}">
                                        next
                                    </a>
                                </c:if>
                            </form:form>
                        </div>
                    <div class="clear"></div>
                        <div class="line"></div>
                        <div class="resultContent">
                            <c:set var="resultItemId" value="${0}"/>
                            <c:forEach items="${summaryentries}" var="enzyme">
                             <c:set var="primAcc" value="${enzyme.uniprotaccessions[0]}"/>
                            <div class="resultItem">
                                <div id="proteinImg">
                                    <c:set var="imgFile" value='${enzyme.pdbeaccession[0]}'/>
                                    <c:set var="imgLink" value=""/>
                                    <c:if test='${imgFile != "" && imgFile != null}'>
                                        <c:set var="imgLink" value="http://www.ebi.ac.uk/pdbe-srv/view/images/entry/${imgFile}_cbc600.png"/>
                                    </c:if>
                                    <img src="${imgLink}" width="110" height="90"/>
                                </div>
                                <div id="desc">
                                    <p>
                                    <a href="entry/${primAcc}">
                                        <c:set var="showName" value="${fn:substring(enzyme.name, 0, 100)}"/>
                                        <c:out value="${showName}"/>
                                       <!-- [<c:out value="${enzyme.uniprotid}"/>]-->
                                    </a>
                                    </p>

                                    <c:set var="function" value="${enzyme.function}"/>
                                    <c:if test='${function != null && function != ""}'>
                                        <p>
                                        Function:
                                        <c:out value="${enzyme.function}"/>.<br/>
                                        </p>
                                    </c:if>
                                    <c:set var="synonym" value="${enzyme.synonym}"/>
                                    <c:set var="synonymSize" value="${fn:length(synonym)}"/>
                                    <c:set var="synLimitedDisplayDefault" value="${5}"/>
                                    <c:set var="synLimitedDisplay" value="${synLimitedDisplayDefault}"/>
                                    <c:if test='${synonymSize>0}'>
                                        <div id ="synonym">
                                        Synonyms:                                        
                                        <c:if test="${synonymSize > 0 && synonymSize <= synLimitedDisplay}">
                                            <c:set var="synLimitedDisplay" value="${synonymSize}"/>
                                        </c:if>

                                        <c:set var="hiddenSyns" value=""/>                                        
                                        <c:forEach var="i" begin="0" end="${synLimitedDisplay-1}">
                                            <c:out value="${synonym[i]}"/>;
                                        </c:forEach>                                        
                                        <c:if test="${synonymSize>synLimitedDisplay}">
                                        <span  id='syn_${resultItemId}' style="display: none">
                                            <c:forEach var="i" begin="${synLimitedDisplay}" end="${synonymSize-1}">
                                                <c:out value="${synonym[i]}"/>;
                                            </c:forEach>
                                            </span>
                                            <br/>
                                            <a class="link" id="<c:out value='syn_link_${resultItemId}'/>"><c:out value="${showButton}"/></a>
                                        </c:if>
                                        </div>
                                    </c:if>
                                </div>
                                    <div id="in">in</div>
                                    <div class="species">
                                        <p>
                                        <a href="entry/${primAcc}">
                                        <c:choose>
                                        <c:when test='${enzyme.species.commonname == ""}'>
                                            <c:out value="${enzyme.species.scientificname}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:out value="${enzyme.species.commonname}"/>
                                        </c:otherwise>
                                        </c:choose>
                                        </a>
                                        </p>
                                        <!--display = 3 = 2 related species + 1 default species -->
                                        <c:set var="relSpeciesMaxDisplay" value="${2}"/>
                                        <c:set var="relspecies" value="${enzyme.relatedspecies}"/>                                        
                                        <c:set var="relSpeciesSize" value="${fn:length(relspecies)}"/>
                                        <c:if test="${relSpeciesSize > 0}">
                                        <c:if test="${relSpeciesSize <= relSpeciesMaxDisplay}">
                                            <c:set var="relSpeciesMaxDisplay" value="${relSpeciesSize}"/>
                                        </c:if>
                                        <c:forEach var = "i" begin="0" end="${relSpeciesMaxDisplay-1}">
                                            <p>
                                                <c:choose>
                                                <c:when test='${relspecies[i].species.commonname == ""}'>
                                                    <c:set var="speciesName" value="${relspecies[i].species.scientificname}"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:set var="speciesName" value="${relspecies[i].species.commonname}"/>
                                                </c:otherwise>
                                                </c:choose>
                                            <a href="entry/${relspecies[i].uniprotaccessions[0]}">
                                                <c:out value="${speciesName}"/>
                                            </a>
                                            </p>                                            
                                        </c:forEach>
                                        <c:if test="${relSpeciesSize > relSpeciesMaxDisplay}">
                                            <div id="relSpecies_${resultItemId}" style="display: none">
                                            <c:forEach var = "i" begin="${relSpeciesMaxDisplay}" end="${relSpeciesSize-1}">
                                                <p>
                                                    <c:choose>
                                                    <c:when test='${relspecies[i].species.commonname == ""}'>
                                                        <c:set var="speciesName" value="${relspecies[i].species.scientificname}"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:set var="speciesName" value="${relspecies[i].species.commonname}"/>
                                                    </c:otherwise>
                                                    </c:choose>
                                                <a href="entry/${relspecies[i].uniprotaccessions[0]}">
                                                    <c:out value="${speciesName}"/>
                                                </a>
                                                </p>
                                            </c:forEach>
                                         </div>
                                         <a class="link" id="<c:out value='relSpecies_link_${resultItemId}'/>"><c:out value="${showButton}"/></a> <br/>
                                         </c:if>
                                       </c:if>
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
            <div class="grid_12">
                    <div class="footer">&copy;
                      <a target="_top" href="http://www.ebi.ac.uk/" title="European Bioinformatics Institute Home Page">European Bioinformatics Institute</a>
                      2011. EBI is an Outstation of the
                      <a href="http://www.embl.org/" target="_blank" title="European Molecular Biology Laboratory Home Page">European Molecular Biology Laboratory</a>.
                    </div>
            </div>
            <div class="clear"></div>
        </div>

    </body>
</html>
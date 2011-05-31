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
            <div class="grid_12">
                <div  id="keywordSearch" class="search">
                <form:form modelAttribute="searchParameters" action="showResults" method="get">
                    <p>
                        <form:input path="keywords" cssClass="field"/>  
                        <input type="submit" value="Search" class="button" />
                    </p>
                </form:form>
                 </div>
            </div>            
            <div class="grid_12 content">
                <c:if test="${enzymeSummaryCollection.enzymesummary!=null && enzymeSummaryCollection.totalfound>0}">
                <div class="filter">
                    <div class="title">
                        Search Filters
                    </div>
                    <div class="line"></div>

                    <div class="sublevel1">
                        <div class="subTitle">
                            Chemical Compounds
                        </div>
                        <div class="content">
                            <div class="text">
                            <span>Sildenafil</span>
                            </div>
                            <div class="checkItem">
                                <input type="checkbox" name="human" value="human" />
                            </div>
                           <div class="clear"></div>
                            <div class="text">
                            <span>Others</span>
                            </div>
                            <div class="checkItem">
                                <input type="checkbox" name="human" value="human" />
                            </div>

                        </div>
                    </div>



                    <div class="sublevel1">
                        <div class="subTitle">
                            Disease
                        </div>
                        <div class="content">
                            <div class="text">
                            <span>Diabetes</span>
                            </div>
                            <div class="checkItem">
                                <input type="checkbox" name="human" value="human" />
                            </div>
                           <div class="clear"></div>
                            <div class="text">
                            <span>Other diseases</span>
                            </div>
                            <div class="checkItem">
                                <input type="checkbox" name="human" value="human" />
                            </div>

                        </div>
                    </div>


                    <div class="sublevel1">
                        <div class="subTitle">
                            Species
                        </div>
                        <div class="content">
                            <div class="text">
                            <span>Human</span>&nbsp;<span class="otherName">(Homo Sapiens)</span>
                            </div>
                            <div class="checkItem">
                                <input type="checkbox" name="human" value="human" />
                            </div>
                           <div class="clear"></div>
                            <div class="text">
                            <span>Rat</span>&nbsp;<span class="otherName">(Rattus norvegicus)</span>
                            </div>
                            <div class="checkItem">
                                <input type="checkbox" name="human" value="human" />
                            </div>

                        </div>
                    </div>


                </div>
                </c:if>                
                <div id="keywordSearchResult" class="result">
                <form:form modelAttribute="enzymeSummaryCollection" action="showResults" method="get">
                    <c:set var="totalfound" value="${enzymeSummaryCollection.totalfound}"/>
                    <c:if test="${totalfound==0}">
                        No results found!
                    </c:if>
                    <c:if test="${enzymeSummaryCollection.enzymesummary!=null && enzymeSummaryCollection.totalfound>0}">
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
                    <div class="comparison">
                        Compare & download
                    </div>
                    <div class="clear"></div>
                        <div class="line"></div>
                    <div id ="allButtons">
                        <input type="button" value="Add All"/><input type="button" value="Remove All"/>
                    </div>                        
                        <div class="resultContent">
                            <c:forEach items="${enzymeSummaryCollection.enzymesummary}" var="enzyme">
                            <div class="resultItem">
                                <div id="proteinImg">
                                    <a href="http://www.ebi.ac.uk/pdbe-srv/view/images/entry/1ha1_cbc600.png" target="blank">
                                    <img src="http://www.ebi.ac.uk/pdbe-srv/view/images/entry/1ha1_cbc600.png" alt="Klematis" width="110" height="90" />
                                    </a>
                                </div>
                                <div id="desc">
                                    <a href="entry">
                                        <c:set var="showName" value="${fn:substring(enzyme.name, 0, 100)}"/>
                                        <c:out value="${showName}"/>
                                    </a>
                                    <br/>
                                    Function:
                                    <c:set var="showFunction" value="${fn:substring(enzyme.function, 0, 100)}"/>
                                    <c:out value="${showFunction}"/>...<br/>
                                    Synonyms:
                                    <c:set var="synSize" value="${0}"/>
                                    <c:forEach items="${enzyme.synonym}" var="syn">                                        
                                        <c:set var="nameSize" value="${nameSize+1}"/>
                                    </c:forEach>
                                    <c:set var="counter" value="${0}"/>
                                    <c:forEach items="${enzyme.synonym}" var="syn">                                        
                                        <c:if test="${nameSize>1 && counter>0}">
                                            ; 
                                        </c:if>
                                        <c:out value="${syn}"/>
                                        <c:set var="counter" value="${counter+1}"/>
                                    </c:forEach>

                                </div>
                                    <div id="in">in</div>
                                    <div class="species">
                                    Human<br/>
                                    Mouse<br/>
                                    Dog<br/>                                    
                                    <a href="">See more</a> <br/>
                                </div>                                    
                            </div>
                            <div id="buttonItems">
                                <br/>
                                <br/>
                                <input type="button" value="Add"/><br/>
                                <input type="button" value="Remove"/>
                                <br/>
                                <br/>
                            </div>
                            <div class="clear"></div>
                            </c:forEach>
                        </div>
                    </c:if>

                    <!--
                    <c:forEach items="${enzymes}" var="enzyme">
                        <p>
                            Uniprot id: <c:out value="${enzyme.uniprotid}"/><br>
                            Uniprot accession:
                        <c:forEach items="${enzyme.uniprotaccessions}" var="uniprotAcccession">
                            <a href="http://www.uniprot.org/uniprot/${uniprotAcccession}" target="blank">
                            <c:out value="${uniprotAcccession}"/>
                        </a>
                        </c:forEach>
                      <br>
                      Uniprot name: <c:out value="${enzyme.name}"/><br>
                      Uniprot species: <c:out value="${enzyme.species.scientificname}"/>
                  </p>
                    </c:forEach>
                    -->

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
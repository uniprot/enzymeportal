<%-- 
    Document   : search
    Created on : Sep 17, 2012, 4:05:40 PM
    Author     : joseph
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!doctype html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="Fn" uri="/WEB-INF/epTagLibray.tld" %>


<!-- paulirish.com/2008/conditional-stylesheets-vs-css-hacks-answer-neither/ -->
<!--[if lt IE 7]> <html class="no-js ie6 oldie" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js ie7 oldie" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js ie8 oldie" lang="en"> <![endif]-->
<!-- Consider adding an manifest.appcache: h5bp.com/d/Offline -->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->

<c:set var="pageTitle" value="Search results"/>
<%@include file="head.jspf" %>

<body class="level2"><!-- add any of your classes or IDs -->
    
    <script>
		$(function() {
			$("#accordion").accordion({
				collapsible : true,
				active : false,
				heightStyle : "content"
			});
		});
	</script>
    
    <div id="skip-to">
            <ul>
                <li><a href="#content">Skip to main content</a></li>
                <li><a href="#local-nav">Skip to local navigation</a></li>
                <li><a href="#global-nav">Skip to EBI global navigation menu</a></li>
                <li><a href="#global-nav-expanded">Skip to expanded EBI global navigation menu (includes all sub-sections)</a></li>
            </ul>
        </div>

        <div id="wrapper" class="container_24">

            <%@include file="header.jspf" %>
            
            <div id="content" role="main" class="grid_24 clearfix">

                <!--Global variables-->
                <c:set var="showButton" value="Show more"/>
                <c:set var="searchText" value="${searchModel.searchparams.text}"/>
                <c:set var="searchSequence" value="${searchModel.searchparams.sequence}"/>
                <c:set var="startRecord" value="${pagination.firstResult}"/>
                <c:set var="searchresults" value="${searchModel.searchresults}"/>
                <c:set var="searchFilter" value="${searchresults.searchfilters}"/>
                <c:set var="summaryEntries" value="${searchresults.summaryentries}"/>
                <c:set var="summaryEntriesSize" value="${fn:length(summaryEntries)}"/>
                <c:set var="totalfound" value="${searchresults.totalfound}"/>
                <c:set var="filterSizeDefault" value="${50}"/>
                <%-- maximum length in words for a text field --%>
                <c:set var="textMaxLength" value="${60}"/>
                <script>

                    var  speciesAutocompleteDataSource = [];
                    var compoundsAutoCompleteDataSource = [];
                    var diseaseAutoCompleteDataSource = [];
                </script>
                
                
                       <c:choose>
                            <c:when test="${searchModel.searchparams.type eq 'SEQUENCE'}">	
                                <c:set var="searchText" value="${searchModel.searchparams.sequence}"/>	
                            </c:when>

                            <c:otherwise>
                                <c:set var="searchText"
                                       value="${Fn:escapeHTML(searchModel.searchparams.text)}"/>
                            </c:otherwise>
                        </c:choose>

                <!-- Suggested layout containers -->  
                <section >
                    <div class="grid_12zzz" style="display: table; margin-left: 0em;">
                        <%@ include file="breadcrumbs.jsp" %>
                    </div>


                </section>

                <section class="grid_24 clearfix">
                    <section class="grid_18 alpha"  >

                        <c:if test="${totalfound eq 0}">
                            <c:if test="${searchText eq ''}">
                               <c:set var="searchText"
                                       value=" "/> 
                            </c:if>
                            <h2>No Enzyme Portal results found</h2>
                            <p class="alert">We're sorry but we couldn't find anything that matched your search for " ${searchText} ". Please try another search or use the<a href="advanceSearch"> advanced search</a></p>
                            <script>
                                $(document).ready(function() {
                                    try {
                                        /* The simplest implementation, used on your zero search results pages */
                                        updateSummary({noResults: true});	       
                                    } catch (except_1) {}
                                });
                            </script>
                        </c:if>
                        <c:if test="${totalfound gt 0}">
                                  <c:choose>
                            <c:when test="${searchModel.searchparams.type eq 'SEQUENCE'}">	
                               <h2>Enzyme Portal results for <span class="searchterm"><i>${fn:substring(searchText, 0, 20)}${fn:length(searchText) gt 20? '...':''}</i>
                                    </span></h2>	
                            </c:when>

                            <c:otherwise>
                                <h2>Enzyme Portal results for <span class="searchterm"><i>${searchText}</i>
                                    </span></h2>
                            </c:otherwise>
                        </c:choose>
                            
                                              
                            <!--    	<p>Showing <strong>X</strong> results from a total of <strong>Y</strong></p>-->
                        </c:if>
                    </section>
                    <c:if test="${searchModel.searchparams.type eq 'KEYWORD'}">
                    <aside class="grid_6 omega shortcuts expander" id="search-extras">	    	
                        <div id="ebi_search_results"><h3 class="slideToggle icon icon-functional" data-icon="u">Show more data from EMBL-EBI</h3>
                        </div>
                    </aside>
                    </c:if>

                </section>

                <section class="grid_6 alpha" id="search-results">


                    <!--                <div class="grid_12 content">-->
                    <c:if test="${ searchresults.totalfound gt 0}">
                        <div class="filter grid_24">
                            <div class="title">
                                Search Filters
                            </div>
                            <div class="line"></div>
                            <form:form id="filtersForm" name="filtersForm" modelAttribute="searchModel" action="search" method="POST">
                                <form:hidden path="searchparams.type" />	
                                <form:hidden path="searchparams.text" />
                                <form:hidden path="searchparams.sequence" />
                                <form:hidden path="searchparams.previoustext" />
                                <input type="hidden" id="filtersFormStart"
                                       name="searchparams.start" value="0"/>
                                <%@ include file="filter-species.jspf"%>
                                <br/>
                                <%@ include file="filter-compounds.jspf"%>
                                <br/>
                                <%@ include file="filter-diseases.jspf"%>
                            </form:form>
                        </div> 
                        <%--filter --%>
                    </c:if>
                </section>
                <section class="grid_18" id="keywordSearchResult">
                    <c:if test="${totalfound eq -100}">
                        <spring:message code="label.search.empty"/>
                    </c:if>
                    <c:if test="${summaryEntriesSize == 0 and ( speciesListSize gt 0 or compoundListSize gt 0 or diseasesListSize gt 0)}">
                        <div class="resultItem">
                            <a href="#"><span class="displayMsg" style="font-size:small;text-align:center">
                                No Result was found for this selection.</span></a>
                        </div>
                    </c:if>
                    <c:if test="${summaryEntriesSize gt 0 and searchresults.totalfound gt 0}">
                            <div style="width: 100%;">
                                <c:set var="totalPages" value="${pagination.lastPage}"/>
                                <c:set var="maxPages" value="${totalPages}"/>
                                <div class="resultText">
                                    <b>${totalfound}</b> results found for
                                    <i>${fn:substring(searchText, 0, 30)}${fn:length(searchText) gt 30? '...':''}</i>,
                                    <c:if test="${totalfound ne summaryEntriesSize}">
                                        filtered to <b>${summaryEntriesSize}</b>,
                                    </c:if>
                                    displaying ${pagination.firstResult+1} - ${pagination.lastResult+1}
                                    <span id="basketAll"
                                        class="icon icon-generic" data-icon="b">
                                        <%@include file="basket-buttons.jspf" %>
                                    </span>
                                </div>
                                <div id="paginationNav" style="text-align: right;">
                                    <form:form modelAttribute="pagination" >
                                        <c:if test="${totalPages gt pagination.maxDisplayedPages}">
                                            <c:set var="maxPages" value="${pagination.maxDisplayedPages}"/>
                                            <c:set var="showNextButton" value="${true}"/>
                                        </c:if>
                                        <input id="prevStart" type="hidden"
                                               value="${pagination.firstResult - pagination.numberOfResultsPerPage}">
                                        <a id="prevButton" href="javascript:void(0);"
                                           style="display:${pagination.currentPage eq 1? 'none' : 'inline'}">
                                            Previous
                                        </a>
                                        Page ${pagination.currentPage} of ${totalPages}
    
                                        <c:if test="${pagination.lastResult+1 lt summaryEntriesSize}">
                                            <input id ="nextStart" type="hidden"
                                                   value="${startRecord + pagination.numberOfResultsPerPage}">                                    
                                            <a id="nextButton" href="javascript:void(0);">
                                                Next
                                            </a>
                                        </c:if>                         
                                        <%-- Add species filter to this form, don't lose it: --%>
                                        <c:forEach var="filterSp" items="${searchModel.searchresults.searchfilters.species}">
                                            <input type="checkbox" style="display: none;" 
                                                   name="searchparams.species"
                                                   value="${filterSp.scientificname}" />
                                        </c:forEach>
                                        <%-- TODO: add also compounds and disease filters --%>
                                    </form:form>
                                </div><!-- pagination -->
                            </div>
                        <div class="clear"></div>
                        <div class="line"></div>
                        <div class="resultContent">
                            <c:set var="resultItemId" value="${0}"/>
                            <c:forEach items="${summaryEntries}"
                                       begin="${pagination.firstResult}"
                                       end="${pagination.lastResult}" var="enzyme" varStatus="vsEnzymes">
                                <div class="grid_23 alpha">
                                    <%@ include file="summary.jspf"%>
                                </div>
                                <div class="grid_1 omega">
    <span class="icon icon-generic" data-icon="b" style="white-space: nowrap;">
    <input type="checkbox" class="forBasket"
        onchange="selectForBasket(event)" title="Add to your basket."
        value="${epfn:getSummaryBasketId(enzyme)}"
        ${not empty basket and
        not empty basket[epfn:getSummaryBasketId(enzyme)]? 'checked': ''}/>
    </span>
                                </div>
                            </c:forEach>
                        </div>
                    </c:if>
                </section>
            </div>

    <%@include file="footer.jspf" %>
    
        </div> <!--! end of #wrapper -->

    </body>
</html>


<%-- 
    Document   : search
    Created on : Sep 17, 2012, 4:05:40 PM
    Author     : joseph
--%>

<!doctype html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib  prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="Fn" uri="/WEB-INF/epTagLibray.tld" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


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
                                    </form:form>
                                </div><!-- pagination -->
                            </div>
                            <%-- Add species filter to this form, don't lose it: --%>
                            <c:forEach var="filterSp" items="${searchModel.searchresults.searchfilters.species}">
                                <input type="checkbox" style="display: none;" 
                                       name="searchparams.species"
                                       value="${filterSp.scientificname}" />
                            </c:forEach>
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

    <footer>
    <!-- Optional local footer (insert citation / project-specific copyright / etc here -->
		  <!--
      <div id="local-footer" class="grid_24 clearfix">
			  <p>How to reference this page: ...</p>
		  </div>
      -->
		<!-- End optional local footer -->
		
		<div id="global-footer" class="grid_24">
						
			<nav id="global-nav-expanded">
				
				<div class="grid_4 alpha">
					<h3 class="embl-ebi"><a href="/" title="EMBL-EBI">EMBL-EBI</a></h3>
				</div>
				
				<div class="grid_4">
					<h3 class="services"><a href="/services">Services</a></h3>
				</div>
				
				<div class="grid_4">
					<h3 class="research"><a href="/research">Research</a></h3>
				</div>
				
				<div class="grid_4">
					<h3 class="training"><a href="/training">Training</a></h3>
				</div>
				
				<div class="grid_4">
					<h3 class="industry"><a href="/industry">Industry</a></h3>
				</div>
				
				<div class="grid_4 omega">
					<h3 class="about"><a href="/about">About us</a></h3>
				</div>

			</nav>
			
			<section id="ebi-footer-meta">
				<p class="address">EMBL-EBI, Wellcome Trust Genome Campus, Hinxton, Cambridgeshire, CB10 1SD, UK &nbsp; &nbsp; +44 (0)1223 49 44 44</p>
				<p class="legal">Copyright &copy; EMBL-EBI 2012 | EBI is an Outstation of the <a href="http://www.embl.org">European Molecular Biology Laboratory</a> | <a href="/about/privacy">Privacy</a> | <a href="/about/cookies">Cookies</a> | <a href="/about/terms-of-use">Terms of use</a></p>	
			</section>

		</div>
		
    </footer>
        </div> <!--! end of #wrapper -->


        <!-- JavaScript at the bottom for fast page loading -->

        <c:if test="${pageContext.request.serverName!='www.ebi.ac.uk'}" >
            <script type="text/javascript">var redline = {}; redline.project_id = 185653108;</script><script id="redline_js" src="http://www.redline.cc/assets/button.js" type="text/javascript">
                
            </script>
               <script>
            $(document).ready(function() {
                setTimeout(function(){
                    // Handler for .ready() called.
                    $("#redline_side_car").css("background-image","url(resources/images/redline_left_button.png)");
                    $("#redline_side_car").css("background-size", "23px auto");
                    $("#redline_side_car").css("display", "block");
                    $("#redline_side_car").css("width", "23px");
                    $("#redline_side_car").css("height", "63px");
                },1000);
            });
        </script>
        </c:if>
        
        <!--        add twitter script for twitterapi-->
<script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0];if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src="https://platform.twitter.com/widgets.js";fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");</script>


<!--        <script src="resources/lib/spineconcept/javascript/jquery-1.5.1.min.js" type="text/javascript"></script>
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
        <script src="resources/javascript/search.js" type="text/javascript"></script>
       -->

<!--    now the frontier js for ebi global result-->
        <script src="//www.ebi.ac.uk/web_guidelines/js/ebi-global-search-run.js"></script>
        <script src="//www.ebi.ac.uk/web_guidelines/js/ebi-global-search.js"></script>

<!--        To be able to run locally as frontier impl only works when deployed to ebi domain-->
<!--  <script src="resources/javascript/ebi-global-search-run.js" type="text/javascript"></script>
    <script src="resources/javascript/ebi-global-search.js" type="text/javascript"></script>-->
    
        <!-- Grab Google CDN's jQuery, with a protocol relative URL; fall back to local if offline -->

        <!--      <script src="//ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js"></script>
                <script>window.jQuery || document.write('<script src="../js/libs/jquery-1.6.2.min.js"><\/script>')</script>-->



        <!-- scripts concatenated and minified via ant build script-->
        <!--
      <script defer src="../js/plugins.js"></script>
        <script defer src="../js/script.js"></script>
        -->
        
         <script defer="defer" src="//www.ebi.ac.uk/web_guidelines/js/cookiebanner.js"></script>  
  <script defer="defer" src="//www.ebi.ac.uk/web_guidelines/js/foot.js"></script>
 
        <!-- end scripts-->


        <!-- Change UA-XXXXX-X to be your site's ID -->
        <!--
      <script>
          window._gaq = [['_setAccount','UAXXXXXXXX1'],['_trackPageview'],['_trackPageLoadTime']];
          Modernizr.load({
            load: ('https:' == location.protocol ? '//ssl' : '//www') + '.google-analytics.com/ga.js'
          });
        </script>
        -->


        <!-- Prompt IE 6 users to install Chrome Frame. Remove this if you want to support IE 6.
             chromium.org/developers/how-tos/chrome-frame-getting-started -->
        <!--[if lt IE 7 ]>
            <script src="//ajax.googleapis.com/ajax/libs/chrome-frame/1.0.3/CFInstall.min.js"></script>
            <script>window.attachEvent('onload',function(){CFInstall.check({mode:'overlay'})})</script>
          <![endif]-->

    </body>
<!--    <script>
        updateSummary();
    </script>-->
</html>


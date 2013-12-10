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
    <head>
<!--        <meta charset="utf-8">-->

        <!-- Use the .htaccess and remove these lines to avoid edge case issues.
             More info: h5bp.com/b/378 -->
        <!-- <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"> -->	<!-- Not yet implemented -->

        <title>Search Result  &lt; Enzyme Portal &gt; &lt; EMBL-EBI</title>
        <meta name="description" content="EMBL-EBI"><!-- Describe what this page is about -->
        <meta name="keywords" content="bioinformatics, europe, institute"><!-- A few keywords that relate to the content of THIS PAGE (not the whol project) -->
        <meta name="author" content="EMBL-EBI"><!-- Your [project-name] here -->

        <!-- Mobile viewport optimized: j.mp/bplateviewport -->
        <meta name="viewport" content="width=device-width,initial-scale=1">

        <!-- Place favicon.ico and apple-touch-icon.png in the root directory: mathiasbynens.be/notes/touch-icons -->
        <!-- Better make some of these! [FR] -->

        <!-- CSS: implied media=all -->
        <!-- CSS concatenated and minified via ant build script-->	<!-- Not yet implemented -->

        <!--        <link rel="stylesheet" href="http://wwwdev.ebi.ac.uk/web_guidelines/css/compliance/develop/boilerplate-style.css">  
        
                <link rel="stylesheet" href="http://wwwdev.ebi.ac.uk/web_guidelines/css/compliance/develop/ebi-global.css" type="text/css" media="screen" />
                <link rel="stylesheet" href="http://wwwdev.ebi.ac.uk/web_guidelines/css/compliance/develop/ebi-visual.css" type="text/css" media="screen">
                <link rel="stylesheet" href="http://wwwdev.ebi.ac.uk/web_guidelines/css/compliance/develop/984-24-col-fluid.css" type="text/css" media="screen" />
        
                <link rel="stylesheet" href="http://wwwdev.ebi.ac.uk/web_guidelines/css/compliance/develop/embl-petrol-colours.css" type="text/css" media="screen">  you can replace this with [projectname]-colours.css. See http://frontier.ebi.ac.uk/web/style/colour for details of how to do this -->


        <!--                <link rel="stylesheet"  href="http://www.ebi.ac.uk/inc/css/contents.css"     type="text/css" />-->
        <!--        <link media="screen" href="resources/lib/spineconcept/css/960gs-fluid/grid.css" type="text/css" rel="stylesheet" />
                <link media="screen" href="resources/lib/spineconcept/css/common.css" type="text/css" rel="stylesheet" />
                <link media="screen" href="resources/lib/spineconcept/css/identification.css" type="text/css" rel="stylesheet" />
                <link media="screen" href="resources/lib/spineconcept/css/species.css" type="text/css" rel="stylesheet" />-->
        <!--        <script src="resources/lib/spineconcept/javascript/jquery-1.5.1.min.js" type="text/javascript"></script>
                <script src="resources/lib/spineconcept/javascript/identification.js" type="text/javascript"></script>-->

<!--        <link rel="stylesheet" href="//www.ebi.ac.uk/web_guidelines/css/compliance/mini/ebi-fluid-embl.css">-->

        <!--
                <link rel="stylesheet" href="resources/css/boilerplate-style.css"> 
        
                <link rel="stylesheet" href="resources/css/ebi-global.css" type="text/css" media="screen" />
        
                <link rel="stylesheet" href="resources/css/ebi-visual.css" type="text/css" media="screen">
                <link rel="stylesheet" href="resources/css/984-24-col-fluid.css" type="text/css" media="screen" />-->
        <link href="resources/css/search.css" type="text/css" rel="stylesheet" />
        <!--           <link rel="stylesheet" href="resources/css/enzyme-portal-colours.css" type="text/css" media="screen" />-->
        <link rel="stylesheet" href="resources/css/embl-petrol-colours.css" type="text/css" media="screen" />

<!--        for production-->
          <link rel="stylesheet" href="//www.ebi.ac.uk/web_guidelines/css/compliance/mini/ebi-fluid-embl.css">

        <!--        javascript was placed here for auto complete otherwise should be place at the bottom for faster page loading-->

        <script src="resources/lib/spineconcept/javascript/jquery-1.5.1.min.js" type="text/javascript"></script>
        <script src="resources/lib/spineconcept/javascript/identification.js" type="text/javascript"></script>
        <script src="resources/javascript/search.js" type="text/javascript"></script>


        <link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" rel="stylesheet" type="text/css"/>
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
        <script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js"></script>
                <script src="http://yui.yahooapis.com/3.4.1/build/yui/yui-min.js"></script>

        <!-- end CSS-->

<!--
        <style type="text/css">
            /* You have the option of setting a maximum width for your page, and making sure everything is centered */
            /* body { max-width: 1600px; margin: 0 auto; } */

            /* --------------------------------
                GLOBAL SEARCH TEMPLATE - START
               -------------------------------- */
            .loading {
                background: url("http://frontier.ebi.ac.uk/ebisearch/examples/ebisearch-globalSearch-template_files/ajax-loader.gif") no-repeat right;
            }

            span.searchterm {
                font-weight: bold;
                font-style: italic;
                padding: 0.2em 0.5em;
                background-color: rgb(238, 238, 238);
                border-radius: 5px 5px 5px 5px;
            }
            /* --------------------------------
                GLOBAL SEARCH TEMPLATE - END
               -------------------------------- */
        </style>-->

        <!-- end CSS-->


        <!-- All JavaScript at the bottom, except for Modernizr / Respond.
             Modernizr enables HTML5 elements & feature detects; Respond is a polyfill for min/max-width CSS3 Media Queries
             For optimal performance, use a custom Modernizr build: www.modernizr.com/download/ -->

        <!-- Full build -->
        <!-- <script src="//www.ebi.ac.uk/web_guidelines/js/libs/modernizr.minified.2.1.6.js"></script> -->

        <!-- custom build (lacks most of the "advanced" HTML5 support -->
        <script src="//www.ebi.ac.uk/ebisearch/examples/ebisearch-globalSearch-template_files/modernizr.js"></script>		

        <!--<! --------------------------------
        GLOBAL SEARCH TEMPLATE - START
       -------------------------------- >-->

        <script type="text/javascript" src="//www.ebi.ac.uk/ebisearch/examples/ebisearch-globalSearch-template_files/jquery-1.8.0.min.js"></script>
        <script type="text/javascript" src="//www.ebi.ac.uk/ebisearch/examples/ebisearch-globalSearch-template_files/jquery-ui-1.8.23.custom.min.js"></script>

        <!--<! --------------------------------
        GLOBAL SEARCH TEMPLATE - END
       -------------------------------- >-->
        
        
               
        
        <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
  <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
  <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
  
 
  


  <script>
  $(function() {
    $( "#accordion" ).accordion({
      
      
      collapsible: true,
      active:false,
      heightStyle: "content"
     
     

  
    });
  });

  </script>
  
  <script>
      $(function() {
//$("#accordionx").accordion({ active: false });

//$("#accordionx").addClass("ui-accordion ui-accordion-icons ui-widget ui-helper-reset")
//  .find("h3")
//    .addClass("ui-accordion-header ui-helper-reset ui-state-default ui-corner-top ui-corner-bottom")
//    .hover(function() { $(this).toggleClass("ui-state-hover"); })
//    .prepend('<span class="ui-icon ui-icon-triangle-1-e"></span>')
//    .click(function() {
//      $(this)
//        .toggleClass("ui-accordion-header-active ui-state-active ui-state-default ui-corner-bottom")
//        .find("> .ui-icon").toggleClass("ui-icon-triangle-1-e ui-icon-triangle-1-s").end()
//        .next().toggleClass("ui-accordion-content-active").slideToggle();
//      return false;
//    })
//    .next()
//      .addClass("ui-accordion-content  ui-helper-reset ui-widget-content ui-corner-bottom")
//      .hide();
      });
</script>


    </head>

    <body class="level2"><!-- add any of your classes or IDs -->
        <div id="skip-to">
            <ul>
                <li><a href="#content">Skip to main content</a></li>
                <li><a href="#local-nav">Skip to local navigation</a></li>
                <li><a href="#global-nav">Skip to EBI global navigation menu</a></li>
                <li><a href="#global-nav-expanded">Skip to expanded EBI global navigation menu (includes all sub-sections)</a></li>
            </ul>
        </div>

        <div id="wrapper" class="container_24">
            <header>
                <div id="global-masthead" class="masthead grid_24">
                    <!--This has to be one line and no newline characters-->
                    <a href="/" title="Go to the EMBL-EBI homepage"><img src="//www.ebi.ac.uk/web_guidelines/images/logos/EMBL-EBI/EMBL_EBI_Logo_white.png" alt="EMBL European Bioinformatics Institute"></a>

                    <nav>
                        <ul id="global-nav">
                            <!-- set active class as appropriate -->
                            <li class="first active" id="services"><a href="/services">Services</a></li>
                            <li id="research"><a href="/research">Research</a></li>
                            <li id="training"><a href="/training">Training</a></li>
                            <li id="industry"><a href="/industry">Industry</a></li>
                            <li id="about" class="last"><a href="/about">About us</a></li>
                        </ul>
                    </nav>

                </div>

                <div id="local-masthead" class="masthead grid_24 nomenu">

                    <!-- local-title -->

                    <!--<div class="grid_12 alpha" id="local-title">
                                                    <h1><a href="/enzymeportal" title="Back to Enzyme Portal homepage">Enzyme Portal</a></h1>
                                            </div>-->
                    <div id="local-title" class="grid_12 alpha logo-title"> 
                        <a href="/enzymeportal" title="Back to Enzyme Portal homepage">
                            <img src="resources/images/enzymeportal_logo.png" alt="Enzyme Portal logo" style="width :64px;height: 64px; margin-right: 0px">
                        </a> <span style="margin-top: 30px"><h1 style="padding-left: 0px">Enzyme Portal</h1></span> </div>

                    <!-- OR... -->

                    <!--                    <div class="grid_12 alpha" id="local-title">
                                            <h1>Enzyme Portal</h1>
                                        </div>-->
                    <!-- -->

                    <div class="grid_12 omega">




                        <%@ include file="frontierSearchBox.jsp" %>
                        
                        
              
                    </div>


                    <nav>
                        <ul class="grid_24" id="local-nav">
                            <li  class="first"><a href="/enzymeportal" title="">Home</a></li>
                            <!--					<li><a href="#">Documentation</a></li>-->
                            <li><a href="faq" title="Frequently Asked questions">FAQ</a></li>
                            <li class="last"><a href="about" title="About Enzyme Portal">About Enzyme Portal</a></li>
                            <!-- If you need to include functional (as opposed to purely navigational) links in your local menu,
                                 add them here, and give them a class of "functional". Remember: you'll need a class of "last" for
                                 whichever one will show up last... 
                                 For example: -->
                            <!--					<li class="functional last"><a href="#" class="icon icon-functional" data-icon="l">Login</a></li>-->
                            <li class="functional"><a href="http://www.ebi.ac.uk/support/index.php?query=Enzyme+portal&referrer=http://www.ebi.ac.uk/enzymeportal/" class="icon icon-static" data-icon="f">Feedback</a></li>
<!--                            <li class="functional"><a href="#" class="icon icon-functional" data-icon="r">Share</a></li>-->
                            <li class="functional"> <a href="https://twitter.com/share" class="icon icon-functional" data-icon="r" data-dnt="true" data-count="none" data-via="twitterapi">Share</a></li>
                        
                        </ul>
                    </nav>
                </div>
            </header>


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
                                <h2>Enzyme Portal results for <span id="searchTerm" class="searchterm"><i>${searchText}</i></span></h2>
                            </c:otherwise>
                          </c:choose>
                            <!--    	<p>Showing <strong>X</strong> results from a total of <strong>Y</strong></p>-->
                        </c:if>
                    </section>
                    <c:if test="${searchModel.searchparams.type ne 'SEQUENCE'}">
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
                  <c:if test="${searchModel.searchparams.type eq 'COMPOUND'}">
                    <figure class="compound structure">
                        <img src="${chebiConfig.compoundImgBaseUrl}${searchText}" alt=""/>
                        <figcaption>
                            <a href="${chebiConfig.compoundBaseUrl}${searchText}"
                                target="_blank"><span id="chebiNameId"></span></a>
                            <form id="goBackStructureSearch"
                                action="${pageContext.request.contextPath}/advanceSearch"
                                method="POST" style="text-align: center;">
                                <input type="submit" value="Edit Query"/>
                                <input type="hidden" name="type" value="COMPOUND"/>
                            </form>
                        </figcaption>
                    </figure>
                    <script>
                    jQuery.ajax({
                    	// FIXME: soft-code this URL!
                        url: "/webservices/chebi/2.0/test/getCompleteEntity?chebiId=${searchText}",
                        success: function(data){
                        	var xmlDoc = jQuery.parseXML(data);
                        	var xmlResult = jQuery(xmlDoc).find('return');
                        	var chebiName = xmlResult.find('chebiAsciiName').text();
                        	$('#chebiNameId').text(
                        			chebiName + ' (' + '${searchText}' + ')');
                        }
                    });
                    </script>
                  </c:if>                                              
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
                        <form:form modelAttribute="pagination" >
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
                                </div>
                                <div id="paginationNav" style="text-align: right;">
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
                                </div><!-- pagination -->
                            </div>
                            <%-- Add species filter to this form, don't lose it: --%>
                            <c:forEach var="filterSp" items="${searchModel.searchresults.searchfilters.species}">
                                <input type="checkbox" style="display: none;" 
                                       name="searchparams.species"
                                       value="${filterSp.scientificname}" />
                            </c:forEach>
                        </form:form>
                        <div class="clear"></div>
                        <div class="line"></div>
                        <div class="resultContent">
                            <c:set var="resultItemId" value="${0}"/>
                            <c:forEach items="${summaryEntries}"
                                       begin="${pagination.firstResult}"
                                       end="${pagination.lastResult}" var="enzyme" varStatus="vsEnzymes">
                                <%@ include file="summary.jspf"%>
                            </c:forEach>
                        </div>
                    </section>
                </c:if>
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


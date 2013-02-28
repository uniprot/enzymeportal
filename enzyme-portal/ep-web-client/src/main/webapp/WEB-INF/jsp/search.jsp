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


<!-- paulirish.com/2008/conditional-stylesheets-vs-css-hacks-answer-neither/ -->
<!--[if lt IE 7]> <html class="no-js ie6 oldie" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js ie7 oldie" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js ie8 oldie" lang="en"> <![endif]-->
<!-- Consider adding an manifest.appcache: h5bp.com/d/Offline -->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->
    <head>
        <meta charset="utf-8">

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
        <script src="http://frontier.ebi.ac.uk/ebisearch/examples/ebisearch-globalSearch-template_files/modernizr.js"></script>		

        <!--<! --------------------------------
        GLOBAL SEARCH TEMPLATE - START
       -------------------------------- >-->

        <script type="text/javascript" src="http://frontier.ebi.ac.uk/ebisearch/examples/ebisearch-globalSearch-template_files/jquery-1.8.0.min.js"></script>
        <script type="text/javascript" src="http://frontier.ebi.ac.uk/ebisearch/examples/ebisearch-globalSearch-template_files/jquery-ui-1.8.23.custom.min.js"></script>

        <!--<! --------------------------------
        GLOBAL SEARCH TEMPLATE - END
       -------------------------------- >-->

        <!--<style type="text/css">       #cookie-banner{position:absolute;top:-9999px;background-color:#222;width:100%;border-bottom:5px solid #222;left:0;}
               #cookie-banner p{margin:5px 0;color:#eee}
               #cookie-banner a{color:#fff}
               #cookie-banner a#cookie-dismiss{margin:0.5em 0;padding:3px 9px;-moz-border-radius:5px;-khtml-border-radius:5px;-webkit-border-radius:5px;border-radius:5px;font-size:108%;border-width:1px;box-shadow:0px 2px 2px #adadad;-moz-box-shadow:0px 2px 2px #adadad;-khtml-box-shadow:0px 2px 2px #adadad;-webkit-box-shadow:0px 2px 2px #adadad;width:auto;padding-top:0px;padding-bottom:0px;border-color:#295c5c;background-color:#207a7a;background-image:-moz-linear-gradient(top, #54bdbd, #207a7a);background-image:-webkit-gradient(linear,left top,left bottom,color-stop(0, #54bdbd),color-stop(1, #207a7a));background-image:-webkit-linear-gradient(#54bdbd, #207a7a);background-image:linear-gradient(top, #54bdbd, #207a7a);filter:progid:DXImageTransform.Microsoft.gradient(startColorStr='#54bdbd', EndColorStr='#207a7a');color:#f8f8f8;text-shadow:#145251 0 1px 1px;display:inline;text-decoration:none;}
             </style>-->

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
                                       value="${searchModel.searchparams.text}"/>
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
                            <!--                        <div class="filter" style="width: 25%; float: left;">                    -->
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


                                <div class="sublevel1"> 


                                    <div class="subTitle">
                                        Species (<c:out value="${fn:length(searchFilter.species)}"></c:out>) 
                                        </div>  
                                    <c:if test="${fn:length(searchFilter.species) gt 12}">           
                                        <!--auto-complete search box-->
                                        <div class="ui-widget grid_12zzz">

                                            <input id="specieAT" itemtype="text"   class="filterSearchBox" placeholder="Enter Species to filter" />

                                            <form:checkbox path="searchparams.species" id="_ctempList_selected" value='' type="hidden"></form:checkbox>


                                            </div> 
                                    </c:if>
                                    <div class="filterContent">
                                        <c:set var="speciesList" value="${searchFilter.species}"/>
                                        <c:set var="speciesListSize" value="${fn:length(speciesList)}"/>
                                        <c:set var="limitedDisplay" value="${filterSizeDefault}"/>

                                        <!--to ensure that the specie list is not empty-->
                                        <c:if test="${speciesListSize > 0}">


                                            <!--  keep track of checked and displayed items.-->

                                            <c:set var="checkedItems" value="0"/> 
                                            <c:set var="displayedItems" value="0"/> 


                                            <c:forEach var="times" begin="1" end="2" step="1">                                                  


                                                <c:forEach var="i" begin="0" end="${speciesListSize}">
                                                    <c:if test='${not empty speciesList[i].scientificname}'>
                                                        <c:if test='${(speciesList[i].selected eq true and (times == 1)) or (speciesList[i].selected eq false and (times == 2))}'>                                                       

                                                            <c:if test='${(speciesList[i].selected eq true and (times == 1))}'>
                                                                <c:set var="checkedItems" value="${checkedItems + 1}"/>
                                                            </c:if> 


                                                            <c:if test='${displayedItems == limitedDisplay}'>
                                                                <div id="species_0" style="display: none">
                                                                </c:if> 

                                                                <div class="filterLine">
                                                                    <div class="text">
                                                                         
                                                                        <c:if test="${Fn:alphaOmegaIsNotNull(speciesList[i].scientificname, speciesList[i].commonname)}">
                                                                             <script>
                                                                                    speciesAutocompleteDataSource.push("${speciesList[i].scientificname}");
                                                                           </script>
                                                                         </c:if>
                                                                         

                                                                        <c:choose>
                                                                           
                                                                            <c:when test="${Fn:omegaIsNull(speciesList[i].scientificname, speciesList[i].commonname)}">
                                                                                <span class="scienceName" style="border-bottom-style:none " >${speciesList[i].scientificname}</span>
                                                                                <script>
                                                                                    speciesAutocompleteDataSource.push("${speciesList[i].scientificname}");
                                                                                </script>
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <span class="scienceName" style="border-bottom-style:none ">${speciesList[i].commonname} <span>[${speciesList[i].scientificname}]</span></span>
                                                                                <script>
           
                                                                                    speciesAutocompleteDataSource.push({label:"${speciesList[i].commonname}",value:"${speciesList[i].scientificname}"});
                                                                                </script>
                                                                                  <script>
                                                                                    speciesAutocompleteDataSource.push("${speciesList[i].scientificname}");
                                                                                </script>

                                                                                
                                                                            </c:otherwise>
                                                                        </c:choose> 

                                                                    </div>

                                                                    <div class="checkItem">
                                                                        <form:checkbox path="searchparams.species" onclick="submit()"  value="${speciesList[i].scientificname}" />

                                                                    </div>
                                                                    <div class="clear"></div>

                                                                </div>

                                                                <c:set var="displayedItems" value="${displayedItems + 1}"/>

                                                            </c:if>

                                                        </c:if>


                                                    </c:forEach>
                                                    <c:if test='${checkedItems gt 0 and checkedItems < speciesListSize}'><hr class="hrLine" /></c:if>


                                                </c:forEach> 


                                                <c:set var="speciesMoreSize" value="${speciesListSize - limitedDisplay}"/>


                                                <c:if test='${displayedItems >= limitedDisplay }'>
                                                </div>  
                                                <a class="showLink" onclick="" id="<c:out value='species_link_0'/>"><c:out value="See ${speciesMoreSize} more"/></a> <br/>
                                            </c:if> 

                                            <c:if test="${speciesListSize gt 12}">
                                                <!--    then add auto complete-->
                                                <script>
                                                    ResultAutoComplete('specieAT', speciesAutocompleteDataSource,'filtersForm','_ctempList_selected')
                                                </script>

                                            </c:if>

                                        </c:if>
                                    </div>
                                </div>

                                <%-- The Start of Compounds  --%>
                                <BR/>


                                <div class="sublevel1">








                                    <div class="subTitle">
                                        Chemical Compounds (<c:out value="${fn:length(searchFilter.compounds)}"></c:out>)
                                        </div>
                                    <c:if test="${fn:length(searchFilter.compounds) gt 12}"> 
                                        <div class="ui-widget">

                                            <input id="compounds_AUTOCOMPLETE" class="filterSearchBox" placeholder="Type a Compound to filter"  />
                                            <form:checkbox path="searchparams.compounds" id="comptempList" value="" type="hidden" onclick="submit()"></form:checkbox>
                                            </div>     

                                    </c:if>
                                    <div class="filterContent">
                                        <c:set var="compoundList" value="${searchFilter.compounds}"/>
                                        <c:set var="compoundListSize" value="${fn:length(compoundList)}"/>
                                        <c:set var="limitedDisplay" value="${filterSizeDefault}"/>





                                        <c:if test="${compoundListSize > 0}">


                                            <!--  keep track of checked and displayed items.-->

                                            <c:set var="checkedItems" value="0"/> 
                                            <c:set var="displayedItems" value="0"/> 


                                            <c:forEach var="times" begin="1" end="2" step="1">                                                  


                                                <c:forEach var="i" begin="0" end="${compoundListSize}">
                                                    <c:if test='${not empty compoundList[i].name}'>
                                                        <c:if test='${(compoundList[i].selected eq true and (times == 1)) or (compoundList[i].selected eq false and (times == 2))}'>                                                       

                                                            <c:if test='${(compoundList[i].selected eq true and (times == 1))}'>
                                                                <c:set var="checkedItems" value="${checkedItems + 1}"/>
                                                            </c:if> 


                                                            <c:if test='${displayedItems == limitedDisplay}'>
                                                                <div id="compound_0" style="display: none">
                                                                </c:if> 

                                                                <div class="filterLine">
                                                                    <div class="text">
                                                                        <xchars:translate>
                                                                            <span class="compoundName" style="border-bottom-style:none " ><c:out value="${compoundList[i].name}" escapeXml="false"/></span>
                                                                            <script> compoundsAutoCompleteDataSource.push({label:"${compoundList[i].name} ",value:"${compoundList[i].name}"});</script>
                                                                        </xchars:translate>
                                                                    </div>
                                                                    <div class="checkItem">
                                                                        <form:checkbox path="searchparams.compounds" value="${compoundList[i].name}" onclick="submit()"/>
                                                                    </div>
                                                                    <div class="clear"></div>
                                                                </div>

                                                                <c:set var="displayedItems" value="${displayedItems + 1}"/>
                                                            </c:if>

                                                        </c:if>


                                                    </c:forEach>
                                                    <c:if test='${checkedItems gt 0 and checkedItems < compoundListSize}'><hr class="hrLine" /></c:if>


                                                </c:forEach> 



                                                <c:set var="compoundMoreSize" value="${compoundListSize - limitedDisplay}"/>

                                                <c:if test='${displayedItems >= limitedDisplay }'>

                                                </div>  
                                                <a class="showLink" id="<c:out value='compound_link_0'/>"><c:out value="See ${compoundMoreSize} more"/></a> <br/>
                                            </c:if> 

                                            <c:if test="${compoundListSize gt 12}">
                                                <!--    then add auto complete-->
                                                <script>
                                                    ResultAutoComplete('compounds_AUTOCOMPLETE', compoundsAutoCompleteDataSource,'filtersForm','comptempList')
                                                </script>

                                            </c:if>

                                        </c:if>          


                                    </div>
                                </div>
                                <br/> <br/>     

                                <div class="sublevel1" >



                                    <div class="subTitle">
                                        Diseases (<c:out value="${fn:length(searchFilter.diseases)}"></c:out>)
                                        </div>
                                    <c:if test="${fn:length(searchFilter.diseases) gt 12}">                                 
                                        <div class="ui-widget">


                                            <input id="diseases_AUTOCOMPLETE" class="filterSearchBox"  placeholder="Type a Disease to filter" />
                                            <form:checkbox path="searchparams.diseases" id="DtempList" value="" type="hidden" onclick="submit()"></form:checkbox>
                                            </div>                                   

                                    </c:if>                                
                                    <div class="filterContent">
                                        <c:set var="diseasesList" value="${searchFilter.diseases}"/>
                                        <c:set var="diseasesListSize" value="${fn:length(diseasesList)}"/>
                                        <c:set var="limitedDisplay" value="${filterSizeDefault}"/>




                                        <c:if test="${diseasesListSize > 0}">


                                            <!--  keep track of checked and displayed items.-->

                                            <c:set var="checkedItems" value="0"/> 
                                            <c:set var="displayedItems" value="0"/> 


                                            <c:forEach var="times" begin="1" end="2" step="1">                                                  


                                                <c:forEach var="i" begin="0" end="${diseasesListSize}">
                                                    <c:if test='${not empty diseasesList[i].name}'>
                                                        <c:if test='${(diseasesList[i].selected eq true and (times == 1)) or (diseasesList[i].selected eq false and (times == 2))}'>                                                       

                                                            <c:if test='${(diseasesList[i].selected eq true and (times == 1))}'>
                                                                <c:set var="checkedItems" value="${checkedItems + 1}"/>
                                                            </c:if> 


                                                            <c:if test='${displayedItems == limitedDisplay}'>
                                                                <div id="diseases_0" style="display: none">
                                                                </c:if> 



                                                                <div class="filterLine">
                                                                    <div class="checkItem">
                                                                        <form:checkbox path="searchparams.diseases" value="${diseasesList[i].name}" onclick="submit()"/>
                                                                    </div>
                                                                    <div class="text">
                                                                        <xchars:translate>
                                                                            <span class="diseaseName" style="border-bottom-style:none "> <c:out value="${diseasesList[i].name}" escapeXml="false"/></span>
                                                                            <script>diseaseAutoCompleteDataSource.push({label:"${diseasesList[i].name} ",value:"${diseasesList[i].name}"});</script>
                                                                        </xchars:translate>
                                                                    </div>                                                  



                                                                    <div class="clear"></div>
                                                                </div>

                                                                <c:set var="displayedItems" value="${displayedItems + 1}"/>
                                                            </c:if>

                                                        </c:if>


                                                    </c:forEach>
                                                    <c:if test='${checkedItems gt 0 and checkedItems < diseasesListSize}'><hr class="hrLine" /></c:if>


                                                </c:forEach> 

                                                <c:set var="diseasesMoreSize" value="${diseasesListSize - limitedDisplay}"/>

                                                <c:if test='${displayedItems >= limitedDisplay }'>

                                                </div>  
                                                <a class="showLink" id="<c:out value='diseases_link_0'/>"><c:out value="See ${diseasesMoreSize} more"/></a> <br/>
                                            </c:if> 

                                            <c:if test="${diseasesListSize gt 12}">
                                                <!--    then add auto complete-->
                                                <script>
                                                    ResultAutoComplete('diseases_AUTOCOMPLETE', diseaseAutoCompleteDataSource,'filtersForm','DtempList')
                                                </script>

                                            </c:if>

                                        </c:if>  

                                    </div>
                                </div>



                                <%-- The end of disease --%>





                            </form:form>
                        </div> 
                        <%--filter --%>
                    </c:if>
                </section>
                <section class="grid_18" id="keywordSearchResult">
                    <!--                    <div id="keywordSearchResult" class="result"
                                             style="width: 70%; float: left;">-->
                    <c:if test="${totalfound eq -100}">
                        <spring:message code="label.search.empty"/>
                    </c:if>
                    <c:if test="${summaryEntriesSize == 0 and ( speciesListSize gt 0 or compoundListSize gt 0 or diseasesListSize gt 0)}">
                        <div class="resultItem">
                            <a href="#" ><span class="displayMsg" style="font-size:small;text-align:center " > No Result was found for this selection.</span></a> 
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

                                    <%@include file="util/prioritiseSpecies.jsp" %>

                                    <c:set var="primAcc" value="${theSpecies.uniprotaccessions[0]}"/>

                                    <div class="resultItem">

                                        <c:choose>
                                            <c:when test="${empty enzyme.relatedspecies}">

                                                <a href="#" ><span class="displayMsg" style="font-size:small;text-align:center " > No Result was found for this Selection.</span></a> 

                                            </c:when>
                                            <c:otherwise>
                                                <div class="proteinImg">
                                                    <c:set var="imgFile" value='${theSpecies.pdbeaccession[0]}'/>
                                                    <c:set var="imgFooter" value=""/>
                                                    <c:if test="${empty imgFile}">
                                                        <c:forEach var="relSp" items="${enzyme.relatedspecies}">
                                                            <c:if test="${empty imgFile and not empty relSp.pdbeaccession}">
                                                                <c:set var="imgFile" value="${relSp.pdbeaccession[0]}"/>
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
                                                                 background-color: #fff;
                                                                 opacity: 0.6; vertical-align: middle;
                                                                 margin-top: 0px; padding: 0px;">No structure available</div>
                                                            <img src="resources/images/noStructure-light-small.png"
                                                                 width="120" height="90" style="border-radius: 10px;"
                                                                 alt="No structure available"
                                                                 title="No structure available"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:set var="imgLink"
                                                                   value="http://www.ebi.ac.uk/pdbe-srv/view/images/entry/${fn:toLowerCase(imgFile)}_cbc120.png"/>
                                                            <a target="blank" href="${imgLink}">
                                                                <img src="${imgLink}" width="120" height="90"
                                                                     alt="PDB ${imgFile}" onerror="noImage(this);"/>
                                                            </a>
                                                            <div class="imgFooter">${imgFooter}</div>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <c:if test='${imgFile != "" && imgFile != null}'>
                                                    </c:if>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                        <div class="desc">
                                            <c:if test='${not empty enzyme.name }'>
                                                <a href="search/${primAcc}/enzyme">
                                                    <c:set var="showName" value="${fn:substring(enzyme.name, 0, 100)}"/>
                                                    <c:out value="${showName}"/>
                                                   <!-- [<c:out value="${enzyme.uniprotid}"/>]-->
                                                    [${empty theSpecies.species.commonname?
                                                       theSpecies.species.scientificname :
                                                       theSpecies.species.commonname}]
                                                </a>


                                        </c:if>

                                        <c:if test="${not empty enzyme.function}">
                                            <div>
                                                <b>Function</b>:
                                                <c:choose>
                                                    <c:when test="${fn:length(fn:split(enzyme.function, ' ')) gt textMaxLength}">
                                                        <c:forEach var="word" items="${fn:split(enzyme.function,' ')}"
                                                                   begin="0" end="${textMaxLength-1}">
                                                            ${word}</c:forEach>
                                                        <span id="fun_${resultItemId}" style="display: none">
                                                            <c:forEach var="word" items="${fn:split(enzyme.function,' ')}"
                                                                       begin="${textMaxLength}">
                                                                ${word}</c:forEach>
                                                            </span>
                                                            <a class="showLink" id="fun_link_${resultItemId}">... Show more about function</a>
                                                    </c:when>
                                                    <c:otherwise>
                                                        ${enzyme.function}
                                                    </c:otherwise>
                                                </c:choose>
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


                                        <!-- disease begins here-->
                                        <c:set var="enzymeDisease" value="${enzyme.diseases}"/>
                                        <c:set var="enzymeDiseaseSize" value="${fn:length(enzymeDisease)}"/>
                                        <c:set var="disLimitedDisplayDefault" value="${5}"/>
                                        <c:set var="disLimitedDisplay" value="${disLimitedDisplayDefault}"/>

                                        <div id="enzymeDisease">
                                            <c:if test="${ enzymeDiseaseSize >0}">
                                                <b>Disease :</b> 
                                                <c:if test="${enzymeDiseaseSize > 0 && enzymeDiseaseSize <= disLimitedDisplay}">
                                                    <c:set var="disLimitedDisplay" value="${enzymeDiseaseSize}"/>
                                                </c:if>

                                                <c:set var="hiddenDis" value=""/> 
                                                <c:forEach var="i" begin="0" end="${disLimitedDisplay-1}">
                                                    <xchars:translate>
                                                        <span class="resultPageDisease"  style="border-bottom-style:none" >${enzymeDisease[i].name}</span>;
                                                    </xchars:translate>

                                                </c:forEach> 


                                                <c:if test="${enzymeDiseaseSize>disLimitedDisplay}">
                                                    <span id='dis_${resultItemId}' style="display: none">
                                                        <c:forEach var="i" begin="${disLimitedDisplay}" end="${enzymeDiseaseSize-1}">
                                                            <xchars:translate>
                                                                <span  class="resultPageDisease" style="border-bottom-style:none">${enzymeDisease[i].name}</span>;
                                                            </xchars:translate>
                                                        </c:forEach>
                                                    </span>
                                                    <a class="showLink" id="<c:out value='dis_link_${resultItemId}'/>">Show more diseases</a>
                                                </c:if>
                                            </c:if>     

                                        </div>
                                        <!--                                                disease ends here-->   

                                        <div>
                                            <!-- div id="in">in</div -->
                                            <div>


                                                <!--display = 3 = 2 related species + 1 default species -->
                                                <c:set var="relSpeciesMaxDisplay" value="${5}"/>
                                                <c:set var="relspecies" value="${enzyme.relatedspecies}"/>                                        
                                                <c:set var="relSpeciesSize" value="${fn:length(relspecies)}"/>
                                                <c:if test="${relSpeciesSize gt 0}">
                                                    <b>Species:</b>
                                                    <c:if test="${relSpeciesSize <= relSpeciesMaxDisplay}">
                                                        <c:set var="relSpeciesMaxDisplay" value="${relSpeciesSize}"/>
                                                    </c:if>
                                                    <c:forEach var="i" begin="0" end="${relSpeciesMaxDisplay-1}">
                                                        <!-- c:if test="${relspecies[i].species.scientificname ne enzyme.species.scientificname}" -->
<!--                                                                <a href="search/${relspecies[i].uniprotaccessions[0]}/enzyme">
                                                                [${empty relspecies[i].species.commonname?
                                                           relspecies[i].species.scientificname :
                                                           relspecies[i].species.commonname}]-->
                                                        <!-- ${relspecies[i].pdbeaccession} -->




                                                        <c:choose>
                                                            <c:when test="${empty relspecies[i].species.commonname}">
                                                                [<a class="popup" href='search/${relspecies[i].uniprotaccessions[0]}/enzyme'
                                                                    >${relspecies[i].species.scientificname}<span>${relspecies[i].species.scientificname}</span></a
                                                                ><c:if test="${not empty relspecies[i].scoring}">
                                                                    <span class="score S${
                                                                          relspecies[i].scoring.bitScore gt 1000? '10':
                                                                              fn:substringBefore(relspecies[i].scoring.bitScore/100, '.')}"
                                                                          title="Bit score for BLAST search. E-value = ${relspecies[i].scoring.evalue}"
                                                                          >${relspecies[i].scoring.bitScore}</span></c:if
                                                                >]
                                                            </c:when>
                                                            <c:otherwise>
                                                                [<a class="popup" href='search/${relspecies[i].uniprotaccessions[0]}/enzyme'
                                                                    >${relspecies[i].species.commonname}<span>${relspecies[i].species.scientificname}</span></a
                                                                ><c:if test="${not empty relspecies[i].scoring}">
                                                                    <span class="score S${
                                                                          relspecies[i].scoring.bitScore gt 1000? '10':
                                                                              fn:substringBefore(relspecies[i].scoring.bitScore/100, '.')}"
                                                                          title="Bit score for BLAST search. E-value = ${relspecies[i].scoring.evalue}"
                                                                          >${relspecies[i].scoring.bitScore}</span></c:if
                                                                >]
                                                            </c:otherwise>
                                                        </c:choose>                                                            <!--                                                                </a>-->
                                                        <!-- /c:if -->                                             
                                                    </c:forEach>
                                                    <c:if test="${relSpeciesSize > relSpeciesMaxDisplay}">
                                                        <span id="relSpecies_${resultItemId}" style="display: none">
                                                            <c:forEach var = "i" begin="${relSpeciesMaxDisplay}" end="${relSpeciesSize-1}">                                                
<!--                                                                        <a href="search/${relspecies[i].uniprotaccessions[0]}/enzyme">
                                                                        [${empty relspecies[i].species.commonname?
                                                                   relspecies[i].species.scientificname :
                                                                   relspecies[i].species.commonname}]-->
                                                                <!-- ${relspecies[i].pdbeaccession} -->


                                                                <c:choose>
                                                                    <c:when test="${empty relspecies[i].species.commonname}">
                                                                        [<a class="popup" href='search/${relspecies[i].uniprotaccessions[0]}/enzyme'
                                                                            >${relspecies[i].species.scientificname}<span>${relspecies[i].species.scientificname}</span></a
                                                                        ><c:if test="${not empty relspecies[i].scoring}">
                                                                            <span class="score S${
                                                                                  relspecies[i].scoring.bitScore gt 1000? '10':
                                                                                      fn:substringBefore(relspecies[i].scoring.bitScore/100, '.')}"
                                                                                  title="Bit score for BLAST search. E-value = ${relspecies[i].scoring.evalue}"
                                                                                  >${relspecies[i].scoring.bitScore}</span></c:if
                                                                        >]
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        [<a class="popup" href='search/${relspecies[i].uniprotaccessions[0]}/enzyme'
                                                                            >${relspecies[i].species.commonname}<span>${relspecies[i].species.scientificname}</span></a
                                                                        ><c:if test="${not empty relspecies[i].scoring}">
                                                                            <span class="score S${
                                                                                  relspecies[i].scoring.bitScore gt 1000? '10':
                                                                                      fn:substringBefore(relspecies[i].scoring.bitScore/100, '.')}"
                                                                                  title="Bit score for BLAST search. E-value = ${relspecies[i].scoring.evalue}"
                                                                                  >${relspecies[i].scoring.bitScore}</span></c:if
                                                                        >]
                                                                    </c:otherwise>
                                                                </c:choose>

                                                                <!--                                                                        </a>-->
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
                        </div><!-- resultContent -->

                    </section>
                    <!--                    </div> keywordSearchResult -->
                    <!--                </div>-->
                    <!--                                           grid_12 content -->
                    <!--            </div>  page container_12 -->
                    <!--                    </div>-->
                    <!--
                    <! --------------------------------
                        GLOBAL SEARCH TEMPLATE - START
                       -------------------------------- >-->

<!--
                    
                                    
                                    <script type="text/javascript">
                                            function renderMenu(elem, items, baseURL) {
                                            var ul = $("<ul>").appendTo(elem);
                                            $.each(items, function(index, item) {
                                                renderItem(ul, item, baseURL);
                                            });
                                        }
                                            
                                        function renderItem(ul, item, baseURL) {
                                            $( "<li>" )
                                                    .append( $( "<a>" ).attr("href", baseURL+item.url)
                                                                                       .text( item.name + " ("+item.numberOfResults+")" )
                                                    ).appendTo( ul );
                                        }
                    
                                            function updateSummary() {
                                                    var query = $("#local-searchbox").val();
                                                    if (query) {
                                                            var searchBaseURL = "http://frontier.ebi.ac.uk/ebisearch/";
                                                            var thisElem = $.find("#search-extras");
                                                            $(thisElem).text("Loading other results");
                                                            $(thisElem).addClass("loading");
                    
                                                            $.ajax({
                                                              searchBaseURL: searchBaseURL, 
                                                              url: searchBaseURL+"globalsearchsummary.ebi?query="+query,
                                                              context: thisElem,
                                                              dataType: "json",
                                                              crossdoamin: true,
                                                              error: function(request, error) {
                                                                      //console.log(arguments);
                                                                      //alert("error occurred: "+error);
                                                              },
                                                              success: function (data, textStatus, jqHXR) {
                                                                      //alert("success");
                                                              }
                                                            }
                                                            
                                                            
                                                            ).done(function( response ) {
                                                              var obj = response;
                                                              $(this).text("");
                                                              $(this).removeClass("loading");
                                                              $(this).append("<p>EBI global search results</p>");
                                                              //$(this).blink({delay: 500});
                                                              $("#page-title").text('Search results for ').append($("<span>").attr("class", "searchterm").text(query));
                                                              renderMenu(this, obj, searchBaseURL);
                                                            });
                                                    }
                                            }
                                            
                                            
                     
                          
                        
                          
                        </script>-->
<!--                     Related results - end -->

                    <!--<! ------------------------------
                    GLOBAL SEARCH TEMPLATE - END
                   ------------------------------ >-->
                    <!--		
                                    <section class="grid_4 omega" id="search-extras">
                                    <p>EBI global search results</p>
                                    </section>-->

                    <!--<aside class="grid_6 omega shortcuts expander" id="search-extras">	    	
                                    <div id="ebi_search_results"><h3 class="slideToggle icon icon-functional" data-icon="u">Show more data from EMBL-EBI</h3>
                                    </div>
                                    </aside>-->





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
                    $("#redline_side_car").css("display", "block");
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


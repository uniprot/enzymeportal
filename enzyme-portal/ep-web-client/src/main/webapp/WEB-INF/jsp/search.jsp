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

        <title>Enzyme Portal &lt; Search Result Page &gt; &lt; EMBL-EBI</title>
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


       

        <link rel="stylesheet" href="resources/css/boilerplate-style.css"> 

        <link rel="stylesheet" href="resources/css/ebi-global.css" type="text/css" media="screen" />

        <link rel="stylesheet" href="resources/css/ebi-visual.css" type="text/css" media="screen">
        <link rel="stylesheet" href="resources/css/984-24-col-fluid.css" type="text/css" media="screen" />
        <link href="resources/css/search.css" type="text/css" rel="stylesheet" />
        <!--           <link rel="stylesheet" href="resources/css/enzyme-portal-colours.css" type="text/css" media="screen" />-->
        <link rel="stylesheet" href="resources/css/embl-petrol-colours.css" type="text/css" media="screen" />
         

        <!--        javascript was placed here for auto complete otherwise should be place at the bottom for faster page loading-->

        <script src="resources/lib/spineconcept/javascript/jquery-1.5.1.min.js" type="text/javascript"></script>
        <script src="resources/lib/spineconcept/javascript/identification.js" type="text/javascript"></script>
        <script src="resources/javascript/search.js" type="text/javascript"></script>


        <link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" rel="stylesheet" type="text/css"/>
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
        <script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js"></script>
        <script src="http://yui.yahooapis.com/3.4.1/build/yui/yui-min.js"></script>

        <!-- end CSS-->


        <!-- All JavaScript at the bottom, except for Modernizr / Respond.
             Modernizr enables HTML5 elements & feature detects; Respond is a polyfill for min/max-width CSS3 Media Queries
             For optimal performance, use a custom Modernizr build: www.modernizr.com/download/ -->

        <!-- Full build -->
        <!-- <script src="../js/libs/modernizr.minified.2.1.6.js"></script> -->

        <!-- custom build (lacks most of the "advanced" HTML5 support -->
        <script src="http://wwwdev.ebi.ac.uk/web_guidelines/js/libs/modernizr.custom.49274.js"></script>
        <!--  <script src="/web_guidelines/js/libs/modernizr.custom.49274.js"></script>		-->


    </head>

    <body class="level2">

        <div id="skip-to">
            <ul>
                <li><a href="#content" title="">Skip to main content</a></li>
                <li><a href="#local-nav" title="">Skip to local navigation</a></li>
                <li><a href="#global-nav" title="">Skip to EBI global navigation menu</a></li>
                <li><a href="#global-nav-expanded" title="">Skip to expanded EBI global navigation menu (includes all sub-sections)</a></li>
            </ul>
        </div>

        <div id="wrapper" class="container_24">
            <header>
                <div id="global-masthead" class="masthead grid_24">
                    <p><!-- EMBL-EBI  --><img src="http://frontier.ebi.ac.uk/sites/ebi.ac.uk/themes/custom/ebiomega/logo.png" alt="European Bioinformatics Institute"></p>

                    <nav>
                        <ul id="global-nav">
                            <li class="first" id="services"><a href="#" title="">Services</a></li>
                            <li id="research"><a href="#" title="">Research</a></li>
                            <li id="training"><a href="#" title="">Training</a></li>
                            <li id="funding"><a href="#" title="">Funding</a></li>
                            <li id="about" class="last"><a href="#" title="">About us</a></li>
                        </ul>
                    </nav>

                </div>

                <div id="local-masthead" class="masthead grid_24 nomenu">

                    <!-- CHOOSE -->

                    <div class="grid_12 alpha" id="local-title-logo">
                            <h1>Enzyme Portal</h1>
                            <p><img src="resources/images/EnzymePortal_v2.png" alt="Enzyme Portal" width="400" height="72" class="logo" /></p>
                    </div>

                    <!-- OR... -->

<!--                    <div class="grid_12 alpha" id="local-title">
                        <h1>Enzyme Portal</h1>
                    </div>-->
                    <!-- -->

                    <div class="grid_12 omega">

                        <!--                        <form id="local-search" name="local-search" action="#" method="post">
                        
                                                    <fieldset>
                        
                                                        <label>
                                                            <input type="text" name="first" id="local-searchbox" />
                                                        </label>	
                        
                                                        <input type="submit" name="submit" value="Search" class="submit" />	
                                                    </fieldset>
                        
                                                </form>-->
                        
                                                  <c:choose>
 	                  <c:when test="${searchModel.searchparams.type eq 'SEQUENCE'}">	
                   <c:set var="searchText" value="${searchModel.searchparams.sequence}"/>	
                 </c:when>
	
                 <c:otherwise>
	                   <c:set var="searchText"
	                     value="${searchModel.searchparams.text}"/>
	                 </c:otherwise>
	               </c:choose>

                        <form:form id="local-search" modelAttribute="searchModel"
                                   action="search" method="POST">
                            <fieldset class="grid_24"> 
                                <label>     
                                    <form:input id="local-searchbox" path="searchparams.text"
                                                cssClass="field" name="first" rel="Enter a name to search"/>
                                </label>
                                <form:hidden id="start" path="searchparams.start" />
                                <form:hidden path="searchparams.previoustext" />
                                <input  type="submit" value="Search"
                                        class="submit" /><br/>
                                <div>
                                    <!--                            <div id="examples">Examples: <a href="search?searchparams.previoustext=&searchparams.start=0&searchparams.text=sildenafil">sildenafil</a> -->
                                    <spring:message code="label.search.example"/>
                                    <a href="search?searchparams.previoustext=&searchparams.start=0&searchparams.text=sildenafil">sildenafil</a>,
                                    <a href="search?searchparams.previoustext=&searchparams.start=0&searchparams.text=Insulin+receptor">Insulin receptor</a>,
                                    <!--                <a href="search?searchparams.previoustext=&searchparams.start=0&searchparams.text=Ceramide+glucosyltransferase">Ceramide glucosyltransferase</a>,
                                                    <a href="search?searchparams.previoustext=&searchparams.start=0&searchparams.text=Phenylalanine-4-hydroxylase">Phenylalanine-4-hydroxylase</a>,
                                                    <a href="search?searchparams.previoustext=&searchparams.start=0&searchparams.text=Cytochrome+P450+3A4">Cytochrome P450 3A4</a>,-->
                                    <a href="search?searchparams.previoustext=&searchparams.start=0&searchparams.text=CFTR">CFTR</a>,
                                    <a href="search?searchparams.previoustext=&searchparams.start=0&searchparams.text=Q13423">Q13423</a>,
                                    <a href="search?searchparams.previoustext=&searchparams.start=0&searchparams.text=REACT_1400.4">REACT_1400.4</a>

                                    <!--                            </div>-->
                                </div>
                            </fieldset>
                        </form:form>



                        <!--                        <form id="local-search" name="local-search" action="#" method="post">
                        
                                                    <fieldset>
                        
                                                        <label>
                                                            <input type="text" name="first" id="local-searchbox" />
                                                        </label>	
                        
                                                        <input type="submit" name="submit" value="Search" class="submit" />	
                                                    </fieldset>
                        
                                                </form>-->
                    </div>

                    <nav>
                        <ul class="grid_24" id="local-nav">
                            <li class="first" class="active"><a href="/enzymeportal" title="">Home</a></li>
                            <!--                            <li><a href="#" title="">TODO 2</a></li>
                                                        <li class="active"><a href="#" title="">TODO 3</a></li>-->
                            <li><a href="faq" title="Frequently Asked questions">FAQ</a></li>
                            <li class="last"><a href="about" title="About Enzyme Portal">About Enzyme Portal</a></li>
                        </ul>
                    </nav>	
                </div>
            </header>
                          
<!--    <div id="content" role="main" class="grid_24 clearfix">
    	
    	<h2>[Page title]</h2>
     Suggested layout containers 
	   
	   <section class="grid_14 push_6" id="search-results">
    		<p>Search results (primary content)</p>
    		<ul>
	    		<li><a href="#">text</a></li>
	    		<li><a href="#">text</a></li>
	    		<li><a href="#">text</a></li>
    		</ul>									
		</section> 
		
		<section class="grid_6 pull_14 alpha" id="search-filters">
			<p>Data facets, search filters, etc</p>
    		<ul>
	    		<li><a href="#">text</a></li>
	    		<li><a href="#">text</a></li>
	    		<li><a href="#">text</a></li>
    		</ul>
		</section>
		
		<section class="grid_4 omega" id="search-extras">
    		<p>EBI global search results</p>
    		<ul>
	    		<li><a href="#">text</a></li>
	    		<li><a href="#">text</a></li>
	    		<li><a href="#">text</a></li>
    		</ul>
		</section>
		 End suggested layout containers 
			
    </div>-->

            <div id="content" role="main" class="grid_24 clearfix">

                <!-- Suggested layout containers -->  
                <section>
                    <div class="grid_12zzz" style="display: table; margin-left: 1em;">
                        <%@ include file="breadcrumbs.jsp" %>
                    </div>
                    <div style="display: table-cell;">
                        <h5 ><a href="/enzymeportal" >Enzyme Portal</a></h5>
                    </div>
                    <div style="display: table-cell;">
                        - Your portal to enzyme-related information at the EBI.
                    </div>

                </section>
<!--                <section>-->


<section class="grid_6" id="search-results">
     
<!--
                    <div class="contents">
                        <div class="page container_12">            
            <div class="page container_12">            -->
              
                <!--Global variables-->
                <c:set var="showButton" value="Show more"/>
                <c:set var="searchText" value="${searchModel.searchparams.text}"/>
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

<!--                <div class="grid_12 content">-->
                    <c:if test="${ searchresults.totalfound gt 0}">
                        <div class="filter">
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
                                        <div class="ui-widget">

                                            <input id="specieAT" itemtype="text"   class="filterSearchBox" placeholder="Search here" />

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
                                                                        <c:choose>
                                                                            <c:when test="${empty speciesList[i].commonname}">
                                                                                <a href='#'>${speciesList[i].scientificname}</a>
                                                                                <script>
                                                                            speciesAutocompleteDataSource.push("${speciesList[i].scientificname}");
                                                                                </script>
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <a class="scienceName" href='#'>${speciesList[i].commonname} <span>[${speciesList[i].scientificname}]</span></a>
                                                                                <script>
           
                                                                            speciesAutocompleteDataSource.push({label:"${speciesList[i].commonname}",value:"${speciesList[i].scientificname}"});
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

                                            <input id="compounds_AUTOCOMPLETE" class="filterSearchBox" placeholder="Search here"  />
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
                                                                            <a class="compoundName" href='#'><c:out value="${compoundList[i].name}" escapeXml="false"/></a>
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


                                            <input id="diseases_AUTOCOMPLETE" class="filterSearchBox"  placeholder="Search here" />
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
                                                                    <div class="text">
                                                                        <xchars:translate>
                                                                            <a class="diseaseName" href="#"> <c:out value="${diseasesList[i].name}" escapeXml="false"/></a>
                                                                            <script>diseaseAutoCompleteDataSource.push({label:"${diseasesList[i].name} ",value:"${diseasesList[i].name}"});</script>
                                                                        </xchars:translate>
                                                                    </div>                                                  


                                                                    <div class="checkItem">
                                                                        <form:checkbox path="searchparams.diseases" value="${diseasesList[i].name}" onclick="submit()"/>
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
               <section class="grid_14" id="keywordSearchResult">
<!--                    <div id="keywordSearchResult" class="result"
                         style="width: 70%; float: left;">-->
                        <c:if test="${totalfound eq 0}">
                            <spring:message code="label.search.empty"/>
                        </c:if>
                        <c:if test="${summaryEntriesSize == 0}">
                            <div class="resultItem">
                                <a href="#" ><span class="displayMsg" style="font-size:small;text-align:center " > No Result was found for this Selection.</span></a> 
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
                                                <div class="grid_3 ">
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
                                                                 background-color: #fff;text-align: center;
                                                                 opacity: 0.6; vertical-align: middle;
                                                                 margin-top: 0px; padding: 0px;">No structure available</div>
                                                            <img src="resources/images/noStructure-light.png"
                                                                 width="110" height="90" style="border-radius: 10px;"
                                                                 alt="No structure available"
                                                                 title="No structure available"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:set var="imgLink"
                                                                   value="http://www.ebi.ac.uk/pdbe-srv/view/images/entry/${imgFile}_cbc600.png"/>
                                                            <a class="noLine" target="blank" href="${imgLink}">
                                                                <img src="${imgLink}" width="110" height="90"
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
                                        <div class="grid_21 desc">
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
                                                            <a href="#" ><span>${enzymeDisease[i].name}</span></a>;
                                                        </xchars:translate>

                                                    </c:forEach> 


                                                    <c:if test="${enzymeDiseaseSize>disLimitedDisplay}">
                                                        <span id='dis_${resultItemId}' style="display: none">
                                                            <c:forEach var="i" begin="${disLimitedDisplay}" end="${enzymeDiseaseSize-1}">
                                                                <xchars:translate>
                                                                    <a href="#" ><span>${enzymeDisease[i].name}</span></a>;
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
                                                                    <a class="popup" href='search/${relspecies[i].uniprotaccessions[0]}/enzyme'>[${relspecies[i].species.scientificname}]<span>${relspecies[i].species.scientificname}</span></a>

                                                                </c:when>
                                                                <c:otherwise>
                                                                    <a class="popup" href='search/${relspecies[i].uniprotaccessions[0]}/enzyme'>[${relspecies[i].species.commonname}]<span>${relspecies[i].species.scientificname}</span></a>

                                                                </c:otherwise>
                                                            </c:choose>
                                                            <!--                                                                </a>-->
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
                                                                            <a class="popup" href='search/${relspecies[i].uniprotaccessions[0]}/enzyme'>[${relspecies[i].species.scientificname}]<span>${relspecies[i].species.scientificname}</span></a>

                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <a class="popup" href='search/${relspecies[i].uniprotaccessions[0]}/enzyme'>[${relspecies[i].species.commonname}]<span>${relspecies[i].species.scientificname}</span></a>

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
                        </c:if>
                 </section>
<!--                    </div> keywordSearchResult -->
<!--                </div>-->
                <!--                                           grid_12 content -->
<!--            </div>  page container_12 -->
<!--                    </div>-->
	<section class="grid_4 omega" id="search-extras">
    		<p>EBI global search results</p>
    		<ul>
	    		<li><a href="#">text</a></li>
	    		<li><a href="#">text</a></li>
	    		<li><a href="#">text</a></li>
    		</ul>
		</section>

<!--                </section>-->
                
                <!--                <section>
                
                
                                    <h2>Enzyme Portal</h2>
                                    <p>Your content</p>										
                                </section> 
                
                                <section>
                                    <h3>[Another title]</h3>
                                    <p>More content in a full-width container.</p>
                                </section>-->
                <!-- End suggested layout containers -->

            </div>


            <footer>
                <!-- Optional local footer (insert citation / project-specific copyright / etc here -->
                <!--
<div id="local-footer" class="grid_24 clearfix">
                        
                </div>
                -->
                <!-- End optional local footer -->

                <div id="global-footer" class="grid_24 clearfix">

                    <nav id="global-nav-expanded">

                        <div class="grid_4 alpha">
                            <h3 class="explore">Explore</h3>
                        </div>

                        <div class="grid_4">
                            <h3 class="services"><a href="#" title="">Services</a></h3>
                        </div>

                        <div class="grid_4">
                            <h3 class="research"><a href="#" title="">Research</a></h3>
                        </div>

                        <div class="grid_4">
                            <h3 class="training"><a href="#" title="">Training</a></h3>
                        </div>

                        <div class="grid_4">
                            <h3 class="funding"><a href="#" title="">Funding</a></h3>
                        </div>

                        <div class="grid_4 omega">
                            <h3 class="about"><a href="#" title="">About us</a></h3>
                        </div>

                    </nav>
                    <!--			<script src="/web_guidelines/js/foot.js"></script>-->
                    <script src="http://wwwdev.ebi.ac.uk/web_guidelines/js/foot.js"></script>

                </div>

                <div class="grid_24">
                    <section id="ebi-footer-meta">
                        <p> &copy; EMBL-EBI European Bioinformatics Institute | <a href="#" title="">Privacy</a> | <a href="#" title="">Cookies</a> | <a href="#" title="">Terms of use</a></p>
                    </section>
                </div>

            </footer>
        </div> <!--! end of #wrapper -->


        <!-- JavaScript at the bottom for fast page loading -->




        <!-- Grab Google CDN's jQuery, with a protocol relative URL; fall back to local if offline -->

        <!--      <script src="//ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js"></script>
                <script>window.jQuery || document.write('<script src="../js/libs/jquery-1.6.2.min.js"><\/script>')</script>-->



        <!-- scripts concatenated and minified via ant build script-->
        <!--
      <script defer src="../js/plugins.js"></script>
        <script defer src="../js/script.js"></script>
        -->
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
</html>


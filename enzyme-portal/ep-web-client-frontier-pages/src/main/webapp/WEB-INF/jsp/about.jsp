<%-- 
    Document   : about
    Created on : Sep 5, 2012, 2:18:11 PM
    Author     : joseph
--%>



<!doctype html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib  prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

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

        <title>[Page title] &lt; [Section title] &lt; EMBL-EBI</title>
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


<!--                <link media="screen" href="resources/lib/spineconcept/css/960gs-fluid/grid.css" type="text/css" rel="stylesheet" />
                <link media="screen" href="resources/lib/spineconcept/css/common.css" type="text/css" rel="stylesheet" />
                <link media="screen" href="resources/lib/spineconcept/css/identification.css" type="text/css" rel="stylesheet" />
                <link media="screen" href="resources/lib/spineconcept/css/species.css" type="text/css" rel="stylesheet" />
                <script src="resources/lib/spineconcept/javascript/jquery-1.5.1.min.js" type="text/javascript"></script>
                <script src="resources/lib/spineconcept/javascript/identification.js" type="text/javascript"></script>-->

        <link rel="stylesheet" href="resources/css/boilerplate-style.css"> 

        <link rel="stylesheet" href="resources/css/ebi-global.css" type="text/css" media="screen" />

        <link rel="stylesheet" href="resources/css/ebi-visual.css" type="text/css" media="screen">
        <link rel="stylesheet" href="resources/css/984-24-col-fluid.css" type="text/css" media="screen" />
        <!--           <link rel="stylesheet" href="resources/css/enzyme-portal-colours.css" type="text/css" media="screen" />-->
        <link rel="stylesheet" href="resources/css/embl-petrol-colours.css" type="text/css" media="screen" />

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
                    <!--
<div class="grid_12 alpha" id="local-title-logo">
                            <h1>[title]</h1>
                            <p>[logo]<img src="" alt="" width="" height="" class="logo" /></p>
                    </div>
                    -->

                    <!-- OR... -->

                    <div class="grid_12 alpha" id="local-title">
                        <h1>Enzyme Portal</h1>
                    </div>
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

<form:form id="local-search" modelAttribute="searchModel"
                	action="search" method="POST">
                            <fieldset> 
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

            <div id="content" role="main" class="grid_24 clearfix">

                <!-- Suggested layout containers -->  
                <section>
                    <div class="grid_12zzz" style="display: table; margin-left: 1em;">
                        <%@ include file="breadcrumbs.jsp" %>
                    </div>
<!--                                    <div class="grid_12zzz" style="margin-left: 1em;">-->
<!--                                        <div style="display: table-cell;">
                                            <h2 ><a href="/enzymeportal" >Enzyme Portal</a></h2>
                                        </div>
                                        <div style="display: table-cell;">
                                            - Your portal to enzyme-related information at the EBI.
                                        </div>-->
<!--                                    </div>-->
                </section>
                <section>
                   
	<h2>About the Enzyme Portal</h2>
	<p>
		The Enzyme Portal is for people who are interested in
		the biology of enzymes and proteins with enzymatic
		activity.
	</p>

	<p>
		It integrates publicly available
		information about enzymes, such as small-molecule chemistry,
		biochemical pathways and drug compounds. It provides a concise
		summary of information from:
		<ul>
			<li><a href="http://www.uniprot.org/help/uniprotkb">UniProt
						knowledge base</a></li>
			<li><a href="http://www.pdbe.org">Protein Data Bank in
						Europe</a></li>
			<li><a href="http://www.ebi.ac.uk/rhea">Rhea</a>, a database
					of enzyme-catalyzed reactions</li>
			<li><a href="http://www.reactome.org">Reactome</a>, a database
					of biochemical pathways</li>
			<li><a href="http://www.ebi.ac.uk/intenz">IntEnz</a>, a resource
					with enzyme nomenclature information</li>
			<li><a href="http://www.ebi.ac.uk/chebi">ChEBI</a> and
					<a href="https://www.ebi.ac.uk/chembl/">ChEMBL</a>,
					which contain information about small molecule chemistry and
					bioactivity</li>
			<li><a href="http://www.ebi.ac.uk/thornton-srv/databases/CoFactor/">CoFactor</a>
					and <a href="http://www.ebi.ac.uk/thornton-srv/databases/MACiE/">MACiE</a>
					for highly detailed, curated information about cofactors and reaction
					mechanisms.</li>
		</ul>
		The Enzyme Portal brings together lots of diverse
		information about enzymes and displays it in an organised overview.
		It covers a large number of species including the key model
		organisms, and provides a simple way to compare orthologues.
	</p>
	<p>
		Give it a try and 
		<a href="http://www.ebi.ac.uk/support/index.php?query=Enzyme+portal&referrer=http://www.ebi.ac.uk/enzymeportal/">let
		us know</a> what you think!
	</p>
<!--        	                <div style="text-align: right;">
                                    <a href="faq" class="showLink">Frequently asked questions ...</a>
	                </div>-->

	<h5>The Enzyme Portal Team</h5>
	<img alt="The Enzyme Portal team" width="400"
		style="float: left; margin: 2ex 2em;"
		src="resources/images/EP_team_photo_4_web.jpg"/>
	<p>
		The Enzyme Portal is designed and developed at the
			EMBL-European Bioinformatics Institute (<a
			href="http://www.ebi.ac.uk/information">EMBL-EBI</a>)
			in the UK. Part of the <a
			href="http://www.embl.org">European Molecular Biology Laboratory</a>,
			EMBL-EBI is the hub of excellence for bioinformatics in Europe. We
			provide freely available life science data and <a
			href="http://www.ebi.ac.uk">services</a>, and
			perform basic <a href="http://www.ebi.ac.uk/research">research</a>
			in computational biology.
	</p>

	<p>
		The Enzyme Portal was created by the <a
		href="http://www.ebi.ac.uk/steinbeck/">Cheminformatics and Metabolism
		Team</a> at EMBL-EBI, which is led by
		<a href="http://www.ebi.ac.uk/Information/Staff/person_maintx.php?s_person_id=922">Christoph
		Steinbeck</a>. Some of the key contributors are:
		<ul>
		<li><a href="http://www.ebi.ac.uk/Information/Staff/person_maintx.php?s_person_id=265">Paula
			de Matos</a>, Project Coordinator and User Experience
			Analyst</li>
		<li><a href="http://www.ebi.ac.uk/Information/Staff/person_maintx.php?s_person_id=690">Rafael
			Alcántara</a>, Hong Cao and
			<a href="http://www.ebi.ac.uk/Information/Staff/person_maintx.php?s_person_id=1518">Joseph
			Onwubiko</a>, Software Developers</li>
		<li><a href="http://www.ebi.ac.uk/Information/Staff/person_maintx.php?s_person_id=1195">Jenny
			Cham</a>, User Experience Consultant</li>
		</ul>
	</p>

	<p>
		We also have the invaluable support of 
	<ul>
		<li><a href="http://www.ebi.ac.uk/Information/Staff/person_maintx.php?s_person_id=467">Jules
			Jacobsen</a> from the UniProt Team</li>
		<li><a href="http://www.ebi.ac.uk/Information/Staff/person_maintx.php?s_person_id=290">Bijay
			Jassal</a>&nbsp;from the Reactome Team</li>
		<li>Gemma Holliday and Julia Fischer, previously from the Thornton Team</li>
		<li><a href="http://www.ebi.ac.uk/Information/Staff/person_maintx.php?s_person_id=839">Syed
			Asad Rahman</a> from the Thornton Team</li>
		<li><a href="http://www.ebi.ac.uk/Information/Staff/person_maintx.php?s_person_id=793">Mickael
			Goujon</a> and
			<a href="http://www.ebi.ac.uk/Information/Staff/person_maintx.php?s_person_id=1175">Francis
			Rowland</a> from the External Services Team</li>
		<li><a href="http://www.ebi.ac.uk/Information/Staff/person_maintx.php?s_person_id=863">James
			Malone</a> and
			<a href="http://www.ebi.ac.uk/Information/Staff/person_maintx.php?s_person_id=413">Helen
			Parkinson</a></li>
	</ul>
	</p>
	 

                </section>

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
        <script src="resources/lib/spineconcept/javascript/jquery-1.5.1.min.js" type="text/javascript"></script>
        <script src="resources/javascript/search.js" type="text/javascript"></script>
            

        <!-- Grab Google CDN's jQuery, with a protocol relative URL; fall back to local if offline -->
        <!--
      <script src="//ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js"></script>
        <script>window.jQuery || document.write('<script src="../js/libs/jquery-1.6.2.min.js"><\/script>')</script>
        -->


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


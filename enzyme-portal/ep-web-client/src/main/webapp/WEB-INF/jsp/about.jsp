<%-- 
    Document   : about
    Created on : Sep 5, 2012, 2:18:11 PM
    Author     : joseph
--%>

<!doctype html>
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
  <!-- <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"> --> <!-- Not yet implemented -->

  <title>About Us &lt; Enzyme Portal &lt; EMBL-EBI</title>
  <meta name="description" content="EMBL-EBI"><!-- Describe what this page is about -->
  <meta name="keywords" content="bioinformatics, europe, institute"><!-- A few keywords that relate to the content of THIS PAGE (not the whol project) -->
  <meta name="author" content="EMBL-EBI"><!-- Your [project-name] here -->

  <!-- Mobile viewport optimized: j.mp/bplateviewport -->
  <meta name="viewport" content="width=device-width,initial-scale=1">

  <!-- Place favicon.ico and apple-touch-icon.png in the root directory: mathiasbynens.be/notes/touch-icons -->

  <!-- CSS: implied media=all -->
  <!-- CSS concatenated and minified via ant build script-->
<!--  <link rel="stylesheet" href="//www.ebi.ac.uk/web_guidelines/css/compliance/develop/boilerplate-style.css">
  <link rel="stylesheet" href="//www.ebi.ac.uk/web_guidelines/css/compliance/develop/ebi-global.css" type="text/css" media="screen">
  <link rel="stylesheet" href="//www.ebi.ac.uk/web_guidelines/css/compliance/develop/ebi-visual.css" type="text/css" media="screen">
  <link rel="stylesheet" href="//www.ebi.ac.uk/web_guidelines/css/compliance/develop/984-24-col-fluid.css" type="text/css" media="screen">
  -->
  <!-- you can replace this with [projectname]-colours.css. See http://frontier.ebi.ac.uk/web/style/colour for details of how to do this -->
  <!-- also inform ES so we can host your colour palette file -->
  <link rel="stylesheet" href="//www.ebi.ac.uk/web_guidelines/css/compliance/develop/embl-petrol-colours.css" type="text/css" media="screen">
  
  <!-- for production the above can be replaced with -->
  <link href="resources/css/search.css" type="text/css" rel="stylesheet" />
  <link rel="stylesheet" href="//www.ebi.ac.uk/web_guidelines/css/compliance/mini/ebi-fluid-embl.css">
   

  <style type="text/css">
  	/* You have the option of setting a maximum width for your page, and making sure everything is centered */
  	/* body { max-width: 1600px; margin: 0 auto; } */
  </style>
  
  <!-- end CSS-->


  <!-- All JavaScript at the bottom, except for Modernizr / Respond.
       Modernizr enables HTML5 elements & feature detects; Respond is a polyfill for min/max-width CSS3 Media Queries
       For optimal performance, use a custom Modernizr build: www.modernizr.com/download/ -->
  
  <!-- Full build -->
  <!-- <script src="//www.ebi.ac.uk/web_guidelines/js/libs/modernizr.minified.2.1.6.js"></script> -->
  
  <!-- custom build (lacks most of the "advanced" HTML5 support -->
  <script src="//www.ebi.ac.uk/web_guidelines/js/libs/modernizr.custom.49274.js"></script>		

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
			<a href="//www.ebi.ac.uk/" title="Go to the EMBL-EBI homepage"><img src="//www.ebi.ac.uk/web_guidelines/images/logos/EMBL-EBI/EMBL_EBI_Logo_white.png" alt="EMBL European Bioinformatics Institute"></a>

			<nav>
				<ul id="global-nav">
          <!-- set active class as appropriate -->
          <li class="first active" id="services"><a href="//www.ebi.ac.uk/services">Services</a></li>
					<li id="research"><a href="//www.ebi.ac.uk/research">Research</a></li>
					<li id="training"><a href="//www.ebi.ac.uk/training">Training</a></li>
					<li id="industry"><a href="//www.ebi.ac.uk/industry">Industry</a></li>
					<li id="about" class="last"><a href="//www.ebi.ac.uk/about">About us</a></li>
				</ul>
			</nav>
			
		</div>
		
		<div id="local-masthead" class="masthead grid_24 nomenu">
			
      <!-- local-title -->
      <!-- NB: for additional title style patterns, see http://frontier.ebi.ac.uk/web/style/patterns -->

<!--			<div class="grid_12 alpha" id="local-title">
				<h1><a href="/enzymeportal" title="Back to Enzyme Portal homepage">Enzyme Portal</a></h1>
			</div>-->
 <div id="local-title" class="grid_12 alpha logo-title"> 
     <a href="/enzymeportal" title="Back to Enzyme Portal homepage">
         <img src="resources/images/enzymeportal_logo.png" alt="Enzyme Portal logo" style="width :64px;height: 64px; margin-right: 0px">
     </a> <span style="margin-top: 30px"><h1 style="padding-left: 0px">Enzyme Portal</h1></span> </div>
      
      <!-- /local-title -->

      <!-- local-search -->
      <!-- NB: if you do not have a local-search, delete the following div, and drop the class="grid_12 alpha" class from local-title above -->
      
			<div class="grid_12 omega">
                <%@ include file="frontierSearchBox.jsp" %>
			</div>

      <!-- /local-search -->

      <!-- local-nav -->
      
			<nav>
				<ul class="grid_24" id="local-nav">
					<li class="first"><a href="/enzymeportal">Home</a></li>
<!--					<li class=""><a href="#">Documentation</a></li>-->
					<li><a href="faq">FAQ</a></li>
					<li class="active"><a href="about">About Enzyme Portal</a></li>
					<!-- If you need to include functional (as opposed to purely navigational) links in your local menu,
					     add them here, and give them a class of "functional". Remember: you'll need a class of "last" for
					     whichever one will show up last... 
					     For example: -->
<!--					<li class="functional last"><a href="#" class="icon icon-functional" data-icon="l">Login</a></li>-->
					<li class="functional"><a href="http://www.ebi.ac.uk/support/index.php?query=Enzyme+portal&referrer=http://www.ebi.ac.uk/enzymeportal/" class="icon icon-static" data-icon="f">Feedback</a></li>
					<li class="functional"><a href="#" class="icon icon-functional" data-icon="r">Share</a></li>
<!--                                        <li class="functional"><a href="https://twitter.com/share" class="icon icon-functional" data-icon="r">Share</a>
<script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0];if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src="//platform.twitter.com/widgets.js";fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");</script></li>
                                        -->
				</ul>
			</nav>		
  
      
      
      <!-- /local-nav -->

    </div>
    </header>
               
    <div id="content" role="main" class="grid_24 clearfix">
    
    <!-- If you require a breadcrumb trail, its root should be your service.
     	   You don't need a breadcrumb trail on the homepage of your service... -->
    <nav id="breadcrumb">
     	<p>
		    <a href="/enzymeportal">Enzyme Portal</a> &gt; 
		    About Enzyme Portal
			</p>
  	</nav>
    	
    <!-- Example layout containers -->
	   
 <!-- Suggested layout containers -->
 
<!-- <div class="content">
    <div class="panel-flexible panels-flexible-about_us clearfix" >
<div class="panel-flexible-inside panels-flexible-about_us-inside">-->
<!--<div class="panels-flexible-row panels-flexible-row-about_us-main-row panels-flexible-row-first clearfix">
  <div class="inside panels-flexible-row-inside panels-flexible-row-about_us-main-row-inside panels-flexible-row-inside-first clearfix">-->
   
	   <section class="grid_18 alpha"> 
<!--<div class="panels-flexible-region panels-flexible-region-about_us-center panels-flexible-region-first ">
  <div class="inside panels-flexible-region-inside panels-flexible-region-about_us-center-inside panels-flexible-region-inside-first">-->

<div id="intro" style="background-color: white;">
    <div class="panel-pane pane-custom pane-3 clearfix">
  
        <h3 class="pane-title">About Enzyme Portal</h3>
    
  
<!--   <div class="pane-content"> -->
    
<p></p>
<div class="caption caption-right"><div class="caption-inner" style="width: 301px;">
                                <%-- b>Welcome to the Enzyme Portal</b --%>
                                <iframe width="301" height="224"
                                        src="http://www.youtube.com/embed/Kldp0WXcxUM"
                                        frameborder="0"
                                        allowfullscreen></iframe>
 </div>
Enzyme Portal - An Introduction</div>
<p></p>
<!--<p>The European Bioinformatics Institute is part of <a href="http://www.embl.org/" class=" external">EMBL</a>, Europe?s flagship laboratory for the life sciences. EMBL-EBI provides freely available&nbsp;<a href="http://www.ebi.ac.uk/services">data from life science experiments</a> covering the full spectrum of molecular biology. About 20% of our institute is devoted to <a href="http://www.ebi.ac.uk/research">investigator-led research</a> using computational approaches to unravel the secrets of life. Our extensive <a href="http://www.ebi.ac.uk/training/">training</a> programme helps researchers in academia and <a href="http://www.ebi.ac.uk/industry/">industry</a> to make the most of the incredible amount of data being produced every day in life science experiments.</p>
<p>We are a non-profit, intergovernmental organisation funded by <a href="http://www.embl.de/aboutus/general_information/organisation/member_states/index.html" class=" external">EMBL member states</a>. Our 520 staff hail from 43 countries, and we welcome a regular stream of visiting scientists throughout the year. We are located on the <a href="http://frontier.ebi.ac.uk/about/visit-us">Wellcome Trust Genome Campus</a> in Hinxton, Cambridge in the United Kingdom.</p>-->

	<p>
		The Enzyme Portal is for those interested in
		the biology of enzymes and proteins with enzymatic
		activity.
<!--	</p>

	<p>-->
		It integrates publicly available
		information about enzymes, such as small-molecule chemistry,
		biochemical pathways and drug compounds. It provides a concise
		summary of information from:
		<ul>
			<li><a href="http://www.uniprot.org/help/uniprotkb">UniProt
						Knowledgebase</a></li>
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
<!--   </div> -->

  
  </div>
</div>
                                
 <div class="panel-separator"></div>
<!--  </div>
</div> -->

                                <section>
                                                                  
                                    <p>
                                    	<h3>The Enzyme Portal Team</h3>
                                        <img alt="The Enzyme Portal team" width="450"
		style="float: left; margin: 2ex 2em; height: 340px "
		src="resources/images/EP_team_photo_4_web.jpg"/>
	<p>
		The Enzyme Portal is designed and developed at the
			EMBL-European Bioinformatics Institute (<a
			href="http://www.ebi.ac.uk/information">EMBL-EBI</a>)
			in the UK. Part of the <a
			href="http://www.embl.org">EMBL Outstation - The European Bioinformatics Institute</a>,
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
           </section>
		
		<section class="grid_6 omega">

	<div class="panels-flexible-region panels-flexible-region-about_us-right_ panels-flexible-region-last ">
  <div class="inside panels-flexible-region-inside panels-flexible-region-about_us-right_-inside panels-flexible-region-inside-last">
      
<div class="shortcuts"><div class="panel-pane pane-custom pane-8 clearfix">
  
        <h3 class="pane-title">Popular</h3>
    
  
<!--   <div class="pane-content"> -->

    

<ul class="split">
 <li><a href="http://www.ebi.ac.uk/training/online/course/enzyme-portal-quick-tour" class="icon icon-generic icon-c1" data-icon="+">Quick Tour</a></li>
   <li><a href="http://nar.oxfordjournals.org/content/41/D1/D773.full" class="icon icon-conceptual icon-c8" data-icon="l">Publications</a></li>
</ul>
<ul class="split">

 <li><a href="http://ebi-cheminf.github.com/enzymeportal/ep/" class="icon icon-generic" data-icon="l">Documentations</a></li>
<!-- <li><a href="http://ebi-cheminf.github.com/enzymeportal/ep/" class="icon icon-generic" data-icon="C">Contacts</a><a href="http://www.ebi.ac.uk/support/index.php?query=Enzyme+portal&referrer=http://www.ebi.ac.uk/enzymeportal/"></a></li>-->
</ul>
<!--   </div> -->

  
  </div>
</div>
<!--      first video-->
      <div class="panel-separator"></div>
 
      <div class="shortcuts">
          <div class="panel-pane pane-custom pane-9 clearfix">
  
        <h3 class="pane-title"> Learn more about Enzyme Portal</h3>
    
  
   <div class="pane-content"> 

                                <%-- b>Explore Enzyme Portal</b --%>

                                   <iframe style=" width: 344.5px; height:200px;" 
                                        src="http://www.youtube.com/embed/b7hFo5iJuoM"
                                        frameborder="0"
                                        allowfullscreen></iframe>
  
  </div>
</div>
      </div>  
         <div class="panel-separator"></div>
         
         <div class="panel-pane pane-custom pane-10 clearfix">
  
      
  
   <div class="pane-content"> 
    
<h3><span id="internal-source-marker_0.35187150686265334" class="icon icon-generic icon-c4" data-icon=")">Technical Documents<br>
</span></h3>
<p><span>The Enzyme Portal is an open source project developed at the EMBL-EBI and the source code is freely available, and can be downloaded from <a href="https://github.com/ebi-cheminf/enzymeportal">GitHub </a>, an online project hosting service. </span></p>
<p><span>Additionally, the technical documentations (Javadoc, project information) are available from this <a href="http://ebi-cheminf.github.com/enzymeportal/ep/">web link </a>. </span></p>

   </div> 

  
  </div>
  </div>
</div>		
		</section>
		

                             
                                







<!--   <section class="grid_18 alpha">
                            <div style="margin-left: auto; margin-right: auto;
                         width: 50%;">
<h3>About the Enzyme Portal</h3>
                        <h2>Welcome to the Enzyme Portal</h2> 
                        <p> The Enzyme Portal is for those interested
                        in the biology of enzymes and proteins with enzymatic
                        activity.</p> 
                        <p>Watch the videos below for a more insight on how to use the Enzyme Portal.</p>
                 
                        <div style="text-align: right;">
                            <a href="about" class="showLink" >More about the
                                Enzyme Portal...</a>
                        </div>
                    </div>

                    <table style="margin-left: auto; margin-right: auto;
                           width: 75em; margin-top: 4ex; margin-bottom: 4ex;">
                        <tr style="text-align: center;">
<table>
    <tr>
                            <td>
                                <%-- b>Welcome to the Enzyme Portal</b --%>
                                <iframe width="360" height="270"
                                        src="http://www.youtube.com/embed/Kldp0WXcxUM"
                                        frameborder="0"
                                        allowfullscreen></iframe>
                            </td>
                            <td>
                                <%-- b>Explore Enzyme Portal</b --%>
                                <iframe width="360" height="270"
                                        src="http://www.youtube.com/embed/b7hFo5iJuoM"
                                        frameborder="0"
                                        allowfullscreen></iframe>
                            </td>
                        </tr>
                    </table>
    </section>-->

<!-- <section>
                   
	<h3>About the Enzyme Portal</h3>
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
						Knowledgebase</a></li>
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
	</p>-->
<!--        	                <div style="text-align: right;">
                                    <a href="faq" class="showLink">Frequently asked questions ...</a>
	                </div>-->
<!--<section class="grid_18 alpha">
	<h4>The Enzyme Portal Team</h4>
	<img alt="The Enzyme Portal team" width="450"
		style="float: left; margin: 2ex 2em;"
		src="resources/images/EP_team_photo_4_web.jpg"/>
	<p>
		The Enzyme Portal is designed and developed at the
			EMBL-European Bioinformatics Institute (<a
			href="http://www.ebi.ac.uk/information">EMBL-EBI</a>)
			in the UK. Part of the <a
			href="http://www.embl.org">EMBL Outstation - The European Bioinformatics Institute</a>,
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
	 

                </section>-->

		<!-- End example layout containers -->
			
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
					<h3 class="embl-ebi"><a href="//www.ebi.ac.uk/" title="EMBL-EBI">EMBL-EBI</a></h3>
				</div>
				
				<div class="grid_4">
					<h3 class="services"><a href="//www.ebi.ac.uk/services">Services</a></h3>
				</div>
				
				<div class="grid_4">
					<h3 class="research"><a href="//www.ebi.ac.uk/research">Research</a></h3>
				</div>
				
				<div class="grid_4">
					<h3 class="training"><a href="//www.ebi.ac.uk/training">Training</a></h3>
				</div>
				
				<div class="grid_4">
					<h3 class="industry"><a href="//www.ebi.ac.uk/industry">Industry</a></h3>
				</div>
				
				<div class="grid_4 omega">
					<h3 class="about"><a href="//www.ebi.ac.uk/about">About us</a></h3>
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

  <!-- Grab Google CDN's jQuery, with a protocol relative URL; fall back to local if offline -->
  <!--
  <script src="//ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js"></script>
  <script>window.jQuery || document.write('<script src="../js/libs/jquery-1.6.2.min.js"><\/script>')</script>
  -->
        <c:if test="${pageContext.request.serverName!='www.ebi.ac.uk'}" >
    <script type="text/javascript">var redline = {}; redline.project_id = 185653108;</script><script id="redline_js" src="http://www.redline.cc/assets/button.js" type="text/javascript"></script>
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

  <!-- Your custom JavaScript file scan go here... change names accordingly -->
    <script src="resources/lib/spineconcept/javascript/jquery-1.5.1.min.js" type="text/javascript"></script>
        <script src="resources/javascript/search.js" type="text/javascript"></script>
  <!--
  <script defer="defer" src="//www.ebi.ac.uk/web_guidelines/js/plugins.js"></script>
  <script defer="defer" src="//www.ebi.ac.uk/web_guidelines/js/script.js"></script>
  -->
  <script defer="defer" src="//www.ebi.ac.uk/web_guidelines/js/cookiebanner.js"></script>  
  <script defer="defer" src="//www.ebi.ac.uk/web_guidelines/js/foot.js"></script>
  <!-- end scripts-->

  <!-- Google Analytics details... -->		
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

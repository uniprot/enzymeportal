<%-- 
    Document   : homepage
    Created on : Aug 15, 2012, 4:35:33 PM
    Author     : joseph
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>


<!doctype html>
<!-- paulirish.com/2008/conditional-stylesheets-vs-css-hacks-answer-neither/ -->
<!--[if lt IE 7]> <html class="no-js ie6 oldie" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js ie7 oldie" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js ie8 oldie" lang="en"> <![endif]-->
<!-- Consider adding an manifest.appcache: h5bp.com/d/Offline -->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib  prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<head>
  <meta charset="utf-8">

  <!-- Use the .htaccess and remove these lines to avoid edge case issues.
       More info: h5bp.com/b/378 -->
  <!-- <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"> -->	<!-- Not yet implemented -->

  <title>Enzyme Portal &lt; EMBL-EBI</title>
  <meta name="description" content="EMBL-EBI">
  <meta name="keywords" content="bioinformatics, europe, institute">
  <meta name="author" content="EMBL-EBI">

  <!-- Mobile viewport optimized: j.mp/bplateviewport -->
  <meta name="viewport" content="width=device-width,initial-scale=1">

  <!-- Place favicon.ico and apple-touch-icon.png in the root directory: mathiasbynens.be/notes/touch-icons -->
  <!-- Better make some of these! [FR] -->

  <!-- CSS: implied media=all -->
  <!-- CSS concatenated and minified via ant build script-->	<!-- Not yet implemented -->
  <link rel="stylesheet" href="resources/css/boilerplate-style.css">
  
  
  <link rel="stylesheet" href="resources/css/ebi-global.css" type="text/css" media="screen" />
  <link rel="stylesheet" href="resources/css/ebi-visual.css" type="text/css" media="screen">
  <link rel="stylesheet" href="resources/css/984-24-col-fluid.css" type="text/css" media="screen" />
  
  <link rel="stylesheet" href="../css/form-jdispatcher.css" type="text/css" media="screen">
  <link rel="stylesheet" href="../css/embl-petrol-colours.css" type="text/css" media="screen">
  
  
  <!-- end CSS-->


  <!-- All JavaScript at the bottom, except for Modernizr / Respond.
       Modernizr enables HTML5 elements & feature detects; Respond is a polyfill for min/max-width CSS3 Media Queries
       For optimal performance, use a custom Modernizr build: www.modernizr.com/download/ -->
 <!--  <script src="../js/libs/modernizr-2.0.6.min.js"></script> -->	<!-- Not yet implemented -->
 
 
         
        <link rel="stylesheet"  href="http://www.ebi.ac.uk/inc/css/contents.css"     type="text/css" />
        <link href="resources/css/search.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="resources/lib/spineconcept/css/960gs-fluid/grid.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="resources/lib/spineconcept/css/common.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="resources/lib/spineconcept/css/identification.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="resources/lib/spineconcept/css/species.css" type="text/css" rel="stylesheet" />
        <script src="resources/lib/spineconcept/javascript/jquery-1.5.1.min.js" type="text/javascript"></script><!--
        <script src="resources/lib/spineconcept/javascript/identification.js" type="text/javascript"></script> 
        <script src="resources/javascript/search.js" type="text/javascript"></script>
        <meta name="google-site-verification" content="tXBo-O4mKKZgv__6QG6iyjqDJibhSb3ZAQtXQGjo86I" />-->
  
  
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
			
			<form id="global-search" name="global-search" action="#" method="post" class="grid_5">
							
				<fieldset>
				
				<label>
				<input type="text" name="first" id="global-searchbox" />
				</label>
								
				<input type="submit" name="submit" value="" class="submit" />
				</fieldset>
				
			</form>
			
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
						
<!--			<div class="grid_12 omega">
				<form id="local-search" name="local-search" action="#" method="post">
								
					<fieldset>
					
					<label>
					<input type="text" name="first" id="local-searchbox" />
					</label>	
									
					<input type="submit" name="submit" value="Search" class="submit" />	
					</fieldset>
					
				</form>
			</div>-->
                       
			
			<nav>
				<ul class="grid_24" id="local-nav">
					<li class="first"><a href="#" title="">Home</a></li>
					<li><a href="#" title="">Item 2</a></li>
					<li><a href="#" title="">Item 3</a></li>
					<li><a href="#" title="">Item 4</a></li>
					<li class="last"><a href="#" title="">About Enzyme Portal</a></li>
				</ul>
			</nav>	
		</div>
    </header>
        
    <div id="content" role="main" class="grid_24 clearfix">
           <div class="grid_12zzz" style="display: table; margin-left: 1em;">
    <%@ include file="breadcrumbs.jsp" %>
</div>    
        
        	<div style="display: table-cell;">
                    <h2>Your portal to enzyme-related information at the EBI.</h2>
	</div>
    
<!--    	<h2>[some Strapline text here]</h2>-->
        
        
        <!--
    	
    	 Optional layout containers 
	    <div class="grid_18 alpha">    
	    	<section>
	    	somthing goes here
										
			</section>    
	    </div>	
			
		<div class="grid_6 omega">
			<aside>
				an aside				
			</aside>
		</div>
		 End optional layout containers 
                
         
			
    </div>-->
   
<!--              <div class="contents">-->
            <div class="page container_12">
               
                <jsp:include page="searchBox.jsp"/>

                <div style="margin-left: auto; margin-right: auto;
                       width: 50%;">
                    
                    <h2>Welcome to the Enzyme Portal</h2> 
                	The Enzyme Portal is for people who are interested
                	in the biology of enzymes and proteins with enzymatic
                	activity.
	                <div style="text-align: right;">
		                <a href="about" class="showLink">More about the
		                	enzyme portal...</a>
	                </div>
                </div>

                   <table style="margin-left: auto; margin-right: auto;
                       width: 75em; margin-top: 4ex; margin-bottom: 4ex;">
                       <tr style="text-align: center;">
                        <td>
                        <%-- b>Welcome to the Enzyme Portal</b --%>
                        <iframe width="360" height="270"
                            src="http://www.youtube.com/embed/Kldp0WXcxUM"
                            frameborder="0"
                            allowfullscreen></iframe>
                        </td>
                        <td>
                        <%-- b>Explore Enzyme Portal</b --%>
                           <iframe class="youtube-player" type="text/html" width="360" height="270" 
                                   src="http://www.youtube.com/embed/b7hFo5iJuoM" frameborder="0"
                                   allowfullscreen>
                           </iframe>
                        </td>
                       </tr>
                   </table>
            </div>
           
        </div>
      
      
      
    
    <footer>
    	<!-- Optional local footer (insert citation / project-specific copyright / etc here -->
		<!--
<div id="local-footer" class="grid_24 clearfix">
			
		</div>
-->
		<!-- End optional local footer -->
		
		<div id="global-footer" class="grid_24 clearfix">
						
			<section id="global-nav-expanded">
				
				<div class="grid_4 alpha">
					<h3 class="explore">Explore</h3>
					<ul>
						<li class="first"><a href="#" title="">News</a></li>
						<li><a href="#">Publications</a></li>
						<li><a href="#">Blogs</a></li>
						<li><a href="#">RSS feeds</a></li>
						<li class="last"><a href="#">Stories</a></li>
					</ul>
				</div>
				
				<div class="grid_4">
					<h3 class="services"><a href="#" title="">Services</a></h3>
					<ul>
						<li class="first"><a href="#" title="">By topic</a></li>
						<li><a href="#">By name (A-Z)</a></li>
						<li class="last"><a href="#">Get help</a></li>
					</ul>		
				</div>
				
				<div class="grid_4">
					<h3 class="research"><a href="#" title="">Research</a></h3>
					<ul>
						<li class="first"><a href="#">Overview</a></li>
						<li><a href="#">Research groups</a></li>
						<li><a href="#">Publications</a></li>
						<li class="last"><a href="#">Postdocs &amp; PhDs</a></li>
					</ul>
				</div>
				
				<div class="grid_4">
					<h3 class="training"><a href="#" title="">Training</a></h3>
					<ul>
						<li class="first"><a href="#">Train online</a></li>
						<li><a href="#">Hands-on courses</a></li>
						<li><a href="#">Events on campus</a></li>
						<li><a href="#">Bioinformatics Roadshow</a></li>
						<li class="last"><a href="#">Contact organisers</a></li>
					</ul>			
				</div>
				
				<div class="grid_4">
					<h3 class="funding"><a href="#" title="">Funding</a></h3>
					<ul>
						<li class="first"><a href="#">How we are funded</a></li>
						<li><a href="#">How money is spent</a></li>
						<li class="last"><a href="#">Fund our work</a></li>
					</ul>			
				</div>
				
				<div class="grid_4 omega">
					<h3 class="about"><a href="#" title="">About us</a></h3>
					<ul>
						<li class="first"><a href="#">Overview</a></li>
						<li><a href="#">History</a></li>
						<li><a href="#">People</a></li>
						<li><a href="#">Funding</a></li>
						<li><a href="#" title="">News</a></li>
						<li id="jobs"><a href="#" title="Jobs, postdocs, PhDs...">Jobs</a></li>
						<li><a href="#" title="e.g. EBI Open  Day; exhibitions...">Events</a></li>
						<li><a href="#">Visit us</a></li>
						<li class="last"><a href="#">Intranet</a></li>
					</ul>
					
				</div>
			</section>

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


<%-- 
    Document   : advanceSearch
    Created on : Nov 19, 2012, 10:55:37 AM
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

        <title>Enzyme Portal &lt;Advance Search &lt; EMBL-EBI</title>
        <meta name="description" content="EMBL-EBI"><!-- Describe what this page is about -->
        <meta name="keywords" content="bioinformatics, europe, institute"><!-- A few keywords that relate to the content of THIS PAGE (not the whol project) -->
        <meta name="author" content="EMBL-EBI"><!-- Your [project-name] here -->

        <!-- Mobile viewport optimized: j.mp/bplateviewport -->
        <meta name="viewport" content="width=device-width,initial-scale=1">

        <!-- Place favicon.ico and apple-touch-icon.png in the root directory: mathiasbynens.be/notes/touch-icons -->
        <!-- Better make some of these! [FR] -->

        <!-- CSS: implied media=all -->
        <!-- CSS concatenated and minified via ant build script-->	<!-- Not yet implemented -->


        <link href="resources/css/search.css" type="text/css" rel="stylesheet" />
        <!--           <link rel="stylesheet" href="resources/css/enzyme-portal-colours.css" type="text/css" media="screen" />-->
        <link rel="stylesheet" href="resources/css/embl-petrol-colours.css" type="text/css" media="screen" />

        <!-- end CSS-->

  <link rel="stylesheet" href="//www.ebi.ac.uk/web_guidelines/css/compliance/mini/ebi-fluid-embl.css">
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

                    <!-- CHOOSE -->
 <div id="local-title" class="grid_12 alpha logo-title"> 
     <a href="/enzymeportal" title="Back to Enzyme Portal homepage">
         <img src="resources/images/enzymeportal_logo.png" alt="Enzyme Portal logo" style="width :64px;height: 64px; margin-right: 0px">
     </a> <span style="margin-top: 30px"><h1 style="padding-left: 0px">Enzyme Portal</h1></span> </div>


                    <nav>
                        <ul class="grid_24" id="local-nav">
                            <li class="first" ><a href="/enzymeportal" title="">Home</a></li>
<!--                            <li><a href="#" title="">TODO 2</a></li>
                            <li class="active"><a href="#" title="">TODO 3</a></li>-->
                            <li><a href="faq" title="Frequently Asked questions">FAQ</a></li>
                            <li class="last"><a href="about" title="About Enzyme Portal">About Enzyme Portal</a></li>
                            <li class="functional"><a href="http://www.ebi.ac.uk/support/index.php?query=Enzyme+portal&referrer=http://www.ebi.ac.uk/enzymeportal/" class="icon icon-static" data-icon="f">Feedback</a></li>
<!--					<li class="functional"><a href="#" class="icon icon-functional" data-icon="r">Share</a></li>-->
                             <li class="functional"> <a href="https://twitter.com/share" class="icon icon-functional" data-icon="r" data-dnt="true" data-count="none" data-via="twitterapi">Share</a></li>
                        
                        </ul>
                    </nav>	
                </div>
            </header>

            
            <div id="content" role="main" class="grid_24 clearfix">

                <!-- Suggested layout containers --> 
          <nav id="breadcrumb">
     	<p>
		    <a href="/enzymeportal">Enzyme Portal</a> &gt; 
		    Advance Search
			</p>
  	</nav>
    

            
            <section>
             <div class="contents">
            <div class="page container_12"> 
                 <%@ include file="searchBox.jsp" %>
            </div>
                     </div>
            </section>
                <!-- End suggested layout containers -->

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

   <!-- Your custom JavaScript file scan go here... change names accordingly -->
    <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
        <script src="resources/javascript/search.js" type="text/javascript"></script>
        
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

        <!--        add twitter script for twitterapi-->
<script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0];if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src="https://platform.twitter.com/widgets.js";fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");</script>

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
</html>

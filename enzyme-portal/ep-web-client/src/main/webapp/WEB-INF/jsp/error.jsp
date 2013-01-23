<%-- 
    Document   : error
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

         <link rel="stylesheet" href="//www.ebi.ac.uk/web_guidelines/css/compliance/mini/ebi-fluid-embl.css">
<!--        <link rel="stylesheet" href="resources/css/boilerplate-style.css"> 

        <link rel="stylesheet" href="resources/css/ebi-global.css" type="text/css" media="screen" />

        <link rel="stylesheet" href="resources/css/ebi-visual.css" type="text/css" media="screen">
        <link rel="stylesheet" href="resources/css/984-24-col-fluid.css" type="text/css" media="screen" />-->
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
                          <%@ include file="frontierSearchBox.jsp" %>

         
                    </div>

           		<nav>
				<ul class="grid_24" id="local-nav">
					<li  class="first"><a href="/enzymeportal" title="">Home</a></li>
					<li><a href="#">Documentation</a></li>
					<li><a href="faq" title="Frequently Asked questions">FAQ</a></li>
					<li class="last"><a href="about" title="About Enzyme Portal">About Enzyme Portal</a></li>
					<!-- If you need to include functional (as opposed to purely navigational) links in your local menu,
					     add them here, and give them a class of "functional". Remember: you'll need a class of "last" for
					     whichever one will show up last... 
					     For example: -->
<!--					<li class="functional last"><a href="#" class="icon icon-functional" data-icon="l">Login</a></li>-->
					<li class="functional"><a href="#" class="icon icon-static" data-icon="f">Feedback</a></li>
<!--					<li class="functional"><a href="#" class="icon icon-functional" data-icon="r">Share</a></li>-->
            				<li class="functional"> <a href="https://twitter.com/share" class="icon icon-functional" data-icon="r" data-dnt="true" data-count="none" data-via="twitterapi">Share</a></li>
                        
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
                   
                    <div style="display: table-cell;">
                        <h4 style="color: red">Error</h4>
                    </div>
                    <div style="display: table-cell;">
                        - There has been an error with your request. Please try again later.
                    </div>
                 
                </section>

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


<%-- 
    Document   : index
    Created on : Sep 3, 2012, 12:11:34 PM
    Author     : joseph
--%>

<!doctype html>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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

  <title>Home &lt; Enzyme Portal &gt; EMBL-EBI</title>
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
<!--  <link href="resources/css/search.css" type="text/css" rel="stylesheet" />-->
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
  
<!--  <script id="redline_js" type="text/javascript">var redline = {}; redline.project_id = 33249186;var b,d;b=document.createElement("script");b.type="text/javascript";b.async=!0;b.src=("https:"===document.location.protocol?"https://data":"http://www")+'.redline.cc/assets/button.js';d=document.getElementsByTagName("script")[0];d.parentNode.insertBefore(b,d);</script>-->

<!-- <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>-->
<!--      <script type="text/javascript" src="javascripts/lib/jquery.jcarousel.min.js"></script>
      <link rel="stylesheet" type="text/css" href="/enzymeportal/images/skins/default/skin2.css" />-->
<!--      <link rel="stylesheet" type="text/css" href="stylesheets/style.css" />-->

<link rel="stylesheet" type="text/css" href="resources/skins/default/skin2.css" />



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


 <div id="local-title" class="grid_12 alpha logo-title"> 
     <a href="/enzymeportal" title="Back to Enzyme Portal homepage">
         <img src="resources/images/enzymeportal_logo.png" alt="Enzyme Portal logo" style="width :64px;height: 64px; margin-right: 0px">
     </a> <span style="margin-top: 30px"><h1 style="padding-left: 0px">Enzyme Portal</h1></span> </div>
<!--      <div class="grid_12 alpha" id="local-title-logo">
          <h1>Enzyme Portal</h1>
           <p><img src="resources/images/enzymeportal_logo.png" alt="Enzyme Portal" width="4" height="40" class="logo" /></p>  
           
                    </div>-->
      
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
					<li  class="active"><a href="/enzymeportal" title="">Home</a></li>
<!--					<li><a href="#">Documentation</a></li>-->
					<li><a href="faq" title="Frequently Asked questions">FAQ</a></li>
                                        
					<li class="last"><a href="about" title="About Enzyme Portal">About Enzyme Portal</a></li>
                                         <li><a href="browse" title="Browse Disease">Browse Disease</a></li>
                                       
					<!-- If you need to include functional (as opposed to purely navigational) links in your local menu,
					     add them here, and give them a class of "functional". Remember: you'll need a class of "last" for
					     whichever one will show up last... 
					     For example: -->
<!--					<li class="functional last"><a href="#" class="icon icon-functional" data-icon="l">Login</a></li>-->
					<li class="functional"><a href="http://www.ebi.ac.uk/support/index.php?query=Enzyme+portal&referrer=http://www.ebi.ac.uk/enzymeportal/" class="icon icon-static" data-icon="f">Feedback</a></li>
<!--					<li class="functional"><a href="#" class="icon icon-functional" data-icon="r">Share</a></li>-->
                                        <li class="functional"> <a href="https://twitter.com/share" class="icon icon-functional" data-icon="r" data-dnt="true" data-count="none" data-via="twitterapi">Share</a></li>
<!--<script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0];if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src="https://platform.twitter.com/widgets.js";fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");</script>-->
				
                                </ul>
			</nav>		
  
      <!-- /local-nav -->

    </div>
    </header>
               
    <div id="content" role="main" class="grid_24 clearfix">
        
        <section class="grid_24">
                            <div style="margin-left: auto; margin-right: auto;
                         width: 50%;">

                        <h2>Welcome to the Enzyme Portal</h2> 
                        <p>You can search this integrated resource to find information about the biology of a protein with enzymatic activity.</p>
                        <p>The Enzyme Portal integrates publicly available information about enzymes, such as small-molecule chemistry, biochemical pathways and drug compounds. It brings together lots of diverse information about enzymes, and covers a large number of species including the key model organisms. 
                          The search results are displayed as a summary, and give you a simple way to compare orthologues.
                        </p>
                    <p>
		Give it a try and 
		<a href="http://www.ebi.ac.uk/support/index.php?query=Enzyme+portal&referrer=http://www.ebi.ac.uk/enzymeportal/">let
                    us know</a> what you think!</p>
<!--                        <p> The Enzyme Portal is for those interested
                        in the biology of enzymes and proteins with enzymatic
                        activity.</p>-->
                 
<!--                        <div style="text-align: right;">-->
<p><p style="text-align: right;"><a href="about" class="showLink" >More about the
                                Enzyme Portal...</a></p>
<!--                    </div>-->
                    </div>
            <div style="margin-left: auto; margin-right: auto;
                          width: 50%;">
 <h3 class="pane-title">Enzyme Portal Resources</h3>
                <div class="shortcuts grid_24">

               <div class="panel-pane pane-custom pane-8 clearfix" >

<!--        <h3 class="pane-title">Enzyme Portal Resources</h3>-->

 
  <div class="containers grid_24">
<!--      <section class="grid_24">-->
         <ul id="mycarousel" class="jcarousel-skin-aqua grid_24">
            <li>
                <div class="grid_24">
                    <img class="grid_3 alpha" src="resources/images/inc/uniprot_logo.gif" />
                    <span class="grid_21 omega">
                     <h4>UniProtKB</h4>
                     <p>The UniProt Knowledgebase (UniProtKB) is the central hub for the collection of functional information on proteins, with accurate, consistent and rich annotation.</p>
                     <p>The UniProt databases are the UniProt Knowledgebase (UniProtKB), the UniProt Reference Clusters (UniRef), and the UniProt Archive (UniParc).</p>
                  </span>
               </div>
            </li>
            <li>
               <div>
                   <img class="grid_3 alpha" src="resources/images/inc/pdbe_logo.png" />
                   <span class="grid_21 omega">
                     <h4>PDBe</h4>
                     <p> PDBe is the European resource for the collection, organisation and dissemination of data on biological macromolecular structures. In collaboration with the other worldwide Protein Data Bank (wwPDB) partners - the Research Collaboratory for Structural Bioinformatics (RCSB) and BioMagResBank (BMRB) in the USA and the Protein Data Bank of Japan (PDBj) - we work to collate, maintain and provide access to the global repository of macromolecular structure data.</p> 
<!--                     <a href="/" title="Link">Click this Link</a>-->
                  </span>
               </div>
            </li>
            <li>
               <div>
                  <img class="grid_3 alpha" src="resources/images/inc/chembl_logo.png" />
                  <span class="grid_21 omega">
                     <h4>ChEMBL</h4>
                     <p>ChEMBL is a database of bioactive drug-like small molecules, it contains 2-D structures, calculated properties (e.g. logP, Molecular Weight, Lipinski Parameters, etc.) and abstracted bioactivities (e.g. binding constants, pharmacology and ADMET data).</p>
                     <p>The data is abstracted and curated from the primary scientific literature, and cover a significant fraction of the SAR and discovery of modern drugs</p>
                  </span>
              </div>
            </li>  
                     <li>
               <div>
                  <img class="grid_3 alpha" src="resources/images/inc/ChEBI_logo.png" />
                  <span class="grid_21 omega">
                     <h4>ChEBI</h4>
                     <p>Chemical Entities of Biological Interest (ChEBI) is a freely available dictionary of molecular entities focused on ?small? chemical compounds. The term ?molecular entity? refers to any constitutionally or isotopically distinct atom, molecule, ion, ion pair, radical, radical ion, complex, conformer, etc., identifiable as a separately distinguishable entity. The molecular entities in question are either products of nature or synthetic products used to intervene in the processes of living organisms.</p>
                  </span>
              </div>
            </li> 
                     <li>
               <div>
                  <img class="grid_3 alpha" src="resources/images/inc/reactome.png" />
                  <span class="grid_21 omega">
                     <h4>REACTOME</h4>
                     <p>REACTOME is an open-source, open access, manually curated and peer-reviewed pathway database. Pathway annotations are authored by expert biologists, in collaboration with Reactome editorial staff and cross-referenced to many bioinformatics databases. These include NCBI Entrez Gene, Ensembl and UniProt databases, the UCSC and HapMap Genome Browsers, the KEGG Compound and ChEBI small molecule databases, PubMed, and Gene Ontology.</p>
                  </span>
              </div>
            </li> 
               <li>
               <div>
                  <img class="grid_3 alpha" src="resources/images/inc/no-image.jpeg" />
                  <span class="grid_21 omega">
                     <h4>MACiE</h4>
                     <p>MACiE, which stands for Mechanism, Annotation and Classification in Enzymes, is a collaborative project between the Thornton Group at the European Bioinformatics Institute and the Mitchell Group at the University of St Andrews (initially within the Unilever Centre for Molecular Informatics part of the University of Cambridge). Metal MACiE, a database of catalytic metal ions, with a view to understanding the functions of the roles and activity of catalytic metals in enzymes.</p>
                  </span>
              </div>
            </li> 
                          <li>
               <div>
                  <img class="grid_3 alpha" src="resources/images/inc/rhea.png" />
                  <span class="grid_21 omega">
                     <h4>Rhea</h4>
                     <p>Rhea is a freely available, manually annotated database of chemical reactions created in collaboration with the Swiss Institute of Bioinformatics (SIB).
All data in Rhea is freely accessible and available for anyone to use.</p>
                  </span>
              </div>
            </li> 
              <li>
               <div>
                  <img class="grid_3 alpha" src="resources/images/inc/intenz.gif" />
                  <span class="grid_21 omega">
                     <h4>IntEnz</h4>
                     <p>IntEnz (Integrated relational Enzyme database) is a freely available resource focused on enzyme nomenclature. IntEnz is created in collaboration with the Swiss Institute of Bioinformatics (SIB). This collaboration is responsible for the production of the ENZYME resource. 
IntEnz contains the recommendations of the Nomenclature Committee of the International Union of Biochemistry and Molecular Biology (NC-IUBMB) on the nomenclature and classification of enzyme-catalysed reactions.</p>
                  </span>
              </div>
            </li> 
         </ul>
<!--      </section>-->
    
         <div class="jcarousel-control">
            <a href="#">1</a>
            <a href="#">2</a>
            <a href="#">3</a>
            <a href="#">4</a>
            <a href="#">5</a>
            <a href="#">6</a>
            <a href="#">7</a>
            <a href="#">8</a>
         </div>
    
<!--      </div>-->






        
<!--                              <ul class="split">
                                  <li>UniProt</li>
                                  <li>ChEBI</li>
                                  <li>ChEMBL</li>
                                  <li>PDBe</li>
                                </ul>
                                <ul class="split">
                                    <li>Reactome</li>
                                    <li>Rhea</li>
                                    <li>IntEnz</li>
                                    <li>MACiE</li>
                                </ul> -->
        
                   
                    
                    </div></div>
                
                </div>
                
                
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

  <!-- Grab Google CDN's jQuery, with a protocol relative URL; fall back to local if offline -->
  <!--
  <script src="//ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js"></script>
  <script>window.jQuery || document.write('<script src="../js/libs/jquery-1.6.2.min.js"><\/script>')</script>
  -->


  <!-- Your custom JavaScript file scan go here... change names accordingly -->
<!--   <script src="resources/lib/spineconcept/javascript/jquery-1.5.1.min.js" type="text/javascript"></script>-->
        <script src="resources/javascript/search.js" type="text/javascript"></script>
         <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
        <script src="resources/javascript/jquery.jcarousel.min.js" type="text/javascript"></script>
       
        
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
  
  
  
       <script type="text/javascript">
            /**
            * We use the initCallback callback
            * to assign functionality to the controls
            */
            function mycarousel_initCallback(carousel) {
                jQuery('.jcarousel-control a').bind('click', function () {
                    carousel.scroll(jQuery.jcarousel.intval(jQuery(this).text()));
                    return false; 
                });

                jQuery('#mycarousel-next').bind('click', function () {
                    carousel.next();
                    return false;
                });

                jQuery('#mycarousel-prev').bind('click', function () {
                    carousel.prev();
                    return false;
                });

                // Pause autoscrolling if the user moves with the cursor over the clip.
        	    carousel.clip.hover(function() {
        	        carousel.stopAuto();
        	    }, function() {
        	        carousel.startAuto();
        	    });

            };

            // Ride the carousel...
            jQuery(document).ready(function() {
              jQuery('#mycarousel').jcarousel({
              scroll: 1, animation:700, visible:1, auto:5, wrap:"both",
                    initCallback: mycarousel_initCallback,
                    itemVisibleInCallback: {
                        onAfterAnimation: function (c, o, i, s) {
                            i = (i - 1) % $('#mycarousel li').size();
                            jQuery('.jcarousel-control a').removeClass('active').addClass('inactive');
                            jQuery('.jcarousel-control a:eq(' + i + ')').removeClass('inactive').addClass('active');
                        }
                    }
          });  
      });
      </script>
  
</body>
</html>

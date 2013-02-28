<%-- 
    Document   : example
    Created on : Jan 31, 2013, 12:11:31 PM
    Author     : joseph
--%>

<!DOCTYPE html>

<!-- paulirish.com/2008/conditional-stylesheets-vs-css-hacks-answer-neither/ -->
<!--[if IE 8]>    <html class="no-js lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->
<head>
		<meta charset="utf-8" />
		
                
<title>Orbit Demo</title>
		
		<!-- Attach our CSS -->
	  	<link rel="stylesheet" href="stylesheets/orbit-1.2.3.css">
	  	<link rel="stylesheet" href="stylesheets/demo-style.css">
	  	
		<!-- Attach necessary JS -->
		<script type="text/javascript" src="javascripts/jquery-1.5.1.min.js"></script>
		<script type="text/javascript" src="javascripts/jquery.orbit-1.2.3.min.js"></script>	
		
			<!--[if IE]>
			     <style type="text/css">
			         .timer { display: none !important; }
			         div.caption { background:transparent; filter:progid:DXImageTransform.Microsoft.gradient(startColorstr=#99000000,endColorstr=#99000000);zoom: 1; }
			    </style>
			<![endif]-->
		
		<!-- Run the plugin -->
		<script type="text/javascript">
			$(window).load(function() {
				$('#featured').orbit();
			});
		</script>
		
	</head>
	<body>
	
	<div class="container">
		<h4>ZURB's Orbit Slider</h4>
		<a href="http://www.zurb.com/playground/orbit-jquery-image-slider">View Docs + Playground for Orbit</a>
	
	
	
	
<!-- =======================================

THE ACTUAL ORBIT SLIDER CONTENT 

======================================= -->

		<div id="featured"> 
			<div class="content" style="">
<!--				<h1>Orbit does content now.</h1>
				<h3>Highlight me...I'm text.</h3>-->

                              <ul class="content">
                                  <li>UniProt <a href=""><img src="dummy-images/overflow.jpg" /></a></li>
                                  <li>ChEBI <img src="dummy-images/captions.jpg" data-caption="#htmlCaption" /></li>
                                  <li>ChEMBL</li>
                                  <li>PDBe</li>
                            </ul>
			</div>
<!--			<a href=""><img src="dummy-images/overflow.jpg" /></a>-->
<!--			<img src="dummy-images/captions.jpg" data-caption="#htmlCaption" />
			<img src="dummy-images/features.jpg"  />-->
		</div>
		<!-- Captions for Orbit -->
		<span class="orbit-caption" id="htmlCaption"><strong>I'm A  Caption:</strong> I can have <a href="#">links</a>, <em>style</em> or anything that is valid markup :)</span>
		
		
		</div>	
	</body>
</html>
		
<!--		 Attach our CSS 
	  	<link rel="stylesheet" href="/enzymeportal/stylesheets/orbit-1.2.3.css">
	  	<link rel="stylesheet" href="/enzymeportal/stylesheets/demo-style.css">
	  	
		 Attach necessary JS 
		<script type="text/javascript" src="jscript/jquery-1.5.1.min.js"></script>
		<script type="text/javascript" src="jscript/jquery.orbit-1.2.3.min.js"></script>	
		
			[if IE]>
			     <style type="text/css">
			         .timer { display: none !important; }
			         div.caption { background:transparent; filter:progid:DXImageTransform.Microsoft.gradient(startColorstr=#99000000,endColorstr=#99000000);zoom: 1; }
			    </style>
			<![endif]
		
		 Run the plugin 
		<script type="text/javascript">
			$(window).load(function() {
				$('#featured').orbit();
			});
		</script>
		
	</head>
	<body>
	
	<div class="container">
		<h4>ZURB's Orbit Slider</h4>
		<a href="http://www.zurb.com/playground/orbit-jquery-image-slider">View Docs + Playground for Orbit</a>
	
	
	
	
 =======================================

THE ACTUAL ORBIT SLIDER CONTENT 

======================================= 
		<div id="featured"> 
			<div class="content" style="">
				<h1>Orbit does content now.</h1>
				<h3>Highlight me...I'm text.</h3>
			</div>
			<a href=""><img src="dummy-images/overflow.jpg" /></a>
			<img src="dummy-images/captions.jpg" data-caption="#htmlCaption" />
			<img src="dummy-images/features.jpg"  />
		</div>
		 Captions for Orbit 
		<span class="orbit-caption" id="htmlCaption"><strong>This is a Caption:</strong> I can have <a href="#">links</a>, <em>style</em> or anything that is valid markup :)</span>
		
		
		
		</div>	
	</body>
</html>-->

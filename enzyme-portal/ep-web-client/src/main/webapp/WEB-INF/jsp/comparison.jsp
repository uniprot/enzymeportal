<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta charset="utf-8">

    <!-- Use the .htaccess and remove these lines to avoid edge case issues.
         More info: h5bp.com/b/378 -->
    <!-- <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"> --> <!-- Not yet implemented -->

    <title>Enzyme Portal - Enzyme comparison</title>
    <meta name="description" content="EMBL-EBI">
    <meta name="keywords" content="bioinformatics, europe, institute">
    <meta name="author" content="EMBL-EBI, Cheminformatics and Metabolism Team">

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
    <link rel="stylesheet" href="//www.ebi.ac.uk/web_guidelines/css/compliance/mini/ebi-fluid-embl.css">

    <link href="resources/css/enzyme.css" type="text/css" rel="stylesheet" />

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
<body class="level2">

    <div id="skip-to">
        <ul>
            <li><a href="#content">Skip to main content</a></li>
            <li><a href="#local-nav">Skip to local navigation</a></li>
            <li><a href="#global-nav">Skip to EBI global navigation menu</a></li>
            <li><a href="#global-nav-expanded">Skip to expanded EBI global navigation menu (includes all sub-sections)</a></li>
        </ul>
    </div>

    <div id="wrapper" class="container_24">
       
        <%@include file="header.jsp" %>
       
        <div id="content" role="main" class="grid_24 clearfix">

            <div id="comparison-outline">
                <ul>
                <c:forEach var="sc" items="${comparison.subComparisons}">
                    <li class="${sc.value.different? 'diff' : ''}">
                        <a href="#${sc.key}"
                            style="font-weight: bold;">${sc.key}</a>
                        <c:if test="${sc.key eq 'Summary'
                            or sc.key eq 'Small molecules'}">
                            <ul>
                            <c:forEach var="ssc"
                                items="${sc.value.subComparisons}">
                                <li class="${ssc.value.different? 'diff' : ''}">
                                    <a href="#${ssc.key}">${ssc.key}</a>
                                </li>
                            </c:forEach>
                            </ul>
                        </c:if>
                    </li>
                </c:forEach>
                </ul>
            </div>
        
            <section class="grid_4 alpha">&nbsp;</section>
            <section class="grid_18 omega">
                <h2>Comparing ${comparison.compared[0].uniprotaccessions[0]}
                    to ${comparison.compared[1].uniprotaccessions[0]}</h2>
            </section>
            <br clear="all"/>

            <section class="grid_4 alpha" id="comparison-header">
                &nbsp;
            </section>
            <section class="grid_20 omega">
                <section class="grid_12 comparison header">
                    ${comparison.compared[0].name}<br/>
                    (${comparison.compared[0].species.scientificname})
                </section>
                <section class="grid_12 omega comparison header">
                    ${comparison.compared[1].name}<br/>
                    (${comparison.compared[1].species.scientificname})
                </section>
                <br clear="all"/>
            </section>

            <c:forEach var="sc" items="${comparison.subComparisons}">
                <section class="grid_4 alpha" id="${sc.key}">&nbsp;</section>
                <section class="grid_20 omega">
                    <fieldset style="border: 1px solid; margin: 0ex 1em">

                        <legend class="comparison header">${sc.key}</legend>

                        <c:choose>
                            <c:when test="${empty sc.value.subComparisons}">
                                <%@include file="comparison-item.jsp" %>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="theComparison"
                                    items="${sc.value.subComparisons}">
                                    <%@include file="comparison-list.jsp" %>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </fieldset>
                </section>
            </c:forEach>
           
        </div>
       
        <%@include file="footer.jsp" %>
        
    </div>
</body>
</html>
<%-- 
    Document   : taxonomy-test
    Created on : Nov 26, 2015, 2:08:07 PM
    Author     : Joseph <joseph@ebi.ac.uk>
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="Fn" uri="/WEB-INF/epTagLibray.tld" %>
<%@taglib prefix="ep" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>


<!-- paulirish.com/2008/conditional-stylesheets-vs-css-hacks-answer-neither/ -->
<!--[if lt IE 7]> <html class="no-js ie6 oldie" lang="en"> <![endif]-->
<!--[if IE 7]> <html class="no-js ie7 oldie" lang="en"> <![endif]-->
<!--[if IE 8]> <html class="no-js ie8 oldie" lang="en"> <![endif]-->
<!-- Consider adding an manifest.appcache: h5bp.com/d/Offline -->
<!--[if gt IE 8]><!-->
<html class="no-js" lang="en" ng-app="enzyme-portal-app"> <!--<![endif]-->
<head>
    <c:set var="pageTitle" value="Browse"/>
    <%@include file="head.jspf" %>


</head>

<body class="level2"><!-- add any of your classes or IDs -->
<div id="skip-to">
    <ul>
        <li><a href="#content">Skip to main content</a></li>
        <li><a href="#local-nav">Skip to local navigation</a></li>
        <li><a href="#global-nav">Skip to EBI global navigation menu</a></li>
        <li><a href="#global-nav-expanded">Skip to expanded EBI global navigation menu (includes all sub-sections)</a>
        </li>
    </ul>
</div>


<div id="wrapper" class="container_24">
    <%@include file="header.jspf" %>
<!--    <script src="http://d3js.org/d3.v3.min.js"></script>
    <script>
  

    </script>-->

    <div id="content" role="main" class="grid_24 clearfix">
        <h1>Taxonomy</h1>
                    <c:forEach var="t" items="${taxonomy}">
                         <br>
                <div>${t.taxId}-${t.scientificName}-${t.commonName} - ${t.numEnzymes}</div>
               
            </c:forEach>
        
<!--        <img id="spinner" ng-src="${pageContext.request.contextPath}/resources/images/loading128.gif" style="display:none;" class="center"/>-->
    </div>

    <%@include file="footer.jspf" %>
</div>




</body>

</html>

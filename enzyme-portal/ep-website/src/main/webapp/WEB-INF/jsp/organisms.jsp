<%--
    Document   : organisms
    Created on : Nov 3, 2014, 4:45:22 PM
    Author     : joseph
--%>


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="Fn" uri="/WEB-INF/epTagLibray.tld" %>
<!DOCTYPE html>
<html ng-app="enzyme-portal-app">
<head>
    <c:set var="pageTitle" value="Model Organisms"/>
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

<div id="wrapper">
    <%@include file="header.jspf" %>
    <script src="http://d3js.org/d3.v3.min.js"></script>
    <script>


    </script>

    <div id="content" role="main" class="clearfix">
        <h1>Taxonomy</h1>
        <div id="taxonomy-tree" taxonomy-tree></div>
        <img id="spinner" ng-src="${pageContext.request.contextPath}/resources/images/loading128.gif" style="display:none;" class="center"/>
    </div>

    <%@include file="footer.jspf" %>
</div>
<!--! end of #wrapper -->
</body>
</html>

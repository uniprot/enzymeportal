<%-- 
    Document   : browse
    Created on : Jul 23, 2013, 12:17:33 PM
    Author     : joseph
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

    <div id="content" role="main" class="grid_24 clearfix" ng-controller="TypeAheadController">
        <h1>Diseases</h1>

        <div class="container-browse-search">
            <%--<input type="text" ng-model="selected" typeahead="state for state in states | filter:$viewValue | limitTo:8" class="form-control">--%>
            <input id="disease-input" class="browse-search" type="text"
                   ng-model="diseasesTypeAheadController"
                   placeholder="Disease name"
                   typeahead="disease for disease in getDiseases($viewValue)"
                   class="form-control"
                   typeahead-on-select="onSelectDiseases($item, $model, $label)"
                   typeahead-no-results="noResults">
                <div ng-show="noResults">
                    No Results Found
                </div>
        </div>

        <c:if test="${not empty diseaseList}">
            <ep:alphabeticalDisplay items="${diseaseList}" type="diseases" maxDisplay="5"/>
        </c:if>
<%-- 
        <c:if test="${not empty alldiseaseList}">
            <div class="grid_24">

                <h3 style="text-align: center">Diseases that starts with letter ${startsWith}</h3>
                <ul>
                    <c:forEach var="data" items="${alldiseaseList}">
                        <div class="grid_6">

                            <li><a   href="${pageContext.request.contextPath}/search-disease?entryid=${data.meshId}&entryname=${data.diseaseName}&AMP;searchparams.type=KEYWORD&searchparams.previoustext=${data.diseaseName}&searchparams.start=0&searchparams.text=${data.diseaseName}">${data.diseaseName} (${0})</a></li>
                        </div>

                    </c:forEach>

                </ul>

            </div>

        </c:if>
            --%>

    </div>

    <%@include file="footer.jspf" %>
</div>
<!--! end of #wrapper -->
</body>

</html>

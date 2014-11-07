<%-- 
    Document   : pathways
    Created on : Oct 31, 2014, 11:38:19 AM
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
        <c:set var="pageTitle" value="Browse pathways"/>
        <%@include file="head.jspf" %>
    <body>
    <div id="wrapper" class="container_24">
        <%@include file="header.jspf" %>

        <div id="content" role="main" class="grid_24 clearfix">
        <h1>Pathways</h1>

        <input type="text" ng-model="pathwayTypeAheadController" placeholder="Pathway name" typeahead="pathway for pathway in getPathways($viewValue)" class="form-control">

<!--         <c:forEach var="data" items="${pathwayList}">
            <a href="${pageContext.request.contextPath}/search/pathways?entryid=${data.pathwayId}&entryname=${data.pathwayName}&AMP;searchparams.type=KEYWORD&searchparams.previoustext=${data.pathwayName}&searchparams.start=0&searchparams.text=${data.pathwayName}">${data.pathwayName}</a>
            <br/>
        </c:forEach>
 -->        </div>
    </div>
    <%@include file="footer.jspf" %>
    </body>
</html>
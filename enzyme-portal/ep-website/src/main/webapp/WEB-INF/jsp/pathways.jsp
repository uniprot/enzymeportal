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
<%@ taglib prefix="ep" tagdir="/WEB-INF/tags/" %>

<!DOCTYPE html>
<html ng-app="enzyme-portal-app">
        <c:set var="pageTitle" value="Browse pathways"/>
        <%@include file="head.jspf" %>
    <body>
    <div id="wrapper">
        <%@include file="header.jspf" %>

        <div id="content" role="main" class="clearfix" ng-controller="TypeAheadController">
            <h1>Pathways</h1>


            <div class="container-browse-search">
            <%--<input type="text" ng-model="selected" typeahead="state for state in states | filter:$viewValue | limitTo:8" class="form-control">--%>
                <input id="pathway-input" class="browse-search" type="text" ng-model="pathwayTypeAheadController"
                       placeholder="Pathway name" typeahead="pathway for pathway in getPathways($viewValue)"
                       class="form-control" typeahead-on-select="onSelectPathways($item, $model, $label)"
                       typeahead-no-results="noResults">
                <div ng-show="noResults">
                    No Results Found
                </div>

            </div>

            <c:if test="${not empty pathwayList}">
                <ep:alphabeticalDisplay items="${pathwayList}" type="pathways" maxDisplay="5"/>
            </c:if>


        </div>
        <%@include file="footer.jspf" %>
    </div>
    </body>
</html>

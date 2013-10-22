<%--
    Shows items to compare side by side, with the proper header and managing
    any lists.
    Requires page scope variable: Map$Entry<String,Comparison> theComparison
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="comparePath" value="${topComparison.key},${theComparison.key}"/>

<c:if test="${not fn:contains(comparePath, 'Protein structures')
    and not fn:contains(comparePath, 'Reactions and pathways')
    and not fn:contains(comparePath, 'Diseases')}">
    <section class="grid_12 comparison subheader" id="${theComparison.key}">
        ${theComparison.key}
    </section>
    <section class="grid_12 comparison subheader omega">
        ${theComparison.key}
    </section>
    <br clear="all"/>
</c:if>

<c:choose>
    <c:when test="${theComparison.value.class.simpleName eq 'ListComparison'}">
        <c:forEach var="sc" items="${theComparison.value.subComparisons}">
            <c:set var="theComparison" value="${sc}"/>
            <c:set var="comparePath" value="${comparePath},${theComparison.key}"/>
            <%@include file="comparison-sideBySide.jsp" %>
        </c:forEach>
    </c:when>
    <c:otherwise>
        <%@include file="comparison-sideBySide.jsp" %>
    </c:otherwise>
</c:choose>

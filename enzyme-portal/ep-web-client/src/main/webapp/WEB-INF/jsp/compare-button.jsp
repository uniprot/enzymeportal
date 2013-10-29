<?xml version="1.0" encoding="UTF-8" ?>
<%--
This JSP fragment displays a button to compare two enzymes. The enzymes to
compare are stored in a session attribute 'enzymesToCompare' of class Map (see
CompareController#updateComparableEnzymes).
This also requires search.js for a javascript function 'updateCompareButton'
used to update the tool-tip of the button with the enzyme names to be compared.
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<button id="compareButton" onclick="javascript:location='compare'"
    ${(empty enzymesToCompare or fn:length(enzymesToCompare) lt 2)?
        'disabled' : ''}>Compare</button>
<c:if test="${not empty enzymesToCompare}">
    <c:set var="single" value="\'"/>
    <c:set var="escapedSingle" value="\\\'"/>
    <c:set var='double' value='\"'/>
    <c:set var='escapedDouble' value='\\\"'/>
    <c:forEach var="entry" items="${enzymesToCompare}" varStatus="vs">
        <c:choose>
            <c:when test="${vs.index eq 0}">
                <c:set var="name1" value="${fn:replace(fn:replace(entry.value, single, escapedSingle), double, escapedDouble)}"/>
            </c:when>
            <c:when test="${vs.index eq 1}">
                <c:set var="name2" value="${fn:replace(fn:replace(entry.value, single, escapedSingle), double, escapedDouble)}"/>
            </c:when>
        </c:choose>
    </c:forEach>
</c:if>
<script>updateCompareButton('${name1}', '${name2}');</script>

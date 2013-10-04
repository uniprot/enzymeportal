<%--
    Shows one item.
    Requires page scope variable: Object item
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:choose>
    <c:when test="${item.class.simpleName eq 'Sequence'}">
        <a href="${item.sequenceurl}" target="_blank">${item.sequence}</a>
        amino acids.
        <%-- FIX THIS: MISSING DATA! Mass: ${item.weight} Da --%>
    </c:when>
    <c:otherwise>
        ${item}
    </c:otherwise>
</c:choose>
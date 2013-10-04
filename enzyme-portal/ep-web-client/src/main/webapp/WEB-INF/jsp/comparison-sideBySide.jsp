<%--
    Shows two items to compare side by side.
    Requires page scope variable: Map$Entry<String,Comparison> theComparison
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<section class="grid_12 ${theComparison.value.different? 'diff':'same'}">
    <c:set var="item" value="${theComparison.value.compared[0]}"/>
    <%@include file="comparison-item.jsp" %>
</section>
<section class="grid_12 omega ${theComparison.value.different? 'diff':'same'}">
    <c:set var="item" value="${theComparison.value.compared[1]}"/>
    <%@include file="comparison-item.jsp" %>
</section>
<br clear="all"/>

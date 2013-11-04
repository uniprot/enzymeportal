<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<c:set var="pageTitle" value="Comparing enzymes"/>
<%@include file="head.jspf"%>
<body class="level2">

    <%@include file="skipto.jspf" %>

    <div id="wrapper" class="container_24">
       
        <%@include file="header.jspf" %>
       
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
                                <li class="${ssc.value.different? 'diff' : 'same'}">
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
                <h2>Comparing enzymes</h2>
            </section>
            <br clear="all"/>

            <section class="grid_4 alpha" id="comparison-header">
                &nbsp;
            </section>
            <section class="grid_20 omega">
                <section class="grid_12 comparison header">
                    <a href="search/${comparison.compared[0].uniprotaccessions[0]}/enzyme">
                    ${comparison.compared[0].name}<br/>
                    (${comparison.compared[0].species.scientificname})
                    </a>
                </section>
                <section class="grid_12 omega comparison header">
                    <a href="search/${comparison.compared[1].uniprotaccessions[0]}/enzyme">
                    ${comparison.compared[1].name}<br/>
                    (${comparison.compared[1].species.scientificname})
                    </a>
                </section>
                <br clear="all"/>
            </section>

            <c:forEach var="topComparison" items="${comparison.subComparisons}">
                <section class="grid_4 alpha" id="${sc.key}">&nbsp;</section>
                <section class="grid_20 omega">
                    <fieldset class="comparison" id="${topComparison.key}">

                        <legend class="comparison header">${topComparison.key}</legend>

                        <c:choose>
                            <c:when test="${empty topComparison.value.subComparisons}">
                                <c:set var="theComparison" value="${topComparison}"/>
                                <%@include file="comparison-sideBySide.jsp" %>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="theComparison"
                                    items="${topComparison.value.subComparisons}">
                                    <%@include file="comparison-list.jsp" %>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </fieldset>
                </section>
            </c:forEach>
           
        </div>
       
        <%@include file="footer.jspf" %>
        
    </div>
</body>
</html>

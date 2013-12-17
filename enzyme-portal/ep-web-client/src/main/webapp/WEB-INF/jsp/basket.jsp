<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="epfn" uri="/WEB-INF/epTagLibray.tld" %>

<!DOCTYPE html>
<html>
<c:set var="pageTitle" value="Compare enzymes"/>
<%@include file="head.jspf"%>
<body class="level2">

    <%@include file="skipto.jspf" %>
    
    <div id="wrapper" class="container_24">
       
        <%@include file="header.jspf" %>
       
        <div id="content" role="main" class="grid_24 clearfix">

            <section class="grid_24 alpha">
                <h2>Compare enzymes</h2>
            </section>
            <br clear="all"/>

            <form action="compare" method="post"
                style="font-family: verdana, sans-serif;">

            <section class="grid_19 alpha">
                <div id="basketEmptyMsg"
                    style="display: ${empty basket? 'block':'none'}">
                    <spring:message code="basket.empty"/>
                </div>
                <div id="basketOneMsg"
                    style="display: ${fn:length(basket) eq 1? 'block':'none'}">
                    <spring:message code="basket.one"/>
                </div>
                <div id="basketFullMsg"
                    style="display: ${fn:length(basket) gt 1? 'block':'none'}">
                    <spring:message code="basket.full" htmlEscape="false"
                        arguments="${fn:length(basket)}"/>
                </div>
            </section>
            <section class="grid_4">
                <c:if test="${fn:length(basket) gt 1}">
                    <button id="compareButton" disabled
                        class="icon icon-functional"
                        data-icon="O">Compare selected</button>
                </c:if>
            </section>
            <section class="grid_1 omega">
            </section>
            
            <c:forEach var="basketItem" items="${basket}" varStatus="vsEnzymes">
                <c:set var="enzyme" value="${basketItem.value}"/>
                <div>
                <section class="grid_19 alpha">
                    <%@include file="util/prioritiseSpecies.jsp" %>
                    <%@include file="summary.jspf" %>
                </section>
                <section class="grid_4">
                    <select class="toCompare" name="acc"
                        onchange="updateCompareButton(event)"
                        title="Select one species to compare.">
                        <c:forEach var="relSp" items="${enzyme.relatedspecies}">
                            <option value="${relSp.uniprotaccessions[0]}"
                                title="${relSp.species.commonname}">${relSp.species.scientificname}</option>
                        </c:forEach>
                        <option selected value=""
                            title="Deselect this enzyme"></option>
                    </select>
                </section>
                <section class="grid_1 omega">
                    <button class="removeFromBasket icon icon-functional"
                        title="Remove this summary from your basket."
                        data-icon="d"
                        value="${epfn:getSummaryBasketId(enzyme)}"
                        onclick="removeFromBasket(event)"></button>
                </section>
                </div>
            </c:forEach>
            
            <script>updateCompareButton();</script>

            </form>

        </div>
        
        <%@include file="footer.jspf" %>
        
    </div>
</body>
</html>

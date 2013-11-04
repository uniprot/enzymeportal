<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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

            <section class="grid_20 alpha">
                You have ${fn:length(basket)} enzymes to compare. Please select
                their species.
            </section>
            <section class="grid_4 omega basket">
                <button id="compareButton" disabled
                    class="icon icon-functional"
                    data-icon="O">Compare selected</button>
            </section>
            
            <c:forEach var="basketItem" items="${basket}" varStatus="vsEnzymes">
                <c:set var="enzyme" value="${basketItem.value}"/>
                <section class="grid_20 alpha">
                    <%@include file="util/prioritiseSpecies.jsp" %>
                    <%@include file="summary.jspf" %>
                </section>
                <section class="grid_4 omega">
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
            </c:forEach>
            
            <script>updateCompareButton();</script>

            </form>

        </div>
        
        <%@include file="footer.jspf" %>
        
    </div>
</body>
</html>

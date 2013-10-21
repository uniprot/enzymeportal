<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Enzyme Portal - Enzyme comparison</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta charset="utf-8">
    <meta name="description" content="EMBL-EBI">
    <meta name="keywords" content="bioinformatics, europe, institute">
    <meta name="author" content="EMBL-EBI, Cheminformatics and Metabolism Team">
    <meta name="viewport" content="width=device-width,initial-scale=1">

    <link rel="stylesheet" href="//www.ebi.ac.uk/web_guidelines/css/compliance/develop/embl-petrol-colours.css" type="text/css" media="screen">
    <link rel="stylesheet" href="//www.ebi.ac.uk/web_guidelines/css/compliance/mini/ebi-fluid-embl.css">
    <link rel="stylesheet" href="resources/css/enzyme.css" type="text/css" />
    <link rel="stylesheet" href="resources/skins/default/skin2.css" type="text/css" />

    <style type="text/css">
      /* You have the option of setting a maximum width for your page, and making sure everything is centered */
      /* body { max-width: 1600px; margin: 0 auto; } */
    </style>

    <script src="//www.ebi.ac.uk/web_guidelines/js/libs/modernizr.custom.49274.js"></script>
    <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
</head>
<body class="level2">

    <div id="skip-to">
        <ul>
            <li><a href="#content">Skip to main content</a></li>
            <li><a href="#local-nav">Skip to local navigation</a></li>
            <li><a href="#global-nav">Skip to EBI global navigation menu</a></li>
            <li><a href="#global-nav-expanded">Skip to expanded EBI global navigation menu (includes all sub-sections)</a></li>
        </ul>
    </div>

    <div id="wrapper" class="container_24">
       
        <%@include file="header.jsp" %>
       
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
                <h2>Comparing ${comparison.compared[0].uniprotaccessions[0]}
                    to ${comparison.compared[1].uniprotaccessions[0]}</h2>
                    ${applicationContext.uniprotConfig}
            </section>
            <br clear="all"/>

            <section class="grid_4 alpha" id="comparison-header">
                &nbsp;
            </section>
            <section class="grid_20 omega">
                <section class="grid_12 comparison header">
                    ${comparison.compared[0].name}<br/>
                    (${comparison.compared[0].species.scientificname})
                </section>
                <section class="grid_12 omega comparison header">
                    ${comparison.compared[1].name}<br/>
                    (${comparison.compared[1].species.scientificname})
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
       
        <%@include file="footer.jsp" %>
        
    </div>
</body>
</html>
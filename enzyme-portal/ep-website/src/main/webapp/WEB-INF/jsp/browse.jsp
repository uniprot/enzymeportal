<%-- 
    Document   : browse
    Created on : Jul 23, 2013, 12:17:33 PM
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


<!-- paulirish.com/2008/conditional-stylesheets-vs-css-hacks-answer-neither/ -->
<!--[if lt IE 7]> <html class="no-js ie6 oldie" lang="en"> <![endif]-->
<!--[if IE 7]> <html class="no-js ie7 oldie" lang="en"> <![endif]-->
<!--[if IE 8]> <html class="no-js ie8 oldie" lang="en"> <![endif]-->
<!-- Consider adding an manifest.appcache: h5bp.com/d/Offline -->
<!--[if gt IE 8]><!-->
<html class="no-js" lang="en"> <!--<![endif]-->
<head>
    <c:set var="pageTitle" value="Browse"/>
    <%@include file="head.jspf" %>


</head>

<body class="level2"><!-- add any of your classes or IDs -->
<div id="skip-to">
    <ul>
        <li><a href="#content">Skip to main content</a></li>
        <li><a href="#local-nav">Skip to local navigation</a></li>
        <li><a href="#global-nav">Skip to EBI global navigation menu</a></li>
        <li><a href="#global-nav-expanded">Skip to expanded EBI global navigation menu (includes all sub-sections)</a>
        </li>
    </ul>
</div>

<div id="wrapper" class="container_24">
    <%@include file="header.jspf" %>

    <div id="content" role="main" class="grid_24 clearfix">
        <c:if test="${not empty diseaseList}">

            <div class="grid_24">
                <div class="clear"></div>
                <c:set var="count" value="0"/>
                <c:set var="maxDisplay" value="5"/>
                <div class="grid_24">
                    <c:set var="alphabet" value="A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z"/>
                    <c:forTokens var="letter" items="${alphabet}" delims="," varStatus="status">
                        <div class="grid_6 disease-overview-box">
                            <c:set var="startsWith" value="${letter}"/>
                            <h3>${startsWith}</h3>
                            <ul>
                                <c:forEach var="data" items="${diseaseList}">

                                    <c:choose>
                                        <c:when test="${(not empty data.diseaseName) && Fn:startsWithLowerCase(data.diseaseName, startsWith) && (count <= maxDisplay)}">
                                            <c:set var="count" value="${count + 1}"/>

                                            <li>
                                                <a href="${pageContext.request.contextPath}/search/disease?entryid=${data.meshId}&entryname=${data.diseaseName}&AMP;searchparams.type=KEYWORD&searchparams.previoustext=${data.diseaseName}&searchparams.start=0&searchparams.text=${data.diseaseName}">${data.diseaseName}</a></li>
                                        </c:when>
                                    </c:choose>

                                </c:forEach>
                            </ul>

                            <c:if test="${count gt maxDisplay}">
                                <div class="caption-right"><a href="${pageContext.request.contextPath}/browse/disease/${startsWith}">View
                                    all ${startsWith}</a></div>
                            </c:if>

                            <c:if test="${count == 0}">
                                <p><em>none</em></p>
                            </c:if>
                            <c:set var="count" value="0"/>
                        </div>
                        <c:if test="${status.count%4 == 0}">
                            <div class="clear"></div>
                        </c:if>
                    </c:forTokens>

                </div>
            </div>

        </c:if>
    </div>

    <%@include file="footer.jspf" %>
</div>
<!--! end of #wrapper -->
</body>

</html>

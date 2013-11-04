<%-- 
    Document   : error
    Created on : Sep 5, 2012, 2:18:11 PM
    Author     : joseph
--%>



<!doctype html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib  prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!-- paulirish.com/2008/conditional-stylesheets-vs-css-hacks-answer-neither/ -->
<!--[if lt IE 7]> <html class="no-js ie6 oldie" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js ie7 oldie" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js ie8 oldie" lang="en"> <![endif]-->
<!-- Consider adding an manifest.appcache: h5bp.com/d/Offline -->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->

<c:set var="pageTitle" value="Error"/>
<%@include file="head.jspf" %>

<body class="level2">
    <%@include file="skipto.jspf" %>
    <div id="wrapper" class="container_24">
        <%@include file="header.jspf" %>

        <div id="content" role="main" class="grid_24 clearfix">
                
            <nav id="breadcrumb">
                <p><a href="/enzymeportal">Enzyme Portal</a> &gt; Error</p>
            </nav>
                
            <c:choose>
                <c:when test="${not empty errorCode}">
                    <%-- No-op, everything done below by spring:message --%>
                </c:when>
                <c:when test="${searchModel.searchparams.type eq 'SEQUENCE'}">
                    <c:set var="errorCode" value="search"/>
                    <c:set var="errorParam" value="sequence search"/>
                    <c:set var="searchText" value="your sequence search"/>	
                </c:when>
                <c:otherwise>
                    <c:set var="errorCode" value="search"/>
                    <c:set var="errorParam"
                        value="search for ${searchModel.searchparams.text}"/>
                    <c:set var="searchText"
                           value="your search for ${searchModel.searchparams.text}"/>
                </c:otherwise>
            </c:choose>
            
            <section>
                <h2><spring:message htmlEscape="false"
                    code="error.${errorCode}.title"/>
                </h2>
                <p class="alert">
                    <spring:message code="error.${errorCode}"
                        arguments="${errorParam}"/>
                </p>
            </section>
            
        </div>

        <%@include file="footer.jspf" %>
    </div> <!--! end of #wrapper -->

</body>
</html>


<%-- 
    Document   : status
    Created on : Oct 20, 2015, 1:46:04 PM
    Author     : joseph
--%>

 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib  prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="Fn" uri="/WEB-INF/epTagLibray.tld" %> 

<!doctype html>
<!-- paulirish.com/2008/conditional-stylesheets-vs-css-hacks-answer-neither/ -->
<!--[if lt IE 7]> <html class="no-js ie6 oldie" lang="en"> <![endif]-->
<!--[if IE 7]> <html class="no-js ie7 oldie" lang="en"> <![endif]-->
<!--[if IE 8]> <html class="no-js ie8 oldie" lang="en"> <![endif]-->
<!-- Consider adding an manifest.appcache: h5bp.com/d/Offline -->
<!--[if gt IE 8]><!-->
<html class="no-js" lang="en"> <!--<![endif]-->
<c:set var="pageTitle" value="Status &lt; Enzyme Portal &gt; EMBL-EBI"/>
<%@include file="head.jspf" %>

<body class="level2"><!-- add any of your classes or IDs -->
<%@include file="skipto.jspf" %>

<div id="wrapper" class="container_24">

    <%@include file="header.jspf" %>

    <div id="content" role="main" class="grid_24 clearfix">

        <!-- If you require a breadcrumb trail, its root should be your service.
                You don't need a breadcrumb trail on the homepage of your service... -->
        <nav id="breadcrumb">
            <p>
                <a href=".">Enzyme Portal</a> &gt;
                Status
            </p>
        </nav>

        <section>
            <c:set var="stat" value=""/>
            <c:choose>
                <c:when test="${status eq 'UP'}">
                <c:set var="stat" value="green"/>    
                </c:when>
                <c:otherwise>
                 <c:set var="stat" value="red"/>   
                </c:otherwise>
            </c:choose>
            
            <h1 id="status-${status}" class="${stat}" style="text-align: center">Enzyme Portal service is ${status}</h1>
            
        </section>

        <!-- End example layout containers -->

    </div>

    <%@include file="footer.jspf" %>

</div>
<!--! end of #wrapper -->

</body>

</html>

<%--
    Document   : error
    Created on : Sep 5, 2012, 2:18:11 PM
    Author     : joseph
--%>



<!doctype html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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

            <div id="content" role="main" class="clearfix">

                <nav id="breadcrumb">
                    <p><a href="/enzymeportal">Enzyme Portal</a> &gt; Error</p>
                </nav>


                <section>
                    <c:set var="errorParam" value=" search request"/>
                    <h2>Service Error - ${error}</h2>

                    <p class="alert">
                        We are sorry but we couldn't find anything that matches your ${errorParam}. Please try again later
                    </p>


                </section>

            </div>

            <%@include file="footer.jspf" %>
        </div> <!--! end of #wrapper -->

    </body>
</html>

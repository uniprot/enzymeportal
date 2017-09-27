<%-- 
    Document   : proteins
    Created on : Sep 26, 2017, 2:35:15 PM
    Author     : <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="Fn" uri="/WEB-INF/epTagLibray.tld" %>
<%@taglib prefix="ep" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
    <head>
        <c:set var="pageTitle" value="Enzymes"/>
        <%@include file="head.jspf" %>

    </head>
    <body class="level2 full-width"><!-- add any of your classes or IDs -->
        <div id="skip-to">
            <ul>
                <li><a href="#content">Skip to main content</a></li>
                <li><a href="#local-nav">Skip to local navigation</a></li>
                <li><a href="#global-nav">Skip to EBI global navigation menu</a></li>
                <li><a href="#global-nav-expanded">Skip to expanded EBI global navigation menu (includes all sub-sections)</a>
                </li>
            </ul>
        </div>
        <div id="wrapper" class="">
            <%@include file="header.jspf" %>
            <div id="content" role="main" class="clearfix" >

                Protein result contents goes here 


            </div>
            <%@include file="footer.jspf" %>
        </div>
        <!--! end of #wrapper -->
    </body>
</html>
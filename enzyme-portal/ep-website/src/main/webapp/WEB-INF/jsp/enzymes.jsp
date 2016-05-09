<%-- 
    Document   : enzymes
    Created on : May 6, 2016, 11:39:04 AM
    Author     : Joseph <joseph@ebi.ac.uk>
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

    <div id="content" role="main" class="grid_24 clearfix" >
        <h1>Dummy Enzymes</h1>



        <c:if test="${not empty enzymes}">
            <div class="grid_24">

                   <ul>
                    <c:forEach var="e" items="${enzymes}">
                     
                        <div><b>Name</b> : ${e.enzymeName} , EC : ${e.ecNumber} , Activities : ${e.catalyticActivity}, Hits : ${fn:length(e.dummyProteinSet)} </div>
                    </c:forEach>

                </ul>

            </div>

        </c:if>

    </div>

    <%@include file="footer.jspf" %>
</div>
<!--! end of #wrapper -->
</body>
</html>

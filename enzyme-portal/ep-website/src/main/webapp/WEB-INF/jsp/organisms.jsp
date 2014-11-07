<%-- 
    Document   : organisms
    Created on : Nov 3, 2014, 4:45:22 PM
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
<html>
    <head>
   <c:set var="pageTitle" value="Model Organisms"/>
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
      
        <h5 class="grid_6 disease-overview-box push_7">Showing ${fn:length(organisms)} Model Organisms </h5>
         <div class="clear"></div>
         <br/>
    <c:forEach var="data" items="${organisms}">
        <div  class="grid_6 disease-overview-box push_7">
        <a   href="${pageContext.request.contextPath}/taxonomy?entryid=${data.taxId}&entryname=${data.scientificName}&AMP;searchparams.type=KEYWORD&searchparams.previoustext=${data.scientificName}&searchparams.start=0&searchparams.text=${data.scientificName}">${data.commonName} - ${data.scientificName} (${data.num_enzymes})</a>         
        </div>
         <div class="clear"></div>
   
        <br/> 
    </c:forEach>
       
      </div>

    <%@include file="footer.jspf" %>
</div>
<!--! end of #wrapper -->
</body>
</html>

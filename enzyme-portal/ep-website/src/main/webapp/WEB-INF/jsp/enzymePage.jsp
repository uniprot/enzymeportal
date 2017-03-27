<%--
    Document   : enzymePage
    Created on : Feb 21, 2017, 3:05:34 PM
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

        <%@include file="head.jspf" %>
        <title>Enzyme Portal Enzyme Page</title>
    </head>
    <body>



    <div id="skip-to">
        <ul>
            <li><a href="#content">Skip to main content</a></li>
            <li><a href="#local-nav">Skip to local navigation</a></li>
            <li><a href="#global-nav">Skip to EBI global navigation menu</a></li>
            <li><a href="#global-nav-expanded">Skip to expanded EBI global navigation menu (includes all sub-sections)</a>
            </li>
        </ul>
    </div>
    <script>
        var speciesAutocompleteDataSource = [];
        var compoundsAutoCompleteDataSource = [];
        var diseaseAutoCompleteDataSource = [];
        var ecAutoCompleteDataSource = [];
    </script>

    <div id="wrapper" class="container_24">
        <%@include file="header.jspf" %>

        <div id="content" role="main" class="grid_24 clearfix" >

            <section class="grid_3">
                <img src="/enzymeportal/resources/images/enzyme_page_logo.jpg">
            </section>

            <section class="grid_15 alpha">
                <h1>Enzyme</h1>
                <h2>${enzymePage.ec} - ${enzymePage.enzymeName}</h2>
            </section>

            <section class="grid_6 alpha">
                <div id="alternative-names">
                    <strong>Alternative Name(s):</strong>
                    <p>${enzymePage.altName}</p>
                </div>
            </section>

            <section>
                <h3>Catalytic Activity</h3>
                <p>${enzymePage.catalyticActivities}</p>
NOTE --
1. title should be Cofactor , and not Co Factor
2.only show cofactor if available : hint use c:if check here.
3. cofactor is a list, pls use c:forEach to loop item
                <h3>Co Factors</h3>
                <p>${enzymePage.cofactor}</p>


                <c:if test="${not empty enzymePage.accessions }">
                    <h3>Associated Proteins</h3>
                    <c:forEach items="${enzymePage.accessions}" var="acc">
                        <ul>
                            <li> <a href="${pageContext.request.contextPath}/search/${acc}/enzyme">${acc}</a></li>
                        </ul>
                    </c:forEach>
                 </c:if>

                 <c:if test="${not empty enzymePage.accessions }">
                    <h3>Citations</h3>
                    <c:forEach items="${enzymePage.citations}" var="citation">
                        <ul>
                            <li> <c:out value="${citation.title}"/></li>
                        </ul>
                    </c:forEach>
               </c:if>
            </section>

        </div>

       <%@include file="footer.jspf" %>
   </div>






    </body>
</html>

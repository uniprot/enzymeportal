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
        <h1>Demo</h1>
        <h2>${enzymePage.enzymeName}</h2>
        <h3>${enzymePage.ec}</h3>
        <h4>${enzymePage.catalyticActivities}</h4>
        
        <h2>characterized Proteins</h2>
        <p>todo ...</p>

        <h2>Associated Proteins</h2>

        <c:forEach items="${enzymePage.accessions}" var="acc">
            <ul>
                <li> <a href="${pageContext.request.contextPath}/search/${acc}/enzyme">${acc}</a></li>

            </ul>
        </c:forEach>

        <h2>Publications</h2>

        <c:forEach items="${enzymePage.citations}" var="citation">
            <ul>
                <li> <c:out value="${citation.title}"/></li>

            </ul>
        </c:forEach>            

    </body>
</html>

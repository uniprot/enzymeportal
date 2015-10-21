<%-- 
    Document   : stat
    Created on : Oct 21, 2015, 12:52:09 PM
    Author     : joseph
--%>


 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib  prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="Fn" uri="/WEB-INF/epTagLibray.tld" %> 

<%-- 
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
    </body>
</html>
--%>

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
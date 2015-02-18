<%-- 
    Document   : reactions
    Created on : Oct 31, 2014, 11:30:03 AM
    Author     : joseph
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Browse | Reactions</title>
    </head>
    <body>
        <h1>Reactions</h1>
    <c:forEach var="data" items="${reactionList}">
        
        <a href="${pageContext.request.contextPath}/search/reactions?entryid=${data.reactionId}&entryname=${data.reactionName}&AMP;searchparams.type=KEYWORD&searchparams.previoustext=${data.reactionName}&searchparams.start=0&searchparams.text=${data.reactionName}">${data.reactionName}</a>
        <br/>
        
    </c:forEach>
    </body>
</html>

<%-- 
    Document   : searchBox
    Created on : Aug 21, 2011, 12:29:50 PM
    Author     : hongcao
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib  prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

    <div class="grid_12">
        <div  id="keywordSearch" class="searchBackground">
            <p>
                <form:input id="searchbox" path="searchparams.text" cssClass="field" rel="Enter a name to search"/>
                <form:hidden id="start" path="searchparams.start" />
                <form:hidden path="searchparams.previoustext" />
                <input id ="searchButton" type="submit" value="Search" class="searchButton" />
                <br/>
                <spring:message code="label.search.example"/>
            </p>
        </div>
    </div>

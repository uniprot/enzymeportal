<%-- 
    Document   : disease
    Created on : Sep 6, 2011, 6:54:12 PM
    Author     : hongcao
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div id="diseaseContent" class="summary">
<h2><c:out value="${enzymeModel.name}"/></h2>
    <c:set var="diseases" value="${enzymeModel.disease}"/>
        <c:set var="diseasesSize" value="${fn:length(diseases)}"/>
        <c:set var="plural" value="y"/>
        <c:if test='${diseasesSize > 1}'>
            <c:set var="plural" value="ies"/>
        </c:if>

    <c:if test='${diseasesSize == 0}'>
        <p><spring:message code="label.entry.molecules.empty" arguments="diseases"/></p>
    </c:if>
    <c:if test='${diseasesSize > 0}'>
    <p><spring:message code="label.entry.disease.found" arguments="${diseasesSize},${plural}"/></p>
    <c:forEach items="${diseases}" var="disease">
        <ul>
            <li>
            <c:out value="${disease.description}"/>
            </li>
        </ul>
    </c:forEach>
    </c:if>
</div>
                                        
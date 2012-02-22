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
            <b style=" font-size:medium" ><a href="${disease.url}" > <c:out value="${disease.name}"/></a></b><br/>
            <c:out value="${disease.description}"/><br/>

            <c:set var="ev" value="${disease.evidence}" />
        </c:forEach>

        <c:forEach items="${ev}" var="v">
            <ul>
                <li> <c:out value="${v}"/></li>

            </ul>
        </c:forEach>
        <div class="note_0">
            <a class="note_0" href="http://www.uniprot.org/uniprot/${enzymeModel.uniprotaccessions[0]}.html#section_comments">View disease evidence in UniProt</a>
        </div><br/>
        <div class="provenance">
            <ul>
                <li class="note_0">Data Sources:
                	<a href="http://www.ebi.ac.uk/efo">EFO</a> and
                    <a href="http://www.uniprot.org">UniProt</a></li>
                <li class="note_1">The mission of UniProt is to provide the
                    scientific community with a comprehensive, high-quality and freely
                    accessible resource of protein sequence and functional information</li>
                <li class="note_2">The Experimental Factor Ontology (EFO) is an
                	application focused ontology modelling the experimental
                	factors in ArrayExpress.</li>
            </ul>
        </div>
    </c:if>



</div>


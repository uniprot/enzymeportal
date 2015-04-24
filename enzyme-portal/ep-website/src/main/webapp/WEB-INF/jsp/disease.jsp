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
    <c:set var="diseases" value="${enzymeModel.disease}"/>
    <c:set var="diseasesSize" value="${fn:length(diseases)}"/>
    <c:set var="plural" value="y"/>
    <c:if test='${diseasesSize > 1}'>
        <c:set var="plural" value="ies"/>
    </c:if>

    <c:if test='${diseasesSize == 0}'>
        <p><spring:message code="label.entry.molecules.empty" arguments="disease"/></p>
    </c:if>
    <c:if test='${diseasesSize > 0}'>
        <p><spring:message code="label.entry.disease.found" arguments="${diseasesSize},${plural}"/></p>
        <c:forEach items="${diseases}" var="disease" varStatus="count">
            <br/><b style=" font-size:medium" ><a href="${disease.url}" target="_blank" > <c:out value="${disease.name}"/></a></b><br/>
            <c:out value="${disease.description}"/><br/>

            <c:set var="ev" value="${disease.evidences}" />
        </c:forEach>
            <hr/>
            <br/>
            <section>
         <c:if test="${fn:length(disease.evidences) > 0}">
            <h3>Disease Evidence</h3>
           
        <c:forEach items="${ev}" var="v">
            <ul>
                <li> <c:out value="${v}"/></li>

            </ul>
        </c:forEach>
            </c:if>
        <div class="note_0">
            <a class="note_0" href="http://www.uniprot.org/uniprot/${enzymeModel.uniprotaccessions[0]}.html#section_comments" target="_blank">View disease evidence in UniProt</a>
        </div>
                
            </section>
        <br/>
        
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


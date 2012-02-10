<%-- 
    Document   : molecules
    Created on : Aug 8, 2011, 6:18:07 PM
    Author     : hongcao
--%>

<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div id="moleculeContent" class="summary">
    <h2><c:out value="${enzymeModel.name}"/></h2>
    <c:set var="molecules" value="${enzymeModel.molecule}"/>
    <div id="molecules">
    	<c:choose>
    		<c:when test="${empty molecules}">
        		<p><spring:message code="label.entry.tab.empty"
        			arguments="small molecules"/></p>
    		</c:when>
    		<c:otherwise>
	            <c:set var="moleculeGroup" value="${molecules.bioactiveLigands}"/>
	            <c:set var="emptyArgs" value="bioactive compounds"/>
	            <c:set var="titleArgs" value="Bioactive compounds,bind to"/>
	            <c:set var="explArgs" value="Bioactive compounds,bind to"/>
	            <div id="bioactiveLigands">
					<%@include  file="moleculeGroup.jsp" %>
	            </div>

	            <c:set var="moleculeGroup" value="${molecules.drugs}"/>
	            <c:set var="emptyArgs" value="drugs"/>
	            <c:set var="titleArgs" value="Drugs,interact with"/>
	            <c:set var="explArgs" value="drugs,interact with"/>
	            <div id="drugs">
					<%@include  file="moleculeGroup.jsp" %>
	            </div>
	
	            <c:set var="moleculeGroup" value="${molecules.activators}"/>
	            <c:set var="emptyArgs" value="activators"/>
	            <c:set var="titleArgs" value="Activators,activate"/>
	            <c:set var="explArgs" value="activators,activate"/>
	            <div id="activators">
					<%@include  file="moleculeGroup.jsp" %>
	            </div>
	
	            <c:set var="moleculeGroup" value="${molecules.inhibitors}"/>
	            <c:set var="emptyArgs" value="inhibitors"/>
	            <c:set var="titleArgs" value="Inhibitors,inhibit"/>
	            <c:set var="explArgs" value="inhibitors,inhibit"/>
	            <div id="inhibitors">
					<%@include  file="moleculeGroup.jsp" %>
	            </div>
<%--
	            <c:set var="moleculeGroup" value="${molecules.cofactors}"/>
	            <c:set var="emptyArgs" value="cofactors"/>
	            <c:set var="titleArgs" value="Cofactors,activate"/>
	            <c:set var="explArgs" value="cofactors,activate"/>
	            <div id="activators">
					<%@include  file="moleculeGroup.jsp" %>
	            </div>
 --%>	
		        <div class="provenance">
		            <ul>
		                <c:set var="provenance" value="${molecules.provenance}"/>
		
		                <c:forEach var="prov" items="${provenance}"
		                           varStatus="vsProv">
		                    <li class="note_${vsProv.index}">${prov}</li>
		                </c:forEach>
		            </ul>
		        </div>
    		</c:otherwise>
    	</c:choose>
    	
    </div>
</div>

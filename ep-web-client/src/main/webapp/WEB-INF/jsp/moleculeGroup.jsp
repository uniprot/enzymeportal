<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:choose>
	<c:when test="${empty moleculeGroup}">
	    <br/>
	    <div>
	        <spring:message code="label.entry.molecules.empty"
	        	arguments="${emptyArgs}"/>
	    </div>
	</c:when>
	<c:otherwise>
	    <fieldset>
	        <legend>
	            <spring:message code="label.entry.molecules.sub.title"
	            	arguments="${titleArgs}"/>
	        </legend>
	        <p>
	        <spring:message code="label.entry.molecules.explanation"
	        	arguments="${explArgs}" />
	        </p>
	        <div style="display: table-row;">
		        <c:forEach var="molecule" items="${moleculeGroup}"
                      begin="0"
                      end="${fn:length(moleculeGroup) gt 3?
                      (2) :
                      (fn:length(moleculeGroup)-1)}">
			        <div style="display: table-cell; vertical-align: top;">
			        	<%@include file="molecule.jsp" %>
					</div>
		        </c:forEach>
		    </div>
	        <c:if test="${fn:length(moleculeGroup) gt 3}">
		        <c:choose>
		        	<c:when test="${moleculeGroupDb eq 'ChEMBL'}">
		        		See all ${fn:length(moleculeGroup)} ${emptyArgs}
			        	in ${moleculeGroupDb}:
						<div id="target_uniprot"
							style="height:250px; width:400px; clear:both">
							Loading ChEMBL data...
							<img src="${pageContext.request.contextPath}/resources/images/loading.gif"
							    alt="Loading..."/>
					    </div>
						<script type="text/javascript"
							src='https://www.ebi.ac.uk/chembldb/index.php/widget/create/target_uniprot/target/compound_mw/uniprot:${enzymeModel.uniprotaccessions[0]}'></script>		        	</c:when>
		        	<c:otherwise>
				        <a href="${moleculeGroupUrl}">See all
				        	${fn:length(moleculeGroup)} ${emptyArgs}
				        	in ${moleculeGroupDb}</a>
		        	</c:otherwise>
		        </c:choose>
	        </c:if>
	    </fieldset>
	</c:otherwise>
</c:choose>

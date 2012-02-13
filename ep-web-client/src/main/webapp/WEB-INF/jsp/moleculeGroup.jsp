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
	        <%-- TODO --%>
	        <a href="${moleculeGroupUrl}">See all ${fn:length(moleculeGroup)}
	        	${emptyArgs} in ${moleculeGroupDb}</a>
	        </c:if>
	    </fieldset>
	</c:otherwise>
</c:choose>

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
	        <c:forEach var="molecule" items="${moleculeGroup}">
	        
				<c:set var="displayName"
					value="${empty molecule.name? molecule.id : molecule.name}"/>
				
				<fieldset class="epBox">
					<c:choose>
						<c:when test="${empty molecule.url}">
							${displayName}
						</c:when>
						<c:otherwise>
							<a href="${molecule.url}" target="blank">${displayName}</a>
						</c:otherwise>
					</c:choose>
					<div>
						<c:if test="${not empty molecule.id and fn:startsWith('CHEBI:', molecule.id)}">
							<div>
								<c:when test="${empty molecule.url}">
									<img src="${chebiImageBaseUrl}${molecule.id}"
										alt="${displayName}" />
								</c:when>
								<c:otherwise>
									<a target="blank" href="${molecule.url}">
										<img src="${chebiImageBaseUrl}${molecule.id}"
											alt="${displayName}" />
									</a>
								</c:otherwise>
							</div>
						</c:if>
						<div>
							<div>${molecule.description}</div>
							<c:if test="${not empty molecule.formula}">
								<div>
									<span class="bold"><spring:message
											code="label.entry.molecules.formula" /></span>:
									${molecule.formula}
								</div>
							</c:if>
						</div>
					</div>
				</fieldset>

        </c:forEach>
    </fieldset>
</c:otherwise>
</c:choose>

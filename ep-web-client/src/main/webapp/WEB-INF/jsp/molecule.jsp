<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
	        
<c:set var="displayName"
	value="${empty molecule.name? molecule.id : molecule.name}"/>

<fieldset class="epBox">
	<%-- Add URL if we only have ID --%>
	<c:if test="${empty molecule.url and not empty molecule.id}">
		<c:choose>
			<c:when test="${fn:startsWith(molecule.id, 'DB')}">
				<c:set var="theUrl" value="http://www.drugbank.ca/drugs/${molecule.id}"/>
			</c:when>
			<c:when test="${fn:startsWith(molecule.id, 'CHEMBL')}">
				<c:set var="theUrl"
					value="https://www.ebi.ac.uk/chembldb/compound/inspect/${molecule.id}"/>
			</c:when>
		</c:choose>
	</c:if>
	<a href="${empty molecule.url? theUrl : molecule.url}"
		target="blank">${displayName}</a>
	<div>
		<c:if test="${not empty molecule.id and fn:startsWith(molecule.id, 'CHEBI:')}">
			<div style="width: 200px;">
				<c:choose>
					<c:when test="${empty molecule.url}">
						<img src="${chebiImageBaseUrl}${molecule.id}"
							alt="(No image available)" />
					</c:when>
					<c:otherwise>
						<a target="blank" href="${molecule.url}">
							<img src="${chebiImageBaseUrl}${molecule.id}"
								alt="(No image available)" />
						</a>
					</c:otherwise>
				</c:choose>
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

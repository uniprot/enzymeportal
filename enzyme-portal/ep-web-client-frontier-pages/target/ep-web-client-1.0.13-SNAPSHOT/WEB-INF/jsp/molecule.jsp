<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
	        
<c:set var="displayName"
	value="${empty molecule.name? molecule.id : molecule.name}"/>

<%-- Compound URL --%>
<c:set var="compoundUrl" value="${molecule.url}"/>
<c:if test="${empty compoundUrl and not empty molecule.id}">
	<%-- Add URL if we only have ID --%>
	<c:choose>
		<c:when test="${fn:startsWith(molecule.id, 'DB')}">
			<c:set var="compoundUrl"
				value="http://www.drugbank.ca/drugs/${molecule.id}"/>
		</c:when>
		<c:when test="${fn:startsWith(molecule.id, 'CHEMBL')}">
			<c:set var="compoundUrl"
				value="https://www.ebi.ac.uk/chembldb/compound/inspect/${molecule.id}"/>
		</c:when>
	</c:choose>
</c:if>

<%-- Image src --%>
<c:if test="${not empty molecule.id}">
	<c:choose>
		<c:when test="${fn:startsWith(molecule.id, 'CHEBI:')}">
			<c:set var="compoundImgUrl"
				value="${chebiImageBaseUrl}${molecule.id}"/>
		</c:when>
		<c:when test="${fn:startsWith(molecule.id, 'CHEMBL')}">
			<c:set var="compoundImgUrl"
				value="${chemblImageBaseUrl}${molecule.id}"/>
		</c:when>
		<c:otherwise>
			<c:set var="compoundImgUrl" value=""/>
		</c:otherwise>
	</c:choose>
</c:if>

<fieldset class="epBox">
	<c:choose>
		<c:when test="${empty compoundUrl}">
			${displayName}
		</c:when>
		<c:otherwise>
			<a href="${compoundUrl}" target="blank">${displayName}</a>
		</c:otherwise>
	</c:choose>
	<div>
		<c:if test="${not empty compoundImgUrl}">
			<div style="width: 200px;">
				<c:choose>
					<c:when test="${empty compoundUrl}">
						<img src="${compoundImgUrl}"
							alt="(No image available)" />
					</c:when>
					<c:otherwise>
						<a target="blank" href="${compoundUrl}">
							<img src="${compoundImgUrl}"
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

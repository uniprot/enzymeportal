<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="epfn" uri="/WEB-INF/epTagLibray.tld" %>
	        
<c:set var="displayName"
	value="${empty molecule.name? molecule.id : molecule.name}"/>

<%-- Compound URL --%>
<c:set var="compoundUrl" value="${epfn:getMoleculeUrl(molecule)}"/>
<c:set var="compoundImgUrl" value="${epfn:getMoleculeImgSrc(molecule)}"/>

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
						<img src="${compoundImgUrl}" class="molecule"
							alt="(No image available)" />
					</c:when>
					<c:otherwise>
						<a style="border-bottom-style: none" target="blank" href="${compoundUrl}">
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

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%--
This JSP fragment exports a variable 'theSpecies' whose value defaults to the
main (default) species for the enzyme object.
If the main species is not included in any existing species filter, the related
species are inspected so that the first match (related species - species filter)
is selected and exported as a 'theSpecies'.
 --%>

<c:set var="theSpecies" value="${enzyme}" />

<c:if test="${not empty searchModel.searchparams.species}">

	<c:set var="fixSp" value="true" />
	<c:forEach var="filterSp" items="${searchModel.searchparams.species}">
		<c:if test="${filterSp eq enzyme.species.scientificname}">
			<c:set var="fixSp" value="false" />
		</c:if>
	</c:forEach>

	<c:if test="${fixSp}">
		<c:set var="theSpecies" value="" />
		<c:forEach var="filterSp" items="${searchModel.searchparams.species}">
			<c:forEach var="sp" items="${enzyme.relatedspecies}">
				<c:if test="${empty theSpecies
					and sp.species.scientificname eq filterSp}">
					<c:set var="theSpecies" value="${sp}" />
				</c:if>
			</c:forEach>
		</c:forEach>
	</c:if>
        
</c:if>


 
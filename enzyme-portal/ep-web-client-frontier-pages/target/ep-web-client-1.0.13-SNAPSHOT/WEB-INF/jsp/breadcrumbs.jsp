<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="breadcrumbs" id="breadcrumbs">
	<ul>
	    <li class="first"><a href="http://www.ebi.ac.uk/" class="firstbreadcrumb">EBI</a></li>
		<li id="homeBreadcrumb"><a href="/enzymeportal">Enzyme Portal</a></li>
		<c:if test="${not empty history}">
			<c:forEach var="hItem" items="${history}">
				<li>
				<c:choose>
					<c:when test="${fn:startsWith(hItem, 'searchparams.text=')}">
						<a href="${pageContext.request.contextPath}/search?${hItem}&searchparams.start=0&searchparams.previoustext=">Search
							for <i>"${fn:substringAfter(hItem, '=')}"</i></a>
					</c:when>
					<c:otherwise>
						<a href="${pageContext.request.contextPath}/search/${hItem}/enzyme"><c:out value="${hItem}"/></a>
					</c:otherwise>
				</c:choose>
				
				</li>
			</c:forEach>
		</c:if>
	</ul>
</div>

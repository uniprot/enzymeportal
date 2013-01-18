<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<form id="breadcrumbs-form" action="${pageContext.request.contextPath}/search"
	method="POST">
	<input type="hidden" name="searchparams.type" id="breadcrumbs-form-type" />
	<input type="hidden" name="searchparams.text" id="breadcrumbs-form-text" />
	<input type="hidden" name="searchparams.sequence" id="breadcrumbs-form-sequence" />
	<input type="hidden" name="searchparams.previoustext" />
	<input type="hidden" name="searchparams.start" value="0" />
</form>

<script type="text/javascript">
/**
 * Sends the form associated to breadcrumbs with the proper values.
 * @param search The keyword/sequence search
 * @param type The type of search (KEYWORD|SEQUENCE)
 */
function submitForm(type, search){
	$('#breadcrumbs-form-type').val(type);
	if (type == 'KEYWORD'){
		$('#breadcrumbs-form-text').val(search);
	} else if (type == 'SEQUENCE'){
		$('#breadcrumbs-form-sequence').val(search);
	}
	document.forms['breadcrumbs-form'].submit();
}
</script>

<nav id="breadcrumb">
     	<p>
                <a href="${pageContext.request.contextPath}">Enzyme Portal</a> 

                    <c:if test="${not empty history}">
			<c:forEach var="hItem" items="${history}">
				&gt;
				<c:choose>
					<c:when test="${fn:startsWith(hItem, 'searchparams.text=')}">
						<a onclick="submitForm('KEYWORD', '${fn:substringAfter(hItem, '=')}')"
							>Search for
							<i>"${fn:substringAfter(hItem, '=')}"</i></a>
					</c:when>
					<c:when test="${fn:startsWith(hItem, 'searchparams.sequence=')}">
						<a onclick="submitForm('SEQUENCE', '${fn:substringAfter(hItem, '=')}')"
							>Search for sequence
<!--							<i>"${fn:substring(fn:substringAfter(hItem, '='), 0, 20)}${fn:length(hItem) gt 20? '...':''}"</i>-->
                                                </a>
					</c:when>
					<c:otherwise>
						<a href="${pageContext.request.contextPath}/search/${hItem}/enzyme"><c:out value="${hItem}"/></a>
					</c:otherwise>
				</c:choose>
				
				</li>
			</c:forEach>
		</c:if>

	</p>
</nav>

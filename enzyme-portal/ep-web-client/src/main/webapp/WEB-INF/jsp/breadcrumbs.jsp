<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="Fn" uri="/WEB-INF/epTagLibray.tld" %>

<form id="breadcrumbs-form" action="${pageContext.request.contextPath}/search"
      method="POST">
    <input type="hidden" name="searchparams.type" id="breadcrumbs-form-type" />
    <input type="hidden" name="searchparams.text" id="breadcrumbs-form-text" />
    <input type="hidden" name="searchparams.sequence" id="breadcrumbs-form-sequence" />
    <input type="hidden" name="searchparams.previoustext" />
    <input type="hidden" name="searchparams.start" value="0" />
</form>

<!--<script type="text/javascript">
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
</script>-->

<nav id="breadcrumb">
    <p>
        <a href="${pageContext.request.contextPath}">Enzyme Portal</a> 

        <c:if test="${not empty history}">
            <c:forEach var="hItem" items="${history}">
                &gt;
                <c:choose>                                      
                    <c:when test="${fn:startsWith(hItem, 'searchparams.text=')}">
                        <c:if test="${not Fn:lastInList(history, hItem)}">
                      
                                
                            <a href="${pageContext.request.contextPath}/search?searchparams.type=KEYWORD&${hItem}&searchparams.start=0&searchparams.previoustext=">Search
							for <i>"${fn:substringAfter(hItem, '=')}"</i></a>
                            </c:if>


                        <c:if test="${Fn:lastInList(history, hItem)}">
                            Search for <i>"${fn:substringAfter(hItem, '=')}"</i>
                        </c:if>
                    </c:when>
                    <c:when test="${fn:startsWith(hItem, 'searchparams.sequence=')}">
                        <c:if test="${not Fn:lastInList(history, hItem)}">
                             <a href="${pageContext.request.contextPath}/search?searchparams.type=SEQUENCE&${hItem}"
                               >Search for
                                <i>"${fn:substring(fn:substringAfter(hItem, '='), 0, 18)}${fn:length(hItem) gt 18? '...':''}"</i>
                            </a>
                        </c:if>

                        <c:if test="${Fn:lastInList(history, hItem)}">
                            Search for <i>"${fn:substring(fn:substringAfter(hItem, '='), 0, 18)}${fn:length(hItem) gt 18? '...':''}"</i>
                        </c:if>
                    </c:when>
                    <c:otherwise>
                        <c:if test="${not Fn:lastInList(history, hItem)}">
                            <a href="${pageContext.request.contextPath}/search/${hItem}/enzyme"><c:out value="${hItem}"/></a>

                        </c:if>
                        <c:if test="${Fn:lastInList(history, hItem)}">
                            <c:out value="${hItem}"/>
                        </c:if>

                    </c:otherwise>
                </c:choose>

                </li>
            </c:forEach>
        </c:if>

    </p>
</nav>

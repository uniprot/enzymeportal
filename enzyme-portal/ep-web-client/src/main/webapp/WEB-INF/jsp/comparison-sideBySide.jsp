<%--
    Shows two items to compare side by side.
    Requires page scope variable: Map$Entry<String,Comparison> theComparison
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:forEach var="i" begin="0" end="1">
    <c:set var="item" value="${theComparison.value.compared[i]}"/>
    <section class="grid_12
        ${(theComparison.value.different and not empty item)? 'diff':'same'}">
        <%@include file="comparison-item.jsp" %>
    </section>
</c:forEach>
<br clear="all"/>
<c:choose>
    <c:when test="${theComparison.key eq 'Sequence'}">
        <script>
        function sendUniprotAlignQuery(){
        	var alignQuery = '${uniprotConfig.alignUrl}'
        		   .replace(/\{0\}/, '${comparison.compared[0].uniprotaccessions[0]}')
                   .replace(/\{1\}/, '${comparison.compared[1].uniprotaccessions[0]}');
        	window.open(alignQuery);
        }
        </script>
        <section class="grid_8 alpha">&nbsp;</section>
        <section class="grid_8">
            <div class="${theComparison.value.different? 'diff':'same'}">
                <button type="submit" id="compare-sequence"
                    name="referrer" value="Enzyme portal"
                    onclick="sendUniprotAlignQuery()"
                	${theComparison.value.different? '' : 'disabled'}>
                    Compare protein sequences
                </button>
            </div>
        </section>
        <section class="grid_8 omega">&nbsp;</section>
    </c:when>
    <c:when test="${theComparison.key eq 'Protein structures'}">
        <script>
        function sendPdbeCompareQuery(){
        	var pdbeQuery = '${pdbStructureCompareUrl}'
        		   + 'q=' + $('#ps0').val() + ';t=' + $('#ps1').val();
        	window.open(pdbeQuery);
        }
        function showStructureImg(pdbId, which){
        	var imgSrc = '${pdbImgUrl}'.replace(/\{0\}/, pdbId);
        	$(which).attr('src', imgSrc);
        }
        </script>
        <section class="grid_8 alpha">
            <div class="${theComparison.value.different? 'diff':'same'}">
            <c:if test="${fn:length(theComparison.value.compared[0]) gt 0}">
                <img id="psImg0"
                    src="${fn:replace(pdbImgUrl, '{0}', theComparison.value.compared[0][0].id)}"/>
            </c:if>
            </div>
        </section>
        <section class="grid_8">
            <div class="${theComparison.value.different? 'diff':'same'}">
                <button type="submit" id="compare-ps"
                    name="referrer" value="Enzyme portal"
                    onclick="sendPdbeCompareQuery()"
                    ${fn:length(theComparison.value.compared[0]) eq 0 or
                        fn:length(theComparison.value.compared[1]) eq 0?
                        'disabled' : ''}>
                    Run protein structure similarity
                </button>
            </div>
        </section>
        <section class="grid_8 omega">
            <div class="${theComparison.value.different? 'diff':'same'}">
            <c:if test="${fn:length(theComparison.value.compared[1]) gt 0}">
                <img id="psImg1"
                    src="${fn:replace(pdbImgUrl, '{0}', theComparison.value.compared[1][0].id)}"/>
            </c:if>
            </div>
        </section>
    </c:when>
</c:choose>

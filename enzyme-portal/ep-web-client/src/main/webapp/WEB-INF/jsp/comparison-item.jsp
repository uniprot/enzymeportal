<%--
    Shows one item.
    Requires page scope variables:
        Object item: the item shown.
        Map$Entry<String,Comparison> theComparison: the comparison to which item
            belongs.
        Map$Entry<String,Comparison> topComparison: the top comparison to which
            theComparison belongs.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="epfn" uri="/WEB-INF/epTagLibray" %>

<c:choose>
    <c:when test="${topComparison.value.class.simpleName eq 'ListComparison'
        and empty topComparison.value.subComparisons}">
        No data available.
    </c:when>
    <c:when test="${theComparison.key eq 'Sequence'}">
        <a href="${item.sequenceurl}" target="_blank">${item.sequence}</a>
        amino acids.
        <span class="FIXME" style="display: none">Mass: ${item.weight} Da</span>
    </c:when>
    <c:when test="${theComparison.key eq 'Protein structures'}">
        <c:choose>
            <c:when test="${fn:length(item) eq 0}">
                No protein structures available.
            </c:when>
            <c:otherwise>
                <select id="ps${i}" size="5" class="ps"
                    onchange="showStructureImg(this.value, '#psImg${i}')">
                    <c:forEach var="ps" items="${item}" varStatus="psVs">
                        <option value="${ps.id}" title="${ps.name}"
                            ${psVs.first? 'selected':'' }>
                            ${ps.id} ${ps.name}
                        </option>
                    </c:forEach>
                </select>
            </c:otherwise>
        </c:choose>
    </c:when>
    <c:when test="${empty item}">
        &nbsp;
    </c:when>
    <c:when test="${topComparison.key eq 'Reactions and pathways'}">
        <div>
            <span class="comparison subheader">Reaction:</span>
            <br/>
            ${item.reaction.name}
        </div>
        <div style="margin-left: 2em">
            <span class="comparison subheader">Rhea identifier:</span>
            <a href="${urlConfig['rhea.reaction']}${item.reaction.id}"
                 target="_blank">RHEA:${item.reaction.id}</a>
            <c:if test="${not empty item.pathways}">
                <br/>
                <span class="comparison subheader">Pathways:</span>
                <c:forEach var="pathway" items="${item.pathways}" varStatus="vs">
                    ${pathway.id}${vs.last? '' : ','}
                </c:forEach>
            </c:if>
        </div>
    </c:when>
    <c:when test="${topComparison.key eq 'Small molecules'}">
        <a href="${epfn:getMoleculeUrl(item)}">${item.name}</a>
        <img src="${epfn:getMoleculeImgSrc(item)}" style=""/>
        <div style="margin-left: 2em">${item.description}
            <c:if test="${not empty item.formula}">
                <br/>Formula: ${item.formula}
            </c:if>
        </div>
    </c:when>
    <c:otherwise>
        ${item}
    </c:otherwise>
</c:choose>
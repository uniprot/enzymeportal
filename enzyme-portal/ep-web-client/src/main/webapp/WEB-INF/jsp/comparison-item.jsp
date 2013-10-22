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
    <c:when test="${fn:contains(comparePath, 'EC classification')}">
        <a href="${intenzConfig.ecBaseUrl}${item}" target="_blank">EC
            ${item}</a>
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
            <c:if test="${not empty item.reaction.id}">
                <div>
                <span class="comparison subheader">Rhea identifier:</span>
                    <a href="${rheaConfig.reactionBaseUrl}${item.reaction.id}"
                         target="_blank">RHEA:${item.reaction.id}</a>
                </div>
            </c:if>
            <c:if test="${not empty item.pathways}">
                <div>
                <span class="comparison subheader">Pathways:</span>
                <c:forEach var="pathway" items="${item.pathways}" varStatus="vs">
                    <a href="${reactomeConfig.eventBaseUrl}${pathway.id}"
                        target="_blank">${pathway.id}</a>${vs.last? '' : ','}
                </c:forEach>
                </div>
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
    <c:when test="${topComparison.key eq 'Diseases'}">
        <a href="${item.url}" style="font-weight: bold">${item.name}</a>:
        ${item.description}
        <c:if test="${not empty item.evidence}">
            <br/><b>Evidence:</b>
            <ul>
                <c:forEach var="ev" items="${item.evidence}">
                    <li>${ev}</li>
                </c:forEach>
            </ul>
        </c:if>
    </c:when>
    <c:otherwise>
        ${item}
    </c:otherwise>
</c:choose>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:choose>
    <c:when test="${empty moleculeGroup or empty moleculeGroup.molecule}">
        <br/>
        <div>
            <spring:message code="label.entry.molecules.empty"
                            arguments="${emptyArgs}"/>
        </div>
    </c:when>
    <c:otherwise>
        <fieldset>
            <legend>
                <spring:message code="label.entry.molecules.sub.title"
                                arguments="${titleArgs}"/>
            </legend>
            <p>
                <spring:message code="label.entry.molecules.explanation"
                                arguments="${explArgs}" />
            </p>
            <div>
                <div>
                    <c:forEach var="molecule" items="${moleculeGroup.molecule}"
                        begin="0"
                        end="11">
                        <div class="small-molecule-container">
                        <%@include file="molecule.jsp" %>
                        </div>
                    </c:forEach>
                </div>
                <c:if test="${fn:length(moleculeGroup.molecule) >= 12}">
                    <a href="#" id="more-molecule-trigger">More...</a>
                    <div id="more-molecule-container" style="display: none">
                        <c:forEach var="molecule" items="${moleculeGroup.molecule}"
                                   begin="12"
                                   end="${fn:length(moleculeGroup.molecule) gt searchConfig.maxMoleculesPerGroup?
                                2 : fn:length(moleculeGroup.molecule)-1}">
                            <div class="small-molecule-container">
                                <%@include file="molecule.jsp" %>
                            </div>
                        </c:forEach>
                    </div>
                </c:if>
            </div>
            <c:if test="${moleculeGroup.totalFound gt searchConfig.maxMoleculesPerGroup}">
                These are only ${searchConfig.maxMoleculesPerGroup} out of
                ${moleculeGroup.totalFound} ${emptyArgs}s found.
            </c:if>
            <a href="${moleculeGroupUrl}">
                <spring:message code="label.entry.molecules.see.more"
                    arguments="${moleculeGroup.totalFound eq 1? 'the' : 'all'},
                    ${moleculeGroup.totalFound eq 1? '' : moleculeGroup.totalFound},
                    ${emptyArgs}${moleculeGroup.totalFound eq 1? '' : 's'},
                    ${moleculeGroupDb}"/>
            </a>
        </fieldset>
    </c:otherwise>
</c:choose>

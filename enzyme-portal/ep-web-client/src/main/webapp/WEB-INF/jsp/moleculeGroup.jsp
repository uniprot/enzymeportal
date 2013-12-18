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
            <div style="display: table-row;">
                <c:forEach var="molecule" items="${moleculeGroup.molecule}"
                    begin="0"
                    end="${fn:length(moleculeGroup.molecule) gt searchConfig.maxMoleculesPerGroup?
                        2 : fn:length(moleculeGroup.molecule)-1}">
                    <div style="display: table-cell; vertical-align: top;">
                    <%@include file="molecule.jsp" %>
                    </div>
                </c:forEach>
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

                <%--
                        <c:choose>
                                <c:when test="${moleculeGroupDb eq 'ChEMBL'}">
                                        See all related ${emptyArgs} in ${moleculeGroupDb}:
                                                <div id="target_uniprot"
                                                        style="height:250px; width:400px; clear:both">
                                                        Loading ChEMBL data...
                                                        <img src="${pageContext.request.contextPath}/resources/images/loading.gif"
                                                            alt="Loading..."/>
                                            </div>
                                                <script type="text/javascript"
                                                        src='https://www.ebi.ac.uk/chembldb/index.php/widget/create/target_uniprot/target/compound_mw/uniprot:${enzymeModel.uniprotaccessions[0]}'></script>		        	</c:when>
                                <c:otherwise>
                                        <a href="${moleculeGroupUrl}">See all
                                                ${fn:length(moleculeGroup)} ${emptyArgs}
                                                in ${moleculeGroupDb}</a>
                                </c:otherwise>
                        </c:choose>
                --%>
        </fieldset>
    </c:otherwise>
</c:choose>

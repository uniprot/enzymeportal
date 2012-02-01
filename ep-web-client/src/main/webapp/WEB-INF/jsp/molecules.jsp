<%-- 
    Document   : molecules
    Created on : Aug 8, 2011, 6:18:07 PM
    Author     : hongcao
--%>

<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars"%>

<div id="moleculeContent" class="summary">
    <h2><c:out value="${enzymeModel.name}"/></h2>
    <c:set var="molecules" value="${enzymeModel.molecule}"/>
    <div id="molecules">
        <c:if test='${molecules!=null}'>

            <c:set var="drugs" value="${molecules.drugs}"/>
            <c:set var="drugsSize" value="${fn:length(drugs)}"/>
            <!--<spring:message code="label.entry.underconstruction"/>-->
            <div id="drugs">
                <c:if test='${drugsSize == 0}'>
                    <br/>
                    <div>
                        <spring:message code="label.entry.molecules.empty" arguments="drugs"/>
                    </div>
                </c:if>
                <c:if test='${drugsSize > 0}'>
                    <fieldset>
                        <legend>
                            <spring:message code="label.entry.molecules.sub.title" arguments="Drugs,interact"/>
                        </legend>
                        <p>
                        <spring:message code="label.entry.molecules.explaination" arguments="drugs,interact with" />
                        </p>
                        <c:forEach var="drug" items="${drugs}">
                            <fieldset class="epBox">
                                <a href="${drug.url}" target="blank">
                                    <c:out value="${drug.name}"/>
                                </a>
                                <div>
                                    <div>
                                        <a target="blank" href="${drug.url}">
                                            <img src="${chebiImageBaseUrl}${drug.id}${chebiImageParams}" alt="${drug.name}"/>
                                        </a>
                                    </div>
                                    <div>
                                        <div>
                                            <c:out value="${drug.description}"/>
                                        </div>
                                        <div>
                                            <div>
                                                <span class="bold"><spring:message code="label.entry.molecules.formula"/></span>: <c:out value="${drug.formula}"/>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </fieldset>
                        </c:forEach>
                    </fieldset>
                </c:if>
            </div>

            <c:set var="inhibitors" value="${molecules.inhibitors}"/>
            <c:set var="inhibitorsSize" value="${fn:length(inhibitors)}"/>
            <div id="inhibitor">
                <c:if test='${inhibitorsSize == 0}'>
                    <br/>
                    <div>
                        <spring:message code="label.entry.molecules.empty" arguments="inhibitors"/>
                    </div>
                </c:if>
                <c:if test='${inhibitorsSize > 0}'>
                    <fieldset>
                        <legend>
                            <spring:message code="label.entry.molecules.sub.title" arguments="Inhibitors,inhibit"/>
                        </legend>
                        <p>
                        <spring:message code="label.entry.molecules.explaination" arguments="inhibitors,inhibit"/>
                        </p>
                        <c:forEach var="inhibitor" items="${inhibitors}">
                            <fieldset class="epBox">
                                <a href="${inhibitor.url}" target="blank">
                                    <c:out value="${inhibitor.name}"/>
                                </a>
                                <div>
                                    <div>
                                        <a target="blank" href="${inhibitor.url}">
                                            <img src="${chebiImageBaseUrl}${inhibitor.id}${chebiImageParams}" alt="${inhibitor.name}"/>
                                        </a>
                                    </div>
                                    <div>
                                        <div>
                                            <c:out value="${inhibitor.description}"/>
                                        </div>
                                        <div>
                                            <div>
                                                <span class="bold"><spring:message code="label.entry.molecules.formula"/></span>: <c:out value="${inhibitor.formula}"/>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </fieldset>
                        </c:forEach>
                    </fieldset>
                </c:if>
            </div>
            <c:set var="activators" value="${molecules.activators}"/>
            <c:set var="activatorsSize" value="${fn:length(activators)}"/>
            <div id="activator">
                <c:if test='${activatorsSize == 0}'>
                    <br/>
                    <div>
                        <spring:message code="label.entry.molecules.empty" arguments="activators"/>
                    </div>
                </c:if>
                <c:if test='${activatorsSize > 0}'>
                    <fieldset>
                        <legend>
                            <spring:message code="label.entry.molecules.sub.title" arguments="Activators,activate"/>
                        </legend>
                        <p>
                        <spring:message code="label.entry.molecules.explaination" arguments="Activators,activate"/>
                        </p>
                        <c:forEach var="activator" items="${activators}">
                            <fieldset class="epBox">
                                <a href="${activator.url}" target="blank">
                                    <c:out value="${activator.name}"/>
                                </a>
                                <div>
                                    <div>
                                        <a target="blank" href="${activator.url}">
                                            <img src="${chebiImageBaseUrl}${activator.id}${chebiImageParams}" alt="${activator.name}"/>
                                        </a>
                                    </div>
                                    <div>
                                        <div>
                                            <c:out value="${activator.description}"/>
                                        </div>
                                        <div>
                                            <div>
                                                <span class="bold"><spring:message code="label.entry.molecules.formula"/></span>: <c:out value="${activator.formula}"/>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </fieldset>
                        </c:forEach>
                    </fieldset>
                </c:if>
            </div>
                 <div class="provenance">
                    <ul>
                        <c:set var="provenance" value="${molecules.provenance}"/>

                        <c:forEach var="prov" items="${provenance}"
                                   varStatus="vsProv">
                            <li class="note_${vsProv.index}">${prov}</li>
                        </c:forEach>
                    </ul>
                </div>
        </div>
    </c:if>
</div>

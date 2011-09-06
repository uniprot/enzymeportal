<%-- 
    Document   : reactionsPathways
    Created on : Aug 15, 2011, 4:03:53 PM
    Author     : hongcao
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>

                                            <div id="reactionContent" class="summary">
                                                <h2><c:out value="${enzymeModel.name}"/></h2>
                                                <c:set var="reactionpathways" value="${enzymeModel.reactionpathway}"/>
                                                <c:forEach items="${reactionpathways}" var="reactionpathway">
                                                    <c:set var="reaction" value="${reactionpathway.reaction}"/>
                                                    <c:set var="pathwayLinks" value="${reactionpathway.pathways}"/>
                                                    <c:set var="pathwaysSize" value="${fn:length(pathwayLinks)}"/>

                                                    <div id="reaction">
                                                        <fieldset>
                                                            <c:if test="${reaction == null}">
                                                                <legend><spring:message code="label.entry.reactionsPathways.found.text.alt" arguments="${pathwaysSize}"/></legend>
                                                            </c:if>
                                                            <c:if test="${reaction != null}">
                                                                <c:set var="rheaEntryUrl" value="${rheaEntryBaseUrl}${reaction.id}"/>
                                                                <legend>
                                                                    <!--<a target="blank" href="${rheaEntryUrl}"><c:out value="${reaction.name}" escapeXml="false"/></a>-->
                                                                    <c:out value="${reaction.name}" escapeXml="false"/>
                                                                </legend>
                                                                <div id="equation">
                                                                    <table>
                                                                        <tr>
                                                                            <c:set var="reactants" value="${reaction.equation.reactantlist}"/>
                                                                            <c:set var="reactantsSize" value="${fn:length(reactants)}"/>
                                                                            <c:set var="products" value="${reaction.equation.productlist}"/>
                                                                            <c:set var="productsSize" value="${fn:length(products)}"/>
                                                                            <c:set var="counter" value="${1}"/>
                                                                            <c:forEach items="${reactants}" var="reactant">
                                                                                <td>
                                                                                    <c:set var="chebiImageUrl" value="${chebiImageBaseUrl}${reactant.id}${chebiImageParams}"/>
                                                                                    <c:set var="chebiEntryUrl" value="${chebiEntryBaseUrl}${reactant.id}${chebiEntryBaseUrlParam}"/>
                                                                                    <a target="blank" href="${chebiEntryUrl}">
                                                                                        <img src="${chebiImageUrl}" alt="${reactant.title}"/>
                                                                                    </a>
                                                                                </td>
                                                                                <c:if test="${counter < reactantsSize}">
                                                                                    <td width="1%">
                                                                                        <b>+</b>
                                                                                    </td>
                                                                                </c:if>
                                                                                <c:set var="counter" value="${counter+1}"/>
                                                                            </c:forEach>

                                                                            <td>
                                                                                <b><c:out value="${reaction.equation.direction}"/></b>
                                                                            </td>

                                                                            <c:set var="counter" value="${1}"/>
                                                                            <c:forEach items="${products}" var="product">
                                                                                <td>
                                                                                    <c:set var="chebiImageUrl" value="${chebiImageBaseUrl}${product.id}${chebiImageParams}"/>
                                                                                    <c:set var="chebiEntryUrl" value="${chebiEntryBaseUrl}${product.id}${chebiEntryBaseUrlParam}"/>
                                                                                    <a target="blank" href="${chebiEntryUrl}">
                                                                                        <img src="${chebiImageUrl}" alt="${product.title}"/>
                                                                                    </a>
                                                                                </td>
                                                                                <c:if test="${counter < productsSize}">
                                                                                    <td>
                                                                                        <b>+</b>
                                                                                    </td>
                                                                                </c:if>
                                                                                <c:set var="counter" value="${counter+1}"/>
                                                                            </c:forEach>
                                                                        </tr>
                                                                    </table>
                                                                </div>
                                                                <div id="reactionDesc">
                                                                    <c:out value="${reaction.description}" escapeXml="false"/>
                                                                </div>
                                                                <div id="rheaExtLinks">
                                                                    <div id="rheaLink" class="inlineLinks">
                                                                        <a target="blank" href="${rheaEntryUrl}">
                                                                            <spring:message code="label.entry.reactionsPathways.link.rhea"/>
                                                                        </a>
                                                                    </div>
                                                                    <c:set var="macielinks" value="${reactionpathway.mechanism}"/>
                                                                    <c:if test="${fn:length(macielinks) > 0}">
                                                                        <c:set var="macieEntryUrl" value="${macielinks[0].href}"/>
                                                                        <div  id="macieLink" class="inlineLinks">
                                                                            <a target="blank" href="${macieEntryUrl}">
                                                                                <spring:message code="label.entry.reactionsPathways.link.macie"/>
                                                                            </a>
                                                                        </div>
                                                                    </c:if>
                                                                </div>
                                                            </c:if>
                                                            <c:if test="${pathwaysSize>0}" >
                                                                <c:if test="${reaction != null}">
                                                                    <spring:message code="label.entry.reactionsPathways.found.text" arguments="${pathwaysSize}"/>
                                                                </c:if>
                                                                <div id="pathways">
                                                                    <c:forEach var="pathway" items="${reactionpathway.pathways}">
                                                                        <fieldset>
                                                                            <c:set var="reactomeUrl" value="${pathway.url}"/>
                                                                            <legend><c:out value="${pathway.name}" escapeXml="false"/></legend>
                                                                            <br/>
                                                                            <div>
                                                                                <div>
                                                                                    <c:out value="${pathway.description}" escapeXml="false"/>
                                                                                </div>
                                                                            </div>
                                                                            <div>
                                                                                <a target="blank" href="${reactomeUrl}">
                                                                                    <!--<img src="../../resources/images/pathwayDiagram.png" alt="?"/>-->
                                                                                    <img src="${reactomeImageBaseUrl}${pathway.image}" alt=""/>
                                                                                </a>
                                                                            </div>
                                                                            <div class="inlineLinks">
                                                                                <a target="blank" href="${reactomeUrl}">
                                                                                    <spring:message code="label.entry.reactionsPathways.link.reactome"/>
                                                                                </a>
                                                                            </div>
                                                                        </fieldset>
                                                                    </c:forEach>
                                                                </div>
                                                            </c:if>
                                                        </fieldset>
                                                    </div>
                                                </c:forEach>

                                            </div>

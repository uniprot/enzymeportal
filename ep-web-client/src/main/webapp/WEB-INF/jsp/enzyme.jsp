<%--
    Document   : entry
    Created on : May 6, 2011, 7:40:14 PM
    Author     : hongcao
--%>
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

                                        <div id="enzymeContent" class="summary">
                                            <h2><c:out value="${enzymeModel.name}"/></h2>
                                            <dl>
                                                <dt>Function</dt>
                                                <dd>
                                                    <ul>
                                                        <li><c:out value="${enzymeModel.function}"/></li>
                                                    </ul>
                                                </dd>
                                            </dl>
                                            <dl>
                                                <dt>EC Classification</dt>
                                                <dd>
                                                    <ul>
                                                        <li>

                                                            <c:set var="echierarchies" value="${enzyme.echierarchies}"/>
                                                            <c:set var="echierarchiesSize" value="${fn:length(echierarchies)}"/>
                                                            <c:forEach var="j" begin="0" end="${echierarchiesSize-1}">
                                                                <c:set var="ecClass" value="${echierarchies[j].ecclass}"/>
                                                                <c:set var="ecClassSize" value="${fn:length(ecClass)}"/>
                                                                <c:if test='${ecClassSize>0}'>
                                                                    <c:set var="ecNumber" value=""/>
                                                                    <c:set var="dot" value=""/>
                                                                    <c:forEach var="i" begin="0" end="${ecClassSize-1}">
                                                                        <c:if test='${i > 0}'>
                                                                            <c:set var="dot" value="."/>
                                                                        </c:if>
                                                                        <c:if test='${i <= 2}'>
                                                                            <c:set var="ecNumber" value="${ecNumber}${dot}${ecClass[i].ec}"/>
                                                                            <c:out value="${ecClass[i].name}"/>
                                                                            >
                                                                        </c:if>

                                                                        <c:if test='${i > 2}'>
                                                                            <c:set var="ecNumber" value="${ecNumber}${dot}${ecClass[i].ec}"/>
                                                                            <c:out value="${ecNumber}"/> -
                                                                            <c:out value="${ecClass[i].name}"/>
                                                                        </c:if>
                                                                    </c:forEach>
                                                                    <br/>
                                                                </c:if>
                                                            </c:forEach>
                                                        </li>
                                                    </ul>
                                                </dd>
                                            </dl>
                                            <dl>
                                                <dt>Enzyme Type</dt>
                                                <dd>
                                                    <ul>
                                                        <li>Enzyme Type</li>
                                                    </ul>
                                                </dd>
                                            </dl>
                                            <dl>
                                                <dt>Other names</dt>
                                                <dd>
                                                    <ul>
                                                        <li>
                                                            <c:set var="synonym" value="${enzymeModel.synonym}"/>
                                                            <c:set var="synonymSize" value="${fn:length(synonym)}"/>
                                                            <c:if test='${synonymSize>0}'>
                                                                <c:forEach var="i" begin="0" end="${synonymSize-1}">
                                                                    <c:out value="${synonym[i]}"/><br>
                                                                </c:forEach>
                                                            </c:if>
                                                        </li>
                                                    </ul>
                                                </dd>
                                            </dl>

                                            <dl>
                                                <dt>Protein Sequence</dt>
                                                <dd>
                                                    <ul>
                                                        <li>
                                                            <c:set var="sequence" value="${enzyme.sequence}"/>
                                                            This sequence has
                                                            <c:out value="${sequence.sequence}"/>
                                                            amino acids and a molecular weight of
                                                            <c:out value="${sequence.weight}"/> <br>
                                                            <a target="blank" href="${sequence.sequenceurl}">View Sequence in Uniprot</a>

                                                        </li>
                                                    </ul>
                                                </dd>
                                            </dl>
                                        </div>

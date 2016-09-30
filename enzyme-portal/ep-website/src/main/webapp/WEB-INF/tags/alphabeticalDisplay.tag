<%@tag description="Display list item alphabetically" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="Fn" uri="/WEB-INF/epTagLibray.tld" %>
<%@taglib prefix="et" tagdir="/WEB-INF/tags" %>


<%@attribute name="items" required="true" type="java.util.List" %>
<%@attribute name="type" required="true" %>
<%@attribute name="maxDisplay" required="true" %>

<div class="grid_24">
    <div class="clear"></div>
    <c:set var="count" value="0"/>
    <div class="grid_24">
        <c:set var="alphabet" value="A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z"/>
        <c:forTokens var="letter" items="${alphabet}" delims="," varStatus="status">
            <div class="grid_6 disease-overview-box">
                <c:set var="startsWith" value="${letter}"/>
                <h3>${startsWith}</h3>
                <ul>
                    <c:forEach var="data" items="${items}">
                        <c:choose>
                            <c:when test="${type == 'pathways'}">
                                <c:if test="${(not empty data.pathwayName) && Fn:startsWithLowerCase(data.pathwayName, startsWith) && (count <= maxDisplay)}">
                                    <c:set var="count" value="${count + 1}"/>

                                    <li>
                                           <a href="${pageContext.request.contextPath}/enzymes?searchKey=${data.pathwayGroupId}&searchparams.type=KEYWORD&searchparams.previoustext=${data.pathwayName}&searchparams.start=0&searchparams.text=${data.pathwayName}&keywordType=PATHWAYS&searchId=${data.pathwayGroupId}">${data.pathwayName}</a>
                                    </li>
                                </c:if>
                            </c:when>
                            <c:when test="${type == 'diseases'}">
                                <c:if test="${(not empty data.diseaseName) && Fn:startsWithLowerCase(data.diseaseName, startsWith) && (count <= maxDisplay)}">
                                    <c:set var="count" value="${count + 1}"/>

                                    <li>
                                        <a href="${pageContext.request.contextPath}/enzymes?searchKey=${data.omimNumber}&searchparams.type=KEYWORD&searchparams.previoustext=${data.diseaseName}&searchparams.start=0&searchparams.text=${data.diseaseName}&keywordType=DISEASE&searchId=${data.omimNumber}">${data.diseaseName}</a>
                                    </li>
                                </c:if>
                            </c:when>
                        </c:choose>
                    </c:forEach>
                </ul>

                <c:if test="${count gt maxDisplay}">
                    <p>...</p>
                </c:if>

                <c:if test="${count == 0}">
                    <p><em>none</em></p>
                </c:if>
                <c:set var="count" value="0"/>
            </div>
            <c:if test="${status.count%4 == 0}">
                <div class="clear"></div>
            </c:if>
        </c:forTokens>

    </div>
</div>
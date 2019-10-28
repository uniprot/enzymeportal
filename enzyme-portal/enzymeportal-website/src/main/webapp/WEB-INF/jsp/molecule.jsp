
<%--
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
--%>
<%@ taglib prefix="epfn" uri="/WEB-INF/epTagLibray.tld" %>

<c:set var="displayName"
       value="${empty molecule.name? molecule.id : molecule.name}"/>

<%-- Compound URL --%>
<%--
<c:set var="compoundUrl" value="${epfn:getMoleculeUrl(molecule)}"/>
<c:set var="compoundImgUrl" value="${epfn:getMoleculeImgSrc(molecule)}"/>
--%>

<c:choose>
    <c:when test="${molecule.source eq 'ChEBI'}">
        <c:set var="compoundUrl" value="https://www.ebi.ac.uk/chebi/searchId.do?chebiId=${molecule.id}"/>
        <c:set var="compoundImgUrl" value="https://www.ebi.ac.uk/chebi/displayImage.do?defaultImage=true&imageIndex=0&chebiId=${molecule.id}"/>  
    </c:when>
    <c:when test="${molecule.source eq 'ChEMBL'}">
        <c:set var="compoundUrl" value="https://www.ebi.ac.uk/chembl/compound_report_card/${molecule.id}"/>
        <c:set var="compoundImgUrl" value="https://www.ebi.ac.uk/chembl/api/data/image/${molecule.id}.svg?engine=indigo"/>   
    </c:when>
</c:choose>

<fieldset class="epBox">
    <c:choose>
        <c:when test="${empty compoundUrl}">
            ${displayName}
        </c:when>
        <c:otherwise>
            <a href="${compoundUrl}" target="_blank">${displayName}</a>
        </c:otherwise>
    </c:choose>
    <div>
        <c:if test="${not empty compoundImgUrl}">
            <div style="width: 200px;">
                <c:choose>
                    <c:when test="${empty compoundUrl}">
                        <img src="${compoundImgUrl}" class="molecule"
                             alt="(No image available)" />  

                    </c:when>
                    <c:otherwise>

                        <a style="border-bottom-style: none" target="blank" href="${compoundUrl}">
                            <img src="${compoundImgUrl}"
                                 alt="(No image available)" />
                        </a>

                    </c:otherwise>
                </c:choose>
            </div>
        </c:if>

    </div>
</fieldset>

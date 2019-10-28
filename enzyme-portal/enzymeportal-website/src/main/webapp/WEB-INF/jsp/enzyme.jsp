<%--
    Document   : entry
    Updated on : May 6, 2012, 7:40:14 PM
    Author     : Joseph
--%>
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>



<style  type="css">
    .up_pftv_category-name {
        background-color: #00a0b2;
        cursor: pointer;
    }
</style>

<div id="enzymeContent" class="summary">
    <dl>
        <dt>Function</dt>
        <dd>
            <ul>
                <li>          
                    <c:choose>
                        <c:when test="${empty enzyme.function}">
                            No description has been found for the function of this enzyme.
                        </c:when>
                        <c:otherwise>
                            ${enzyme.function}
                        </c:otherwise>
                    </c:choose>
                </li>
            </ul>
        </dd>
    </dl>

    <c:if test="${not empty enzyme.enzymeHierarchies}">

        <dl>
            <dt>EC Classification</dt>
            <dd>
                <ul id="intenz-ec">
                    <li>
                        <c:set var="echierarchies" value="${enzyme.enzymeHierarchies}"/>
                        <c:set var="echierarchiesSize" value="${fn:length(echierarchies)}"/>

                        <c:choose>
                            <c:when test='${echierarchiesSize > 0}'>

                                <c:forEach var="j" begin="0" end="${echierarchiesSize-1}">
                                    <c:set var="ecClass" value="${echierarchies[j].ecclass}"/>
                                    <c:set var="ecClassSize" value="${fn:length(ecClass)}"/>
                                    <c:if test='${not empty ecClass && ecClassSize > 0}'>
                                        <c:forEach var="i" begin="0" end="${ecClassSize-1}">
                                            <c:if test='${i <= 2}'>
                                                <c:set var="ecNumber" value="${ecClass[i].ec}"/>
                                                <c:choose>
                                                    <c:when test="${not empty ecClass[i].name}">
                                                        <a target="_blank" href="${intenzEntryBaseUrl}${ecNumber}"><c:out value="${ecClass[i].name}"/></a>
                                                        &gt;      
                                                    </c:when>
                                                    <c:otherwise>
                                                        <%--
                                                        <c:set var="webLink" value="https://www.ebi.ac.uk/intenz/query?cmd=SearchEC&ec="/>
                                                        <a target="_blank" href="${webLink}${ecNumber}"><c:out value="${ecNumber}"/></a> 
                                                        --%>
                                                        <c:set var="webLink" value="${pageContext.request.contextPath}/ec/"/>
                                                        <a href="${webLink}${ecNumber}"><c:out value="${ecNumber}"/></a>          
                                                    </c:otherwise>
                                                </c:choose>

                                            </c:if>

                                            <c:if test='${i > 2}'>
                                                <c:set var="ecNumber" value="${ecClass[i].ec}"/>
                                                <a target="blank" href="${intenzEntryBaseUrl}${ecNumber}">
                                                    <c:out value="${ecNumber}"/> -
                                                    <c:out value="${ecClass[i].name}"/>
                                                </a>
                                            </c:if>
                                        </c:forEach>
                                        <br/>
                                    </c:if>

                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                This enzyme has been partially classified because its catalytic activity is either not well known or well known, but not yet classified by IUBMB.
                            </c:otherwise>
                        </c:choose>

                    </li>
                </ul>
            </dd>
        </dl>
    </c:if>
    <c:if test="${not empty enzyme.synonyms}">
        <dl>
            <dt>Other names</dt>
            <dd>
                <ul>
                    <li>
                        <c:set var="synonym" value="${enzyme.synonyms}"/>
                        <c:set var="synonymSize" value="${fn:length(synonym)}"/>
                        <c:if test='${synonymSize>0}'>
                            <c:forEach var="i" begin="0" end="${synonymSize-1}">

                                <span class="synonyms" >  <c:out value="${synonym[i]};"/></span>
                            </c:forEach>
                        </c:if>
                    </li>
                </ul>
            </dd>
        </dl>
    </c:if>

    <dl>
        <dt>Protein Sequence</dt>
        <dd>
            <ul>
                <li>

                    This sequence has ${enzyme.sequence} amino acids.
                </li>
            </ul>
        </dd>
        <p><a target="_blank" href="https://www.uniprot.org/uniprot/${accession}#${accession}-details" > View Sequence in UniProt</a></p>       



    </dl>


    <div id="feature-viewer"></div>


    <div class="provenance">
        <ul>
            <li class="note_0">Data Source:
                <a href="https://www.ebi.ac.uk/intenz/query?cmd=Search&q=${ecNumber}">IntEnz</a> &AMP; <a href="https://www.uniprot.org/uniprot/${enzyme.accession}" >UniProt</a> </li>
            <li class="note_1">IntEnz - (Integrated relational Enzyme database) is a freely available resource focused on enzyme nomenclature. </li>
            <li class="note_2">UniProt - The mission of UniProt is to provide the scientific community with a comprehensive, high-quality and freely accessible resource of protein sequence and functional information </li>
        </ul>
    </div>

</div>
<%-- Javascript at the bottom for faster loading --%>

<script>
    window.onload = function () {
        var fvrDiv = document.getElementById('feature-viewer');
        var ProtVista = require('ProtVista');
        var instance = new ProtVista({
            el: fvrDiv,
            uniprotacc: '${enzyme.accession}'
        });
    };
</script>



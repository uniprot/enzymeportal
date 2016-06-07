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
    <dl>
        <dt>Function</dt>
        <dd>
            <ul>
                <li>          
                    <c:choose>
                        <c:when test="${empty enzymeModel.function}">
                            No description has been found for the function of this enzyme.
                        </c:when>
                        <c:otherwise>
                            ${enzymeModel.function}
                        </c:otherwise>
                    </c:choose>
                </li>
            </ul>
        </dd>
    </dl>
    
    <c:if test="${not empty enzyme.echierarchies}">

    <dl>
        <dt>EC Classification</dt>
        <dd>
            <ul id="intenz-ec">
                <li>
                    <c:set var="echierarchies" value="${enzyme.echierarchies}"/>
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
                                                    <c:set var="webLink" value="http://www.ebi.ac.uk/intenz/query?cmd=SearchEC&ec="/>
                                        <a target="_blank" href="${webLink}${ecNumber}"><c:out value="${ecNumber}"/></a>            
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
    <c:if test="${not empty enzymeModel.synonyms}">
    <dl>
        <dt>Other names</dt>
        <dd>
            <ul>
                <li>
                    <c:set var="synonym" value="${enzymeModel.synonyms}"/>
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
                    <c:set var="sequence" value="${enzyme.sequence}"/>
                    This sequence has ${sequence.length} amino acids<c:if
                        test="${not empty sequence.weight}">
                        and a molecular weight of ${sequence.weight}</c:if>.
                </li>
            </ul>
        </dd>
        <c:choose>
            <c:when test="${not empty sequence.sequenceurl}">
                 <p><a target="blank" href="${sequence.sequenceurl}">View Sequence in UniProt</a></p>
            </c:when>
            <c:otherwise>
           <p><a target="_blank" href="http://www.uniprot.org/uniprot/${accession}#${accession}-details" > View Sequence in UniProt</a></p>       
            </c:otherwise>
        </c:choose>
       
      
    </dl>
      <script>
		window.onload = function() {
			var yourDiv = document.getElementById('yourDiv');
			var biojs_vis_proteinFeaturesViewer = require('biojs-vis-proteinfeaturesviewer');
			var instance = new biojs_vis_proteinFeaturesViewer({
				el: '#feature-viewer',
				uniprotacc : '${accession}'
			});
		}
	</script>
	<div id="feature-viewer"></div>                  
    <c:set var="provenance" value="${enzyme.provenance}"/>
    <div class="provenance">
        <ul>
            <li class="note_0">Data Source:
                <a href="http://www.ebi.ac.uk/intenz/">${provenance[0]}</a> &AMP; <a href="http://www.uniprot.org/" >${provenance[1]}</a> </li>
            <li class="note_1">${provenance[2]} </li>
            <li class="note_2">${provenance[3]} </li>
        </ul>
    </div>

</div>

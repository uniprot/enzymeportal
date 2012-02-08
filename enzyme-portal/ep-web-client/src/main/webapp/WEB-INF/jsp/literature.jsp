<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.EnumSet,
		uk.ac.ebi.ep.adapter.literature.SimpleLiteratureAdapter.CitationLabel"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="summary">
<h2><c:out value="${enzymeModel.name}"/></h2>

<c:choose>
	<c:when test="${empty enzymeModel.literature}">
		<p><spring:message code="label.entry.tab.empty"
			arguments="literature"/></p>
	</c:when>
	<c:otherwise>
	
<div style="position: relative; width: 100%; top: 2ex; padding-bottom: 6ex;">

<div class="literature" style="position: relative; width: 100%; top: 6ex;">
<c:set var="allLabels" value="" />
<c:set var="allLabelCodes" value="" />

<div id="citationsList">
<c:forEach var="labelledCitation" items="${enzymeModel.literature}">
	<c:set var="cit" value="${labelledCitation.citation}"/>
	<c:set var="citationClass" value="" />
	<c:forEach var="label" items="${labelledCitation.labels}">
		<c:set var="citationClass" value="${citationClass} cit-${label.code}" />
		<c:if test="${not fn:contains(allLabelCodes, label.code)}">
			<%-- allLabels: pipe-separated list of CitationLabels --%>
			<c:set var="allLabels" value="${allLabels}|${label.displayText}"/>
			<%-- allLabelCodes: pipe-separated list of CitationLabel codes --%>
			<c:set var="allLabelCodes" value="${allLabelCodes}|${label.code}"/>
		</c:if>
	</c:forEach>

	<div class="${citationClass}">
		<div class="pub_title" style="font-weight: bold;">
			<a href="http://www.ebi.ac.uk/citexplore/citationDetails.do?externalId=${cit.externalId}&amp;dataSource=${cit.dataSource}"
                title="View ${cit.dataSource} ${cit.externalId} in CiteXplore"
                target="_blank" class="extLink ${cit.dataSource}"
                >${empty cit.titleNonAscii? cit.title : cit.titleNonAscii}</a>
	    </div>
	    <c:if test="${not empty cit.abstractText}">
	    <div class="pub_abstract" style="display: table-row;">
	    	<div onclick="$('#cit-${cit.externalId}').toggle()"
	    		style="display: table-cell; white-space: nowrap; cursor: pointer; font-size: smaller;">
	    		Toggle abstract &gt; </div>
			<div id="cit-${cit.externalId}" style="display: none; margin-left: 1em;">
				${cit.abstractText}</div>
	    </div>
	    </c:if>
		<div class="pub_authors">
            <c:set var="authors" value=""/>
			<c:forEach var="author" varStatus="avs"
				items="${cit.authorCollection}">
				<c:set var="authors"
					value="${authors}${avs.index gt 0? ',' : '' } ${author.fullName}"/>
			</c:forEach>
			<c:choose>
				<c:when test="${fn:length(cit.authorCollection) gt 10}">
					${cit.authorCollection[0].fullName}
					<span title="${authors}">et al.</span>
				</c:when>
				<c:otherwise>
					${authors}
				</c:otherwise>
			</c:choose>
			(${cit.journalIssue.yearOfPublication})
		</div>
        <div class="pub_info">
            <i>${cit.journalIssue.journal.title}</i>
            <b>${cit.journalIssue.volume}</b>, ${cit.pageInfo}
        </div>		
    </div>
</c:forEach>
</div>
</div>

<script type="text/javascript">
function filterCitations(){
    var cits = document.getElementById('citationsList').children;
    var citFilters = document.getElementById('literatureFilters');
    var cbs = citFilters.getElementsByTagName('input');
    var i, j;
    for (i = 0; i < cits.length; i++){
        var show = false;
        for (j = 0; j < cbs.length; j++){
            var matches = cits[i].className.indexOf(cbs[j].value) > -1;
            if (matches && cbs[j].checked){
                show = true;
                break;
            }
        }
        cits[i].style.display = show? 'block' : 'none';
    }
}
</script>

<div id="literatureFilters" class="subTitle"
    style="position: absolute; top: 0; width: 100%;">
	Filters:
	<c:set var="splitLabelCodes" value="${fn:split(allLabelCodes, '|')}"/>
	<c:forEach var="citationLabel" varStatus="clvs"
		items="${fn:split(fn:trim(allLabels), '|')}">
		<label style="font-weight: normal; color: black">
            <input type="checkbox" checked="checked"
                value="cit-${splitLabelCodes[clvs.index]}"
			    onclick="filterCitations();"/>
            ${citationLabel}
        </label>
	</c:forEach>
</div>

</div>

<div class="provenance">
	<ul>
		<li class="note_0">Data Source:
			<a href="http://www.ebi.ac.uk/citexplore/">CiteXplore</a></li>
		<li class="note_1">CiteXplore combines literature search with text
			mining tools for biomedical publications.</li>
	</ul>
</div>

	</c:otherwise>
</c:choose>


</div>

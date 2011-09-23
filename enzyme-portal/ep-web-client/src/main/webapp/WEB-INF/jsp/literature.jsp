<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.EnumSet,
		uk.ac.ebi.ep.adapter.literature.SimpleLiteratureAdapter.CitationLabel"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="summary">
<h2><c:out value="${enzymeModel.name}"/></h2>

<c:choose>
	<c:when test="${empty enzymeModel.literature}">
		There is no literature information available for this enzyme.
	</c:when>
	<c:otherwise>
	
<div style="position: relative; width: 100%; top: 3ex;">

<div class="literature" style="position: relative; width: 100%; top: 3ex;">
<c:set var="allLabels" value="" />
<c:set var="allLabelCodes" value="" />
<ol>
<c:forEach var="labelledCitation" items="${enzymeModel.literature}">
	<c:set var="cit" value="${labelledCitation.citation}"/>
	<c:set var="citationClass" value="" />
	<c:forEach var="label" items="${labelledCitation.labels}">
		<c:set var="citationClass" value="${citationClass} cit-${label.code}" />
		<c:if test="${not fn:contains(allLabels, label)}">
			<%-- allLabels: pipe-separated list of CitationLabels --%>
			<c:set var="allLabels" value="${allLabels}|${label.displayText}"/>
			<%-- allLabelCodes: pipe-separated list of CitationLabel codes --%>
			<c:set var="allLabelCodes" value="${allLabelCodes}|${label.code}"/>
		</c:if>
	</c:forEach>
	
	<li class="${citationClass}" style="display: inline;">
		<div class="pub_title" style="font-weight: bold;">
			<a href="http://www.ebi.ac.uk/citexplore/citationDetails.do?externalId=${cit.externalId}&amp;dataSource=${cit.dataSource}"
                title="View ${cit.dataSource} ${cit.externalId} in CiteXplore"
                target="_blank" class="extLink ${cit.dataSource}"
                >${empty cit.titleNonAscii? cit.title : cit.titleNonAscii}</a>
	    </div>
	    <c:if test="${not empty cit.abstractText}">
	    <div class="pub_abstract" style="display: table-row;">
	    	<div onclick="$('#cit-${cit.externalId}').toggle()"
	    		style="display: table-cell; white-space: nowrap; cursor: pointer;">
	    		Toggle abstract &gt;</div>
			<div id="cit-${cit.externalId}" style="display: none">${cit.abstractText}</div>
	    </div>
	    </c:if>
		<div class="pub_authors">
			<c:forEach var="author" varStatus="avs"
				items="${cit.authorCollection}">
				<c:set var="authors"
					value="${authors}${avs.index gt 0? ',' : '' } ${author.fullName}"/>
			</c:forEach>
			<c:choose>
				<c:when test="${fn:length(cit.authorCollection) gt 10}">
					<c:set var="firstAuthor"
						value="${cit.authorCollection[0]}"/>
					${firstAuthor.fullName} <span title="${authors}">et al.</span>
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
	</li>
</c:forEach>
</ol>
</div>

<script type="text/javascript">
<!--
function filterCitations(cb){
	var cssClass = '.cit-' + cb.value;
	$(cssClass).css('display', cb.checked? 'inline' : 'none');
}
//-->
</script>

<div id="literatureFilters" style="position: absolute; top: 0; width: 100%">
	<b>Filters:</b>
	<c:set var="splitLabelCodes" value="${fn:split(allLabelCodes, '|')}"/>
	<c:forEach var="citationLabel" varStatus="clvs"
		items="${fn:split(fn:trim(allLabels), '|')}">
		<label><input type="checkbox" checked="checked" value="${splitLabelCodes[clvs.index]}"
			onclick="filterCitations(this);"/>${citationLabel}</label>
	</c:forEach>
</div>

</div>

	</c:otherwise>
</c:choose>

</div>

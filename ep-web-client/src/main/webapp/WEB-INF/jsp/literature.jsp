<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript">
<!--
function filterCitations(cb){
	var jQueryClass = '.' + cb.value;
	$(jQueryClass).css('display', cb.checked? 'inline' : 'none');
}
//-->
</script>

<div class="summary">
<h2><c:out value="${enzymeModel.name}"/></h2>

<c:choose>
	<c:when test="${empty enzymeModel.literature}">
		There is no literature information available for this enzyme.
	</c:when>
	<c:otherwise>
	
<div style="position: relative; width: 100%; top: 3ex;">

<div class="literature" style="position: relative; width: 100%; top: 3ex;">
<ol>
<c:forEach var="labelledCitation" items="${enzymeModel.literature}">
	<c:set var="cit" value="${labelledCitation.citation}"/>
	
	<c:set var="citationLabel" value="" />
	<c:forEach var="label" items="${labelledCitation.label}">
		<c:set var="citationLabel" value="${citationLabel} ${label}" />
	</c:forEach>
	<c:set var="citationLabels" value="${citationLabels} ${citationLabel}" />
	
	<li class="${citationLabel}" style="display: inline;">
		<div class="pub_title" style="font-weight: bold;">
			<a href="http://www.ebi.ac.uk/citexplore/citationDetails.do?externalId=${cit.externalId}&amp;dataSource=${cit.dataSource}"
                title="View ${cit.dataSource} ${cit.externalId} in CiteXplore"
                target="_blank" class="extLink ${cit.dataSource}"
                >${empty cit.titleNonAscii? cit.title : cit.titleNonAscii}</a>
	    </div>
	    <div class="pub_abstract">
			${cit.abstractText}
	    </div>
		<div class="pub_authors">
			<c:forEach var="author" varStatus="avs"
				items="${labelledCitation.citation.authorCollection}">
				${avs.index > 0? ',' : '' } ${author.fullName}
			</c:forEach>
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

<c:set var="shownFilters" value="" />
<div id="literatureFilters" style="position: absolute; top: 0; width: 100%">
	<b>Filters:</b>
	<c:forEach var="citationLabel" items="${fn:split(fn:trim(citationLabels), ' ')}">
		<c:if test="${not fn:contains(shownFilters, citationLabel)}">
		<label><input type="checkbox" checked="checked" value="${citationLabel}"
			onclick="filterCitations(this);"/>${citationLabel}</label>
		<c:set var="shownFilters" value="${shownFilters} ${citationLabel}" />
		</c:if>
	</c:forEach>
</div>

</div>

	</c:otherwise>
</c:choose>

</div>

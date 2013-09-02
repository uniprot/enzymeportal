<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.EnumSet,
		uk.ac.ebi.ep.adapter.literature.CitationLabel"%>
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
	
<div style="position: relative; width: 100%; top: 2ex; padding-bottom: 10ex;">

<div class="literature" style="position: relative; width: 100%; top: 10ex;">
<c:set var="allLabels" value="" />
<c:set var="allLabelCodes" value="" />

<c:set var="numOfJournals" value="0"/>
<c:set var="numOfBooks" value="0"/>
<c:set var="numOfPatents" value="0"/>
<c:set var="numOfOthers" value="0"/>

<div id="citationsList">
<c:forEach var="labelledCitation" items="${enzymeModel.literature}">
	<c:set var="cit" value="${labelledCitation.citation}"/>
	
	<c:choose>
		<c:when test="${not empty cit.journalInfo}">
			<c:set var="citationClass" value="journal" />
			<c:set var="numOfJournals" value="${numOfJournals + 1}"/>
		</c:when>
		<c:when test="${not empty cit.bookOrReportDetails}">
			<c:set var="citationClass" value="book" />
			<c:set var="numOfBooks" value="${numOfBooks + 1}"/>
		</c:when>
		<c:when test="${not empty cit.patentDetails}">
			<c:set var="citationClass" value="patent" />
			<c:set var="numOfPatents" value="${numOfPatents + 1}"/>
		</c:when>
		<c:otherwise>
			<c:set var="citationClass" value="other" />
			<c:set var="numOfOthers" value="${numOfOthers + 1}"/>
		</c:otherwise>
	</c:choose>
	
	<c:forEach var="label" items="${labelledCitation.labels}">
		<c:set var="citationClass" value="${citationClass} cit-${label.code}" />
		<c:choose>
			<c:when test="${not fn:contains(allLabelCodes, label.code)}">
				<%--
				allLabels: pipe-separated list of CitationLabels
				--%>
				<c:set var="allLabels"
					value="${allLabels}|${label.displayText}"/>
				<%--
				allLabelCodes: pipe-separated list of codes; each code is
				followed by as many hash (#) characters as occurrences in the
				citations list.
				--%>
				<c:set var="allLabelCodes"
					value="${allLabelCodes}|${label.code}#"/>
			</c:when>
			<c:otherwise>
				<c:set var="oneMore" value="${label.code}#"/>
				<c:set var="allLabelCodes"
					value="${fn:replace(allLabelCodes, label.code, oneMore)}"/>
			</c:otherwise>
		</c:choose>
		<c:if test="${not fn:contains(allLabelCodes, label.code)}">
		</c:if>
	</c:forEach>

	<div class="${citationClass}">
		<div class="pub_title" style="font-weight: bold;">
			<a href="http://europepmc.org/abstract/${cit.source}/${cit.id}"
                title="View ${cit.source} ${cit.id} in Europe PubMed Central"
                target="_blank" class="extLink ${cit.source}"
                >${cit.title}</a>
	    </div>
		<div class="pub_authors">
            <c:set var="authors" value=""/>
			<c:forEach var="author" varStatus="avs"
				items="${cit.authorList.author}">
				<c:set var="authors"
					value="${authors}${avs.index gt 0? ',' : '' } ${author.fullName}"/>
			</c:forEach>
			<c:choose>
				<c:when test="${fn:length(cit.authorList.author) gt 10}">
					${cit.authorList.author[0].fullName}
					<span title="${authors}">et al.</span>
				</c:when>
				<c:otherwise>
					${authors}
				</c:otherwise>
			</c:choose>
			<c:choose>
				<c:when test="${not empty cit.journalInfo}">
					(${cit.journalInfo.yearOfPublication})
				</c:when>
				<c:when test="${not empty cit.bookOrReportDetails}">
					(${cit.bookOrReportDetails.yearOfPublication})
				</c:when>
				<c:otherwise>
					(${cit.pubYear})
				</c:otherwise>
			</c:choose>
		</div>
        <div class="pub_info">
			<c:choose>
				<c:when test="${not empty cit.journalInfo}">
		            <i>${cit.journalInfo.journal.title}</i>
		            <b>${cit.journalInfo.volume}</b>
                    ${empty cit.pageInfo? '':','} ${cit.pageInfo}
				</c:when>
				<c:when test="${not empty cit.bookOrReportDetails}">
					${cit.brSummary}
				</c:when>
				<c:when test="${not empty cit.patentDetails}">
					<i>Patent ${cit.id}</i>
				</c:when>
				<c:otherwise>
				</c:otherwise>
			</c:choose>
        </div>		
	    <c:if test="${not empty cit.abstractText}">
	    <div class="pub_abstract" style="display: table-row;">
	    	<div onclick="$('#cit-${cit.id}').toggle()"
	    		style="display: table-cell; white-space: nowrap; cursor: pointer; font-size: smaller;">
	    		Toggle abstract &gt; </div>
			<div id="cit-${cit.id}" style="display: none; margin-left: 1em;">
				${cit.abstractText}</div>
	    </div>
	    </c:if>
    </div>
</c:forEach>
</div>
</div>

<script type="text/javascript">
function filterCitations(){
    var cits = document.getElementById('citationsList').children;
    //var citFilters = document.getElementById('literatureFilters');
    var tabFilters = document.getElementById('tabFilters');
    var citTypeFilters = document.getElementById('citTypeFilters');
    var tabCbs = tabFilters.getElementsByTagName('input');
    var citTypeCbs = citTypeFilters.getElementsByTagName('input');
    var i, j;
    for (i = 0; i < cits.length; i++){
        var isTab = false, isCitType = false;
        for (j = 0; j < tabCbs.length; j++){
            var matches = cits[i].className.indexOf(tabCbs[j].value) > -1;
            if (matches && tabCbs[j].checked){
                isTab = true;
                break;
            }
        }
        if (isTab){
            for (j = 0; j < citTypeCbs.length; j++){
                var matches = cits[i].className.indexOf(citTypeCbs[j].value) > -1;
                if (matches && citTypeCbs[j].checked){
                    isCitType = true;
                    break;
                }
            }
        }
        cits[i].style.display = isTab && isCitType? 'block' : 'none';
    }
}
</script>

<div id="literatureFilters" class="subTitle"
    style="position: absolute; top: 0; width: 100%;">
    <span style="color: black;">${fn:length(enzymeModel.literature)}
    <span style="font-weight: normal;">references.</span>
    <c:if test="${fn:length(enzymeModel.literature)
        eq requestScope['uk.ac.ebi.ep:name=literatureConfig'].maxCitations}"><a
        title="Europe PubMed Central"
        href="http://europepmc.org/search/?page=1&query=UNIPROT_PUBS:${enzymeModel.uniprotaccessions[0]}" target="_blank">
        See more in EPMC...</a>
    </c:if>
    </span>

    <br/>

    <span id="tabFilters">
	Filters:
	<c:set var="splitLabelCodes" value="${fn:split(allLabelCodes, '|')}"/>
	<c:forEach var="citationLabel" varStatus="clvs"
		items="${fn:split(fn:trim(allLabels), '|')}">
		<label class="citationFilter">
			<c:set var="labelCode"
				value="${fn:substringBefore(splitLabelCodes[clvs.index], '#')}"/>
			<c:set var="dataIcon" value="${
			    labelCode eq 'ENZYME'? 'P':
			    labelCode eq 'PROTEIN_STRUCTURE'? 's':
			    labelCode eq 'SMALL_MOLECULES'? 'b':
			    labelCode eq 'DISEASES'? 'v': ''
			    }"/>
            <c:set var="iconClass" value="${labelCode eq 'DISEASES'?
                'icon-species': 'icon-conceptual'}"/>
            <input type="checkbox" checked="checked"
                value="cit-${labelCode}"
			    onclick="filterCitations();"/>
            <span data-icon="${dataIcon}" class="label icon ${iconClass}">
                ${citationLabel}</span>
            (${fn:length(splitLabelCodes[clvs.index])-fn:length(labelCode)})
        </label>
	</c:forEach>
    </span>
	<span id="citTypeFilters"
		style="display: ${fn:length(enzymeModel.literature) gt numOfJournals?
		'inline' : 'none'}">
	Publication type:
	<c:if test="${numOfJournals gt 0}">
		<label style="font-weight: normal; color: black">
            <input type="checkbox" checked="checked"
                value="journal"
			    onclick="filterCitations();"/>
            Journal articles (${numOfJournals})
        </label>
	</c:if>
	<c:if test="${numOfBooks gt 0}">
		<label style="font-weight: normal; color: black">
            <input type="checkbox" checked="checked"
                value="book"
			    onclick="filterCitations();"/>
            Books (${numOfBooks})
        </label>
	</c:if>
	<c:if test="${numOfPatents gt 0}">
		<label style="font-weight: normal; color: black">
            <input type="checkbox" checked="checked"
                value="patent"
			    onclick="filterCitations();"/>
            Patents (${numOfPatents})
        </label>
	</c:if>
	<c:if test="${numOfOthers gt 0}">
		<label style="font-weight: normal; color: black">
            <input type="checkbox" checked="checked"
                value="other"
			    onclick="filterCitations();"/>
            Others (${numOfOthers})
        </label>
	</c:if>
	</span>
</div>

</div>

<div class="provenance">
	<ul>
		<li class="note_0">Data Source:
			<a href="http://europepmc.org/">Europe PMC</a></li>
		<li class="note_1">Europe PubMed Central (Europe PMC) offers free access
		    to biomedical literature resources.</li>
	</ul>
</div>

	</c:otherwise>
</c:choose>


</div>

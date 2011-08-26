<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- HERE FILTERS --%>

<div class="literature">
<ol>
<c:forEach var="labelledCitation" items="${enzymeModel.literature}">
	<c:set var="cit" value="${labelledCitation.citation}"/>
	<li class="${labelledCitation.label}">
		<div class="pub_title">
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

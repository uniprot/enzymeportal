<%-- 
    Document   : searchBox
    Created on : Aug 21, 2011, 12:29:50 PM
    Author     : hongcao
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib  prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script>
function showCard(tabId){
	var s = tabId.split('-tab-');
	var cardsId = '#' + s[0];
	var tabsId = '#' + s[0] + '-tabs';
	var theCardId = '#' + s[0] + '-' + s[1];
	var theTabId = '#' + s[0] + '-tab-' + s[1];
	$(cardsId).children().css('display', 'none');
	$(tabsId).children().removeClass('selected');
	$(theCardId).css('display', 'block');
	$(theTabId).addClass('selected');
}

function submitKeywordForm(text){
	$('#search-keyword-text').val(text);
	$('#search-keyword-submit').click();
}
</script>

<ul id="search-tabs"
	style="width: 70em; margin-left: auto; margin-right: auto;">
	<li id="search-tab-keyword"
		class="searchTab ${empty searchModel.searchparams.type or
			searchModel.searchparams.type eq 'KEYWORD'? 'selected':'' }"
		onclick="showCard(this.id);">
		<spring:message code="label.search.tab.keyword"/>
	</li>
	<li id="search-tab-sequence"
		class="searchTab ${searchModel.searchparams.type eq 'SEQUENCE'?
			'selected':'' }"
		onclick="showCard(this.id);">
		<spring:message code="label.search.tab.sequence"/>
	</li>
	<!--
	<li><a href="#compoundSearch"><spring:message
		code="label.search.tab.compound"/></a></li> 
	 -->
</ul>

<form:form id="searchForm" modelAttribute="searchModel"
	action="${pageContext.request.contextPath}/search" method="POST">
	<form:hidden path="searchparams.previoustext" />

<div id="search" style="min-height: 20ex;">
	<div id="search-keyword" class="searchBackground searchTabContent"
		style="margin-left: auto; margin-right: auto; width: 65em;
		display: ${empty searchModel.searchparams.type or
			searchModel.searchparams.type eq 'KEYWORD'? 'block':'none' };">
		<form:input id="search-keyword-text" path="searchparams.text"
			cssClass="field" rel="Enter a name to search" />
		<button id="search-keyword-submit" type="submit"
			name="searchparams.type" value="KEYWORD"
			class="searchButton">Search</button>
		<br />
		<spring:message code="label.search.example" />
		<a class="formSubmit"
			onclick="submitKeywordForm('sildenafil')">sildenafil</a>,
		<a class="formSubmit"
			onclick="submitKeywordForm('Insulin receptor')">Insulin receptor</a>,
		<a class="formSubmit"
			onclick="submitKeywordForm('Ceramide glucosyltransferase')">Ceramide
			glucosyltransferase</a>,
		<a class="formSubmit"
			onclick="submitKeywordForm('Phenylalanine-4-hydroxylase')"
			>Phenylalanine-4-hydroxylase</a>,
		<a class="formSubmit"
			onclick="submitKeywordForm('Cytochrome P450 3A4')">Cytochrome
			P450 3A4</a>,
		<a class="formSubmit"
			onclick="submitKeywordForm('CFTR')">CFTR</a>,
		<a class="formSubmit"
			onclick="submitKeywordForm('Q13423')">Q13423</a>,
		<a class="formSubmit"
			onclick="submitKeywordForm('REACT_1400.4')">REACT_1400.4</a>
	</div>
	
	<div id="search-sequence" class="searchBackground searchTabContent"
		style="margin-left: auto; margin-right: auto; width: 65em;
		display: ${searchModel.searchparams.type eq 'SEQUENCE'?
			'block':'none' };">
		<form:textarea path="searchparams.sequence" cols="80" rows="5"
	    		 title="Enter a protein sequence to search" />
		<button id="search-sequence-submit" type="submit"
			name="searchparams.type" value="SEQUENCE"
	      	class="searchButton">Search</button>
	</div>
</div>

</form:form>

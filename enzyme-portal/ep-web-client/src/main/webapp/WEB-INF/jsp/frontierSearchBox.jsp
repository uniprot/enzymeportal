<%-- 
    Document   : frontierSearchBox
    Created on : Dec 3, 2012, 11:13:00 AM
    Author     : joseph
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib  prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
 <script src="resources/javascript/search.js" type="text/javascript"></script>
<script>
//function showCard(tabId){
//	var s = tabId.split('-tab-');
//	var cardsId = '#' + s[0];
//	var tabsId = '#' + s[0] + '-tabs';
//	var theCardId = '#' + s[0] + '-' + s[1];
//	var theTabId = '#' + s[0] + '-tab-' + s[1];
//	$(cardsId).children().css('display', 'none');
//	$(tabsId).children().removeClass('selected');
//	$(theCardId).css('display', 'block');
//	$(theTabId).addClass('selected');
//}

function submitKeywordForm(text){
	$('#local-searchbox').val(text);
	$('#search-keyword-submit').click();
}
</script>

<!--<ul id="search-tabs"
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
	
	<li><a href="#compoundSearch"><spring:message
		code="label.search.tab.compound"/></a></li> 
	 
</ul>-->
                
                
                
    
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
         
     

				<form:form id="local-search" name="local-search" modelAttribute="searchModel"
                                      action="${pageContext.request.contextPath}/search" method="POST">
						<form:hidden path="searchparams.previoustext" />		
					<fieldset>
					
                                            <div class="left" > 

						<label>
<!--						<input type="text" name="first" id="local-searchbox">-->
                                   <form:input id="local-searchbox" name="first" path="searchparams.text"
			                         rel="Enter a name to search" />

						</label>
						<!-- Include some example searchterms - keep them short and few! -->
                                                <span class="examples">Examples: <a class="formSubmit" onclick="submitKeywordForm('sildenafil')">Sildenafil</a>, <a class="formSubmit" onclick="submitKeywordForm('CFTR')">CFTR</a>, <a class="formSubmit" onclick="submitKeywordForm('REACT_1400.4')">REACT_1400.4</a>, <a class="formSubmit" onclick="submitKeywordForm('Q13423')">Q13423</a></span>
					</div>
					
					<div class="right">
                                            <input id="search-keyword-submit" type="submit" name="submit" value="Search" class="submit">
                                                <form:hidden path="searchparams.type" value="KEYWORD"/>
                                      
                                                
						<!-- If your search is more complex than just a keyword search, you can link to an Advanced Search,
						     with whatever features you want available -->
						<span class="adv"><a href="/enzymeportal/advanceSearch" id="adv-search" title="Advanced">Advanced</a></span>
                                                
                             
					</div>									
					
					</fieldset>
					
				</form:form>
			

      <!-- /local-search -->        
         
         
         
                  
         
         
         
         
         
         
         
         
         
         
         



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


<!-- <script src="resources/javascript/search.js" type="text/javascript"></script>
   <link href="resources/css/search.css" type="text/css" rel="stylesheet" />-->
<!--<link rel="stylesheet" href="//www.ebi.ac.uk/web_guidelines/css/compliance/mini/ebi-fluid-embl.css">-->
<!--   <link media="screen" href="../../resources/css/enzyme.css" type="text/css" rel="stylesheet" />-->
 
   
    
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



				<form:form id="local-search" name="local-search" modelAttribute="searchModel"
                                      action="${pageContext.request.contextPath}/search" method="POST">
						<form:hidden path="searchparams.previoustext" />		
					<fieldset>
					
                                            <div class="left" > 

						<label>

                                   <form:input id="local-searchbox" name="first" path="searchparams.text" />

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
         
         
         
                  
         
         
         
         
         
         
         
         
         
         
         



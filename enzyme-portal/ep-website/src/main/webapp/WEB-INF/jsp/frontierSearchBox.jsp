<%-- 
    Document   : frontierSearchBox
    Created on : Dec 3, 2012, 11:13:00 AM
    Author     : joseph
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib  prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ page language="java" pageEncoding="UTF-8"%>




				<form:form id="local-search" name="local-search" modelAttribute="searchModel"
                                      action="${pageContext.request.contextPath}/search" method="POST">
						<form:hidden path="searchparams.previoustext" />		
					<fieldset>
					
                                            <div class="left" > 

						<label>
                                                                                          
                                   <form:input id="local-searchbox" name="first" path="searchparams.text" />
                                 
						</label>                                                                           
						<!-- Include some example searchterms - keep them short and few! -->
                                                <span class="examples">Examples:  <a href="${pageContext.request.contextPath}/search?searchparams.type=KEYWORD&searchparams.previoustext=&searchparams.start=0&searchparams.text=sildenafil">sildenafil</a>, <a href="${pageContext.request.contextPath}/search?searchparams.type=KEYWORD&searchparams.previoustext=&searchparams.start=0&searchparams.text=CFTR">CFTR</a>,  <a href="${pageContext.request.contextPath}/search?searchparams.type=KEYWORD&searchparams.previoustext=&searchparams.start=0&searchparams.text=REACT_1400.4">REACT_1400.4</a>, <a href="${pageContext.request.contextPath}/search?searchparams.type=KEYWORD&searchparams.previoustext=&searchparams.start=0&searchparams.text=Q13423">Q13423</a></span>
					</div>
					
					<div class="right">
                                            <input id="search-keyword-submit" type="submit" name="submit" value="Search" class="submit">
                                                <form:hidden path="searchparams.type" value="KEYWORD"/>
                                      
                                                
						<!-- If your search is more complex than just a keyword search, you can link to an Advanced Search,
						     with whatever features you want available -->
						<span class="adv"><a href="${pageContext.request.contextPath}/advanceSearch" id="adv-search" title="Advanced">Advanced</a></span>
                                                
                             
					</div>									
					
					</fieldset>
					
				</form:form>
			

      <!-- /local-search -->        
         
         
         
                  
         
         
         
         
         
         
         
         
         
         
         



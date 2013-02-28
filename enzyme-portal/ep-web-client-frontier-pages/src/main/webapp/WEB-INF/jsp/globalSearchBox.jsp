<%-- 
    Document   : globalSearchBox
    Created on : Sep 17, 2012, 9:49:15 AM
    Author     : joseph
--%>


<!doctype html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib  prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<form:form id="local-search" commandName="searchparams" modelAttribute="/about" 
                	action="search" method="POST">
                            <fieldset> 
                                <label>     
	               <form:input id="local-searchbox" path="searchparams.text"
                                   cssClass="field" name="first" rel="Enter a name to search"/>
                                </label>
	                <form:hidden id="start" path="searchparams.start" />
	                <form:hidden path="searchparams.previoustext" />
	                <input  type="submit" value="Search"
                                class="submit" /><br/>
                        <div>
<!--                            <div id="examples">Examples: <a href="search?searchparams.previoustext=&searchparams.start=0&searchparams.text=sildenafil">sildenafil</a> -->
                              <spring:message code="label.search.example"/>
                <a href="search?searchparams.previoustext=&searchparams.start=0&searchparams.text=sildenafil">sildenafil</a>,
                <a href="search?searchparams.previoustext=&searchparams.start=0&searchparams.text=Insulin+receptor">Insulin receptor</a>,
<!--                <a href="search?searchparams.previoustext=&searchparams.start=0&searchparams.text=Ceramide+glucosyltransferase">Ceramide glucosyltransferase</a>,
                <a href="search?searchparams.previoustext=&searchparams.start=0&searchparams.text=Phenylalanine-4-hydroxylase">Phenylalanine-4-hydroxylase</a>,
                <a href="search?searchparams.previoustext=&searchparams.start=0&searchparams.text=Cytochrome+P450+3A4">Cytochrome P450 3A4</a>,-->
                <a href="search?searchparams.previoustext=&searchparams.start=0&searchparams.text=CFTR">CFTR</a>,
                <a href="search?searchparams.previoustext=&searchparams.start=0&searchparams.text=Q13423">Q13423</a>,
                <a href="search?searchparams.previoustext=&searchparams.start=0&searchparams.text=REACT_1400.4">REACT_1400.4</a>
                            
<!--                            </div>-->
                        </div>
                        </fieldset>
				</form:form>
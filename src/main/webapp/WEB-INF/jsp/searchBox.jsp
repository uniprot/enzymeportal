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

<div style="width: 70em; margin-left: auto; margin-right: auto;">
    <div id="keywordSearch" class="searchBackground">
        <div style="width: 65em; margin-left: auto; margin-right: auto; margin-top: 2ex;">
        <%-- 
            <div style="text-align: left !important; margin: 0ex;">
                <span class="selected searchTab">
                    Keyword search
                </span>
                <span class="disabled searchTab">
                    Chemical structure search
                </span>
                <span class="disabled searchTab">
                    Protein sequence search
                </span>
            </div>
         --%>
            <div class="searchTabContent">
                <form:form id="searchForm" modelAttribute="searchModel"
                	action="search" method="POST">
	                <form:input id="searchbox" path="searchparams.text"
	                	cssClass="field" rel="Enter a name to search"/>
	                <form:hidden id="start" path="searchparams.start" />
	                <form:hidden path="searchparams.previoustext" />
	                <input id ="searchButton" type="submit" value="Search"
	                	class="searchButton" />
				</form:form>
                <br/>
                <spring:message code="label.search.example"/>
                <a href="search?searchparams.previoustext=&searchparams.start=0&searchparams.text=sildenafil">sildenafil</a>,
                <a href="search?searchparams.previoustext=&searchparams.start=0&searchparams.text=Insulin+receptor">Insulin receptor</a>,
                <a href="search?searchparams.previoustext=&searchparams.start=0&searchparams.text=Ceramide+glucosyltransferase">Ceramide glucosyltransferase</a>,
                <a href="search?searchparams.previoustext=&searchparams.start=0&searchparams.text=Phenylalanine-4-hydroxylase">Phenylalanine-4-hydroxylase</a>,
                <a href="search?searchparams.previoustext=&searchparams.start=0&searchparams.text=Cytochrome+P450+3A4">Cytochrome P450 3A4</a>,
                <a href="search?searchparams.previoustext=&searchparams.start=0&searchparams.text=CFTR">CFTR</a>,
                <a href="search?searchparams.previoustext=&searchparams.start=0&searchparams.text=Q13423">Q13423</a>,
                <a href="search?searchparams.previoustext=&searchparams.start=0&searchparams.text=REACT_1400.4">REACT_1400.4</a>
            </div>
        </div>
        </p>
    </div>
</div>

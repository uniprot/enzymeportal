<%-- 
    Document   : proteins
    Created on : Sep 26, 2017, 2:35:15 PM
    Author     : <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="Fn" uri="/WEB-INF/epTagLibray.tld" %>
<%@taglib prefix="ep" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
    <head>
        <c:set var="pageTitle" value="Enzymes"/>
        <%@include file="head.jspf" %>

    </head>
    <body class="level2 full-width"><!-- add any of your classes or IDs -->
        <div id="skip-to">
            <ul>
                <li><a href="#content">Skip to main content</a></li>
                <li><a href="#local-nav">Skip to local navigation</a></li>
                <li><a href="#global-nav">Skip to EBI global navigation menu</a></li>
                <li><a href="#global-nav-expanded">Skip to expanded EBI global navigation menu (includes all sub-sections)</a>
                </li>
            </ul>
        </div>
        <div id="wrapper" class="">
            <%@include file="header.jspf" %>
            <div id="content" role="main" class="clearfix" >
               
                <div class="row" id="title-row">
                    <section class="large-2 columns">
                        <img src="/enzymeportal/resources/images/protein_page_logo2.png">
                    </section>

                    <section class="large-10 columns">
                        <h2>Protein Results</h2>
                        <h4>${ebiResult.hitCount} associated proteins found for "${searchKey}" with E.C ${ec}</h4>
                    </section>
                </div>
                
                
                


                
                <div class="row">

                    <section id="search-filters" class="large-3 columns">
                        <form:form id="facetFilterForm" action="${pageContext.request.contextPath}/search" modelAttribute="searchModel" method="POST">
                            <input type="hidden" id="paginationPage" name="servicePage" value="">
                            <input type="hidden" name="keywordType" value="${keywordType}">
                            <input type="hidden" id="searchId" name="searchId" value="${searchId}"/>
                             <input type="hidden" id="ec" name="ec" value="${ec}"/>
    
                              <input type="hidden" id="searchTerm" name="searchKey" value="${searchKey}"></input>
   
                                     <%--
                               <form:hidden path="searchparams.text" value="${searchTerm}" />
                                <form:hidden path="searchparams.type" value="${keywordType}"/>   
                                <input type="hidden" id="searchKey" name="searchKey" value="${searchKey}"></input>
                                      --%>
                             
                            <div class="filter">
                              
                                <div id="activeFilters" class="sublevel1">
                                    <div class="subTitle">Active filters</div>
                                    <ul>
                                        <li id="activeOrganisms"></li>
                                    </ul>
                                </div>

                                <c:forEach var="facet" items="${proteinFacet}">  
                                    
                                    <%--
                                    
                                     <div>
                                         <input type="hidden" id="filtersApplied" value="${filtersApplied}"></input>
                                     </div>
                                         
                                     --%> 
                                 
                                         
                                         
<!--                                    NOTE:: this is example (data only) of other facets, please apply css & javascript where applicable     
                                         -->
                                         
                                         <%@ include file="facets.jsp"%>
                                         
                                 </c:forEach>    
                            </div>
                        </form:form>
                    </section>
                    <section class="resultContent large-9 columns" id="keywordSearchResult">

                                <div class="action-buttons">
                                    <%@include file="basket-buttons.jspf" %>
                                </div>
                                
                      <section class="pagination-container">

                    <c:if test="${page.totalElements gt page.size}">
                        <nav class="paginationContainer">
                              <ul class="pagination" role="navigation" aria-label="Pagination">

                                <c:if test="${currentIndex >= 2}">
                                    <li class="pagination-previous"><a class="paginationLink" id="${currentIndex - 1}" href="#" aria-label="Page ${page.number}"></a></li>
                                </c:if>

                                <li>Page ${page.number + 1} of ${page.totalPages}</li>

                                <c:if test="${currentIndex < page.totalPages}">
                                    <li class="pagination-next"><a class="paginationLink" id="${currentIndex + 1}" href="#" aria-label="Next"></a></li>
                                </c:if>

                              </ul>
                        </nav>
                    </c:if>
                </section>           
                                
                            <div class="clear"></div>
                        <div class="line"></div>
                        <div class="resultContent">         
                        
                          <c:set var="resultItemId" value="${0}"/>
                  
                         <c:forEach items="${proteinView}" var="enzyme">
                                <%@ include file="summary.jsp"%>
                        </c:forEach>
          
                        </div>
                    </section>


                </div>    
                

            </div>
            <%@include file="footer.jspf" %>
        </div>
        <!--! end of #wrapper -->
    </body>
</html>

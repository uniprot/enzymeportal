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
                        <img src="/enzymeportal/resources/images/protein_page_logo.jpg">
                    </section>

                    <section class="large-10 columns">
                        <h2>Protein View Page v2</h2>
                        <h4>${ebiResult.hitCount} enzymatic activities found for "${searchKey}"</h4>
                    </section>
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

                
                <div class="row">



                    <section id="search-filters" class="large-3 columns">

                        <form id="facetFilterForm" action="${pageContext.request.contextPath}/enzymes" method="POST">
                            <input type="hidden" id="paginationPage" name="servicePage" value="">
                            <input type="hidden" name="keywordType" value="${keywordType}">
                            <input type="hidden" id="searchId" name="searchId" value="${searchId}"/>
                            <div class="filter">
                                <input type="hidden" id="searchKey" name="searchKey" value="${searchKey}"></input>
                                <div id="activeFilters" class="sublevel1">
                                    <div class="subTitle">Active filters</div>
                                    <ul>
                                        <li id="activeOrganisms"></li>
                                    </ul>
                                </div>

                             <%--   <c:forEach var="facet" items="${enzymeFacet}">  --%>
                                     <div>
                                         <input type="hidden" id="filtersApplied" value="${filtersApplied}"></input>
                                         <br />test 2<br />
                                         <c:if test="${facet.id eq 'enzyme_family'}">
                                         <div class="sublevel1">
                                             <div class="subTitle">Enzyme Family</div>
                                             <ul>
                                                 <c:forEach var="v" items="${facet.facetValues}">
                                                     <li><input id="enzyme_family_${v.value}" name="filterFacet" value="enzyme_family:${v.value}" type="checkbox" onChange="this.form.submit()"> ${v.label} (${v.count})</li>
                                                 </c:forEach>
                                             </ul>
                                         </div>
                                         </c:if>

                                     </div>
                             <%--    </c:forEach>    --%>
                            </div>
                        </form>

                    </section>











                        <div class="resultContent">
                              <c:set var="resultItemId" value="${0}"/>
                            <c:choose>
                                <c:when test="${searchModel.searchparams.type eq 'SEQUENCE'}">
                               <c:forEach items="${proteinView}"
                                       begin="${pagination.firstResult}"
                                       end="${pagination.lastResult}" var="enzyme" varStatus="vsEnzymes">
                                    <%@ include file="summaryX.jsp"%>
                            </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach items="${proteinView}" var="enzyme">
                                    <%@ include file="summaryX.jsp"%>
                            </c:forEach>
                                    <%--
                              <c:forEach items="${summaryEntries}"
                                       begin="${pagination.firstResult}"
                                       end="${pagination.lastResult}" var="enzyme" varStatus="vsEnzymes">
                                    <%@ include file="summary.jspf"%>
                            </c:forEach>
                            
                                --%>
                                    </c:otherwise>
                            </c:choose>


                        </div>
                                

  






                </div>    
                
                
                
                
                
                
                
                
                
               <br /> 
                Bottom of new stuff
                
<%-- 
                <c:forEach var="p" items="${proteinView}" >
                    <p>${p.proteinName}</p>
                </c:forEach>
--%>

            </div>
            <%@include file="footer.jspf" %>
        </div>
        <!--! end of #wrapper -->
    </body>
</html>
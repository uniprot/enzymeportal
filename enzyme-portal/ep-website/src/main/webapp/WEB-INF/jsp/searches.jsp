<%-- 
    Document   : searches
    Created on : Nov 9, 2015, 2:16:11 PM
    Author     : joseph
--%>




<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="epfn" uri="/WEB-INF/epTagLibray.tld" %>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>

   <c:set var="pageTitle" value="Search results"/>
<%@include file="head.jspf" %>

<body class="level2 ${totalfound eq 0? 'noresults' : ''}">

 <script src="http://code.jquery.com/jquery-2.0.2.min.js"></script>


  <script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
   <script src="${pageContext.request.contextPath}/resources/javascript/search.js"></script>
 
 
    <script>
		$(function() {
			$("#accordion").accordion({
				collapsible : true,
				active : false,
				heightStyle : "content"
			});
		});
	</script>
    
    <div id="skip-to">
            <ul>
                <li><a href="#content">Skip to main content</a></li>
                <li><a href="#local-nav">Skip to local navigation</a></li>
                <li><a href="#global-nav">Skip to EBI global navigation menu</a></li>
                <li><a href="#global-nav-expanded">Skip to expanded EBI global navigation menu (includes all sub-sections)</a></li>
            </ul>
        </div> 
    
    
    

    
             <div id="wrapper" class="container_24">

            <%@include file="header.jspf" %>
            
            <div id="content" role="main" class="grid_24 clearfix"> 
                
                               <!--Global variables-->
                <c:set var="showButton" value="Show more"/>
                <c:set var="searchText" value="${searchModel.searchparams.text}"/>
                <c:set var="searchSequence" value="${searchModel.searchparams.sequence}"/>
                <%-- <c:set var="startRecord" value="${pagination.firstResult}"/>--%>
                <c:set var="searchresults" value="${searchModel.searchresults}"/>
                <c:set var="searchFilter" value="${searchresults.searchfilters}"/>
                
                 <c:set var="summaryEntries" value="${searchresults.summaryentries}"/>
                  <c:set var="summaryEntriesSize" value="${fn:length(summaryEntries)}"/>
                      <c:set var="totalfound" value="${searchresults.totalfound}"/>
                
                <c:set var="filterSizeDefault" value="${50}"/>
                <script>
                    var  speciesAutocompleteDataSource = [];
                    var compoundsAutoCompleteDataSource = [];
                    var diseaseAutoCompleteDataSource = [];
                </script>
                
                
                       <c:choose>
                            <c:when test="${searchModel.searchparams.type eq 'SEQUENCE'}">	
                                <c:set var="searchText" value="${searchModel.searchparams.sequence}"/>	
                            </c:when>

                            <c:otherwise>
                                <c:set var="searchText"
                                       value="${Fn:escapeHTML(searchModel.searchparams.text)}"/>
                            </c:otherwise>
                        </c:choose> 
                

                
                       <!-- Suggested layout containers -->  
                <section >
                    <div class="grid_12zzz" style="display: table; margin-left: 0em;">
                        <%@ include file="breadcrumbs.jsp" %>
                    </div>


                </section>
                    
                                <section class="grid_24 clearfix">
                    <section class="grid_18 alpha"  >
     
                          
                        <c:if test="${totalfound eq 0 && empty searchresults.searchfilters}">
                            <c:if test="${searchText eq ''}">
                               <c:set var="searchText"
                                       value=" "/> 
                            </c:if>
                            <h2>No results found</h2>
                            <p class="alert">We're sorry but we couldn't find anything that matched your search for " ${searchText} ". Please try another search or use the<a href="advanceSearch"> advanced search</a></p>
                            <script>
                                $(document).ready(function() {
                                    try {
                                        /* The simplest implementation, used on your zero search results pages */
                                        updateSummary({noResults: true});	       
                                    } catch (except_1) {}
                                });
                            </script>
                        </c:if>
                        <c:if test="${totalfound gt 0}">
                               <h2>${totalfound} results found</h2>
                        </c:if>
                          <c:if test="${totalfound eq 0 && not empty searchresults.searchfilters}">
                               <h2>${totalfound} result found</h2>
                        </c:if>
                    </section>
                    <c:if test="${searchModel.searchparams.type ne 'SEQUENCE' && not empty searchresults.searchfilters}">
                        <script src="${pageContext.request.contextPath}/resources/javascript/ebi-global-search-run.js"></script>
                        <script src="${pageContext.request.contextPath}/resources/javascript/ebi-global-search.js"></script>
                        <aside class="grid_6 omega shortcuts expander" id="search-extras">	    	
                        <div id="ebi_search_results"><h3
                            class="slideToggle icon icon-functional"
                            data-icon="u">Show more data from EMBL-EBI</h3>
                        </div>
                    </aside>
                    </c:if>

                </section>   
                    
                    
                    
                    
                    
                    
                    <section class="grid_6 alpha" id="search-results">
                         <c:set var="id" value=""/> 
                        <c:set var="name" value=""/> 
                        
                         <c:if test="${not empty searchresults.searchfilters}">
                             <c:choose>
                                 <c:when test="${not empty ec}">
                                   <c:set var="id" value="${ec}"/>   
                                 </c:when>
                                 <c:when test="${not empty taxId}">
                                     <c:set var="id" value="${taxId}"/> 
                                 </c:when>
                             </c:choose>
                                <c:choose>
                                 <c:when test="${not empty ecname}">
                                   <c:set var="name" value="${ecname}"/>   
                                 </c:when>
                                 <c:when test="${not empty organismName}">
                                     <c:set var="name" value="${organismName}"/> 
                                 </c:when>
                             </c:choose>
                        <div class="filter grid_24">
                            <div class="title">
                                Search Filters
                            </div>
                            <div class="line"></div>
                            <form:form id="filtersForm" name="filtersForm" modelAttribute="searchModel" action="${pageContext.request.contextPath}/search-enzymes/filter?entryid=${id}&entryname=${name}&searchTerm=${searchTerm}" method="POST">
                                <form:hidden path="searchparams.type" />	
                                <form:hidden path="searchparams.text" />
                                <form:hidden path="searchparams.sequence" />
                                <form:hidden path="searchparams.previoustext" />
                                <input type="hidden" id="filtersFormStart"
                                       name="searchparams.start" value="0"/>
                                <input id="pageNumber" type="hidden" name="pageNumber" value="0"/>
                                <input type="hidden" name="ec" value="${ec}"/>
                                <input type="hidden" name="ecname" value="${ecname}"/>
                                
                                  <input type="hidden" name="entryid" value="${taxId}"/>
                                <input type="hidden" name="entryname" value="${organismName}"/>
                                
                               
              
                                <%@ include file="filter-species.jspf"%>
                                <br/>
                                
                                <%@ include file="filter-compounds.jspf"%>
                                <br/>
                                <%@ include file="filter-diseases.jspf"%>
                                                                <br/>
                                 <%@ include file="filter-family.jsp"%>
                            </form:form>
                        </div> 
                        <%--filter --%>
                    </c:if>        
                        
                        
                        
                        
                        
                        
                    </section>
                
         <section class="grid_18" id="keywordSearchResult">       
                
        
  <c:if test="${totalfound gt 0 and page.totalElements gt 0}">  
<div style="width: 100%;">

                      <div class="action-buttons">
                  <%@include file="basket-buttons.jspf" %>
              </div>
   

                 <%--
                  <c:choose>
                      <c:when test="${filtering eq true}">
                              <c:url var="firstUrl" value="/search-enzymes/filter/page=1?ec=${ec}&ecname=${ecname}" />
                                <c:url var="lastUrl" value="/search-enzymes/filter/page=${page.totalPages}?ec=${ec}&ecname=${ecname}" />
                                <c:url var="prevUrl" value="/search-enzymes/filter/page=${currentIndex - 1}?ec=${ec}&ecname=${ecname}" />
                                <c:url var="nextUrl" value="/search-enzymes/filter/page=${currentIndex + 1}?ec=${ec}&ecname=${ecname}" />  
                      </c:when>
                      <c:otherwise>
                               <c:url var="firstUrl" value="/search-enzymes/page=1?ec=${ec}&ecname=${ecname}" />
                                <c:url var="lastUrl" value="/search-enzymes/page=${page.totalPages}?ec=${ec}&ecname=${ecname}" />
                                <c:url var="prevUrl" value="/search-enzymes/page=${currentIndex - 1}?ec=${ec}&ecname=${ecname}" />
                                <c:url var="nextUrl" value="/search-enzymes/page=${currentIndex + 1}?ec=${ec}&ecname=${ecname}" /> 
                      </c:otherwise>
                  </c:choose>
               --%>
                          
              
</div>
  
       <c:if test="${summaryEntriesSize gt 0 and searchresults.totalfound gt 0}">	

           <form>

<div id="paginationNavi" style="text-align: right;">
  <c:if test="${page.totalElements gt page.size}">	
<c:choose>
    <c:when test="${currentIndex == 1}">
       <a class="disabled" href="#">previous</a>
    </c:when>
    <c:otherwise>

          <a id="prevButton" href="javascript:void(0);">previous</a>
            <input id="prevPage" type="hidden"value="${currentIndex - 1}">
<!--                  <input id="ec" type="hidden"value="${ec}">
          <input id="ecname" type="hidden"value="${ecname}">
          <input type="hidden" name="entryid" value="${taxId}"/>
          <input type="hidden" name="entryname" value="${organismName}"/>-->
    </c:otherwise>
</c:choose>
  </c:if>


    Page ${page.number + 1} of ${page.totalPages} 

  <c:if test="${page.totalElements gt page.size}">	
    <c:choose>
        <c:when test="${currentIndex == page.totalPages}">
       <a class="disabled" href="#">next</a>

    </c:when>
    <c:otherwise>
        <a id="nextButton" href="javascript:void(0);">next </a>
        <input id="nextPage" type="hidden"value="${currentIndex + 1}">
<!--         <input id="ec" type="hidden"value="${ec}">
          <input id="ecname" type="hidden"value="${ecname}">-->
        </c:otherwise>
    </c:choose> 
  </c:if>

</div>
<div class="clearfix"></div>
    <!-- // Pagination END -->

       <%-- Add species filter to this form, don't lose it:
<c:forEach var="filterSp" items="${searchModel.searchresults.searchfilters.species}">
    <input type="checkbox" style="display: none;" 
           name="searchparams.species"
           value="${filterSp.scientificname}" />
</c:forEach>--%>
<%-- TODO: add also compounds and disease filters --%>
           </form>
                                        </c:if>            
              
              
        
         <div class="clear"></div>
          <div class="line"></div>
          
    <div class="resultContent">
 <c:set var="resultItemId" value="${0}"/>
       <c:forEach var="enzyme" items="${summaryEntries}">
          <c:set var="primAcc" value="${enzyme.accession}"/>
            <c:if test="${not empty enzyme.relatedspecies}">
         <c:set var="theSpecies" value="${enzyme.relatedspecies[0]}" /> 
                    </c:if>  
       <%@ include file="summary.jspf"%>
</c:forEach>


</div> 
          
          
   </c:if>          
          
          
            

</section>
              </div>
              
          
     
        <script type="text/javascript">
            jQuery(document).ready(function() {
        
                
               
                $("#filtersForm").submit(function(e) {

                    var frm = $('#filtersForm');
                    //$(frm).ajaxSubmit({
                        $.ajax({
                        type: frm.attr('method'),
                        url: frm.attr('action'),
                        data: frm.serialize(),
                        dataType: "HTML",
                        success: function(data) {
                            //console.log(data);
                          
    
              
                        }
                    });

                    //e.preventDefault();
                });    
        

                
                
            });




        </script>             
              
              
              
              
              
              
              

    <%@include file="footer.jspf" %>
    
        </div> <!--! end of #wrapper -->
        
        
      
        
        
        
        
        
    </body>
</html>


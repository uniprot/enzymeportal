<%-- 
    Document   : species
    Created on : Oct 19, 2015, 2:06:39 PM
    Author     : joseph
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib  prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="Fn" uri="/WEB-INF/epTagLibray.tld" %> 
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Infinite Scroll Specie Page</title>
    </head>
    <body>
        <h1>Species Facet infinite scroll</h1>
        
                            
       <div class="sublevel1 ">
    <div class="subTitle">
        Species (<c:out value="${page.totalElements} - ${ec}"></c:out>)
    </div>
    <c:if test="${fn:length(species) gt 12}">
        <!--auto-complete search box-->
        <div class="ui-widget grid_12zzz">
            <input id="specieAT" itemtype="text" name="speciefilter" class="filterSearchBox" placeholder="Enter Species to filter" />
            <input id="_ctempList_selected" name="_ctempList_selected" type="hidden" value=""/>
        </div>
    </c:if>
        <div class="filterContent icontainer" >
        <c:set var="speciesList" value="${species}"/>
        <c:set var="speciesListSize" value="${fn:length(speciesList)}"/>
      
            
                    
                    
      
                       <c:forEach items="${species}" var="sp">

                           <div  class="infitem">
                               ${sp.scientificName} 
                           </div>
                       </c:forEach>
       
                    
     
        <c:if test="${not empty species}">
        <c:url var="firstUrl" value="/species/${ec}/page/1/" />
        <c:url var="lastUrl" value="/species/${ec}/page/${page.totalPages}/" />
        <c:url var="prevUrl" value="/species/${ec}/page/${currentIndex - 1}/" />
        <c:url var="nextUrl" value="/species/${ec}/page/${currentIndex + 1}/" />

    </c:if> 
     
                                                                                                
     <c:if test="${page.totalElements gt page.size}">
         <ul id="pagination">
<c:choose>
<c:when test="${currentIndex == 1}">   
   <li class="previous disabled"><a href="#"><i class="fa fa-angle-left"></i> Older</a></li>
    </c:when>
    <c:otherwise>
    <li class="previous"><a href="${prevUrl}"><i class="fa fa-angle-left"></i> Older</a></li>
    
    </c:otherwise>
</c:choose>

<c:choose>
<c:when test="${currentIndex == page.totalPages}">
<li class="next disabled"><a href="#">Newer <i class="fa fa-angle-right"></i></a></li>
    </c:when>
    <c:otherwise>
  
    <li class="next"><a class="next" href="${nextUrl}">Newer <i class="fa fa-angle-right"></i></a></li>
</c:otherwise>
</c:choose>
         
         </ul>
     </c:if>      
                    

            </div>
            </div>
        
        
          <script src="http://code.jquery.com/jquery-2.1.0.min.js"></script>

      <script src="${pageContext.request.contextPath}/resources/javascript/jquery-ias.min.js"></script>

    <script type="text/javascript">
         $(document).ready(function () {
        var ias = $.ias({
            container: ".icontainer",
            item: ".infitem",
            pagination: "#pagination",
            next: ".next"
        });

//        ias().extension(new IASPagingExtension());
        ias.extension(new IASSpinnerExtension());
        ias.extension(new IASTriggerExtension({offset: 50}));
        ias.extension(new IASNoneLeftExtension({text: 'no more species to load.'}));

 });
    </script> 
    </body>
</html>

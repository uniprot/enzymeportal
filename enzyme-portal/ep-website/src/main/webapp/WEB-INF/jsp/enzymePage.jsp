<%--
    Document   : enzymePage
    Created on : Feb 21, 2017, 3:05:34 PM
    Author     : Joseph <joseph@ebi.ac.uk>
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

        <%@include file="head.jspf" %>
        <title>Enzyme Portal Enzyme Page</title>
    </head>
    <body>



    <div id="skip-to">
        <ul>
            <li><a href="#content">Skip to main content</a></li>
            <li><a href="#local-nav">Skip to local navigation</a></li>
            <li><a href="#global-nav">Skip to EBI global navigation menu</a></li>
            <li><a href="#global-nav-expanded">Skip to expanded EBI global navigation menu (includes all sub-sections)</a>
            </li>
        </ul>
    </div>
    <script>
        var speciesAutocompleteDataSource = [];
        var compoundsAutoCompleteDataSource = [];
        var diseaseAutoCompleteDataSource = [];
        var ecAutoCompleteDataSource = [];
    </script>

    <div id="wrapper" class="container_24">
        <%@include file="header.jspf" %>

        <div id="content" role="main" class="clearfix" >

            <div class="row" id="title-row">
                <div class="large-2 columns">
                    <img src="/enzymeportal/resources/images/enzyme_page_logo.jpg">
                </div>

                <section class="large-7 columns">
                    <h1>Enzyme</h1>
                    <h2>${enzymePage.ec} - ${enzymePage.enzymeName}</h2>
                </section>

                <section class="large-3 columns">
                    <div id="alternative-names">
                        <c:if test="${not empty enzymePage.altNames}">
                        <strong>Alternative Name(s):</strong>
                        <c:forEach items="${enzymePage.altNames}" var="a">
                           <p>${a.altName}</p> 
                        </c:forEach>
                        
                        </c:if>
                    </div>
                </section>

            </div>
            <section class="row">
                <h3>Catalytic Activity</h3>
                <p>${enzymePage.catalyticActivities}</p>


                <h3>Cofactors</h3>
                     <c:choose>
                       <c:when test="${not empty enzymePage.cofactors }">
                           <c:forEach items="${enzymePage.cofactors}" var="c">
                            <p>${c.cofactor}</p>
                        </c:forEach>
                       </c:when>
                       <c:otherwise>
                           There are no Cofactors for this Enzyme
                       </c:otherwise>
                     </c:choose>


                <h3>Associated Proteins</h3>
                     <c:choose>
                       <c:when test="${not empty enzymePage.proteins }">
                           
                           <h4>Note:: Hit count : ${enzymePage.proteins.hitCount}</h4>
                           <c:forEach items="${enzymePage.proteins.entries}" var="p">
                               
                               <ul>
                                   <li> <a href="${pageContext.request.contextPath}/search/${p.proteinGroupId}/proteins">${p.proteinName}</a></li>
                               </ul>
                           </c:forEach>
                       </c:when>
                       <c:otherwise>
                           There are no Associated Proteins for this Enzyme
                       </c:otherwise>
                     </c:choose>




                <h3>Citations</h3>
                <c:choose>
                  <c:when test="${not empty enzymePage.citations }">
                      <c:forEach items="${enzymePage.citations}" var="citation">
                          <ul>
                              <li> <c:out value="${citation.title}"/></li>
                          </ul>
                      </c:forEach>
                  </c:when>
                  <c:otherwise>
                      There are no citations for this Enzyme
                  </c:otherwise>
                </c:choose>







            </section>

        </div>

       <%@include file="footer.jspf" %>
   </div>






    </body>
</html>

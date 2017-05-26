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

                <section class="large-10 columns">
                    <h1>Enzyme</h1>
                    <h2>${enzymePage.ec} - ${enzymePage.enzymeName}</h2>
                </section>

            </div>

            <div class="row" id="title-row">
               <div class="large-2 columns"></div>
               <div class="large-10 columns">
                   <h5>Alternative Name(s):</h5>
                   <div id="alternative-names">
                       <c:if test="${not empty enzymePage.altNames}">
                           <ul>
                               <c:forEach items="${enzymePage.altNames}" var="altName">
                                   <li>${altName}</li>
                               </c:forEach>
                            </ul>
                       </c:if>
                   </div>
               </div>
             </div>

            <section class="row enzymeContent">
                <div class="large-2 columns"></div>
                <div class="large-10 columns">
                    <h3>Catalytic Activity</h3>
                    <p>${enzymePage.catalyticActivities}</p>

                    <section>
                    <h3>Cofactors</h3>
                         <c:choose>
                           <c:when test="${not empty enzymePage.cofactors }">
                               <c:forEach items="${enzymePage.cofactors}" var="c">
                                <p>${c}</p>
                            </c:forEach>
                           </c:when>
                           <c:otherwise>
                               There are no Cofactors for this Enzyme
                           </c:otherwise>
                         </c:choose>

                    </section>
                    <section>
                    <h3>Associated Proteins</h3>
                         <c:choose>
                           <c:when test="${not empty enzymePage.proteins.entries }">
                               <c:set var="count" value="0" scope="page" />
                               <table id="associatedProteins">
                                  <tr>
                                      <th>Protein name</th>
                                      <th>Organism</th>
                                  </tr>
                                   <c:forEach items="${enzymePage.proteins.entries}" var="p" varStatus="proteinsCounter">
                                       <c:if test="${proteinsCounter.count <= 5}">
                                       <tr>
                                           <td><a href="${pageContext.request.contextPath}/search/${p.primaryAccession}/enzyme">${p.proteinName}</td>
                                           <td>${p.primaryOrganism}</td>
                                       </tr>
                                       </c:if>
                                   </c:forEach>

                                   <c:if test="${fn:length(enzymePage.proteins.entries) > 5}">
                                   <tr>
                                       <td colspan="3">button here1</td>
                                   </tr>
                                   </c:if>
                                   <c:if test="${fn:length(enzymePage.proteins.entries) <5}">
                                   <tr>
                                       <td colspan="3">button here2</td>
                                   </tr>
                                   </c:if>

                               </table>
                           </c:when>
                           <c:otherwise>
                               There are no Associated Proteins for this Enzyme
                           </c:otherwise>
                         </c:choose>
                    </section>

                    <section>
                    <h3>Citations</h3>
                    <c:choose>
                      <c:when test="${not empty enzymePage.citations }">
                          <ul id="citations">
                          <c:forEach items="${enzymePage.citations}" var="citation">

                                  <li> <c:out value="${citation.title}"/></li>

                          </c:forEach>
                          </ul>
                      </c:when>
                      <c:otherwise>
                          There are no citations for this Enzyme
                      </c:otherwise>
                    </c:choose>

                    </section>

                </div>

            </section>

        </div>

       <%@include file="footer.jspf" %>
   </div>






    </body>
</html>

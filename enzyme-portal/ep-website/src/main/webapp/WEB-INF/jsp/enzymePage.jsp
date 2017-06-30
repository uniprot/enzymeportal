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
                <c:choose>
                   <c:when test="${not empty enzymePage.altNames}">
                       <h5>Alternative Name(s)</h5>
                   </c:when>
                   <c:otherwise>
                       <h5 class="noResults">Alternative Name(s)</h5>
                   </c:otherwise>
                    </c:choose>

                   <div id="alternative-names">
                       <c:choose>
                       <c:when test="${not empty enzymePage.altNames}">
                           <ul>
                               <c:forEach items="${enzymePage.altNames}" var="altName">
                                   <li>${altName}</li>
                               </c:forEach>
                            </ul>
                       </c:when>
                       <c:otherwise>
                           <p class="noResults">There are no alternative names for this Enzyme</p>
                       </c:otherwise>
                       </c:choose>
                   </div>
               </div>
             </div>

            <section class="row enzymeContent">
                <div class="large-2 columns"></div>
                <div class="large-10 columns">
                    <h3>Catalytic Activity</h3>
                    <p>${enzymePage.catalyticActivities}</p>

                    <section>
                         <c:choose>
                           <c:when test="${not empty enzymePage.cofactors }">
                               <h3>Cofactors</h3>
                               <c:forEach items="${enzymePage.cofactors}" var="c">
                                <p>${c}</p>
                                </c:forEach>
                           </c:when>
                           <c:otherwise>
                               <h3 class="noResults">Cofactors</h3>
                               <p class="noResults"> There are no Cofactors for this Enzyme</p>
                           </c:otherwise>
                         </c:choose>

                    </section>
                    <section>
                    <h3>Associated Proteins</h3>
                         <c:choose>
                           <c:when test="${not empty enzymePage.proteins.entries }">
                               <c:set var="count" value="0" scope="page" />



                               <form:form id="proteinViewForm-${enzymePage.ec}" action="${pageContext.request.contextPath}/search" modelAttribute="searchModel" method="POST">
                                   <input name="keywordType" type="hidden" value="EC">
                                   <input name="searchTerm" type="hidden" value="${enzymePage.ec}">
                                   <input name="searchId" type="hidden" value="${enzymePage.ec}">
                                   <input name="ec" type="hidden" value="${enzymePage.ec}">
                                   <form:hidden path="searchparams.previoustext" />
                                   <form:hidden path="searchparams.text" value="${enzymePage.ec}-${enzymePage.ec}" />
                                  <form:hidden path="searchparams.type" value="KEYWORD"/>


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


                                   <tr>

                                       <td colspan="3">
                                   <c:if test="${enzymePage.numProteins > 5}">
                                         <button id="full-view" class="full-view icon icon-functional btn" data-icon="F" type="submit"> View all ${enzymePage.numProteins} Proteins</button>
                                   </c:if>
                                       </td>

                                   </tr>

                                   <%--<c:if test="${fn:length(enzymePage.proteins.entries) > 5}">--%>
                                   <%--<tr>--%>
                                       <%--<td colspan="3">button here1</td>--%>
                                   <%--</tr>--%>
                                   <%--</c:if>--%>
                                   <%--<c:if test="${fn:length(enzymePage.proteins.entries) <5}">--%>
                                   <%--<tr>--%>
                                       <%--<td colspan="3">--%>
                                           <%----%>

                                           <%--<c:if test="${enzyme.numProteins >= 5}">--%>
                                                 <%--<button id="full-view" class="full-view icon icon-functional btn" data-icon="F" type="submit"> View all ${enzyme.numProteins} Proteins</button>--%>
                                           <%--</c:if>--%>


                                   <%--</c:if>--%>

                               </table>
                               </form:form>
                           </c:when>
                           <c:otherwise>
                               There are no Associated Proteins for this Enzyme
                           </c:otherwise>
                         </c:choose>
                    </section>

                    <section>

                    <c:choose>
                      <c:when test="${not empty enzymePage.citations }">
                          <h3>Citations</h3>
                          <ul id="citations">
                          <c:forEach items="${enzymePage.citations}" var="citation">

                                  <li>

                              <a href="http://europepmc.org/abstract/${citation.source}/${citation.id}"
                                              title="View ${citation.source} ${citation.id} in Europe PubMed Central"
                                              target="_blank" class="extLink ${citation.source}">${citation.title}</a>
                                  </li>

                          </c:forEach>
                          </ul>

                          <a href="http://europepmc.org/search?page=1&query=${enzymePage.enzymeName}" target="_blank">
                              <button id="all-proteins" class="full-view icon icon-functional btn" data-icon="F" type="submit"> View more in Europe PMC</button>
                          </a>
                      </c:when>
                      <c:otherwise>
                          <h3 class="noResults">Citations</h3>
                          <p class="noResults">There are no citations for this Enzyme</p>
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

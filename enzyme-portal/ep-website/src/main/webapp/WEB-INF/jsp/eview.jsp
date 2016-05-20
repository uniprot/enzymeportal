<%-- 
    Document   : eview
    Created on : May 17, 2016, 5:34:48 PM
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
    <c:set var="pageTitle" value="Enzymes"/>
    <%@include file="head.jspf" %>


</head>

<body class="level2"><!-- add any of your classes or IDs -->
<div id="skip-to">
    <ul>
        <li><a href="#content">Skip to main content</a></li>
        <li><a href="#local-nav">Skip to local navigation</a></li>
        <li><a href="#global-nav">Skip to EBI global navigation menu</a></li>
        <li><a href="#global-nav-expanded">Skip to expanded EBI global navigation menu (includes all sub-sections)</a>
        </li>
    </ul>
</div>

<div id="wrapper" class="container_24">
    <%@include file="header.jspf" %>

    <div id="content" role="main" class="grid_24 clearfix" >
        <h1>${ebiResult.hitCount} Enzymes found for ${searchKey}</h1>

         <c:if test="${not empty enzymeView}">
             <div class="grid_6">
<!--                 TAXONOMY,enzyme_family,disease_name,compound_type-->
                 <c:forEach var="facet" items="${enzymeFacet}">
                     <div>
                         <c:if test="${facet.id eq 'enzyme_family'}">
                             <h3>Enzyme Family</h3>
                             <ul>
                             <c:forEach var="v" items="${facet.facetValues}">
                                 <li><a href="${v.value} ">${v.label} </a>(${v.count})</li>   
                             </c:forEach>
                             </ul>
                         </c:if>
                         <c:if test="${facet.id eq 'compound_type'}">
                             <h3>compounds</h3>
                             <ul>
                             <c:forEach var="v" items="${facet.facetValues}">
                                 <li><a href="${v.value} ">${v.label} </a>(${v.count})</li>   
                             </c:forEach>
                             </ul>
                         </c:if>
                          <c:if test="${facet.id eq 'disease_name'}">
                             <h3>Diseases</h3>
                             <ul>
                             <c:forEach var="v" items="${facet.facetValues}">
                                 <li><a href="${v.value} ">${v.label} </a>(${v.count})</li>   
                             </c:forEach>
                             </ul>
                         </c:if>
                            <c:if test="${facet.id eq 'TAXONOMY'}">
                                <h3>Organism</h3>
                                <ul>
                             <c:forEach var="v" items="${facet.facetValues}">
                                 <li><a href="${v.value} ">${v.label} </a>(${v.count})</li>   
                             </c:forEach>
                                 </ul>
                             
                         </c:if>
                     </div> 
                 </c:forEach>
            </div>
            <div class="grid_18">

                   <table id="enzymeResults" cellpadding="60px">
                       <tr>
                           <th>Name</th>
                           <th>Hits</th>
                           <th>Enzyme Family</th>
                           <th>EC</th>
                           <th>Catalytic Activity</th>
                           <th>Species</th>
                       </tr>

                       <c:forEach var="enzyme" items="${enzymeView}">
                           <tr class="enzymeRow">
                               <td class="enzymeName sideTwizzle">${enzyme.enzymeName}</td>
                               <td>${enzyme.numEnzymeHits}</td>
                               <td>${enzyme.enzymeFamily}</td>
                               <td>${enzyme.ec}</td>
                               <td>${enzyme.catalyticActivities}</td>
                               <td class="hiddenX">${enzyme.species}</td>
                               

                           </tr>
                           <tr id="proteinList" style="display: none">
                               <td colspan="6">
                                <table id="enzymeResultsProteins">
                                    <tr>
                                        <th> </th>
                                        <th>Associated Proteins:</th>
                                    </tr>
                                    <c:forEach var="protein" items="${enzyme.proteins}">
                                    <tr class="proteinRow">
                                        <td> </td>
                                        <td><a href="${pageContext.request.contextPath}/search/${protein.accession}/enzyme">${protein.proteinName}[Human]</a></td>

<!--                                        <td>Human, Greater horseshoe bat, Rat, Mouse, Thirtenn-lined ground squirrel</td>-->

                                    </tr>
                                    </c:forEach>
                                    <tr class="proteinRow">
                                        <td></td>
                                        <td><a id="full-view" href="#" class="icon icon-functional btn" data-icon="F">Full View</a></td>
                                    </tr>
                                </table>
                                </td>

                           </tr>
                       </c:forEach>

                   </table>
<%-- 
                    <c:forEach var="e" items="${enzymes}">
                     
                        <div><b>Name</b> : ${e.enzymeName} , EC : ${e.ecNumber} , Activities : ${e.catalyticActivity}, Hits : ${fn:length(e.dummyProteinSet)} </div>
                       <c:forEach var="p" items="${e.dummyProteinSet}">

                        <div> ${p.proteinName} </div>
                        </c:forEach>
                    </c:forEach>
--%>
             

            </div>


        </c:if>

    </div>

    <%@include file="footer.jspf" %>
</div>
<!--! end of #wrapper -->
</body>
</html>


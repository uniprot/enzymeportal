<%-- 
    Document   : search
    Created on : Mar 31, 2011, 7:57:06 PM
    Author     : hongcao
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
    <head>
        <title>Enzyme Portal</title>
    </head>
    <body>
        <div id="keywordSearch">
            <form:form modelAttribute="searchParameters" action="showResults" method="get">
                <p>
                    <form:input path="keywords" size="80"/><input type="submit" />
                </p>
            </form:form>
        </div>
    <div id="keywordSearchResult">
      <form:form modelAttribute="enzymeSummaryCollection" action="showResults" method="get">
        <c:set var="totalfound" value="${enzymeSummaryCollection.totalfound}"/>
        <c:if test="${totalfound==0}">
            No results found!
        </c:if>
         <c:if test="${enzymeSummaryCollection.enzymesummary!=null && enzymeSummaryCollection.totalfound>0}">
             <table width="100%"><tr><td width="30%">
About <c:out value="${totalfound}"/> results found
                     </td><td width="20%"></td>
                     <td width="50%">
            <form:form modelAttribute="pagination">
                <c:set var="totalPages" value="${pagination.totalPages}"/>
                <c:set var="maxPages" value="${totalPages}"/>
                <c:if test="${totalPages>pagination.maxDisplayedPages}">
                    <c:set var="maxPages" value="${pagination.maxDisplayedPages}"/>
                    <c:set var="showNextButton" value="${true}"/>
                </c:if>
                <c:forEach var="i" begin="1" end="${maxPages}">
                  <c:set var="start" value="${(i-1)*pagination.numberResultsPerPage}"/>
                  <a href="showResults?keywords=${searchParameters.keywords}&start=${start}">
                      <c:out value="${i}"/>
                  </a>
                  &nbsp;
              </c:forEach>
                <c:if test="${showNextButton==true}">
                  <a href="showResults?keywords=${searchParameters.keywords}&start=${pagination.start+pagination.numberResultsPerPage}">
                      next
                  </a>
                </c:if>
            </form:form>

                     </td></tr></table>
                
                <table border="1"  width="100%">
                    <thead>
                        <tr>
                            <td width="10%">Id</td>
                            <td width="30%">Accessions</td>
                            <td width="30%">Name</td>
                            <td width="30%">Species</td>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${enzymeSummaryCollection.enzymesummary}" var="enzyme">
                        <tr>
                            <td><c:out value="${enzyme.uniprotid}"/></td>
                            <td>
                              <c:forEach items="${enzyme.uniprotaccessions}" var="uniprotAcccession">
                                  <!--TODO make the url base configured-->
                                  <a href="http://www.uniprot.org/uniprot/${uniprotAcccession}" target="blank">
                                      <c:out value="${uniprotAcccession}"/>
                                  </a>
                                  &nbsp;
                              </c:forEach>                                
                            </td>
                            <td><c:out value="${enzyme.name}"/></td>
                            <td><c:out value="${enzyme.species.scientificname}"/></td>
                        </tr>
                        </c:forEach>
                    </tbody>
                </table>
    </c:if>

                <!--
                <c:forEach items="${enzymes}" var="enzyme">
                    <p>
                        Uniprot id: <c:out value="${enzyme.uniprotid}"/><br>
                        Uniprot accession: 
                          <c:forEach items="${enzyme.uniprotaccessions}" var="uniprotAcccession">
                              <a href="http://www.uniprot.org/uniprot/${uniprotAcccession}" target="blank">
                                  <c:out value="${uniprotAcccession}"/>
                              </a>
                          </c:forEach>
                        <br>
                        Uniprot name: <c:out value="${enzyme.name}"/><br>
                        Uniprot species: <c:out value="${enzyme.species.scientificname}"/>
                    </p>
                </c:forEach>
                -->
          
    </form:form>
      </div>
    </body>
</html>

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
            <form:form modelAttribute="searchParameters" action="showResults" method="post">                
                <p>
                    <form:input path="keywords" size="80"/><input type="submit" />
                </p>
            </form:form>
        </div>
        <div id="keywordSearchResult">            
            <form:form modelAttribute="enzymeSummaryCollection" action="showResults" method="post">
                <c:if test="${enzymeSummaryCollection.enzymesummary!=null}">
                About <c:out value="${enzymeSummaryCollection.totalfound}"/> results found
                <table border="1">
                    <thead>
                        <tr>
                            <td>Id</td>
                            <td width="500">Accessions</td>
                            <td>Name</td>
                            <td>Species</td>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${enzymeSummaryCollection.enzymesummary}" var="enzyme">
                        <tr>
                            <td><c:out value="${enzyme.uniprotid}"/></td>
                            <td>
                              <c:forEach items="${enzyme.uniprotaccessions}" var="uniprotAcccession">
                                  <a href="http://www.uniprot.org/uniprot/${uniprotAcccession}" target="blank">
                                      <c:out value="${uniprotAcccession}"/>
                                  </a>
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

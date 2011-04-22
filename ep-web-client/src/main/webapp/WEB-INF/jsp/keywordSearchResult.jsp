<%-- 
    Document   : keywordSearchResult
    Created on : Apr 4, 2011, 3:24:13 PM
    Author     : hongcao
--%>
<%@ page session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
  <head><title>Search Results</title></head>
  <body>
    <h3>Search Results</h3>
    <form:form modelAttribute="enzymes" action="showResults" method="post">
    <c:forEach items="${enzymes}" var="enzyme">
        <p>
          Uniprot id: <c:out value="${enzyme.uniprotid}"/><br>
          Uniprot accession:
          <c:forEach items="${enzyme.uniprotaccession}" var="uniprotAcccession">
              <c:out value="${uniprotAcccession}"/><br>
          </c:forEach>

          Uniprot name: <c:out value="${enzyme.name}"/>
        </p>
    </c:forEach>
    </form:form>
  </body>
</html>
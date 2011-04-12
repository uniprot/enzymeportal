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
                    <form:input path="keywords" /><input type="submit" />
                </p>
            </form:form>
        </div>
        <div id="keywordSearchResult">
            <h3>Search Results</h3>
            <form:form modelAttribute="enzymes" action="showResults" method="post">
                <c:forEach items="${enzymes}" var="enzyme">
                    <p>
                        Uniprot id: <c:out value="${enzyme.uniprotid}"/><br>
                        Uniprot accession: <c:out value="${enzyme.uniprotaccession}"/><br>
                        Uniprot name: <c:out value="${enzyme.name}"/>
                    </p>
                </c:forEach>
            </form:form>
        </div>
    </body>
</html>

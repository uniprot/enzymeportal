<%-- 
    Document   : search
    Created on : Jun 9, 2014, 3:07:27 PM
    Author     : joseph
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Enzyme Portal | Search</title>
    </head>
    <body>
        <h1>Diseases found</h1>

        <table>

            <thead>
                <tr>

                    <th class="center">Disease Id</th>
                    <th class="center">Disease Name</th>

                </tr>
            </thead>
            <tbody>
                <c:forEach var="d" items="${diseases}">
                <tr>
                    <td>${d.diseaseId}</td>
                    <td>${d.diseaseName}</td>
                </tr>
            </c:forEach>
                
            </tbody>      



        </table>


    </body>
</html>

<%-- 
    Document   : errors
    Created on : 15-Feb-2012, 12:02:21
    Author     : joseph
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%--<%@page contentType="text/html" pageEncoding="UTF-8"%>--%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Enzyme Entry</title>
        <link rel="stylesheet"  href="http://www.ebi.ac.uk/inc/css/contents.css"     type="text/css" />
        <link media="screen" href="../../resources/lib/spineconcept/css/960gs-fluid/grid.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="../../resources/lib/spineconcept/css/common.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="../../resources/lib/spineconcept/css/summary.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="../../resources/lib/spineconcept/css/literature.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="../../resources/lib/spineconcept/css/species.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="../../resources/lib/spineconcept/javascript/jquery-ui/css/custom-theme/jquery-ui-1.8.11.custom.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="../../resources/css/enzyme.css" type="text/css" rel="stylesheet" />

        <link href="../../resources/css/search.css" type="text/css" rel="stylesheet" />
        <script src="../../resources/lib/spineconcept/javascript/jquery-1.5.1.min.js" type="text/javascript"></script>
        <script src="../../resources/lib/spineconcept/javascript/jquery-ui/js/jquery-1.5.1.min.js" type="text/javascript"></script>
        <script src="../../resources/lib/spineconcept/javascript/jquery-ui/js/jquery-ui-1.8.11.custom.min.js" type="text/javascript"></script>
        <script src="../../resources/lib/spineconcept/javascript/summary.js" type="text/javascript"></script>
    </head>
    <body>
        <div class="grid_12 content">
            <div class="iconGroup">
                <h2 style="color: red;text-align:center" >Service Error</h2>
                There has been an error with your request. Please try again later.
            </div>
            <div class="clear"></div>
        </div>
<%--<jsp:include page="footer.jsp"/>--%>
    </div>
</body>
</html>

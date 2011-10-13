<%--
    Document   : search
    Created on : Mar 31, 2011, 7:57:06 PM
    Author     : hongcao
--%>
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib  prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
    <head>
        <title>Enzyme Portal</title>
        <link rel="stylesheet"  href="http://www.ebi.ac.uk/inc/css/contents.css"     type="text/css" />
        <link media="screen" href="resources/lib/spineconcept/css/960gs-fluid/grid.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="resources/lib/spineconcept/css/common.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="resources/lib/spineconcept/css/identification.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="resources/lib/spineconcept/css/species.css" type="text/css" rel="stylesheet" />
        <script src="resources/lib/spineconcept/javascript/jquery-1.5.1.min.js" type="text/javascript"></script>
        <script src="resources/lib/spineconcept/javascript/identification.js" type="text/javascript"></script>
        <link href="resources/css/search.css" type="text/css" rel="stylesheet" />
        <script src="resources/javascript/search.js" type="text/javascript"></script>
    </head>
    <body>
        <jsp:include page="header.jsp"/>
        <div class="contents">
            <div class="page container_12">
                <form:form id="searchForm" modelAttribute="searchModel" action="search" method="POST">
                <jsp:include page="subHeader.jsp"/>
                
                
                    <jsp:include page="searchBox.jsp"/>

                    <div class="grid_12 content">
                        <div class="iconGroup">
                            <fieldset class="iconField">
                                    <legend>Enzyme Classification</legend>
                                    <div class="iconBox">
                                        <img src="resources/images/classification.png"
                                        	alt="?" width="100px" height="100px"/>
                                    </div>
                            </fieldset>
                            <fieldset class="iconField">
                                    <legend>Disease</legend>
                                    <div class="iconBox">
                                        <img src="resources/images/disease3.png"
                                        	alt="?" width="100px" height="100px"/>
                                    </div>
                            </fieldset>                            
                            <fieldset class="iconField">
                                    <legend>Expression</legend>
                                    <div class="iconBox">
                                        <img src="resources/images/expression.png"
                                        	alt="?" width="100px" height="100px"/>
                                    </div>
                            </fieldset>
                        </div>
                        <div class="iconGroup">
                            <fieldset class="iconField">
                                    <legend>Chemical Compound</legend>
                                    <div class="iconBox">
                                        <img src="resources/images/compounds.png"
                                        	alt="?" width="100px" height="100px"/>
                                    </div>
                            </fieldset>
                            <fieldset class="iconField">
                                    <legend>Taxonomy</legend>
                                    <div class="iconBox">
                                        <img src="resources/images/taxonomy.png"
											alt="?" width="100px" height="100px"/>
                                    </div>
                            </fieldset>
                        </div>
                    </div>

                </form:form>
            </div>
            <jsp:include page="footer.jsp"/>
        </div>
    </body>
</html>
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
        <link rel="stylesheet" type="text/css"
        	href="http://www.ebi.ac.uk/inc/css/contents.css" />
		<link rel="stylesheet" type="text/css" href="resources/css/search.css"/>
        <link media="screen" href="resources/lib/spineconcept/css/960gs-fluid/grid.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="resources/lib/spineconcept/css/common.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="resources/lib/spineconcept/css/identification.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="resources/lib/spineconcept/css/species.css" type="text/css" rel="stylesheet" />

        <script src="resources/lib/spineconcept/javascript/jquery-1.5.1.min.js" type="text/javascript"></script>
        <script src="resources/lib/spineconcept/javascript/identification.js" type="text/javascript"></script>
        <script src="resources/javascript/search.js" type="text/javascript"></script>
        <meta name="google-site-verification" content="tXBo-O4mKKZgv__6QG6iyjqDJibhSb3ZAQtXQGjo86I" />
    </head>
    <body>
        <jsp:include page="header.jsp"/>
        <div class="contents">
            <div class="page container_12">
                <jsp:include page="subHeader.jsp"/>
                <jsp:include page="searchBox.jsp"/>

                <div style="margin-left: auto; margin-right: auto;
                       width: 50%;">
	                <h2>Welcome to the Enzyme Portal</h2>
                	The Enzyme Portal is for people who are interested
                	in the biology of enzymes and proteins with enzymatic
                	activity.
	                <div style="text-align: right;">
		                <a href="about" class="showLink">More about the
		                	enzyme portal...</a>
	                </div>
                </div>

                   <table style="margin-left: auto; margin-right: auto;
                       width: 75em; margin-top: 4ex; margin-bottom: 4ex;">
                       <tr style="text-align: center;">
                        <td>
                        <%-- b>Welcome to the Enzyme Portal</b --%>
                        <iframe width="360" height="270"
                            src="http://www.youtube.com/embed/Kldp0WXcxUM"
                            frameborder="0"
                            allowfullscreen></iframe>
                        </td>
                        <td>
                        <%-- b>Explore Enzyme Portal</b --%>
                        <iframe width="360" height="270"
                            src="http://www.youtube.com/embed/b7hFo5iJuoM"
                            frameborder="0"
                            allowfullscreen></iframe>
                        </td>
                       </tr>
                   </table>
<%--
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
 --%>
            </div>
            <jsp:include page="footer.jsp"/>
        </div>
    </body>
</html>
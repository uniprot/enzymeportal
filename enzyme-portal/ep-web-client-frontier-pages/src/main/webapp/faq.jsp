<%-- 
    Document   : faqPage
    Created on : 17-Apr-2012, 11:22:09
    Author     : joseph
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
        <jsp:include page="WEB-INF/jsp/header.jsp"/>
        <div class="contents">
            <div class="page container_12">
                <jsp:include page="WEB-INF/jsp/subHeader.jsp"/>
            </div>
            <div class="grid_12 content">

                <h1>Frequently Asked Questions</h1>
                <ul>
                    <li><a href="faq.jsp#01">How is the EnzymePortal different from BRENDA?</a></li>
                </ul>
                <a name="01"></a><h2>How is the EnzymePortal different from BRENDA?</h2>
                <p>The <a href="/enzymeportal" class="showLink" >EnzymePortal</a>  is a one-stop shop for enzyme-related information in resources developed at the EBI. It accumulated this information and aims to present it to the scientist with a unified user experience. The EnzymePortal team does not curate enzyme information and therefore is a secondary information resource or portal. At some point, a user interested in more detail will always leave the EP pages and refer to the information in the underlying primary database (Uniprot, PDB, etc.) directly.
                </p><p><a href="http://www.brenda-enzymes.info/" >BRENDA</a>  is the most comprehensive resource about enzymes world-wide and has invested a great amount into the abstraction and curation about enzymes and their related information. BRENDA contains valuable information that can not be found in the EnzymePortal at the moment, such as kinetic, specifity, stability, application, disease-related and engineering data. As a primary resource, BRENDA could be a candidate for an information source for the EP in the future.</p>

            </div>

            <jsp:include page="WEB-INF/jsp/footer.jsp"/>
        </div>
    </body>
</html>

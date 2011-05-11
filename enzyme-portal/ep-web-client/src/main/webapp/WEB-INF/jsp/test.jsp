<%--
    Document   : search
    Created on : Mar 31, 2011, 7:57:06 PM
    Author     : hongcao
--%>
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
    <head>
        <title>Enzyme Portal</title>
        <!--
        <link media="screen" href="resources/spineconcept/css/960gs/reset.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="resources/spineconcept/css/960gs/text.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="resources/spineconcept/css/960gs/960.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="resources/spineconcept/css/common.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="resources/spineconcept/css/identification.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="resources/spineconcept/css/species.css" type="text/css" rel="stylesheet" />-->
        <!--<link media="screen" href="resources/spineconcept/javascript/jquery-ui/css/custom-theme/jquery-ui-1.8.11.custom.css" type="text/css" rel="stylesheet" />-->
       <link media="screen" href="resources/spineconcept/css/common.css" type="text/css" rel="stylesheet" />
        <link href="resources/lib/layout-default-latest.css" type="text/css" rel="stylesheet" />
        <link href="resources/spineconcept/css/epHome.css" type="text/css" rel="stylesheet" />
        <script type="text/javascript" src="resources/lib/jquery-latest.js"></script>
        <script type="text/javascript" src="resources/lib/jquery-ui-latest.js"></script>
        <script type="text/javascript" src="resources/lib/jquery.layout-latest.js"></script>

        <!--<link href="resources/spineconcept/css/epHome.css" type="text/css" rel="stylesheet" />-->
        <!--
        <script src="resources/spineconcept/javascript/jquery-1.5.1.min.js" type="text/javascript"></script>
        <script src="resources/spineconcept/javascript/jquery-ui/js/jquery-ui-1.8.11.custom.min.js" type="text/javascript"></script>
        <script src="resources/spineconcept/javascript/identification.js" type="text/javascript"></script>
        -->
        <script type="text/javascript">

                $(document).ready(function () {
                        $('#container').layout();
                });

        </script>
    </head>
    <body>
        <!--
        <div class="page container_12">
            <div class="grid_12">
                <div class="breadcrumbs" wicket:id="breadcrumbs">
                    <ul>
                        <li class="first"><a href="">EBI</a></li>
                        <li><a href="">Databases</a></li>
                        <li><a href="">Enzyme Portal</a></li>
                        <li><a href="">Search</a></li>
                    </ul>
                </div>
            </div>

            <div class="grid_12 search">
                <div id="keywordSearch">
                    <form:form modelAttribute="searchParameters" action="showResults" method="get">
                        <p>
                            <form:input path="keywords" cssClass="field"/>
                            <input type="submit" value="Search" class="button"/>
                        </p>
                    </form:form>
                </div>
            </div>
            <div>
                Testttttttttttttttttttttt
                <br/>
                sd
                <br/>
                sd
                <br/>
                sd
                <br/>
                sd
                <br/>
                s
                f
            </div>
            <div class="grid_12">
                <div class="footer">&copy;
                    <a target="_top" href="http://www.ebi.ac.uk/" title="European Bioinformatics Institute Home Page">European Bioinformatics Institute</a>
				  2011. EBI is an Outstation of the
                    <a href="http://www.embl.org/" target="_blank" title="European Molecular Biology Laboratory Home Page">European Molecular Biology Laboratory</a>.
                </div>
            </div>
            <div class="clear"></div>
        </div>
        -->
<div id="container">
	<div class="pane ui-layout-center">
		Center
		<p><a href="http://layout.jquery-dev.net/demos.html"><b>Go to the Demos page</b></a></p>

	</div>
	<div class="pane ui-layout-north">
            <div id="keywordSearch" class="search">
                <form:form modelAttribute="searchParameters" action="showResults" method="get">
                    <p>
                        <form:input path="keywords" cssClass="field"/><input type="submit" value="Search" class="button"/>
                    </p>
                </form:form>
            </div>
        </div>
	<div class="pane ui-layout-south">South</div>
	<div class="pane ui-layout-west">West</div>
</div>

    </body>
</html>
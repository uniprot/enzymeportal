<%--
    Document   : entryPageError
    Created on : 15-Feb-2012, 12:02:21
    Author     : joseph
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<!-- paulirish.com/2008/conditional-stylesheets-vs-css-hacks-answer-neither/ -->
<!--[if lt IE 7]> <html class="no-js ie6 oldie" lang="en"> <![endif]-->
<!--[if IE 7]> <html class="no-js ie7 oldie" lang="en"> <![endif]-->
<!--[if IE 8]> <html class="no-js ie8 oldie" lang="en"> <![endif]-->
<!-- Consider adding an manifest.appcache: h5bp.com/d/Offline -->
<!--[if gt IE 8]><!-->
<html class="no-js" lang="en"> <!--<![endif]-->
    <c:set var="pageTitle" value="About Us &lt; Enzyme Portal &gt; EMBL-EBI"/>
    <%@include file="head.jspf" %>

    <body class="level2 full-width"><!-- add any of your classes or IDs -->
        <%@include file="skipto.jspf" %>

        <div id="wrapper">

            <%@include file="header.jspf" %>

            <div id="content" role="main" class="clearfix">

                <!-- If you require a breadcrumb trail, its root should be your service.
                        You don't need a breadcrumb trail on the homepage of your service... -->
                <nav id="breadcrumb">
                    <p>
                        <a href=".">Enzyme Portal</a> &gt;
                        Service error
                    </p>
                </nav>

                <!-- Example layout containers -->

                <!-- Suggested layout containers -->



                <div class="row">

                    <section class="large-12 columns">

                        <h4 style="text-align: center">Enzyme Portal Service Error</h4>
                        <p class="alert">We're sorry but there was an error in your search. Please try again later or  <a rel="noopener noreferrer" target="_blank" href="https://www.ebi.ac.uk/support/index.php?query=Enzyme+portal&referrer=https://www.ebi.ac.uk/enzymeportal/"> report this issue here</a></p>

                    </section>

                </div>

                <%@include file="footer.jspf" %>

            </div>
            <!--! end of #wrapper -->
        </div>
    </body>

</html>


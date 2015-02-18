<%-- 
    Document   : browse_enzymes
    Created on : Jul 23, 2013, 12:17:33 PM
    Author     : joseph
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib  prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="Fn" uri="/WEB-INF/epTagLibray.tld" %>

<!DOCTYPE html>



<!-- paulirish.com/2008/conditional-stylesheets-vs-css-hacks-answer-neither/ -->
<!--[if lt IE 7]> <html class="no-js ie6 oldie" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js ie7 oldie" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js ie8 oldie" lang="en"> <![endif]-->
<!-- Consider adding an manifest.appcache: h5bp.com/d/Offline -->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->
    <head>
<c:set var="pageTitle" value="Browse Enzymes"/>
<%@include file="head.jspf" %>


    </head>

    <body class="level2"><!-- add any of your classes or IDs -->
        <div id="skip-to">
            <ul>
                <li><a href="#content">Skip to main content</a></li>
                <li><a href="#local-nav">Skip to local navigation</a></li>
                <li><a href="#global-nav">Skip to EBI global navigation menu</a></li>
                <li><a href="#global-nav-expanded">Skip to expanded EBI global navigation menu (includes all sub-sections)</a></li>
            </ul>
        </div>

        <div id="wrapper" class="container_24">
             <%@include file="header.jspf" %>
             
            <div id="content" role="main" class="grid_24 clearfix">

                 <div class="grid_24">
                    <h3 style="text-align: center; margin-right: 10em">Browse Enzymes By EC classification</h3><br/>
                    <div class="grid_6" style="margin-left: 30em">
                        
                <div style="text-align: center; min-width: 170px">
                        <div style="text-align: left; margin-left: auto; margin-right: auto; width: 170px">
                           <ul style="list-style-type: none; padding-left: 5px; margin-left: 0px">
                                    <li><a href="${pageContext.request.contextPath}/browse/enzyme/1/Oxidoreductases">EC 1</a>&nbsp;&nbsp;Oxidoreductases</li>
                                        <li><a href="${pageContext.request.contextPath}/browse/enzyme/2/Transferases">EC 2</a>&nbsp;&nbsp;Transferases</li>
                                        <li><a href="${pageContext.request.contextPath}/browse/enzyme/3/Hydrolases">EC 3</a>&nbsp;&nbsp;Hydrolases</li>
                                        <li><a href="${pageContext.request.contextPath}/browse/enzyme/4/Lyases">EC 4</a>&nbsp;&nbsp;Lyases</li>
                                        <li><a href="${pageContext.request.contextPath}/browse/enzyme/5/Isomerases">EC 5</a>&nbsp;&nbsp;Isomerases</li>
                                        <li><a href="${pageContext.request.contextPath}/browse/enzyme/6/Ligases">EC 6</a>&nbsp;&nbsp;Ligases</li>
                                </ul>
                        </div>
                </div>

                    </div>
                </div>  
         

            </div>




            <%@include file="footer.jspf" %>
        </div> <!--! end of #wrapper -->


       
    </body>

</html>

    <%-- 
    Document   : advanceSearch
    Created on : Nov 19, 2012, 10:55:37 AM
    Author     : joseph
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<!-- paulirish.com/2008/conditional-stylesheets-vs-css-hacks-answer-neither/ -->
<!--[if lt IE 7]> <html class="no-js ie6 oldie" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js ie7 oldie" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js ie8 oldie" lang="en"> <![endif]-->
<!-- Consider adding an manifest.appcache: h5bp.com/d/Offline -->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->

<c:set var="pageTitle" value="Enzyme Portal &lt;Advanced Search &lt; EMBL-EBI"/>
<%@include file="head.jspf" %>

 <script src="http://code.jquery.com/jquery-2.0.2.min.js"></script>

  <script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>

    <body class="level2">

        <%@include file="skipto.jspf" %>

        <div id="wrapper" class="container_24">
            <%@include file="header.jspf" %>
            
            <div id="content" role="main" class="grid_24 clearfix">

                <!-- Suggested layout containers --> 
          <nav id="breadcrumb">
     	<p>
		    <a href="${pageContext.request.contextPath}">Enzyme Portal</a> &gt; 
		    Sequence Search
			</p>
  	</nav>
    

            
            <section>
             <div class="contents">
            <div class="page container_12"> 
                 <%@include file="searchBox.jsp" %>
            </div>
                     </div>
            </section>
                <!-- End suggested layout containers -->

            </div>
               
            <%@include file="footer.jspf" %>
    
        </div> <!--! end of #wrapper -->

    </body>
</html>

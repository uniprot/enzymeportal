<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib  prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!-- paulirish.com/2008/conditional-stylesheets-vs-css-hacks-answer-neither/ -->
<!--[if lt IE 7]> <html class="no-js ie6 oldie" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js ie7 oldie" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js ie8 oldie" lang="en"> <![endif]-->
<!-- Consider adding an manifest.appcache: h5bp.com/d/Offline -->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->

<c:set var="pageTitle" value="BLAST search... &lt; Enzyme Portal &lt; EMBL-EBI"/>
<%@include file="head.jspf" %>

<body class="level2">

    <%@include file="skipto.jspf" %>

	<script type="text/javascript">
		function checkJob(){
			document.forms['check-job'].submit();
		}
		setTimeout(checkJob, 5000);
	</script>

    <div id="wrapper">

        <%@include file="header.jspf" %>

        <div id="content" role="main" class="clearfix">

            <nav id="breadcrumb">
             	<p>
        		    <a href="/enzymeportal">Enzyme Portal</a> &gt;
        		    Sequence search in progress ....
        		</p>
          	</nav>

        	<form:form id="check-job" modelAttribute="searchModel"
    	    	action="${pageContext.request.contextPath}/checkJob" method="POST">
    	    	<form:hidden path="searchparams.text" />
    	    	<form:hidden path="searchparams.sequence" />
    		    <form:hidden path="searchparams.previoustext" />
    		    <form:hidden path="searchparams.start" />
    		    <form:hidden path="searchparams.type" />
			    <input type="hidden" name="jobId" value="${jobId}"/>
        	</form:form>

            <section>
                <div style="margin: 4ex 4em;">
                     Job ID: ${jobId}
                    <img alt="Searching..." src="resources/images/loading16.gif"
                    	style="margin: 0ex 1em"/>
                    Searching protein sequence...
                </div>
            </section>

        </div>

        <%@include file="footer.jspf" %>

    </div>

</body>
</html>

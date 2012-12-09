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
        <link href="http://www.ebi.ac.uk/inc/css/contents.css"
        	rel="stylesheet" type="text/css" />
        <link href="resources/css/search.css"
        	rel="stylesheet" type="text/css" />
        <link href="resources/lib/spineconcept/css/common.css"
        	rel="stylesheet" type="text/css" media="screen" />
        <script type="text/javascript"
        	src="resources/javascript/search.js"></script>
    </head>
   <body>
    	<script type="text/javascript">
    		function checkJob(){
    			document.forms['check-job'].submit();
    		}
    		setTimeout(checkJob, 5000);
    	</script>
       
   <jsp:include page="frontierHeader.jsp"/>
  
                
            <div id="content" role="main" class="grid_24 clearfix">
    
    <!-- If you require a breadcrumb trail, its root should be your service.
     	   You don't need a breadcrumb trail on the homepage of your service... -->
    <nav id="breadcrumb">
     	<p>
		    <a href="/enzymeportal">Enzyme Portal</a> &gt; 
		    Sequence search in progress ....
			</p>
  	</nav>
    
    	<form:form id="check-job" modelAttribute="searchModel"
			    	action="checkJob" method="POST">
			    	<form:hidden path="searchparams.text" />
			    	<form:hidden path="searchparams.sequence" />
				    <form:hidden path="searchparams.previoustext" />
				    <form:hidden path="searchparams.start" />
				    <form:hidden path="searchparams.type" />
				    <input type="hidden" name="jobId" value="${jobId}"/>
	</form:form>
    	
                                    <section>
                                                  <div style="margin: 4ex 4em;">
                <!-- Job ID: ${jobId} -->
                <img alt="Searching..." src="resources/images/loading.gif"
                	style="margin: 0ex 1em"/>
                Searching protein sequence...
                </div>
                                        
                                    </section>
    
	   
  			
    </div>      
                
         <jsp:include page="footer.jsp"/>        
           
         
                
                
                
		
    </body>
</html>
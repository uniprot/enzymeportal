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
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Enzyme Entry</title>
    </head>
    <body>
        <div class="content">
<!--            <div class="iconGroup">
                <h2 style="color: red;text-align:center" >Service Error</h2>
                <p style="text-align: center">There has been an error with your request. Please try again later.</p>
            </div>-->

                <!-- Suggested layout containers -->
                <section>

                    <h4 style="text-align: center">Enzyme Portal Service Error</h4>
                            <p class="alert">We're sorry but there was an error in your search for ${enzymeModel.name} . Please try again later or use the <a href="${pageContext.request.contextPath}/advanceSearch">advanced search</a></p>

                </section>

                <!-- End suggested layout containers -->
            <div class="clear"></div>
        </div>

<!--    </div>-->
</body>
</html>

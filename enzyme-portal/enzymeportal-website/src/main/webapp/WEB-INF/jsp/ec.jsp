<%-- 
    Document   : ecnumber
    Created on : Nov 18, 2013, 11:56:35 AM
    Author     : joseph
--%>



<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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



        <!-- History.js -->
        <script src="//browserstate.github.io/history.js/scripts/bundled/html4+html5/jquery.history.js"></script>




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
                    <div class="clear"></div>

                    <div class="grid_24">


                        <c:if test="${not empty selectedEc }">

                            <c:forEach var="selected" items="${selectedEc}"> 
                                <c:if test="${not empty selected.name }">
                                    <c:choose>
                                        <c:when test="${fn:length(selectedEc) gt 1}">


                                            <xchars:translate>
                                                <h2><a  href="${pageContext.request.contextPath}/browse/enzyme?ec=${selected.ec}&amp;ecname=${selected.name}">EC ${selected.ec}</a> - ${selected.name}</h2> 
                                            </xchars:translate>

                                        </c:when>
                                        <c:otherwise>
                                            <h2 class="active">EC ${selected.ec} - ${selected.name}</h2>    
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                                <c:if test="${not empty selected.subclassName }">
                                    <c:choose>
                                        <c:when test="${fn:length(selectedEc) gt 2}">  

                                            <xchars:translate>
                                                <h2 style="margin-left: 1em"> <a  href="${pageContext.request.contextPath}/browse/enzyme?ec=${selected.ec}&amp;subecname=${selected.subclassName}">EC ${selected.ec}</a> - ${selected.subclassName} </h2> 
                                            </xchars:translate>

                                        </c:when>
                                        <c:otherwise>
                                            <h2 style="margin-left: 1em">EC ${selected.ec} - ${selected.subclassName}</h2>        
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                                <c:if test="${not empty selected.entries }">
                                    <xchars:translate>
                                        <h3 style="margin-left: 2.5em">EC ${selected.ec} - ${selected.subsubclassName}</h3>
                                        <hr/>
                                    </xchars:translate> 
                                </c:if>





                            </c:forEach>
                        </c:if>





                        <c:if test="${ empty json.description}">
                            <h3>Description</h3>
                            <p> <i>No description available.</i></p>

                        </c:if>
                        <c:if test="${not empty json.description}">
                            <h3>Description</h3>
                            <p>${json.description}</p>

                        </c:if>
                        <h3>Content</h3>
                        <c:choose>
                            <c:when test="${not empty json.children}">
                                <div class="resultText" style="display: table-row">
                                    <c:forEach var="data" items="${json.children}"> 

                                        <div class="result"  style="display: table-row;vertical-align: top;padding-left: 1em;padding-top: 1ex; min-height: 10px">
                                            <a  href="${pageContext.request.contextPath}/browse/enzyme?ec=${data.ec}&amp;subecname=${data.name}">EC ${data.ec}</a>  - ${data.name}

                                        </div>


                                    </c:forEach>

                                </div>                            
                            </c:when>
                            <c:when test="${not empty json.subSubclasses}">
                                <div class="resultText" style="display: table-row">
                                    <c:forEach var="subsub" items="${json.subSubclasses}"> 

                                        <div class="result"  style="display: table-row;vertical-align: top;padding-left: 1em;padding-top: 1ex; min-height: 10px">

                                            <xchars:translate>
                                                <a  href="${pageContext.request.contextPath}/browse/enzyme?ec=${subsub.ec}&amp;subsubecname=${subsub.name}">EC ${subsub.ec}</a>  - ${subsub.name} 
                                            </xchars:translate>
                                        </div>


                                    </c:forEach>

                                </div>                            
                            </c:when>
                            <c:when test="${not empty json.entries}">
                                <div class="resultText" style="display: table-row">
                                    <c:forEach var="entry" items="${json.entries}"> 

                                        <div class="result"  style="display: table-row;vertical-align: top;padding-left: 1em;padding-top: 1ex; min-height: 10px">
                                            <%--
                                                 <a href="${pageContext.request.contextPath}/enzymes?searchKey=${entry.ec}&searchparams.type=KEYWORD&searchparams.previoustext=${entry.ec}&searchparams.start=0&searchparams.text=${entry.ec}&keywordType=EC&searchId=${entry.ec}">${entry.ec}</a>  - ${entry.name}
                                                 
                                            --%>  
                                            <a href="${pageContext.request.contextPath}/ec/${entry.ec}">${entry.ec}</a>  - ${entry.name}    
                                        </div>


                                    </c:forEach>

                                </div>                            
                            </c:when>
                            <c:otherwise>
                                <div class="resultText" style="display: table-row">

                                    <p> <i>No Active Enzyme Classification Numbers found</i></p>
                                </div>                    
                            </c:otherwise>
                        </c:choose>




                    </div>


                </div>

            </div>

            <script type='text/javascript'>//<![CDATA[ 
                $(function () {
                    var History = window.History;
                    if (History.enabled) {
                        State = History.getState();
                        // set initial state to first page that was loaded
                        History.pushState({urlPath: window.location.pathname}, $("title").text(), State.urlPath);
                    } else {
                        return false;
                    }

                    var loadAjaxContent = function (target, urlBase, selector) {
                        $(target).load(urlBase + ' ' + selector);
                    };


                    var updateContent = function (State) {
                        var url = State.url;
                        var $target = $(State.data.target);

                        $.ajax({
                            url: url,
                            data: null,
                            type: "GET",
                            //beforeSend: function (xhr) { xhr.setRequestHeader('X-Target', frame); },
                            success: function (data) {
                                $target.html(data);
                            }
                        });
                    };



                    // Content update and back/forward button handler
                    History.Adapter.bind(window, 'statechange', function () {
                        updateContent(History.getState());



                    });


                });//]]>  

            </script>                   

            <%@include file="footer.jspf" %>
        </div> <!--! end of #wrapper -->



    </body>

</html>


<%-- 
    Document   : ecnumber
    Created on : Nov 18, 2013, 11:56:35 AM
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
                                      
                                            <form:form commandName="searchModel" action="${pageContext.request.contextPath}/browse/ec/${selected.ec}" method="post">
                                                 <xchars:translate>
                                               <h2><a onclick="submit()" href="#">EC ${selected.ec}</a> - ${selected.name}</h2> 
                                                 </xchars:translate>
                                               <input type="hidden" name="ec" value="${selected.ec}"/>
                                               <input type="hidden" name="ecname" value="${selected.name}"/>
                                           </form:form>
                                      
                                           </c:when>
                                       <c:otherwise>
                                           <h2 class="active">EC ${selected.ec} - ${selected.name}</h2>    
                                       </c:otherwise>
                                       </c:choose>
                                         </c:if>
                                        <c:if test="${not empty selected.subclassName }">
                                        <c:choose>
                                           <c:when test="${fn:length(selectedEc) gt 2}">  
                                       
                                          
                                           
                                                <form:form commandName="searchModel" action="${pageContext.request.contextPath}/browse/ec/${selected.ec}" method="post">
                                                      <xchars:translate>
                                                     <h2 style="margin-left: 1em"> <a onclick="submit()" href="#">EC ${selected.ec}</a> - ${selected.subclassName} </h2> 
                                                      </xchars:translate>
                                             <input type="hidden" name="ec" value="${selected.ec}"/>
                                               <input type="hidden" name="subecname" value="${selected.subclassName}"/>
                                                </form:form>
                                          
                                      
                                       
                                           </c:when>
                                        <c:otherwise>
                                      <h2 style="margin-left: 1em">EC ${selected.ec} - ${selected.subclassName}</h2>        
                                        </c:otherwise>
                                        </c:choose>
                                      </c:if>
                                       <c:if test="${not empty selected.entries }">
                                           <xchars:translate>
<!--                                           <h2 style="margin-left: 1.5em"><a href="${pageContext.request.contextPath}/ecnumber?ec=${selected.ec}&amp;subsubecname=${selected.subsubclassName}">EC ${selected.ec}</a> - ${selected.subsubclassName}</h2>-->
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
                                             <form:form commandName="searchModel" action="${pageContext.request.contextPath}/browse/ec/${data.ec}" method="post">
                                                 <a onclick="submit()" href="#">EC ${data.ec}</a>  - ${data.name}
                                               <input type="hidden" name="ec" value="${data.ec}"/>
                                               <input type="hidden" name="subecname" value="${data.name}"/>
                                                    <form:hidden path="searchparams.type" value="KEYWORD"/>
                                                    <form:hidden path="searchparams.text" value="${data.name}"/>
                                                    <form:hidden path="searchparams.previoustext" />
                                             </form:form>
                                        </div>


                                    </c:forEach>

                                </div>                            
                            </c:when>
                             <c:when test="${not empty json.subSubclasses}">
                                <div class="resultText" style="display: table-row">
                                    <c:forEach var="subsub" items="${json.subSubclasses}"> 
                                        
                                        <div class="result"  style="display: table-row;vertical-align: top;padding-left: 1em;padding-top: 1ex; min-height: 10px">
                                            <form:form  commandName="searchModel" action="${pageContext.request.contextPath}/browse/ec/${subsub.ec}" method="post">
                                                <xchars:translate>
                                                <a onclick="submit()" href="#">EC ${subsub.ec}</a>  - ${subsub.name} 
                                                </xchars:translate>
                                                <input type="hidden" name="ec" value="${subsub.ec}"/>
                                               <input type="hidden" name="subsubecname" value="${subsub.name}"/>
                                                   <form:hidden path="searchparams.type" value="KEYWORD"/>
                                                    <form:hidden path="searchparams.text" value="${subsub.name}"/>
                                                    <form:hidden path="searchparams.previoustext" />
                                            </form:form>
                                        </div>


                                    </c:forEach>

                                </div>                            
                            </c:when>
                               <c:when test="${not empty json.entries}">
                                <div class="resultText" style="display: table-row">
                                    <c:forEach var="entry" items="${json.entries}"> 
                                       
                                        <div class="result"  style="display: table-row;vertical-align: top;padding-left: 1em;padding-top: 1ex; min-height: 10px">
                                             <form:form commandName="searchModel" action="${pageContext.request.contextPath}/search/ec/${entry.ec}" method="post">
                                                 <a onclick="submit()" href="#"> ${entry.ec}</a>  - ${entry.name} 
                                              <input type="hidden" name="ec" value="${entry.ec}"/>
                                              <input type="hidden" name="entryecname" value="${entry.name}"/>
                                                 <form:hidden path="searchparams.type" value="KEYWORD"/>
                                                    <form:hidden path="searchparams.text" value="${entry.name}"/>
                                                    <form:hidden path="searchparams.previoustext" />
                                             </form:form>
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
  $(function(){
  var History = window.History;
  if (History.enabled) {
      State = History.getState();
      // set initial state to first page that was loaded
      History.pushState({urlPath: window.location.pathname}, $("title").text(), State.urlPath);
  } else {
      return false;
  }

  var loadAjaxContent = function(target, urlBase, selector) {
      $(target).load(urlBase + ' ' + selector);
  };

//  var updateContent = function(State) {
//      alert(State);
//      var selector = '#' + State.data.urlPath.substring(1);
//    if ($(selector).length) { //content is already in #hidden_content
//        $('#content').children().appendTo('#hidden_content');
//        $(selector).appendTo('#content');
//    } else { 
//        $('#content').children().clone().appendTo('#hidden_content');
//        loadAjaxContent('#content', State.url, selector);
//    }
//  };

 var updateContent = function(State) {
        var url = State.url;
        var $target = $(State.data.target);
        //ajaxPost(url, $target);
        console.log(State);
        
                 $.ajax({
        url: url,
        data : null,
        type: "GET",
        //beforeSend: function (xhr) { xhr.setRequestHeader('X-Target', frame); },
        success: function (data) {
            $target.html(data);
        }
    });
  };
  


  // Content update and back/forward button handler
  History.Adapter.bind(window, 'statechange', function() {
      updateContent(History.getState());
      // var State = History.getState();
//console.log(State); 
//       var url = State.data.url;
//        var $target = $(State.data.target);
//        //console.log($target);
//        alert(State.data.target);
//        var url = State.url;


   
//         $.ajax({
//        url: url,
//        data : null,
//        type: "POST",
//        //beforeSend: function (xhr) { xhr.setRequestHeader('X-Target', frame); },
//        success: function (data) {
//            $target.html(data);
//        }
//    });
        
    
  });

  // navigation link handler
//  $('body').on('click', 'a', function(e) {
//      var urlPath = $(this).attr('href');
//      var title = $(this).text();
//      History.pushState({urlPath: urlPath}, title, urlPath);
//      return false; // prevents default click action of <a ...>
//  });
  });//]]>  

  </script>                   
                        
            <footer>
                <!-- Optional local footer (insert citation / project-specific copyright / etc here -->
                <!--
    <div id="local-footer" class="grid_24 clearfix">
                        <p>How to reference this page: ...</p>
                </div>
                -->
                <!-- End optional local footer -->

                <div id="global-footer" class="grid_24">

                    <nav id="global-nav-expanded">

                        <div class="grid_4 alpha">
                            <h3 class="embl-ebi"><a href="/" title="EMBL-EBI">EMBL-EBI</a></h3>
                        </div>

                        <div class="grid_4">
                            <h3 class="services"><a href="/services">Services</a></h3>
                        </div>

                        <div class="grid_4">
                            <h3 class="research"><a href="/research">Research</a></h3>
                        </div>

                        <div class="grid_4">
                            <h3 class="training"><a href="/training">Training</a></h3>
                        </div>

                        <div class="grid_4">
                            <h3 class="industry"><a href="/industry">Industry</a></h3>
                        </div>

                        <div class="grid_4 omega">
                            <h3 class="about"><a href="/about">About us</a></h3>
                        </div>

                    </nav>

                    <section id="ebi-footer-meta">
                        <p class="address">EMBL-EBI, Wellcome Trust Genome Campus, Hinxton, Cambridgeshire, CB10 1SD, UK &nbsp; &nbsp; +44 (0)1223 49 44 44</p>
                        <p class="legal">Copyright &copy; EMBL-EBI 2012 | EBI is an Outstation of the <a href="http://www.embl.org">European Molecular Biology Laboratory</a> | <a href="/about/privacy">Privacy</a> | <a href="/about/cookies">Cookies</a> | <a href="/about/terms-of-use">Terms of use</a></p>	
                    </section>

                </div>

            </footer>
        </div> <!--! end of #wrapper -->


        <!-- JavaScript at the bottom for fast page loading -->

        <c:if test="${pageContext.request.serverName!='www.ebi.ac.uk'}" >
            <script type="text/javascript">var redline = {}; redline.project_id = 185653108;</script><script id="redline_js" src="http://www.redline.cc/assets/button.js" type="text/javascript">
                
            </script>
            <script>
                $(document).ready(function() {
                    setTimeout(function(){
                        // Handler for .ready() called.
                        $("#redline_side_car").css("background-image","url(resources/images/redline_left_button.png)");
                        $("#redline_side_car").css("background-size", "23px auto");
                        $("#redline_side_car").css("display", "block");
                        $("#redline_side_car").css("width", "23px");
                        $("#redline_side_car").css("height", "63px");
                    },1000);
                });
            </script>
        </c:if>

        <!--        add twitter script for twitterapi-->
        <script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0];if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src="https://platform.twitter.com/widgets.js";fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");</script>



        <!--    now the frontier js for ebi global result-->
        <script src="//www.ebi.ac.uk/web_guidelines/js/ebi-global-search-run.js"></script>
        <script src="//www.ebi.ac.uk/web_guidelines/js/ebi-global-search.js"></script>



        <script defer="defer" src="//www.ebi.ac.uk/web_guidelines/js/cookiebanner.js"></script>  
        <script defer="defer" src="//www.ebi.ac.uk/web_guidelines/js/foot.js"></script>

        <!-- end scripts-->


        <!-- Change UA-XXXXX-X to be your site's ID -->
        <!--
      <script>
          window._gaq = [['_setAccount','UAXXXXXXXX1'],['_trackPageview'],['_trackPageLoadTime']];
          Modernizr.load({
            load: ('https:' == location.protocol ? '//ssl' : '//www') + '.google-analytics.com/ga.js'
          });
        </script>
        -->


        <!-- Prompt IE 6 users to install Chrome Frame. Remove this if you want to support IE 6.
             chromium.org/developers/how-tos/chrome-frame-getting-started -->
        <!--[if lt IE 7 ]>
            <script src="//ajax.googleapis.com/ajax/libs/chrome-frame/1.0.3/CFInstall.min.js"></script>
            <script>window.attachEvent('onload',function(){CFInstall.check({mode:'overlay'})})</script>
          <![endif]-->

    </body>

</html>



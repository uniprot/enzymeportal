<%-- 
    Document   : entry
    Created on : Sep 18, 2012, 10:52:21 AM
    Author     : joseph
--%>



<!doctype html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="epfn" uri="/WEB-INF/epTagLibray.tld" %>
<!-- paulirish.com/2008/conditional-stylesheets-vs-css-hacks-answer-neither/ -->
<!--[if lt IE 7]> <html class="no-js ie6 oldie" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js ie7 oldie" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js ie8 oldie" lang="en"> <![endif]-->
<!-- Consider adding an manifest.appcache: h5bp.com/d/Offline -->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->

<c:set var="pageTitle" value="Entry page"/>
<%@include file="head.jspf" %>

    <body class="level2">

        <%@include file="skipto.jspf" %>

        <div id="wrapper" class="container_24">

            <%@include file="header.jspf" %>
            
            <div id="content" role="main" class="grid_24 clearfix">
 <%@ include file="breadcrumbs.jsp" %>
                <!-- Suggested layout containers -->  

                
                            <form:form id="entryForm" modelAttribute="enzymeModel" action="entry" method="GET">
                                <!--<c:set var="chebiImageBaseUrl" value="http://www.ebi.ac.uk/chebi/displayImage.do?defaultImage=true&chebiId="/>-->
                                <c:set var="chebiImageBaseUrl"
                                       value="http://www.ebi.ac.uk/chebi/displayImage.do?defaultImage=true&imageIndex=0&chebiId="/>
                                <c:set var="chebiImageParams"
                                       value="&dimensions=200&scaleMolecule=true"/>
                                <c:set var="chebiEntryBaseUrl"
                                       value="http://www.ebi.ac.uk/chebi/searchId.do?chebiId="/>
                                <c:set var="chebiEntryBaseUrlParam" value=""/>
                                <c:set var="rheaEntryBaseUrl"
                                       value="http://www.ebi.ac.uk/rhea/reaction.xhtml?id="/>
                                <c:set var="intenzEntryBaseUrl"
                                       value="http://www.ebi.ac.uk/intenz/query?cmd=SearchEC&ec="/>
                                <c:set var="chemblImageBaseUrl"
                                       value="https://www.ebi.ac.uk/chembldb/compound/displayimage/"/>
                                <c:set var="reactomeBaseUrl"
                                       value="http://www.reactome.org/cgi-bin/link?SOURCE=Reactome&ID="/>
                                <c:set var="reactomeImageBaseUrl"
                                       value="http://www.reactome.org/"/>

                                <c:set var="enzyme" value="${enzymeModel.enzyme}"/>
                                <!--requestedfield is an enum type in the controller. Its value has to be one of the values in the Field variable in the controller-->
                                <c:set var="requestedfield" value="${enzymeModel.requestedfield}"/>
                                <c:if test='${requestedfield=="enzyme"}'>
                                    <c:set var="enzymeSelected" value="selected"/>
                                </c:if>
                                <c:if test='${requestedfield=="proteinStructure"}'>
                                    <c:set var="proteinStructureSelected" value="selected"/>
                                </c:if>
                                <c:if test='${requestedfield=="reactionsPathways"}'>
                                    <c:set var="reactionsPathwaysSelected" value="selected"/>
                                </c:if>
                                <c:if test='${requestedfield=="molecules"}'>
                                    <c:set var="moleculesSelected" value="selected"/>
                                </c:if>
                                <c:if test='${requestedfield=="diseaseDrugs"}'>
                                    <c:set var="diseaseDrugsSelected" value="selected"/>
                                </c:if>
                                <c:if test='${requestedfield=="literature"}'>
                                    <c:set var="literatureSelected" value="selected"/>
                                </c:if>
                                <c:set var="relSpecies" value="${enzymeModel.relatedspecies}"/>
                                <section>
<div style="float: right;">
    <input type="checkbox" class="forBasket" onchange="selectForBasket(event)" 
        title="Add this enzyme to your basket."
        value="${epfn:getSummaryBasketId(enzymeModel)}"
        ${not empty basket and
        not empty basket[epfn:getSummaryBasketId(enzymeModel)]? 'checked': ''}/> Add
            to basket
</div>
                                <div class="header">
                                        <div class="grid_8 prefix_8 suffix_6 alpha">
                                            <div class="panel">
                                                <div wicket:id="classification">
                                                    <div class="classification">
                                                        <div class="label">ORGANISMS</div>
                                                        <div class='box selected ${fn:replace(relSpecies[0].species.scientificname, " ", "_")}'>
                                                            <span class="name"><c:out value="${relSpecies[0].species.commonname}"/></span>
                                                            <span class="extra"
                                                                  title="${relSpecies[0].species.scientificname}"
                                                                  style="overflow: hidden;">${relSpecies[0].species.scientificname}</span>
                                                        </div>
                                                    </div>
                                                    <div class="selection">
                                                        <ul>                    
                                                            <c:if test="${fn:length(relSpecies)<=0}">
                                                                <c:forEach begin="0" end="${fn:length(relSpecies)}" var="i">
                                                                    <c:set var="species" value="${relSpecies[i].species}"/>
                                                                    <a href="../${relSpecies[i].uniprotaccessions[0]}/${requestedfield}">
                                                                        <c:set var="select" value=""/>
                                                                        <c:if test="${i==0}">
                                                                            <c:set var="select" value="selected"/>
                                                                        </c:if>
                                                                        <li class="${select}">
                                                                            <div class='box ${fn:replace(species.scientificname, " ", "_")}'>
                                                                                <span class="name"><c:out value="${species.commonname}"/></span>
                                                                                <span class="extra"
                                                                                      title="${species.scientificname}"
                                                                                      style="overflow: hidden;">${species.scientificname}</span>
                                                                            </div>
                                                                        </li>
                                                                    </a>
                                                                </c:forEach>
                                                            </c:if>
                                                            <c:if test="${fn:length(relSpecies)>0}">
                                                                <c:forEach begin="0" end="${fn:length(relSpecies)-1}" var="i">
                                                                    <c:set var="species" value="${relSpecies[i].species}"/>
                                                                    <a href="../${relSpecies[i].uniprotaccessions[0]}/${requestedfield}">
                                                                        <c:set var="select" value=""/>
                                                                        <c:if test="${i==0}">
                                                                            <c:set var="select" value="selected"/>
                                                                        </c:if>
                                                                        <li class="${select}">
                                                                            <div class='box ${fn:replace(species.scientificname, " ", "_")}'>
                                                                                <span class="name"><c:out value="${species.commonname}"/></span>
                                                                                <span class="extra"
                                                                                      title="${species.scientificname}"
                                                                                      style="overflow: hidden;">${species.scientificname}</span>
                                                                            </div>
                                                                        </li>
                                                                    </a>
                                                                </c:forEach>
                                                            </c:if>
                                                        </ul>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <%--
                                        <div class="grid_1 omega">
                                            <div class="menu">
                                                <a href="http://www.ebi.ac.uk/inc/help/search_help.html" class="help">Help</a>
                                                <a href="" wicket:id="print" class="print"><span wicket:id="printLabel">Print</span></a>
                                            </div>
                                        </div>
                                        --%>
<!--                                </div>-->
                            </div>
                                </section>
            </div>

                                <div class="grid_24 gradient">
                                    <div wicket:id="reference" class="content">
                                        <div class="column1">
                                            <ul>                                    
                                                <li id="enzyme" class="tab protein ${enzymeSelected}">
                                                    <a href="enzyme">
                                                        <span class="inner_tab">
                                                            <span class="icon"></span>
                                                            <span class="label icon icon-conceptual" data-icon="P">
                                                               <spring:message code="label.entry.enzyme.title"/>
                                                            </span>
                                                        </span>
                                                    </a>
                                                </li>
                                                <li id="structure" class="tab structure ${proteinStructureSelected}">
                                                    <a href="proteinStructure">
                                                        <span class="inner_tab">
                                                            <span class="icon"></span>
                                                            <span class="label icon icon-conceptual icon-c4" data-icon="s">
                                                                <spring:message code="label.entry.proteinStructure.title"/>
                                                            </span>
                                                        </span>
                                                    </a>
                                                </li>
                                                <li id="reaction" class="tab reaction ${reactionsPathwaysSelected}">
                                                    <a href="reactionsPathways">
                                                        <span class="inner_tab">
                                                            <span class="icon"></span>
                                                            
                                                            <span class="label icon icon-conceptual" data-icon="y">
                                                                <spring:message code="label.entry.reactionsPathways.title"/>
                                                                
                                                            </span>
                                                        </span>
                                                    </a>
                                                </li>
                                                <li id="molecule" class="tab molecule ${moleculesSelected}">
                                                    <a href="molecules">
                                                        <span class="inner_tab">
                                                            <span class="icon"></span>
                                                             <span class="label icon icon-conceptual" data-icon="b">
                                                                <spring:message code="label.entry.molecules.title"/>
                                                            </span>
                                                        </span>
                                                    </a>
                                                </li>
                                                <li id="disease" class="tab disease ${diseaseDrugsSelected}">
                                                    <a href="diseaseDrugs">
                                                        <span class="inner_tab">
                                                            <span class="icon"></span>
                                                            <span class="label icon icon-species" data-icon="v">
                                                                <spring:message code="label.entry.disease.title"/>
                                                            </span>
                                                        </span>
                                                    </a>
                                                </li>
                                                <li id="literature" class="tab literature ${literatureSelected}">
                                                    <a href="literature">
                                                        <span class="inner_tab">
                                                            <span class="icon"></span>
                                                            <span class="label icon icon-conceptual" data-icon="l">
                                                                <spring:message code="label.entry.literature.title"/>
                                                            </span>
                                                        </span>
                                                    </a>
                                                </li>
                                            </ul>
                                        </div>
                                        <div class="column2">

                                            <c:if test='${requestedfield=="enzyme"}'>
                                                <c:set var="_enzyme" value="${enzymeModel.enzyme}"/>

                                                <c:if test='${_enzyme.enzymetype[0] == "error"}'>
                                                    <div class="node grid_24">
                                                        <div class="view grid_24">
                                                            <%@include file="errors.jsp" %>
                                                        </div>
                                                    </div>
                                                </c:if>
                                                <c:if test='${_enzyme.enzymetype[0] != "error"}'>
                                                    <div class="node grid_24">
                                                        <div class="view grid_24">
                                                            <%@include file="enzyme.jsp" %>
                                                        </div>
                                                    </div>
                                                </c:if>
                                            </c:if>

                                            <!--START PROTEIN STRUCTURE TAB-->
                                            <c:if test='${requestedfield=="proteinStructure"}'>
                                                <c:set var="structure" value="${enzymeModel.proteinstructure}"/>   
                                                <c:if test='${structure[0].name == "error"}'>
                                                    <div class="node grid_24">
                                                        <div class="view grid_24">
                                                            <%@include file="errors.jsp" %>
                                                        </div>
                                                    </div>
                                                </c:if>
                                                <c:if test='${structure[0].name != "error"}'>
                                                    <div class="node grid_24 clearfix">
                                                        <div class="view grid_24 clearfix">
                                                            <%@include file="proteinStructure.jsp" %>
                                                        </div>
                                                    </div>
                                                </c:if>
                                            </c:if>

                                            <!--START REACTIONS & PATHWAYS TAB-->
                                            <c:if test='${requestedfield=="reactionsPathways"}'>

                                                <c:set var="pathway" value="${enzymeModel.reactionpathway}"/>   
                                                <c:if test='${pathway[0].reaction.name == "error"}'>
                                                    <div class="node grid_24">
                                                        <div class="view grid_24">
                                                            <%@include file="errors.jsp" %>
                                                        </div>
                                                    </div>
                                                </c:if>
                                                <c:if test='${pathway[0].reaction.name != "error"}'>
                                                    <div class="node grid_24">
                                                        <div class="view grid_24">
                                                            <%@include file="reactionsPathways.jsp" %>
                                                        </div>
                                                    </div>
                                                </c:if>
                                            </c:if>

                                            <!--START SMALL MOLECULES TAB-->
                                            <c:if test='${requestedfield=="molecules"}'>
                                                <c:set var="chemEntity" value="${enzymeModel.molecule}"/>
                                                <c:choose>
                                                    <c:when test="${not empty chemEntity.drugs
                                                        and not empty chemEntity.drugs.molecule
                                                        and chemEntity.drugs.molecule[0] eq 'error'}">
                                                        <div class="node grid_24">
                                                            <div class="view grid_24">
                                                                <%@include file="errors.jsp" %>
                                                            </div>
                                                        </div>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="node grid_24">
                                                            <div class="view grid_24">
                                                                <%@include file="molecules.jsp" %>
                                                            </div>
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:if>
                                            <!--START DISEASE & DRUGS TAB-->
                                            <c:if test='${requestedfield=="diseaseDrugs"}'>
                                                <c:set var="diseases" value="${enzymeModel.disease}"/>
                                                <c:if test='${diseases[0].name == "error"}'>
                                                    <div class="node grid_24">
                                                        <div class="view grid_24">
                                                            <%@include file="errors.jsp" %>
                                                        </div>
                                                    </div>
                                                </c:if>
                                                <c:if test='${diseases[0].name != "error"}'>
                                                    <div class="node grid_24">
                                                        <div class="view grid_24">
                                                            <%@include file="disease.jsp" %>
                                                        </div>
                                                    </div>
                                                </c:if>
                                            </c:if>

                                            <!--START literature TAB-->
                                            <c:if test='${requestedfield=="literature"}'>

                                                <c:set var="lit" value="${enzymeModel.literature}"/>   
                                                <c:if test='${lit[0] == "error"}'>
                                                    <div class="node grid_24">
                                                        <div class="view grid_24">
                                                            <%@include file="errors.jsp" %>
                                                        </div>
                                                    </div>
                                                </c:if>
                                                <c:if test='${lit[0] != "error"}'>
                                                    <div class="node grid_24">
                                                        <div class="view grid_24">
                                                            <%@include file="literature.jsp" %>
                                                        </div>
                                                    </div>
                                                </c:if>
                                            </c:if>
                                        </div>
                                </div>
                            </form:form>
                            <div class="clear"></div>
                        </div>



    
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
        <script src="../../resources/lib/spineconcept/javascript/jquery-1.5.1.min.js" type="text/javascript"></script>
        <script src="../../resources/javascript/search.js" type="text/javascript"></script>

        <c:if test="${pageContext.request.serverName!='www.ebi.ac.uk'}" >
    <script type="text/javascript">var redline = {}; redline.project_id = 185653108;</script><script id="redline_js" src="http://www.redline.cc/assets/button.js" type="text/javascript"></script>
              <script>
            $(document).ready(function() {
                setTimeout(function(){
                    // Handler for .ready() called.
                    $("#redline_side_car").css("background-image","url(/enzymeportal/resources/images/redline_left_button.png)");
                    $("#redline_side_car").css("display", "block");
                },1000);
            });
        </script>
        </c:if>

<!--        add twitter script for twitterapi-->
<script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0];if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src="https://platform.twitter.com/widgets.js";fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");</script>

        <!-- Grab Google CDN's jQuery, with a protocol relative URL; fall back to local if offline -->
        <!--
      <script src="//ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js"></script>
        <script>window.jQuery || document.write('<script src="../js/libs/jquery-1.6.2.min.js"><\/script>')</script>
        -->


        <!-- scripts concatenated and minified via ant build script-->
        <!--
      <script defer src="../js/plugins.js"></script>
        <script defer src="../js/script.js"></script>
        -->
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

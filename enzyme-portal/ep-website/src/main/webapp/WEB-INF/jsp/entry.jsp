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
                                <c:if test='${requestedfield=="proteinstructure"}'>
                                    <c:set var="proteinStructureSelected" value="selected"/>
                                </c:if>
                                <c:if test='${requestedfield=="reactionspathways"}'>
                                    <c:set var="reactionsPathwaysSelected" value="selected"/>
                                </c:if>
                                <c:if test='${requestedfield=="molecules"}'>
                                    <c:set var="moleculesSelected" value="selected"/>
                                </c:if>
                                <c:if test='${requestedfield=="diseasedrugs"}'>
                                    <c:set var="diseaseDrugsSelected" value="selected"/>
                                </c:if>
                                <c:if test='${requestedfield=="literature"}'>
                                    <c:set var="literatureSelected" value="selected"/>
                                </c:if>
                                <c:set var="selectedSpecies" value="${enzymeModel.species}" />
                                <c:set var="relSpecies" value="${enzymeModel.relatedspecies}"/>

                                <section>
                                    <div class="header">
                                        <h2><c:out value="${enzymeModel.name}"/></h2>
                                        <div class="push_4 grid_20 entry-buttons">
                                            <c:if test="${empty basket ||empty basket[epfn:getSummaryBasketId(enzymeModel)]}">
                                                <input type="hidden" id="enzymeId" value="${epfn:getSummaryBasketId(enzymeModel)}"/>
                                                <a id="add-to-basket" href="#" class="icon icon-generic btn" data-icon="b">Add to Basket</a>
                                                <script>
                                                    $('#add-to-basket').click(function(){
                                                        ajaxBasket($('#enzymeId').val(), true);
                                                        $(this).hide();
                                                    });
                                                </script>
                                            </c:if>
                                            <strong>&nbsp;&nbsp;Organism:</strong>
                                            <div class="panel">
                                                <div class="classification">
                                                    <div class='box selected ${fn:replace(selectedSpecies.scientificname, " ", "_")}'>
                                                        <span class="name"><c:out value="${selectedSpecies.commonname}"/></span>
                                                                            <span class="extra"
                                                                                  title="${selectedSpecies.scientificname}"
                                                                                  style="overflow: hidden;">${selectedSpecies.scientificname}</span>
                                                    </div>
                                                </div>
                                                <div class="selection">
                                                    <ul>
                                                        <c:if test="${fn:length(relSpecies)<=0}">
                                                            <c:forEach begin="0" end="${fn:length(relSpecies)}" var="i">
                                                                <c:set var="species" value="${relSpecies[i].species}"/>
                                                                <c:set var="select" value=""/>
                                                                <c:if test="${i==0}">
                                                                    <c:set var="select" value="selected"/>
                                                                </c:if>
                                                                <li class="${select}">
                                                                    <a href="../${relSpecies[i].uniprotaccessions[0]}/${requestedfield}">
                                                                        <div class='box ${fn:replace(species.scientificname, " ", "_")}'>
                                                                            <span class="name"><c:out value="${species.commonname}"/></span>
                                                                                                <span class="extra"
                                                                                                      title="${species.scientificname}"
                                                                                                      style="overflow: hidden;">${species.scientificname}</span>
                                                                        </div>
                                                                    </a>
                                                                </li>
                                                            </c:forEach>
                                                        </c:if>
                                                        <c:if test="${fn:length(relSpecies)>0}">
                                                            <c:forEach begin="0" end="${fn:length(relSpecies)-1}" var="i">
                                                                <c:set var="species" value="${relSpecies[i].species}"/>
                                                                <c:set var="select" value=""/>
                                                                <c:if test="${i==0}">
                                                                    <c:set var="select" value="selected"/>
                                                                </c:if>
                                                                <li class="${select}">
                                                                    <a href="../${relSpecies[i].uniprotaccessions[0]}/${requestedfield}">
                                                                        <div class='box ${fn:replace(species.scientificname, " ", "_")}'>
                                                                            <span class="name"><c:out value="${species.commonname}"/></span>
                                                                                                <span class="extra"
                                                                                                      title="${species.scientificname}"
                                                                                                      style="overflow: hidden;">${species.scientificname}</span>
                                                                        </div>
                                                                    </a>
                                                                </li>
                                                            </c:forEach>
                                                        </c:if>
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </section>
                                <div class="clearfix"></div>
                                <div class="gradient">
                                    <div wicket:id="reference" class="content">
                                        <div class="column1 grid_4 alpha">
                                            <ul>                                    
                                                <li id="enzyme" class="protein">
                                                    <a href="enzyme" class="tab ${enzymeSelected}">
                                                            <span class="label icon icon-conceptual" data-icon="P">
                                                               <spring:message code="label.entry.enzyme.title"/>
                                                            </span>
                                                    </a>
                                                </li>
                                                <li id="structure" class="structure">
                                                    <a href="proteinStructure" class="tab ${proteinStructureSelected}">
                                                            <span class="label icon icon-conceptual icon-c4" data-icon="s">
                                                                <spring:message code="label.entry.proteinStructure.title"/>
                                                            </span>
                                                    </a>
                                                </li>
                                                <li id="reaction" class="reaction">
                                                    <a href="reactionsPathways" class="tab ${reactionsPathwaysSelected}">
                                                            <span class="label icon icon-conceptual" data-icon="y">
                                                                <spring:message code="label.entry.reactionsPathways.title"/>
                                                                
                                                            </span>
                                                    </a>
                                                </li>
                                                <li id="molecule" class="molecule">
                                                    <a href="molecules" class="tab ${moleculesSelected}">
                                                             <span class="label icon icon-conceptual" data-icon="b">
                                                                <spring:message code="label.entry.molecules.title"/>
                                                            </span>
                                                    </a>
                                                </li>
                                                <li id="disease" class="disease">
                                                    <a href="diseaseDrugs" class="tab ${diseaseDrugsSelected}">
                                                            <span class="label icon icon-species" data-icon="v">
                                                                <spring:message code="label.entry.disease.title"/>
                                                            </span>
                                                    </a>
                                                </li>
                                                <li id="literature" class="literature">
                                                    <a href="literature" class="tab ${literatureSelected}">
                                                            <span class="label icon icon-conceptual" data-icon="l">
                                                                <spring:message code="label.entry.literature.title"/>
                                                            </span>
                                                    </a>
                                                </li>
                                            </ul>
                                        </div>
                                        <div class="column2 grid_20 omega">

                                            <c:if test='${requestedfield=="enzyme"}'>
                                                <c:set var="_enzyme" value="${enzymeModel.enzyme}"/>

                                                <c:if test='${_enzyme.enzymetype[0] == "error"}'>
                                                    <div class="node ">
                                                        <div class="view ">
                                                            <%@include file="errors.jsp" %>
                                                        </div>
                                                    </div>
                                                </c:if>
                                                <c:if test='${_enzyme.enzymetype[0] != "error"}'>
                                                    <div class="node ">
                                                        <div class="view ">
                                                            <%@include file="enzyme.jsp" %>
                                                        </div>
                                                    </div>
                                                </c:if>
                                            </c:if>

                                            <!--START PROTEIN STRUCTURE TAB-->
                                            <c:if test='${requestedfield=="proteinstructure"}'>
                                                <c:set var="structure" value="${enzymeModel.proteinstructure}"/>   
                                                <c:if test='${structure[0].name == "error"}'>
                                                    <div class="node ">
                                                        <div class="view ">
                                                            <%@include file="errors.jsp" %>
                                                        </div>
                                                    </div>
                                                </c:if>
                                                <c:if test='${structure[0].name != "error"}'>
                                                    <div class="node  ">
                                                        <div class="view ">
                                                            <%@include file="proteinStructure.jsp" %>
                                                        </div>
                                                    </div>
                                                </c:if>
                                            </c:if>

                                            <!--START REACTIONS & PATHWAYS TAB-->
                                            <c:if test='${requestedfield=="reactionspathways"}'>

                                                <c:set var="pathway" value="${enzymeModel.reactionpathway}"/>   
                                                <c:if test='${pathway[0].reaction.name == "error"}'>
                                                    <div class="node ">
                                                        <div class="view ">
                                                            <%@include file="errors.jsp" %>
                                                        </div>
                                                    </div>
                                                </c:if>
                                                <c:if test='${pathway[0].reaction.name != "error"}'>
                                                    <div class="node ">
                                                        <div class="view ">
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
                                                        <div class="node ">
                                                            <div class="view ">
                                                                <%@include file="errors.jsp" %>
                                                            </div>
                                                        </div>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="node ">
                                                            <div class="view ">
                                                                <%@include file="molecules.jsp" %>
                                                            </div>
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:if>
                                            <!--START DISEASE & DRUGS TAB-->
                                            <c:if test='${requestedfield=="diseasedrugs"}'>
                                                <c:set var="diseases" value="${enzymeModel.disease}"/>
                                                <c:if test='${diseases[0].name == "error"}'>
                                                    <div class="node ">
                                                        <div class="view ">
                                                            <%@include file="errors.jsp" %>
                                                        </div>
                                                    </div>
                                                </c:if>
                                                <c:if test='${diseases[0].name != "error"}'>
                                                    <div class="node ">
                                                        <div class="view ">
                                                            <%@include file="disease.jsp" %>
                                                        </div>
                                                    </div>
                                                </c:if>
                                            </c:if>

                                            <!--START literature TAB-->
                                            <c:if test='${requestedfield=="literature"}'>

                                                <c:if test="${not empty enzymeModel.literature}">
                                                <c:set var="lit" value="${enzymeModel.literature}"/>   
                                                <c:if test='${lit[0] == "error"}'>
                                                    <div class="node ">
                                                        <div class="view ">
                                                            <%@include file="errors.jsp" %>
                                                        </div>
                                                    </div>
                                                </c:if>
                                                <c:if test='${lit[0] != "error"}'>
                                                    <div class="node ">
                                                        <div class="view ">
                                                            <%@include file="literature.jsp" %>
                                                        </div>
                                                    </div>
                                                </c:if>
                                                </c:if>
                                            </c:if>
                                        </div>
                                        <div class="clearfix"></div>
                                </div>
                            </form:form>
                        </div>

    <%@include file="footer.jspf" %>
    
        </div> <!--! end of #wrapper -->

    </body>
</html>

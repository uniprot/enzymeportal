<%--
    Document   : entry
    Created on : Sep 18, 2012, 10:52:21 AM
    Author     : joseph
--%>

<!doctype html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--
<%@ taglib prefix="form" uri="https://www.springframework.org/tags/form"%>

<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars"%>
--%>
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

    <body class="level2 full-width">

        <div id="wrapper">

            <%@include file="header.jspf" %>

            <div id="content" role="main" class="clearfix">
                <%@ include file="breadcrumbs.jsp" %>
                <!-- Suggested layout containers -->


                <form:form id="entryForm" modelAttribute="enzymeModel" action="entry" method="GET">
                    <!--<c:set var="chebiImageBaseUrl" value="https://www.ebi.ac.uk/chebi/displayImage.do?defaultImage=true&chebiId="/>-->
                    <c:set var="chebiImageBaseUrl"
                           value="https://www.ebi.ac.uk/chebi/displayImage.do?defaultImage=true&imageIndex=0&chebiId="/>
                    <c:set var="chebiImageParams"
                           value="&dimensions=200&scaleMolecule=true"/>
                    <c:set var="chebiEntryBaseUrl"
                           value="https://www.ebi.ac.uk/chebi/searchId.do?chebiId="/>
                    <c:set var="chebiEntryBaseUrlParam" value=""/>
                    <%--
                    <c:set var="rheaEntryBaseUrl"
                           value="http://www.ebi.ac.uk/rhea/reaction.xhtml?id="/>
                    --%>
                    <c:set var="rheaEntryBaseUrl"
                           value="https://www.rhea-db.org/reaction?id="/>
                    <c:set var="intenzEntryBaseUrl"
                           value="http://www.ebi.ac.uk/intenz/query?cmd=SearchEC&ec="/>
                    <c:set var="chemblImageBaseUrl"
                           value="https://www.ebi.ac.uk/chembldb/compound/displayimage/"/>
                    <c:set var="reactomeBaseUrl"
                           value="https://www.reactome.org/cgi-bin/link?SOURCE=Reactome&ID="/>
                    <c:set var="reactomeImageBaseUrl"
                           value="https://www.reactome.org/"/>

                    <c:set var="enzyme" value="${enzymeModel.enzyme}"/>
                    <!--requestedfield is an enum type in the controller. Its value has to be one of the values in the Field variable in the controller-->
                    <c:set var="requestedfield" value="${enzymeModel.requestedfield}"/>
                    <c:if test='${requestedfield=="enzyme"}'>
                        <c:set var="enzymeSelected" value="selected"/>
                    </c:if>
                    <c:if test='${requestedfield=="proteinstructure"}'>
                        <c:set var="proteinStructureSelected" value="selected"/>
                    </c:if>
                    <c:if test='${requestedfield=="reactionsMechanisms"}'>
                        <c:set var="reactionsMechanismsSelected" value="selected"/>
                    </c:if>
                    <c:if test='${requestedfield=="pathways"}'>
                        <c:set var="pathwaysSelected" value="selected"/>
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
                        <div class="header row">


                            <div class="row" id="title-row">
                                <section class="large-3 columns">
                                    <img src="/enzymeportal/resources/images/protein_page_logo2.png">
                                </section>
                                <section class="large-9 columns">
                                    <h2><c:out value="${enzymeModel.name}"/></h2>

                                    <div class="entry-buttons">
                                        <c:if test="${empty basket ||empty basket[enzymeModel.accession]}">
                                            <input type="hidden" id="enzymeId" value="${enzymeModel.accession}"/>
                                            <a id="add-to-basket" href="#" class="icon icon-generic btn" data-icon="b"> Add to Basket</a>
                                            <script>
                                                $('#add-to-basket').click(function () {
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


                                </section>
                            </div>






                        </div>
                    </section>


                    <div class="clearfix"></div>
                    <div class="gradient">
                        <div wicket:id="reference" class="row content">
                            <div class="large-3 columns column1">
                                <ul class="no-bullet">
                                    <li id="enzyme" class="protein">
                                        <a href="enzyme" class="tab ${enzymeSelected}">
                                            <span class="labelEP icon icon-conceptual" data-icon="P">
                                                Protein Summary
                                            </span>
                                        </a>
                                    </li>
                                    <li id="structure" class="structure">
                                        <a href="proteinStructure" class="tab ${proteinStructureSelected}">
                                            <span class="labelEP icon icon-conceptual icon-c4" data-icon="s">
                                                <spring:message code="label.entry.proteinStructure.title"/>
                                            </span>
                                        </a>
                                    </li>
                                    <li id="mechanism" class="reaction">
                                        <a href="reactionsMechanisms" class="tab ${reactionsMechanismsSelected}">
                                            <span class="labelEP icon icon-conceptual" data-icon="y">
                                                <spring:message code="label.entry.reactionsMechanisms.title"/>

                                            </span>
                                        </a>
                                    </li>
                                    <li id="pathway" class="reaction">
                                        <a href="pathways" class="tab ${pathwaysSelected}">
                                            <span class="labelEP icon icon-conceptual" data-icon="y">
                                                <spring:message code="label.entry.pathways.title"/>

                                            </span>
                                        </a>
                                    </li>
                                    <li id="reaction" class="reaction">
                                        <a href="reactionsPathways" class="tab ${reactionsPathwaysSelected}">
                                            <span class="labelEP icon icon-conceptual" data-icon="y">
                                                <spring:message code="label.entry.reactionsPathways.title"/>

                                            </span>
                                        </a>
                                    </li>
                                    <li id="molecule" class="molecule">
                                        <a href="molecules" class="tab ${moleculesSelected}">
                                            <span class="labelEP icon icon-conceptual" data-icon="b">
                                                <spring:message code="label.entry.molecules.title"/>
                                            </span>
                                        </a>
                                    </li>
                                    <li id="disease" class="disease">
                                        <a href="diseaseDrugs" class="tab ${diseaseDrugsSelected}">
                                            <span class="labelEP icon icon-species" data-icon="v">
                                                <spring:message code="label.entry.disease.title"/>
                                            </span>
                                        </a>
                                    </li>
                                    <li id="literature" class="literature">
                                        <a href="literature" class="tab ${literatureSelected}">
                                            <span class="labelEP icon icon-conceptual" data-icon="l">
                                                <spring:message code="label.entry.literature.title"/>
                                            </span>
                                        </a>
                                    </li>
                                </ul>
                            </div>
                            <div class="large-9 columns column2">

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

                                <!--START REACTIONS & MECHANISM TAB-->
                                <c:if test='${requestedfield=="reactionsMechanisms"}'>
                                    <c:set var="rheaReactions" value="${enzymeModel.enzymeReactions}"/>
                                    <c:set var="mechanisms" value="${enzymeModel.reactionMechanisms}"/>
                        
                                    <div class="node ">
                                        <div class="view ">
                                            <%@include file="reactionsMechanisms.jsp" %>
                                        </div>
                                    </div>
                                    <%--    
                            <c:set var="mechanisms" value="${enzymeModel.reactionMechanisms}"/>
                            <c:set var="rhea" value="${enzymeModel.enzymeReactions}"/>

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
                                    --%>
                                </c:if>

                                <!--START PATHWAYS TAB-->
                                <c:if test='${requestedfield=="pathways"}'>
                                    <c:set var="pathways" value="${enzymeModel.pathways}"/>
                                    <div class="node ">
                                        <div class="view ">
                                            <%@include file="reactomePathways.jsp" %>
                                        </div>
                                    </div>
                                    <%--       
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
                                    --%>
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

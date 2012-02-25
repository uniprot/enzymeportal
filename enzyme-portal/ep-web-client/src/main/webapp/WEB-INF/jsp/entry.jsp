<%-- 
    Document   : enzyme
    Created on : May 6, 2011, 7:40:14 PM
    Author     : hongcao
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
        <title>Enzyme Entry</title>
        <link rel="stylesheet"  href="http://www.ebi.ac.uk/inc/css/contents.css"     type="text/css" />
        <link media="screen" href="../../resources/lib/spineconcept/css/960gs-fluid/grid.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="../../resources/lib/spineconcept/css/common.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="../../resources/lib/spineconcept/css/summary.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="../../resources/lib/spineconcept/css/literature.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="../../resources/lib/spineconcept/css/species.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="../../resources/lib/spineconcept/javascript/jquery-ui/css/custom-theme/jquery-ui-1.8.11.custom.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="../../resources/css/enzyme.css" type="text/css" rel="stylesheet" />

        <link href="../../resources/css/search.css" type="text/css" rel="stylesheet" />
        <script src="../../resources/lib/spineconcept/javascript/jquery-1.5.1.min.js" type="text/javascript"></script>
        <script src="../../resources/lib/spineconcept/javascript/jquery-ui/js/jquery-1.5.1.min.js" type="text/javascript"></script>
        <script src="../../resources/lib/spineconcept/javascript/jquery-ui/js/jquery-ui-1.8.11.custom.min.js" type="text/javascript"></script>
        <script src="../../resources/lib/spineconcept/javascript/summary.js" type="text/javascript"></script>
    </head>
    <body>
        <jsp:include page="header.jsp"/>
        <div class="contents">
            <div class="container_12">
                <jsp:include page="subHeader.jsp"/>
                <form:form id="entryForm" modelAttribute="enzymeModel" action="entry" method="GET">
                    <!--<c:set var="chebiImageBaseUrl" value="http://www.ebi.ac.uk/chebi/displayImage.do?defaultImage=true&chebiId="/>-->
                    <c:set var="chebiImageBaseUrl" value="http://www.ebi.ac.uk/chebi/displayImage.do?defaultImage=true&imageIndex=0&chebiId="/>
                    <c:set var="chebiImageParams" value="&dimensions=200&scaleMolecule=true"/>
                    <c:set var="chebiEntryBaseUrl" value="http://www.ebi.ac.uk/chebi/searchId.do?chebiId="/>
                    <c:set var="chebiEntryBaseUrlParam" value=""/>
                    <c:set var="rheaEntryBaseUrl" value="http://www.ebi.ac.uk/rhea/reaction.xhtml?id="/>
                    <c:set var="intenzEntryBaseUrl" value="http://www.ebi.ac.uk/intenz/query?cmd=SearchEC&ec="/>
                    <c:set var="chemblImageBaseUrl" value="https://www.ebi.ac.uk/chembldb/compound/displayimage/"/>

                    <c:set var="reactomeImageBaseUrl" value="http://www.reactome.org/"/>

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
                    <div class="grid_12 header"  style="">
                        <div class="container_12">
                            <div class="grid_4 prefix_4 suffix_3 alpha">
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
                        </div>
                    </div>
                </div>
                <div class="container_12 gradient">
                    <div class="grid_12">
                        <div wicket:id="reference" class="content">
                            <div class="column1">
                                <ul>                                    
                                    <li id="enzyme" class="tab protein ${enzymeSelected}">
                                        <a href="enzyme">
                                            <span class="inner_tab">
                                                <span class="icon"></span>
                                                <span class="label">
                                                    <spring:message code="label.entry.enzyme.title"/>
                                                </span>
                                            </span>
                                        </a>
                                    </li>
                                    <li id="structure" class="tab structure ${proteinStructureSelected}">
                                        <a href="proteinStructure">
                                            <span class="inner_tab">
                                                <span class="icon"></span>
                                                <span class="label">
                                                    <spring:message code="label.entry.proteinStructure.title"/>
                                                </span>
                                            </span>
                                        </a>
                                    </li>
                                    <li id="reaction" class="tab reaction ${reactionsPathwaysSelected}">
                                        <a href="reactionsPathways">
                                            <span class="inner_tab">
                                                <span class="icon"></span>
                                                <span class="label">
                                                    <spring:message code="label.entry.reactionsPathways.title"/>
                                                </span>
                                            </span>
                                        </a>
                                    </li>
                                    <li id="molecule" class="tab molecule ${moleculesSelected}">
                                        <a href="molecules">
                                            <span class="inner_tab">
                                                <span class="icon"></span>
                                                <span class="label">
                                                    <spring:message code="label.entry.molecules.title"/>
                                                </span>
                                            </span>
                                        </a>
                                    </li>
                                    <li id="disease" class="tab disease ${diseaseDrugsSelected}">
                                        <a href="diseaseDrugs">
                                            <span class="inner_tab">
                                                <span class="icon"></span>
                                                <span class="label">
                                                    <spring:message code="label.entry.disease.title"/>
                                                </span>
                                            </span>
                                        </a>
                                    </li>
                                    <li id="literature" class="tab literature ${literatureSelected}">
                                        <a href="literature">
                                            <span class="inner_tab">
                                                <span class="icon"></span>
                                                <span class="label">
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
                                        <div class="node">
                                            <div class="view">
                                                <%@include file="errors.jsp" %>
                                            </div>
                                        </div>
                                    </c:if>
                                     <c:if test='${_enzyme.enzymetype[0] != "error"}'>
                                    <div class="node">
                                        <div class="view">
                                            <%@include file="enzyme.jsp" %>
                                        </div>
                                    </div>
                                     </c:if>
                                </c:if>

                                <!--START PROTEIN STRUCTURE TAB-->
                                <c:if test='${requestedfield=="proteinStructure"}'>
                                    <c:set var="structure" value="${enzymeModel.proteinstructure}"/>   
                                    <c:if test='${structure[0].name == "error"}'>
                                        <div class="node">
                                            <div class="view">
                                                <%@include file="errors.jsp" %>
                                            </div>
                                        </div>
                                    </c:if>
                                    <c:if test='${structure[0].name != "error"}'>
                                    <div class="node">
                                        <div class="view">
                                            <%@include file="proteinStructure.jsp" %>
                                        </div>
                                    </div>
                                    </c:if>
                                </c:if>

                                <!--START REACTIONS & PATHWAYS TAB-->
                                <c:if test='${requestedfield=="reactionsPathways"}'>
                                  
                                   <c:set var="pathway" value="${enzymeModel.reactionpathway}"/>   
                                    <c:if test='${pathway[0].reaction.name == "error"}'>
                                        <div class="node">
                                            <div class="view">
                                                <%@include file="errors.jsp" %>
                                            </div>
                                        </div>
                                    </c:if>
                                    <c:if test='${pathway[0].reaction.name != "error"}'>
                                    <div class="node">
                                        <div class="view">
                                            <%@include file="reactionsPathways.jsp" %>
                                        </div>
                                    </div>
                                    </c:if>
                                </c:if>

                                <!--START SMALL MOLECULES TAB-->
                                <c:if test='${requestedfield=="molecules"}'>
                                    <c:set var="chemEntity" value="${enzymeModel.molecule}"/>
                                    <c:if test='${chemEntity.drugs[0].name == "error"}'>
                                        <div class="node">
                                            <div class="view">
                                                <%@include file="errors.jsp" %>
                                            </div>
                                        </div>
                                    </c:if>

                                    <c:if test='${chemEntity.drugs[0].name != "error"}'>
                                        <div class="node">
                                            <div class="view">
                                                <%@include file="molecules.jsp" %>
                                            </div>
                                        </div>
                                    </c:if>
                                </c:if>
                                <!--START DISEASE & DRUGS TAB-->
                                <c:if test='${requestedfield=="diseaseDrugs"}'>
                                    <c:set var="diseases" value="${enzymeModel.disease}"/>
                                    <c:if test='${diseases[0].name == "error"}'>
                                        <div class="node">
                                            <div class="view">
                                                <%@include file="errors.jsp" %>
                                            </div>
                                        </div>
                                    </c:if>
                                    <c:if test='${diseases[0].name != "error"}'>
                                        <div class="node">
                                            <div class="view">
                                                <%@include file="disease.jsp" %>
                                            </div>
                                        </div>
                                    </c:if>
                                </c:if>

                                <!--START literature TAB-->
                                <c:if test='${requestedfield=="literature"}'>
                                    
                                    <c:set var="lit" value="${enzymeModel.literature}"/>   
                                    <c:if test='${lit[0] == "error"}'>
                                        <div class="node">
                                            <div class="view">
                                                <%@include file="errors.jsp" %>
                                            </div>
                                        </div>
                                    </c:if>
                                    <c:if test='${lit[0] != "error"}'>
                                    <div class="node">
                                        <div class="view">
                                            <%@include file="literature.jsp" %>
                                        </div>
                                    </div>
                                    </c:if>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </form:form>
                <div class="clear"></div>
            </div>
            <jsp:include page="footer.jsp"/>
        </div>
    </body>
</html>

<%-- 
    Document   : enzyme
    Created on : May 6, 2011, 7:40:14 PM
    Author     : hongcao
--%>
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
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
        <div class="headerdiv" id="headerdiv" style="position:absolute; z-index: 1;">
            <iframe src="http://www.ebi.ac.uk/inc/head.html" name="head" id="head" frameborder="0" marginwidth="0px" marginheight="0px" scrolling="no"  width="100%" style="position:absolute; z-index: 1; height: 800px;">
				[Your user agent does not support frames or is currently configured not to display iframes.]
            </iframe>
        </div>
        <form:form id="entryForm" modelAttribute="enzymeModel" action="entry" method="GET">
            <c:set var="enzyme" value="${enzymeModel.enzyme}"/>
            <c:set var="relSpecies" value="${enzymeModel.relatedspecies}"/>
            relatedspecies
            <div class="contents">
                <div class="container_12">
                    <div class="grid_12">
                        <div class="breadcrumbs" wicket:id="breadcrumbs">
                            <ul>
                                <li class="first"><a href="http://www.ebi.ac.uk/" class="firstbreadcrumb">EBI</a></li>
                                <li><a href="http://www.ebi.ac.uk/Databases/">Databases</a></li>
                                <li><a href="http://www.ebi.ac.uk/Databases/">Enzyme Portal</a></li>
                            </ul>
                        </div>
                    </div>
                    <div class="grid_12">
                        <h1 wicket:id="title">Enzyme</h1>
                    </div>
                    <div class="grid_12 header"  style="">
                        <div class="container_12">
                            <div class="grid_4 prefix_4 suffix_3 alpha">
                                <div class="panel">
                                    <div wicket:id="classification">
                                        <div class="classification">
                                            <div class="label">ORGANISMS</div>
                                            <div class='box selected ${fn:replace(relSpecies[0].species.scientificname, " ", "_")}'>
                                                <span class="name"><c:out value="${relSpecies[0].species.commonname}"/></span>
                                                <span class="extra"><c:out value="${relSpecies[0].species.scientificname}"/></span>
                                            </div>
                                        </div>
                                        <div class="selection">
                                            <ul>
                                                <c:forEach begin="0" end="${fn:length(relSpecies)-1}" var="i">
                                                    <c:set var="species" value="${relSpecies[i].species}"/>
                                                    <a href="${relSpecies[i].uniprotaccessions[0]}">
                                                        <c:set var="select" value=""/>
                                                        <c:if test="${i==0}">
                                                            <c:set var="select" value="selected"/>
                                                        </c:if>
                                                        <li class="${select}">
                                                            <div class='box ${fn:replace(species.scientificname, " ", "_")}'>
                                                                <span class="name"><c:out value="${species.commonname}"/></span>
                                                                <span class="extra"><c:out value="${species.scientificname}"/></span>
                                                            </div>
                                                        </li>
                                                    </a>
                                                </c:forEach>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="grid_1 omega">
                                <div class="menu">
                                    <a href="http://www.ebi.ac.uk/inc/help/search_help.html" class="help">Help</a>
                                    <a href="" wicket:id="print" class="print"><span wicket:id="printLabel">Print</span></a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="container_12 gradient">
                    <div class="grid_12">
                        <div wicket:id="reference" class="content">
                            <div class="column1">
                                <ul>
                                    <li id="enzyme" class="tab protein selected">
                                        <a href="">
                                            <span class="inner_tab">
                                                <span class="icon"></span>
                                                <span class="label">
                                                    <spring:message code="label.entry.enzyme.title"/>
                                                </span>
                                            </span>
                                        </a>
                                    </li>
                                    <li id="structure" class="tab structure">
                                        <a href="entry/id/P86883">
                                            <span class="inner_tab">
                                                <span class="icon"></span>
                                                <span class="label">
                                                    <spring:message code="label.entry.proteinStructure.title"/>
                                                </span>
                                            </span>
                                        </a>
                                    </li>
                                    <li id="reaction" class="tab reaction">
                                        <a href="reactionsPathways">
                                            <span class="inner_tab">
                                                <span class="icon"></span>
                                                <span class="label">
                                                    <spring:message code="label.entry.reactionsPathways.title"/>
                                                </span>
                                            </span>
                                        </a>
                                    </li>
                                    <li id="molecule" class="tab molecule">
                                        <span class="inner_tab">
                                            <span class="icon"></span>
                                            <span class="label">
                                                <spring:message code="label.entry.molecules.title"/>
                                            </span>
                                        </span>
                                    </li>
                                    <li id="disease" class="tab disease">
                                        <a href="">
                                            <span class="inner_tab">
                                                <span class="icon"></span>
                                                <span class="label">
                                                    <spring:message code="label.entry.disease.title"/>
                                                </span>
                                            </span>
                                        </a>
                                    </li>
                                    <li id="literature" class="tab literature">
                                        <a href="">
                                            <span class="inner_tab">
                                                <span class="icon"></span>
                                                <span class="label">
                                                    <spring:message code="label.entry.literarture.title"/>
                                                </span>
                                            </span>
                                        </a>
                                    </li>
                                </ul>
                            </div>
                            <div class="column2">
                                <div class="node">
                                    <div class="view">
                                        <div id="enzymeContent" class="summary">
                                            <h2><c:out value="${enzymeModel.name}"/></h2>
                                            <dl>
                                                <dt>Function</dt>
                                                <dd>
                                                    <ul>
                                                        <li><c:out value="${enzymeModel.function}"/></li>
                                                    </ul>
                                                </dd>
                                            </dl>
                                            <dl>
                                                <dt>EC Classification</dt>
                                                <dd>
                                                    <ul>
                                                        <li>

                                                            <c:set var="echierarchies" value="${enzyme.echierarchies}"/>
                                                            <c:set var="echierarchiesSize" value="${fn:length(echierarchies)}"/>
                                                            <c:forEach var="j" begin="0" end="${echierarchiesSize-1}">
                                                                <c:set var="ecClass" value="${echierarchies[j].ecclass}"/>
                                                                <c:set var="ecClassSize" value="${fn:length(ecClass)}"/>
                                                                <c:if test='${ecClassSize>0}'>
                                                                    <c:set var="ecNumber" value=""/>
                                                                    <c:set var="dot" value=""/>
                                                                    <c:forEach var="i" begin="0" end="${ecClassSize-1}">
                                                                        <c:if test='${i > 0}'>
                                                                            <c:set var="dot" value="."/>
                                                                        </c:if>
                                                                        <c:if test='${i <= 2}'>
                                                                            <c:set var="ecNumber" value="${ecNumber}${dot}${ecClass[i].ec}"/>
                                                                            <c:out value="${ecClass[i].name}"/>
                                                                            >
                                                                        </c:if>

                                                                        <c:if test='${i > 2}'>
                                                                            <c:set var="ecNumber" value="${ecNumber}${dot}${ecClass[i].ec}"/>
                                                                            <c:out value="${ecNumber}"/> -
                                                                            <c:out value="${ecClass[i].name}"/>
                                                                        </c:if>
                                                                    </c:forEach>
                                                                    <br/>
                                                                </c:if>
                                                            </c:forEach>
                                                        </li>
                                                    </ul>
                                                </dd>
                                            </dl>
                                            <dl>
                                                <dt>Enzyme Type</dt>
                                                <dd>
                                                    <ul>
                                                        <li>Enzyme Type</li>
                                                    </ul>
                                                </dd>
                                            </dl>
                                            <dl>
                                                <dt>Other names</dt>
                                                <dd>
                                                    <ul>
                                                        <li>
                                                            <c:set var="synonym" value="${enzymeModel.synonym}"/>
                                                            <c:set var="synonymSize" value="${fn:length(synonym)}"/>
                                                            <c:if test='${synonymSize>0}'>
                                                                <c:forEach var="i" begin="0" end="${synonymSize-1}">
                                                                    <c:out value="${synonym[i]}"/><br>
                                                                </c:forEach>
                                                            </c:if>
                                                        </li>
                                                    </ul>
                                                </dd>
                                            </dl>

                                            <dl>
                                                <dt>Protein Sequence</dt>
                                                <dd>
                                                    <ul>
                                                        <li>
                                                            <c:set var="sequence" value="${enzyme.sequence}"/>
                                                            This sequence has
                                                            <c:out value="${sequence.sequence}"/>
                                                            amino acids and a molecular weight of
                                                            <c:out value="${sequence.weight}"/> <br>
                                                            <a target="blank" href="${sequence.sequenceurl}">View Sequence in Uniprot</a>

                                                        </li>
                                                    </ul>
                                                </dd>
                                            </dl>
                                        </div>
                                    </div>




                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="clear"></div>
                </div>
            </div>
        </form:form>
        <script type="text/javascript">
            function getQueryString() {
                /* variable searchTerm is automaticatlly added by the framework */
                return searchTerm;
            }

            window.onload=function() {
                if (navigator.userAgent.indexOf('MSIE') != -1) {
                    document.getElementById('head').allowTransparency = true;
                }
            }
        </script>
    </body>
</html>
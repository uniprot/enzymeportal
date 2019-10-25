<%-- 
    Document   : reactionsMechanisms
    Created on : Jul 19, 2018, 1:17:00 PM
    Author     : Joseph
--%>


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%--
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars"%>
--%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<script language="JavaScript" type="text/javascript" src="${pageContext.request.contextPath}/resources/javascript/biojs/Biojs.js"></script>
<script language="JavaScript" type="text/javascript" src="${pageContext.request.contextPath}/resources/javascript/biojs/Biojs.Rheaction.js"></script>

<%--<script language="JavaScript" type="text/javascript" src="${pageContext.request.contextPath}/resources/javascript/biojs/jquery-1.6.4.js"></script>--%>

<link href=" ${pageContext.request.contextPath}/resources/javascript/biojs/biojs.Rheaction.css" rel="stylesheet" type="text/css" />
<link href=" ${pageContext.request.contextPath}/resources/javascript/biojs/biojspcviz.css" rel="stylesheet" type="text/css" />

<h1>DATA ${enzymeModel.enzymeReactions}</h1>
<h1>REACT ${enzymeModel.reactionMechanisms}</h1>
<div id="reactionContent" class="summary">
    <c:if test="${ not empty enzymeModel.catalyticActivities}">
        <p></p>

        <c:if test="${fn:length(enzymeModel.catalyticActivities) == 1}">
            <h5>Catalytic Activity</h5>
        </c:if>
        <c:if test="${fn:length(enzymeModel.catalyticActivities) > 1}">
            <h5>Catalytic Activities</h5>   
        </c:if>

        <ul style="list-style-type: square">
            <c:forEach items="${enzymeModel.catalyticActivities}" var="activity" >
                <li class="reaction"><b>${activity}</b></li>   
                    </c:forEach>

        </ul>


    </c:if>
    <c:set var="rheaReactions" value="${enzymeModel.enzymeReactions}"/>
    <c:set var="mechanisms" value="${enzymeModel.reactionMechanisms}"/>

    <%--
    <c:set var="reactionpathways" value="${enzymeModel.reactionpathway}"/>
  <c:set var="pathways" value="${enzymeModel.pathways}"/>
    --%>

    <c:choose>
        <c:when test="${empty rheaReactions && empty mechanisms}">
            <p>There is no information available about reactions catalised by
                this enzyme.</p>
            </c:when>
            <c:otherwise>


            <c:if test="${enzymeModel.numReactions > 1}">
                <div class="main_link"><b>${enzymeModel.numReactions} reactions found</b></div>  
            </c:if>
            <c:if test="${enzymeModel.numReactions == 1}">
                <div class="main_link"><b>${enzymeModel.numReactions} reaction found</b></div>  
            </c:if>    




            <c:if test="${rheaReactions == null}">
                <b>No Reactions found for this enzyme</b>
            </c:if>
            <c:if test="${rheaReactions != null}">

                <c:forEach items="${rheaReactions}" var="reaction" varStatus="loop">          
                    <c:set var="rheaEntryUrl" value="${rheaEntryBaseUrl}${reaction.id}"/>
                    <div class="reaction block">            
                        <b>
                            <!--<a target="blank" href="${rheaEntryUrl}"><c:out value="${reaction.name}" escapeXml="false"/></a>-->
                            <c:out value="${reaction.name}" escapeXml="false"/> 
                        </b>
                        <br/>
                        <div id="bjs_${loop.count}" style="margin-top: 10px">

                        </div>

                        <script>
                            $(document).ready(function () {
                                new Biojs.Rheaction({
                                    target: 'bjs_${loop.count}',
                                    id: '${reaction.id}',
                                    proxyUrl: '${pageContext.request.contextPath}/proxy.jsp'
                                });
                            });
                        </script>
                        <div id="reactionDesc-${rpVs.index}">
                            <c:out value="${reaction.description}" escapeXml="false"/>
                        </div>
                        <div id="extLinks-${rpVs.index}">
                            <c:if test="${not empty reaction.id}">
                                <div class="rhea inlineLinks">
                                    <a target="blank" href="${rheaEntryUrl}">
                                        View reaction in Rhea
                                    </a>

                                </div>
                                <div class="rhea inlineLinks">
                                    <a target="blank" href="http://www.genome.jp/dbget-bin/www_bget?rn:${reaction.keggId}">
                                        View Reaction In Kegg
                                    </a>

                                </div>
                            </c:if>


                        </div>



                    </c:forEach>
                </div>
            </c:if>


        </div>


        <!--                   add reaction mechnisms here-->
        <div style="margin-top: 10px"></div>

        <c:set var="mechanismSize" value="${fn:length(mechanisms)}"/>

        <c:if test="${mechanismSize>0}" >
            <c:if test="${mechanisms != null}">

                <p>${mechanismSize} Reaction Mechanisms found for this enzyme</p>
            </c:if>
            <div id="pathways">                       
                <c:forEach var="m" items="${mechanisms}">

                    <div class="large-9 columns">
                        <div id="featured-entry">
                            <div class="row">
                                <div class="large-9 columns" id="example-header">
                                    <h3>${m.enzymeName}</h3>
                                </div>

                            </div>
                            <a href="https://www.ebi.ac.uk/thornton-srv/m-csa/entry/${m.mcsaId}"><img id="feature-image" src="https://www.ebi.ac.uk/thornton-srv/m-csa${m.imageId}"></a>

                            <p class="text-justify"> ${m.mechanismDescription} </p>

                        </div>
                    </div>   






                </c:forEach>
            </div>
        </c:if>


        <div class="provenance">
            <ul>
                <li class="note_0">Data Source:
                    <a href="https://www.rhea-db.org/searchresults?q=${enzymeModel.accession}" >Rhea</a> &AMP; <a href="https://www.ebi.ac.uk/thornton-srv/m-csa/search/?s=${enzymeModel.accession}">M-CSA</a></li>

                <li class="note_2">Rhea is a freely available, manually annotated database of chemical reactions created in collaboration with the Swiss Institute of Bioinformatics (SIB).All data in Rhea is freely accessible and available for anyone to use. </li>
                <li class="note_1">Mechanism and Catalytic Site Atlas (M-CSA): a database of enzyme reaction mechanisms and active sites. </li>

            </ul>
        </div>
    </c:otherwise>
</c:choose> 

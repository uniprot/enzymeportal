<%-- 
    Document   : reactionsMechanisms
    Created on : Jul 19, 2018, 1:17:00 PM
    Author     : Joseph
--%>


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<script language="JavaScript" type="text/javascript" src="${pageContext.request.contextPath}/resources/javascript/biojs/Biojs.js"></script>
<script language="JavaScript" type="text/javascript" src="${pageContext.request.contextPath}/resources/javascript/biojs/Biojs.Rheaction.js"></script>


<link href=" ${pageContext.request.contextPath}/resources/javascript/biojs/biojs.Rheaction.css" rel="stylesheet" type="text/css" />
<link href=" ${pageContext.request.contextPath}/resources/javascript/biojs/biojspcviz.css" rel="stylesheet" type="text/css" />

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
    <c:set var="numReactions" value="${fn:length(enzymeModel.enzymeReactions)}"/>
    <%--
    <c:set var="mechanisms" value="${enzymeModel.reactionMechanism.mechanisms}"/>
    --%>
 
    <c:set var="mechanismSize" value="${enzymeModel.reactionMechanism.count}"/>





    <c:if test="${empty rheaReactions && mechanismSize == 0 && empty enzymeModel.catalyticActivities}">
        <p class="noResults">There is no information available about reactions catalised by this enzyme.</p>
    </c:if>



    <c:if test="${numReactions > 1}">
        <div class="main_link"><b>${numReactions} reactions found</b></div>  
    </c:if>
    <c:if test="${numReactions == 1}">
        <div class="main_link"><b>${numReactions} reaction found</b></div>  
    </c:if>    




    <c:if test="${ rheaReactions == null || empty rheaReactions && (mechanismSize != 0 || not empty enzymeModel.catalyticActivities)}">
        <b class="noResults">No Reactions found for this enzyme</b>
    </c:if>
    <c:if test="${rheaReactions != null}">

        <c:forEach items="${rheaReactions}" var="reaction" varStatus="loop"> 
            <c:set var="rheaEntryUrl" value="${rheaEntryBaseUrl}${reaction.id}"/>
            <div class="reaction block">            
                <b>
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
                <%--
                <div id="reactionDesc-${rpVs.index}">
                    <c:out value="${reaction.description}" escapeXml="false"/>
                </div>
                --%>
                <div id="extLinks-${rpVs.index}">
                    <c:if test="${not empty reaction.id}">
                        <div class="rhea inlineLinks">
                            <a target="blank" href="${rheaEntryUrl}">
                                View reaction in Rhea
                            </a>

                        </div>
                    </c:if>
                    <c:if test="${not empty reaction.keggId}">
                        <div class="rhea inlineLinks">
                            <a target="blank" href="http://www.genome.jp/dbget-bin/www_bget?rn:${reaction.keggId}">
                                View Reaction In Kegg
                            </a>

                        </div>
                    </c:if>


                </div>


            </div>
        </c:forEach>

    </c:if>




    <!--                   add reaction mechnisms here-->
    <div style="margin-top: 10px"></div>
    <br/>




    <c:if test="${mechanismSize ==0 && (not empty rheaReactions || not empty enzymeModel.catalyticActivities)}">
        <p class="noResults">No Reaction Mechanism found for this enzyme</p>
    </c:if>

    <c:if test="${mechanismSize>0}">
        <c:choose>
            <c:when test="${mechanismSize == 1}">
                <b><a href="https://www.ebi.ac.uk/thornton-srv/m-csa/entry/${result.mcsaId}" target="_blank">${mechanismSize}</a> Reaction Mechanism found for this enzyme</b> 
            </c:when>
            <c:otherwise>
                <b>${mechanismSize} Reaction Mechanisms found for this enzyme</b>
            </c:otherwise>
        </c:choose>

        <!--           mechanism tab           -->
        

        <%--
        <c:set var="results" value="${enzymeModel.reactionMechanism.results}"/>
        <c:forEach var="result"  items="${results}"  >
               <%@ include file="mcsa.jsp"%> 
        </c:forEach>
        --%>

        <c:set var="result" value="${enzymeModel.reactionMechanism.results[0]}"/>


        <ul class="tabs" data-active-collapse="true"  data-tabs id="mechanism-tabs-ec">
            <%@ include file="mcsa.jsp"%>
            <div style="padding-left: 2em;">
                <a   href="https://www.ebi.ac.uk/thornton-srv/m-csa/entry/${result.mcsaId}" target="_blank">
                    <button style="margin-bottom: 1em;" id="all-proteins" class="full-view icon icon-functional btn" data-icon="F" type="submit"> View Reaction Mechanisms in M-CSA</button>
                </a>
            </div>
        </ul>






    </c:if>


    <br/>
    <c:if test="${not empty rheaReactions || mechanismSize != 0 || not empty enzymeModel.catalyticActivities}">
        <div class="provenance">
            <ul>
                <li class="note_0">Data Source:
                    <a target="_blank" href="https://www.rhea-db.org/searchresults?q=${enzymeModel.accession}" >Rhea</a> &AMP; <a target="_blank" href="https://www.ebi.ac.uk/thornton-srv/m-csa/search/?s=${enzymeModel.accession}">M-CSA</a>&AMP; <a target="_blank" href="https://www.genome.jp/kegg/">KEGG</a></li>

                <li class="note_2">Rhea is a freely available, manually annotated database of chemical reactions created in collaboration with the Swiss Institute of Bioinformatics (SIB).All data in Rhea is freely accessible and available for anyone to use. </li>
                <li class="note_1">Mechanism and Catalytic Site Atlas (M-CSA): a database of enzyme reaction mechanisms and active sites. </li>
                <li class="note_1">KEGG reaction is a database of chemical reactions, mostly enzymatic reactions, containing all reactions that appear in the KEGG metabolic pathway maps and additional reactions that appear only in the Enzyme Nomenclature.</li>

            </ul>
        </div>
    </c:if>
</div>
<%-- 
    Document   : reactionsMechanisms
    Created on : Jul 19, 2018, 1:17:00 PM
    Author     : Joseph
--%>


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="epfn" uri="/WEB-INF/epTagLibray.tld" %>


<script language="JavaScript" type="text/javascript" src="${pageContext.request.contextPath}/resources/javascript/biojs/Biojs.js"></script>
<script language="JavaScript" type="text/javascript" src="${pageContext.request.contextPath}/resources/javascript/biojs/Biojs.Rheaction.js"></script>


<link href=" ${pageContext.request.contextPath}/resources/javascript/biojs/biojs.Rheaction.css" rel="stylesheet" type="text/css" />
<link href=" ${pageContext.request.contextPath}/resources/javascript/biojs/biojspcviz.css" rel="stylesheet" type="text/css" />

<!--rhea web component-->
<script src="https://unpkg.com/@webcomponents/custom-elements"></script>
<script type="module" src="https://unpkg.com/@swissprot/rhea-reaction-visualizer@0.0.16/index.js"></script>

<style>
    td{
        width:100px;
    }
    @media (max-width: 600px) {
        thead {
            display: none;
        }
        tr{
            display:grid;
            border-bottom:1px solid #000000;
        }
        td:nth-of-type(1):before { content: "Kinetics"; }
        td:nth-of-type(2):before { content: "pH dependence"; }
        td:nth-of-type(3):before { content: "Temperature dependence"; }

        td:before{
            width:150px;
            float:left;
        }
        td{
            width:300px;
        }
    }
</style>



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
            <%--
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
            --%>
            <%--
            <div id="reactionDesc-${rpVs.index}">
                <c:out value="${reaction.description}" escapeXml="false"/>
            </div>
            --%>
            <%--
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
            --%>
           
            <div class="webPage">
                <div id="bjs_${loop.count}" style="margin-top: 10px"></div>
                <hr style="border-color: #08a1b1"/>
                <rhea-reaction rheaid="${epfn:formatRheaId(reaction.id)}"  showids></rhea-reaction>

            </div>      
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
             
        </c:forEach>

    </c:if>




    <!--                   add reaction mechnisms here-->
    <div style="margin-top: 10px"></div>
    <br/>




    <c:if test="${mechanismSize ==0 && (not empty rheaReactions || not empty enzymeModel.catalyticActivities)}">
        <p class="noResults">No Reaction Mechanism found for this enzyme</p>
    </c:if>

    <c:if test="${mechanismSize>0}">
        <c:set var="result" value="${enzymeModel.reactionMechanism.results[0]}"/>
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




        <ul class="tabs" data-active-collapse="true"  data-tabs id="mechanism-tabs-ec">
            <%@ include file="mcsa.jsp"%>
            <div style="padding-left: 2em;">
                <a   href="https://www.ebi.ac.uk/thornton-srv/m-csa/entry/${result.mcsaId}" target="_blank">
                    <button style="margin-bottom: 1em;" id="all-proteins" class="full-view icon icon-functional btn" data-icon="F" type="submit"> View Reaction Mechanisms in M-CSA</button>
                </a>
            </div>
        </ul>






    </c:if>




    <c:choose>
        <c:when test="${not empty enzymeModel.kinetics }">
            <h5> Reaction parameters </h5>
            <c:set var="count" value="0" scope="page" />
            <c:set var="kinetics" value="${enzymeModel.kinetics}"/>
            <c:set var="phDependences" value="${enzymeModel.phDependences}"/>
            <c:set var="temperatureDependences" value="${enzymeModel.temperatureDependences}"/>
            <div style="overflow-x:auto;">

                <ul class="tabs" data-deep-link="true" data-update-history="true" data-deep-link-smudge="true" data-deep-link-smudge-delay="500" data-tabs id="deeplinked-tabs">
                    <li class="tabs-title is-active title"><a href="#kinetic" aria-selected="true">Kinetic Parameters</a></li>
                    <li class="tabs-title"><a href="#ph" class="title">pH dependence</a></li>
                    <li class="tabs-title title"><a href="#temp">Temperature dependence</a></li>
                </ul>


                <div class="tabs-content" data-tabs-content="deeplinked-tabs">
                    <div class="tabs-panel is-active" id="kinetic">
                        <c:choose>
                            <c:when test="${empty kinetics.km && kinetics.vmax}">
                                <p class="noResults">There are no Kinetic Parameters information for this Enzyme.</p> 
                            </c:when>
                            <c:otherwise>

                                <ul style="list-style-type: square">

                                    <c:forEach items="${kinetics.km}" var="km" varStatus="kcount">
                                        <c:if test="${kcount.count <= 10}">
                                            <li><b>KM</b>=${km.value} </li>     
                                            </c:if>

                                    </c:forEach>    


                                </ul>   

                                <ul style="list-style-type: circle">
                                    <c:forEach items="${kinetics.vmax}" var="vmax" varStatus="vcount">
                                        <c:if test="${vcount.count <= 10}">
                                            <li><b>Vmax</b>=${vmax.value}</li>    
                                            </c:if>

                                    </c:forEach>

                                </ul>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="tabs-panel" id="ph">
                        <c:choose>
                            <c:when test="${not empty phDependences}">


                                <ul style="list-style-type: circle">

                                    <c:forEach items="${phDependences}" var="phDependence">
                                        <li>${phDependence.value}</li>
                                        </c:forEach> 

                                </ul>
                            </c:when>
                            <c:otherwise>
                                <p class="noResults">There are no ph Dependences information for this Enzyme.</p>        
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="tabs-panel" id="temp">

                        <c:choose>
                            <c:when test="${not empty temperatureDependences}">

                                <ul style="list-style-type: circle">
                                    <c:forEach items="${temperatureDependences}" var="temperatureDependence">
                                        <li>${temperatureDependence.value}</li>
                                        </c:forEach>      
                                </ul>
                            </c:when>
                            <c:otherwise>
                                <p class="noResults">There are no temperature Dependences information for this Enzyme.</p>        
                            </c:otherwise>
                        </c:choose>

                    </div>
                </div>

                <%--
                <table>
                    <thead>
                        <tr>
                            <th>Kinetics</th>
                            <th>pH dependence</th>
                            <th>Temperature dependence</th>

                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>

                                <ul style="list-style-type: square">

                                    <c:forEach items="${kinetics.km}" var="km" varStatus="kcount">
                                        <c:if test="${kcount.count <= 3}">
                                            <li><b>KM</b>=${km.value} </li>     
                                            </c:if>

                                    </c:forEach>    


                                </ul>   

                                <ul style="list-style-type: circle">
                                    <c:forEach items="${kinetics.vmax}" var="vmax" varStatus="vcount">
                                        <c:if test="${vcount.count <= 3}">
                                            <li><b>Vmax</b>=${vmax.value}</li>    
                                            </c:if>

                                    </c:forEach>

                                </ul> 
                            </td>
                            <td>
                                <ul style="list-style-type: none">

                                    <c:forEach items="${phDependences}" var="phDependence">
                                        <li>${phDependence.value}</li>
                                        </c:forEach> 

                                </ul> 
                            </td>
                            <td>
                                <ul style="list-style-type: none">
                                    <c:forEach items="${temperatureDependences}" var="temperatureDependence">
                                        <li>${temperatureDependence.value}</li>
                                        </c:forEach>      
                                </ul>   
                            </td>



                        </tr>
                        <tr>

                            <td colspan="12">

                                <a   href="https://www.uniprot.org/uniprot/${enzymeModel.accession}" target="_blank">
                                    <button id="all-proteins" class="full-view icon icon-functional btn" data-icon="F" type="submit"> View all in Uniprot</button>
                                </a>

                            </td>

                        </tr>

                    </tbody>
                </table>
                --%>
                <table>
                    <tr>

                        <td colspan="12">

                            <a rel="noopener noreferrer"   href="https://www.uniprot.org/uniprot/${enzymeModel.accession}" target="_blank">
                                <button id="all-proteins" class="full-view icon icon-functional btn" data-icon="F" type="submit"> View all in Uniprot</button>
                            </a>

                        </td>

                    </tr>
                </table>
            </div>
        </c:when>
        <c:otherwise>
            <p class="noResults">There are no reaction parameters information for this Enzyme.</p> 
        </c:otherwise>
    </c:choose>




    <br/>
    <c:if test="${not empty rheaReactions || mechanismSize != 0 || not empty enzymeModel.catalyticActivities}">
        <div class="provenance">
            <ul>
                <li class="note_0">Data Source:
                    <a rel="noopener noreferrer" target="_blank" href="https://www.rhea-db.org/searchresults?q=${enzymeModel.accession}" >Rhea</a> &AMP; <a rel="noopener noreferrer" target="_blank" href="https://www.ebi.ac.uk/thornton-srv/m-csa/search/?s=${enzymeModel.accession}">M-CSA</a>&AMP; <a rel="noopener noreferrer" target="_blank" href="https://www.genome.jp/kegg/">KEGG</a>&AMP; <a rel="noopener noreferrer" target="_blank" href="https://www.ebi.ac.uk/metabolights/index">MetaboLights</a></li>

                <li class="note_2">Rhea is a freely available, manually annotated database of chemical reactions created in collaboration with the Swiss Institute of Bioinformatics (SIB).All data in Rhea is freely accessible and available for anyone to use. </li>
                <li class="note_1">Mechanism and Catalytic Site Atlas (M-CSA): a database of enzyme reaction mechanisms and active sites. </li>
                <li class="note_1">KEGG reaction is a database of chemical reactions, mostly enzymatic reactions, containing all reactions that appear in the KEGG metabolic pathway maps and additional reactions that appear only in the Enzyme Nomenclature.</li>
                <li class="note_1">MetaboLights is a database for Metabolomics experiments and derived information.</li>

            </ul>
        </div>
    </c:if>
</div>
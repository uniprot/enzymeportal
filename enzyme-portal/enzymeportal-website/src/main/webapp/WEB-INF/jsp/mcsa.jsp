<%-- 
    Document   : mcsa
    Created on : 20-Jun-2019, 16:33:23
    Author     : joseph
--%>
<style>
    .docs-js.label {
        background: #08a1b1;
        color:whitesmoke;
        margin-left: 10px;
        /*    vertical-align: sub;*/
    }
</style>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="mechanism-box">

    <div id="featured-entry">
        <div class="row">

            <c:choose>
                <c:when test="${result.reference}">
                    <c:if test="${null != enzymeModel.accession && (enzymeModel.accession ne result.referenceUniprotId)}">
                        <h3 id="proteinNameTitle"><a target="_blank" href="https://www.ebi.ac.uk/thornton-srv/m-csa/entry/${result.mcsaId}">${result.enzymeName}</a> <span class="right docs-js label badge secondary" data-tooltip tabindex="2" title="This protein is homologous to the M-CSA reference protein (Uniprot Accession : ${result.referenceUniprotId})."><a href="${pageContext.request.contextPath}/search/${result.referenceUniprotId}/enzyme">By Reference</a></span></h3>

                    </c:if>
                    <c:if test="${null != enzymeModel.accession && (enzymeModel.accession eq result.referenceUniprotId)}">
                        <h3 id="proteinNameTitle"><a target="_blank" href="https://www.ebi.ac.uk/thornton-srv/m-csa/entry/${result.mcsaId}">${result.enzymeName}</a> <span class="right docs-js label badge secondary" data-tooltip tabindex="2" title="This protein is homologous to the M-CSA reference protein (Uniprot Accession : ${result.referenceUniprotId})."><a href="#">By Reference</a></span></h3>

                    </c:if>
                    <c:if test="${null == enzymeModel.accession }">
                        <h3 id="proteinNameTitle"><a target="_blank" href="https://www.ebi.ac.uk/thornton-srv/m-csa/entry/${result.mcsaId}">${result.enzymeName}</a> <span class="right docs-js label badge secondary" data-tooltip tabindex="2" title="This protein is homologous to the M-CSA reference protein (Uniprot Accession : ${result.referenceUniprotId})."><a href="${pageContext.request.contextPath}/search/${result.referenceUniprotId}/enzyme">By Reference</a></span></h3>

                    </c:if>  

                </c:when>
                <c:otherwise>
                    <h3 id="proteinNameTitle"><a target="_blank" href="https://www.ebi.ac.uk/thornton-srv/m-csa/entry/${result.mcsaId}">${result.enzymeName}</a></h3>

                </c:otherwise>
            </c:choose>


        </div> 


        <div class="row">  <div class="text-justify summary"> ${result.description} </div></div>
        <br/>


        <c:set var="m" value="${result.reaction.mechanisms[0]}"/>

        <ul class="tabs" data-tabs id="mechanism-tabs-${result.mcsaId}">



            <c:if test="${m.isDetailed}">

                <li class="tabs-title is-active"><a href="#summary-${result.mcsaId}${m.mechanismId}" aria-selected="true">Summary</a></li> 


                <c:forEach var="step" items="${m.steps}">
                    <c:choose>
                        <c:when test="${step.isProduct}">
                            <li class="tabs-title"><a data-tabs-target="step-${result.mcsaId}${m.mechanismId}${step.stepId}" href="#step-${result.mcsaId}${m.mechanismId}${step.stepId}">Products</a></li>

                        </c:when>
                        <c:otherwise>
                            <li class="tabs-title"><a data-tabs-target="step-${result.mcsaId}${m.mechanismId}${step.stepId}" href="#step-${result.mcsaId}${m.mechanismId}${step.stepId}">Step ${step.stepId}</a></li>



                        </c:otherwise>
                    </c:choose>


                </c:forEach>



            </c:if>

        </ul> 



        <div class="tabs-content" data-tabs-content="mechanism-tabs-${result.mcsaId}">



            <div class="tabs-panel is-active"  id="summary-${result.mcsaId}${m.mechanismId}">

                <div class="row">
                    <div class="columns large-6">

                        <div class="text-justify summary">${m.mechanismText} </div>

                    </div>
                    <div class="columns large-6">

                        <strong>Catalytic Residues</strong>
                        <table style="width:100%">
                            <tr>

                                <c:if test="${not empty result.residues[0].residueSequences[0]}">
                                    <th>AA</th>
                                    <th>Uniprot</th> 
                                    <th>Uniprot Resid</th>    
                                    </c:if>
                                    <c:if test="${not empty result.residues[0].residueChains[0]}">
                                    <th>PDB </th>
                                    <th>PDB Resid</th>      
                                    </c:if>

                            </tr>
                            <c:forEach var="residue" items="${result.residues}">
                                <tr>
                                    <c:forEach var="rs" items="${residue.residueSequences}">

                                        <td>${rs.code}</td>
                                        <td>${rs.uniprotId}</td>
                                        <td>${rs.resid}</td>


                                    </c:forEach>
                                    <c:forEach var="rc" items="${residue.residueChains}">
                                        <td>${rc.pdbId}</td>
                                        <td>${rc.resid}</td>  
                                    </c:forEach>
                                </tr>
                            </c:forEach>

                        </table>



                    </div> 


                    <div class="rowX columns large-12">
                        <c:if test="${not empty m.componentsSummary}">
                            <strong>Step Components </strong>
                            <p> ${m.componentsSummary}</p>           
                        </c:if>


                    </div>
                </div>
            </div>


            <c:if test="${m.isDetailed}">


                <c:forEach var="s" items="${m.steps}">
                    <div class="tabs-panel" id="step-${result.mcsaId}${m.mechanismId}${s.stepId}"> 
                        <div class="row">
                            <div class="columns large-6">     
                                <a target="_blank" href="//${s.figure}"> <img class="feature-image-mcsa" src="//${s.figure}"/> </a>

                            </div> 
                            <div class="rowX columns large-12"> 
                                <c:choose>
                                    <c:when test="${s.isProduct eq false}">
                                        <strong>Step ${s.stepId}. </strong> 
                                        <p>${s.description}</p>
                                    </c:when>
                                    <c:when test="${s.isProduct eq true}">
                                        <strong>Products. </strong> 
                                        <p>The products of the reaction.</p>
                                    </c:when>
                                </c:choose>

                            </div>
                        </div>
                    </div> 
                </c:forEach>

            </c:if> 







        </div> 

    </div> 
</div>
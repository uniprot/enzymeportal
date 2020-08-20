<%-- 
    Document   : smallMolecules
    Created on : 24-May-2019, 15:39:50
    Author     : joseph
--%>


<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<div id="moleculeContent" class="summary">
 

    <c:set var="cofactors" value="${enzymeModel.cofactors}"/> 
    <c:set var="inhibitors" value="${enzymeModel.inhibitors}"/>
    <c:set var="activators" value="${enzymeModel.activators}"/>

    <div id="molecules">

        <div id="cofactors">

            <c:choose>
                <c:when test="${empty cofactors}">
                    <div class="noResults">No cofactors found for this enzyme! </div>
                </c:when>
                <c:otherwise>
                    <fieldset>
                        <legend>
                            Cofactors that might activate this enzyme
                        </legend>
                        <p>
                            The following compound(s) have been classified as cofactors of the enzyme. They have been associated with this enzyme, but it is uncertain whether they activate this enzyme.
                        </p>
                    </fieldset>

                    <section>
                        <div>
                            <c:forEach var="molecule" items="${cofactors}"
                                       begin="0"
                                       end="11">
                                <div class="small-molecule-container">
                                    <%@include file="molecule.jsp" %>
                                </div>
                            </c:forEach>
                        </div>
                        <c:if test="${fn:length(cofactors) >= 12}">
                            <a href="#" id="more-molecule-trigger">Show all ${fn:length(cofactors)} cofactors found...</a>
                            <div id="more-molecule-container" style="display: none">
                                <c:forEach var="molecule" items="${cofactors}"
                                           begin="12"
                                           end="${fn:length(cofactors) gt 3?
                                                  2 : fn:length(cofactors)-1}">
                                           <div class="small-molecule-container">
                                               <%@include file="molecule.jsp" %>
                                           </div>
                                </c:forEach>
                            </div>
                        </c:if>
                    </section>   


                </c:otherwise>
            </c:choose>
         
        </div>

        <div id="activators">
                   <c:choose>
                <c:when test="${empty activators}">
                    <div class="noResults">No activators found for this enzyme! </div>
                </c:when>
                <c:otherwise>
                    <fieldset>
                        <legend>
                            Activators that might activate this enzyme.
                        </legend>
                        <p>
                           The following compound(s) have been classified as activators. The compound(s) listed has(ve) been approved as treatment drugs in at least one country/area.
                        </p>
                    </fieldset>

                    <section>
                        <div>
                            <c:forEach var="molecule" items="${activators}"
                                       begin="0"
                                       end="11">
                                <div class="small-molecule-container">
                                    <%@include file="molecule.jsp" %>
                                </div>
                            </c:forEach>
                        </div>
                        <c:if test="${fn:length(activators) >= 12}">
                            <a href="#" id="more-molecule-trigger">Show all ${fn:length(activators)} activators found...</a>
                            <div id="more-molecule-container" style="display: none">
                                <c:forEach var="molecule" items="${activators}"
                                           begin="12"
                                           end="${fn:length(activators) gt 3?
                                                  2 : fn:length(activators)-1}">
                                           <div class="small-molecule-container">
                                               <%@include file="molecule.jsp" %>
                                           </div>
                                </c:forEach>
                            </div>
                        </c:if>
                    </section>   


                </c:otherwise>
            </c:choose>
        </div>

        <div id="inhibitors">
                   <c:choose>
                <c:when test="${empty inhibitors}">
                    <div class="noResults">No inhibitors found for this enzyme! </div>
                </c:when>
                <c:otherwise>
                    <fieldset>
                        <legend>
                            Inhibitors that might inhibit this enzyme
                        </legend>
                        <p>
                         The following compound(s) have been classified as inhibitors. The compound(s) listed has(ve) been approved as treatment drugs in at least one country/area.
                        </p>
                    </fieldset>

                    <section>
                        <div>
                            <c:forEach var="molecule" items="${inhibitors}"
                                       begin="0"
                                       end="11">
                                <div class="small-molecule-container">
                                    <%@include file="molecule.jsp" %>
                                </div>
                            </c:forEach>
                        </div>
                        <c:if test="${fn:length(inhibitors) >= 12}">
                            <a href="#" id="more-molecule-trigger">Show all ${fn:length(inhibitors)} inhibitors found...</a>
                            <div id="more-molecule-container" style="display: none">
                                <c:forEach var="molecule" items="${inhibitors}"
                                           begin="12"
                                           end="${fn:length(inhibitors) > 20?
                                                  15 : fn:length(inhibitors)-1}">
                                           <div class="small-molecule-container">
                                               <%@include file="molecule.jsp" %>
                                           </div>
                                </c:forEach>
                            </div>
                        </c:if>
                    </section>   


                </c:otherwise>
            </c:choose>


        </div>


        <c:if test="${not empty enzymeModel.chemblTargetId}">
            <%@include  file="chembl-target.jsp" %>  
        </c:if>  


        <c:if test="${not empty enzymeModel.chemblTargetId || not empty cofactors || not empty inhibitors || not empty activators}">
            <div class="provenance">
                <ul>
                    <li class="note_0">Data Source:
                        <a rel="noopener noreferrer" target="_blank" href="https://www.ebi.ac.uk/chebi/">ChEBI</a> &AMP; <a rel="noopener noreferrer" target="_blank" href="https://www.ebi.ac.uk/chembl/" >ChEMBL</a> </li>
                    <li class="note_1">ChEBI - (Chemical Entities of Biological Interest) is a freely available dictionary of molecular entities focused on small chemical compounds. </li>
                    <li class="note_2">ChEMBL is a database of bioactive drug-like small molecules, it contains 2-D structures, calculated properties (e.g. logP, Molecular Weight, Lipinski Parameters, etc.) and abstracted bioactivities (e.g. binding constants, pharmacology and ADMET data). </li>
                </ul>
            </div>         
        </c:if>



    </div>
</div>


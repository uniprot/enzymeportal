<%-- 
    Document   : molecules
    Created on : Aug 8, 2011, 6:18:07 PM
    Author     : hongcao
--%>

<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div id="moleculeContent" class="summary">
    <c:set var="molecules" value="${enzymeModel.molecule}"/>
    <div id="molecules">
        <c:choose>
            <c:when test="${empty molecules}">
                <p><spring:message code="label.entry.tab.empty"
                                arguments="small molecules"/></p>
            </c:when>
            <c:otherwise>
 <%--            
                <c:set var="moleculeGroup" value="${molecules.bioactiveLigands}"/>
                <c:set var="emptyArgs" value="bioactive compound"/>
                <c:set var="titleArgs" value="Bioactive compounds,bind to"/>
                <c:set var="explArgs" value="Bioactive compounds,bind to"/>
                <c:set var="moleculeGroupDb" value="ChEMBL"/>
                <c:set var="moleculeGroupUrl"
                       value="https://www.ebi.ac.uk/chembl/widget/search/target/compound_mw/uniprot:${enzymeModel.uniprotaccessions[0]}"/>
                <div id="bioactiveLigands">
                    <%@include  file="moleculeGroup.jsp" %>
                </div>

                <c:set var="moleculeGroup" value="${molecules.drugs}"/>
                <c:set var="emptyArgs" value="drug"/>
                <c:set var="titleArgs" value="Drugs,interact with"/>
                <c:set var="explArgs" value="drugs,interact with"/>
                <c:set var="moleculeGroupDb" value="UniProt"/>
                <c:set var="moleculeGroupUrl"
                       value="http://www.uniprot.org/uniprot/${enzymeModel.uniprotaccessions[0]}#section_x-ref_other"/>
                <div id="drugs">
                    <%@include  file="moleculeGroup.jsp" %>
                </div>
--%>
                <c:set var="moleculeGroup" value="${molecules.activators}"/>
                <c:set var="emptyArgs" value="activator"/>
                <c:set var="titleArgs" value="Activators,activate"/>
                <c:set var="explArgs" value="activators,activate"/>
                <%--
                <c:set var="moleculeGroupDb" value="UniProt"/>
                <c:set var="moleculeGroupUrl"
                       value="http://www.uniprot.org/uniprot/${enzymeModel.uniprotaccessions[0]}#section_comments"/>
                --%>
                <div id="activators">
                    <%@include  file="moleculeGroup.jsp" %>
                </div>

                <c:set var="moleculeGroup" value="${molecules.inhibitors}"/>
                <c:set var="emptyArgs" value="inhibitor"/>
                <c:set var="titleArgs" value="Inhibitors,inhibit"/>
                <c:set var="explArgs" value="inhibitors,inhibit"/>
                 <%--
                <c:set var="moleculeGroupDb" value="UniProt"/>
                <c:set var="moleculeGroupUrl"
                       value="http://www.uniprot.org/uniprot/${enzymeModel.uniprotaccessions[0]}#section_comments"/>
                --%>
                <div id="inhibitors">
                    <%@include  file="moleculeGroup.jsp" %>
                </div>
                
                <c:set var="moleculeGroup" value="${molecules.cofactors}"/>
                <c:set var="emptyArgs" value="cofactor"/>
                <c:set var="titleArgs" value="Cofactors,activate"/>
                <c:set var="explArgs" value="cofactors of the enzyme EC number(s),activate"/>
                <c:set var="moleculeGroupDb" value="IntEnz"/>
                <%-- FIXME: WHAT IF THE ENZYME HAS MORE THAN ONE EC NUMBER? --%>
                <c:set var="moleculeGroupUrl" value="http://www.ebi.ac.uk/intenz/query?cmd=SearchEC&ec=${enzymeModel.ec[0]}"/>
                <div id="cofactors">
                	<%@include  file="moleculeGroup.jsp" %>
                </div>
                
                <c:set var="provenance" value="${molecules.provenance}"/>
                <div class="provenance">
                    <ul>
                        <li class="note_0">Data Source:
                            <a href="http://www.ebi.ac.uk/chebi/">${provenance[0]}</a> &AMP; <a href="https://www.ebi.ac.uk/chembl/" >${provenance[1]}</a> </li>
                        <li class="note_1">${provenance[2]} </li>
                        <li class="note_2">${provenance[3]} </li>
                    </ul>
                </div>
            </c:otherwise>
        </c:choose>

    </div>
</div>

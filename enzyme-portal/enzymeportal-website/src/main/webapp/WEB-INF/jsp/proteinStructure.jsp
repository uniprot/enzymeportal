<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div style="display: none;">
<div id="loading">
<img src="${pageContext.request.contextPath}/resources/images/loading16.gif"
	alt="Loading..."/>
</div>
</div>

<div class="summary">
<c:set var="proteinStructures" value="${enzymeModel.proteinstructure}" />
<c:choose>
	<c:when test="${fn:length(enzymeModel.proteinstructure) eq 0}">
            <p class="noResults">There is no structure information available for this enzyme.</p>
	</c:when>
	<c:otherwise>

        <div class="view">



        <%-- proteinStructures --%>
    
<!--        <div class="clearfix margin-top-large"></div>-->
        
        <div class="summary structure">
            <div class="summary">
            <div class="large-12 columns row structure-view">    
         <protvista-structure hide-controls="true"  accession="${enzymeModel.accession}" ></protvista-structure>
            </div>  
            </div>
        </div>
        
            <br/>
         <div class="provenance">
        
            <ul>
                <li class="note_0">Data Source:
                    <a href="https://www.ebi.ac.uk/pdbe/pdbe-kb/proteins/${enzymeModel.accession}">PDBe</a> 
                </li>
                <li class="note_1">EMBL-EBI's Protein Data Bank in Europe (PDBe) is the European resource for the collection, organisation and dissemination of data on biological macromolecular structures. In collaboration with the other worldwide Protein Data Bank (wwPDB) partners we work to collate, maintain and provide access to the global repository of macromolecular structure data (PDB). </li>
            </ul>
        </div>
            
            
        </div>
    </c:otherwise>
</c:choose>
</div>


  <!-- Required for IE11 -->
        <script src="https://cdn.jsdelivr.net/npm/babel-polyfill/dist/polyfill.min.js" defer></script>
        <!-- Web component polyfill (only loads what it needs) -->
        <script src="https://cdn.jsdelivr.net/npm/@webcomponents/webcomponentsjs/webcomponents-lite.js" charset="utf-8" defer></script>
        <!-- Required to polyfill modern browsers as code is ES5 for IE... -->
        <script src="https://cdn.jsdelivr.net/npm/@webcomponents/webcomponentsjs/custom-elements-es5-adapter.js" charset="utf-8"
            defer></script>

<!--                    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/javascript/litemol/css/LiteMol-plugin.min.css" type="text/css" />-->
        <script src="https://cdn.jsdelivr.net/npm/protvista-structure/dist/LiteMol-plugin.min.js" charset="utf-8" defer></script>
        
     <script src="https://cdn.jsdelivr.net/npm/protvista-structure@1.0.8-alpha.0/dist/protvista-structure.js" charset="utf-8" defer></script>
        
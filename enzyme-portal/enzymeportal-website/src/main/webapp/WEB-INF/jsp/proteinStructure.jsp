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
     
<!--        <protvista-uniprot accession="P05067" config="protvista-structure-adapter" ></protvista-uniprot>-->
<!--        <protvista-structure hide-controls="true"  accession="${enzymeModel.accession}" ></protvista-structure>-->
<!-- <div>
      <protvista-uniprot accession="P05067"></protvista-uniprot>
    </div>-->

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
<!--        <script src="https://cdn.jsdelivr.net/npm/babel-polyfill/dist/polyfill.min.js" defer></script>
         Web component polyfill (only loads what it needs) 
        <script src="https://cdn.jsdelivr.net/npm/@webcomponents/webcomponentsjs/webcomponents-lite.js" charset="utf-8" defer></script>
         Required to polyfill modern browsers as code is ES5 for IE... 
        <script src="https://cdn.jsdelivr.net/npm/@webcomponents/webcomponentsjs/custom-elements-es5-adapter.js" charset="utf-8"
            defer></script>

                    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/javascript/litemol/css/LiteMol-plugin.min.css" type="text/css" />
        <script src="https://cdn.jsdelivr.net/npm/protvista-structure/dist/LiteMol-plugin.min.js" charset="utf-8" defer></script>
        
     <script src="https://cdn.jsdelivr.net/npm/protvista-structure@1.0.8-alpha.0/dist/protvista-structure.js" charset="utf-8" defer></script>
<script src="https://cdn.jsdelivr.net/npm/protvista-structure@2.2.0/dist/protvista-structure.js" charset="utf-8" defer></script>
    -->

<!--see documentation here-->
<!--https://github.com/ebi-webcomponents/nightingale/tree/master/packages/protvista-structure-->
<!--https://github.com/ebi-webcomponents/protvista-uniprot#configuration-->

<!-- Fetch polyfill and D3 library -->
<script src="https://cdn.jsdelivr.net/npm/whatwg-fetch@3.0.0/dist/fetch.umd.min.js" defer></script>
<script src="https://d3js.org/d3.v4.min.js" charset="utf-8" defer></script>

<!-- LiteMol plugin and style -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/litemol@2.4.2/dist/css/LiteMol-plugin.min.css" type="text/css"></link>
<script src="https://cdn.jsdelivr.net/npm/litemol@2.4.2/dist/js/LiteMol-plugin.min.js" defer></script>

<!-- Webcomponents polyfill for IE -->
<script src="https://cdn.jsdelivr.net/npm/@webcomponents/webcomponentsjs@2.2.10/webcomponents-bundle.js" charset="utf-8" defer></script>
<script src="https://cdn.jsdelivr.net/npm/@webcomponents/webcomponentsjs@2.2.10/custom-elements-es5-adapter.js" charset="utf-8" defer></script>

<!-- Babel polyfill / The order between this and webcomponents polyfill is important and means we can't bundle it with the main component -->
<!-- See https://github.com/babel/babel/issues/9829 -->
<script src="https://cdn.jsdelivr.net/npm/@babel/polyfill@7.4.4/dist/polyfill.min.js" charset="utf-8" defer></script>



<script src="https://cdn.jsdelivr.net/npm/protvista-uniprot@2.0.9/dist/protvista-uniprot.js" defer></script>
<!--<script src="https://cdn.jsdelivr.net/npm/protvista-structure@2.3.5/dist/protvista-structure.js" charset="utf-8" defer></script>-->

<!--<script src="https://cdn.jsdelivr.net/npm/protvista-structure@latest/dist/protvista-structure.min.js" charset="utf-8" defer></script>-->
<script src="https://cdn.jsdelivr.net/npm/protvista-structure@2.3.5/dist/protvista-structure.min.js" charset="utf-8" defer></script>



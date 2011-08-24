<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:choose>
	<c:when test="${fn:length(enzymeModel.pdbeaccession) eq 0}">
		There is no structure information available for this enzyme.
	</c:when>
	<c:otherwise>
        <c:set var="proteinStructures" value="${enzymeModel.proteinstructure}" />
		<script>
            function showStructure(pdbCode){
                var children = document.getElementById('proteinStructures').childNodes;
                for (var i = 0; i < children.length; i++){
                    if (children[i].nodeName.toLowerCase() == 'div'){
                    	children[i].style.display = 'none';
                    }
                }
                document.getElementById(pdbCode).style.display = 'block';
                return false;
            }
        </script>
        
        <div class="references">
			<div class="button">${fn:length(enzymeModel.pdbeaccession)}
				protein structure${fn:length(enzymeModel.pdbeaccession) gt 1? 's':''}
				available</div>
            <table style="display: none;">
            <c:forEach var="proteinStructure" items="${proteinStructures}" varStatus="vs">
                <tr class="${(vs.index % 2) eq 1? 'odd':'even'}"
                	onclick="return showStructure('${proteinStructure.id}');">
                    <td><a href="#">${proteinStructure.id}</a></td>
                    <td>${proteinStructure.description}</td>
                </tr>
            </c:forEach>
            </table>
            <div id="proteinStructures">
            <c:forEach var="proteinStructure" items="${proteinStructures}" varStatus="vs">
			<div class="summary structure" id="${proteinStructure.id}"
                style="display: ${vs.index eq 0? 'block':'none'}">
				<div class="summary">
					<h2>${proteinStructure.description}</td></h2>
					<div class="main_link">
						<a rel="external"
							href="http://www.ebi.ac.uk/pdbe-srv/view/entry/${proteinStructure.id}/summary">View
							in PDBe</a>
					</div>
					<div class="image">
						<a rel="external" title="Click for an interactive viewer"
							href="${proteinStructure.image.href}"><img
							alt="${proteinStructure.image.caption}"
							src="${proteinStructure.image.source}"/><span
							class="caption">${proteinStructure.image.caption}</span></a>
					</div>
					<dl>
						<dt>Description</dt>
						<dd>
							<ul>
								<li class="note_${vs.index}">${proteinStructure.description}</li>
							</ul>
						</dd>
					</dl>
                    <c:forEach var="summary" items="${proteinStructure.summary}"
                        varStatus="vsSum">
					<dl>
						<dt>${summary.label}</dt>
						<dd>
							<ul>
							<c:forEach var="note" items="${summary.note}" varStatus="vsSumNote">
								<li class="note_${vsSumNote.index}">${note}</li>
							</c:forEach>
							</ul>
						</dd>
					</dl>
                    </c:forEach>
					<div class="image wide"></div>
					<div class="provenance">
						<ul>
                        <c:forEach var="prov" items="${proteinStructure.provenance}"
                            varStatus="vsProv">
							<li class="note_${vsProv.index}">${prov}</li>
                        </c:forEach>
						</ul>
					</div>
				</div>
			</div>
            </c:forEach>
            </div><%-- proteinStructures --%>
		</div>
	</c:otherwise>
</c:choose>

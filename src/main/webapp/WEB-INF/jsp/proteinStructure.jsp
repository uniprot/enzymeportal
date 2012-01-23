<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div style="display: none;">
<div id="loading">
<img src="${pageContext.request.contextPath}/resources/images/loading.gif"
	alt="Loading..."/>
</div>
</div>

<c:set var="proteinStructures" value="${enzymeModel.proteinstructure}" />
<c:choose>
	<c:when test="${fn:length(enzymeModel.proteinstructure) eq 0}">
		There is no structure information available for this enzyme.
	</c:when>
	<c:otherwise>
		<script>
            function showStructure(pdbCode){
            	var structures = document.getElementById('proteinStructures');
                var divs = structures.childNodes;
                for (var i = 0; i < divs.length; i++){
                    if (divs[i].nodeName.toLowerCase() == 'div'){
                    	divs[i].style.display = 'none';
                    }
                }
            	var divId = '#structure-' + pdbCode;
                var strDiv = document.getElementById('structure-' + pdbCode);
                if (strDiv){
                	strDiv.style.display = 'block';
                } else {
                	var newStrDiv = document.createElement('div');
                	newStrDiv.setAttribute('id', 'structure-' + pdbCode);
                	newStrDiv.setAttribute('class', 'summary structure');
                	structures.appendChild(newStrDiv);
                	$('#loading').clone().appendTo(divId);
                	var pdbReqUrl = '${pageContext.request.contextPath}/ajax/pdbe/' + pdbCode;
                	$(divId).load(pdbReqUrl);
                }
                return false;
            }
        </script>
        
        <div class="view">
        	<c:if test="${fn:length(proteinStructures) gt 1}">
	        	<div class="references">
					<div class="button">${fn:length(proteinStructures)}
						protein structures available</div>
		            <table style="display: none;">
		            <c:forEach var="proteinStructure" items="${proteinStructures}" varStatus="vs">
		                <tr class="${(vs.index % 2) eq 1? 'odd':'even'}"
		                	onclick="return showStructure('${proteinStructure.id}');">
		                    <td><a href="#">${proteinStructure.id}</a></td>
		                    <td>${proteinStructure.name}</td>
		                </tr>
		            </c:forEach>
<%--
					<c:forEach var="pdbId" items="${enzymeModel.pdbeaccession}"
						varStatus="vs">
		                <tr class="${(vs.index % 2) eq 1? 'odd':'even'}"
		                	onclick="return showStructure('${pdbId}');">
		                    <td><a href="#">${pdbId}</a></td>
		                </tr>
					</c:forEach>
 --%>
		            </table>
	            </div>
        	</c:if>
            <div id="proteinStructures">
				<div class="summary structure" id="structure-${proteinStructures[0].id}">
					<img src="${pageContext.request.contextPath}/resources/images/loading.gif"
						alt="Loading..."/>
				</div>
<script>
$('#structure-${proteinStructures[0].id}')
	.load("${pageContext.request.contextPath}/ajax/pdbe/${proteinStructures[0].id}");
</script>
            </div><%-- proteinStructures --%>
		</div>
	</c:otherwise>
</c:choose>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:choose>
	<c:when test="${fn:length(enzymeModel.pdbeaccession) eq 0}">
		There is no structure information available for this enzyme.
	</c:when>
	<c:otherwise>
		<div class="references">
			<div class="button">${fn:length(enzymeModel.pdbeaccession)}
				protein structure${fn:length(enzymeModel.pdbeaccession) gt 1? 's':''}
				available</div>
			<c:if test="${fn:length(enzymeModel.pdbeaccession) gt 1}">
				<table style="display: none;">
				<c:forEach var="pdbCode" items="${enzymeModel.pdbeaccession}" varStatus="vs">
					<tr class="${(vs.index % 2) eq 1? 'odd':'even'}
						${pdbCode eq param.pdbCode or (empty param.pdbCode and vs.index eq 0)?
						'selected':''}">
						<td><a href="?pdbCode=${pdbCode}">${pdbCode}</a></td>
						<td><%-- TODO: here the description of the structure --%></td>
					</tr>
				</c:forEach>
				</table>
			</c:if>
			<c:set var="proteinStructure" value="${enzymeModel.proteinstructure}" />
			<div class="summary structure">
				<div class="summary">
					<h2>${proteinStructure.title}</td></h2>
					<div class="main_link">
						<a rel="external"
							href="http://www.ebi.ac.uk/pdbe-srv/view/entry/${proteinStructure.pdbCode}/summary">View
							in PDBe</a>
					</div>
					<div class="image">
						<a rel="external" title="Click for an interactive viewer"
							href="http://www.ebi.ac.uk/pdbe-srv/view/entry/${proteinStructure.pdbCode}/openastex"><img
							alt="${proteinStructure.image.link[1]}"
							src="http://www.ebi.ac.uk/pdbe-srv/view/images/entry/${proteinStructure.pdbCode}_cbc600.png"/><span
							class="caption">${proteinStructure.image.link[0]}</span></a>
					</div>
					<dl>
						<dt>Description</dt>
						<dd>
							<ul>
							<c:forEach var="desc" varStatus="vs"
								items="${proteinStructure.summary.description}">
								<li class="note_${vs.index}">${desc}</li>
							</c:forEach>
							</ul>
						</dd>
					</dl>
					<dl>
						<dt>Method</dt>
						<dd>
							<ul>
							<c:forEach var="method" varStatus="vs"
								items="">
								<li class="note_${vs.index}">${proteinStructure.summary.method}</li>
							</c:forEach>
							</ul>
						</dd>
					</dl>
					<dl>
						<dt>Experiment</dt>
						<dd>
							<ul>
								<li class="note_${vs.index}"></li>
							</ul>
						</dd>
					</dl>
					<dl>
						<dt>Dates</dt>
						<dd>
							<ul>
								<li class="note_${vs.index}"></li>
							</ul>
						</dd>
					</dl>
					<dl>
						<dt>Deposited by</dt>
						<dd>
							<ul>
								<li class="note_${vs.index}"></li>
							</ul>
						</dd>
					</dl>
					<dl>
						<dt>Primary citation</dt>
						<dd></dd>
					</dl>
					<dl>
						<dt>Chain B</dt>
						<dd></dd>
					</dl>
					<dl>
						<dt>Chain A</dt>
						<dd></dd>
					</dl>
					<dl>
						<dt>Structural domains</dt>
						<dd></dd>
					</dl>
					<div class="image wide"></div>
					<div class="provenance">
						<ul>
							<li class="note_${vs.index}"></li>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</c:otherwise>
</c:choose>

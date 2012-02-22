<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="summary structure" id="${proteinStructure.id}">
    <div class="summary">
        <h2>${proteinStructure.description}</h2>
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
             <c:set var="provenance" value="${proteinStructure.provenance}"/>
            <ul>
                <li class="note_0">
                    <a href="http://www.ebi.ac.uk/pdbe/">${provenance[0]}</a> </li>
                <li class="note_1">${provenance[1]} </li>
                <li class="note_2">${provenance[2]} </li>
            </ul>
        </div>
    </div>
</div>

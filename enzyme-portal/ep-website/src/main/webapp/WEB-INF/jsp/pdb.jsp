<%-- 
    Document   : pdb
    Created on : Feb 12, 2015, 11:44:50 AM
    Author     : joseph
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="summary structure" id="${proteinStructure.id}">
    <div class="summary">
        <h3><span>${proteinStructure.title}</span></h3>
               <div class="main_link">
            <a rel="external" target="_blank"
               href="http://www.ebi.ac.uk/pdbe-srv/view/entry/${proteinStructure.id}/summary">View
                in PDBe</a>
        </div>

        <div class="image">
            <a rel="external" title="Click for an interactive viewer" target="_blank" 
                           href="http://www.ebi.ac.uk/pdbe-srv/view/entry/${proteinStructure.id}/openastex"><img
                    alt="Ribbon structure of ${proteinStructure.id}"
                    src="http://www.ebi.ac.uk/pdbe-srv/view/images/entry/${fn:toLowerCase(proteinStructure.id)}_cbc600.png"/>
                            <span
                    class="caption">Ribbon structure of ${proteinStructure.id}</span></a>
        </div>



        <dl>
            <dt>Method</dt>
            <dd>
                <ul>
                    <c:forEach var="method" items="${proteinStructure.experimentMethod}">
                     <li class="note_${vs.index}">${method}</li>   
                    </c:forEach>
                    
                </ul>
            </dd>
        </dl>
        
        
          <dl>
            <dt>Experiment</dt>
            <dd>
                <ul>
                   <li class="note_${vs.index}">Resolution    : ${proteinStructure.resolution}&#194;</li> 
                   <li class="note_${vs.index}">R-Factor      : ${proteinStructure.rFactor}&#37;</li> 
                   <li class="note_${vs.index}">Free R-Factor : ${proteinStructure.rFree}&#37;</li> 
                   
                   
                </ul>
            </dd>
        </dl>
                   
                   
               <dl>
            <dt>Dates</dt>
            <dd>
                <ul>
                   <li class="note_${vs.index}">Deposited : ${proteinStructure.depositionDate}</li> 
                   <li class="note_${vs.index}">Released   : ${proteinStructure.releaseDate}</li> 
                   <li class="note_${vs.index}">Revised     : ${proteinStructure.revisionDate}</li> 
                   
                   
                </ul>
            </dd>
        </dl>              
                   
             <dl>
            <dt>Entry Author(s)</dt>
            <dd>
              <div class="note_${vs.index}">
                    <c:forEach var="author" items="${proteinStructure.entryAuthors}">
                     ${author.fullName}, 
                    </c:forEach>
                    
              </div>  
            </dd>
        </dl> 
                    
                    
                    <dl>
            <dt>Primary Citation</dt>
            <dd>
                
                <div class="note_${vs.index}">${proteinStructure.primaryCitation}</div> 
               <div>${proteinStructure.primaryCitationInfo}</div> 
                   
                   
                
            </dd>
        </dl> 
               
               
               <c:forEach var="entity" varStatus="vsSum" items="${proteinStructure.pdbEntities}">
                  
            <dl>
                <dt>${entity.label}</dt>
                <dd>
                    <ul>
                        <c:forEach var="mol" items="${entity.molecules}" varStatus="vsSumNote">
                            <li class="note_${vsSumNote.index}">${mol.moleculeName}</li>
                              <c:if test="${not empty mol.chemCompIds && fn:length(mol.chemCompIds) > 0}">
                                  <c:forEach var="chemCompIds" items="${mol.chemCompIds}">
                                  <li class="note_${vsSumNote.index}">Ligands: ${chemCompIds}</li>
                                  </c:forEach>
                            </c:if>
                              <c:if test="${not empty mol.length}">
                              <li class="note_${vsSumNote.index}">Residues: ${mol.length}</li>    
                             </c:if>
  
                              
                               
                        </c:forEach>
                        <c:if test="${entity.protein eq true}">
                              <li>Type: Protein</li>    
                         </c:if>    
                         <c:if test="${not empty entity.organism}">
                         <li>Organism: ${entity.organism}</li> 
                        </c:if>
                             
                    </ul>
                </dd>
            </dl>
        </c:forEach>       
               
           
               
               <c:if test="${not empty proteinStructure.structuralDomain}">
            <dl>
            <dt>Structural Domains</dt>
            <dd>
                <div class="note_${vs.index}">${proteinStructure.structuralDomain}</div> 
            </dd>
        </dl>         
               </c:if>
               
 
            
               <div class="image wide"></div>
        <div class="provenance">
             <c:set var="provenance" value="${proteinStructure.provenance}"/>
            <ul>
                <li class="note_0">
                    <a href="http://www.ebi.ac.uk/pdbe/">${provenance[0]}</a> </li>
                <li class="note_1">${provenance[1]} </li>
            </ul>
        </div>     
            
            
                
          <%--       
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
           
             --%>
            
    </div>
</div>

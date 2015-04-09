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
                   <li class="note_${vs.index}">Spacegroup : ${proteinStructure.spacegroup}</li>
                   
                   
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
            <dt>Authors</dt>
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
               
               <c:if test="${not empty proteinStructure.polypeptides}">
        
                  
            <dl>
                <dt>Polymers</dt>
                <dd>
                    <c:forEach var="peptides" varStatus="vsSum" items="${proteinStructure.polypeptides}">
                    <ul>
                        
                        <li class="note_${vsSumNote.index}">Chain: <strong>${peptides.chainId}</strong></li>
                                         
                        <li class="note_${vsSumNote.index}">Name: ${peptides.moleculeName}</li>
                                          
                       
                         <c:if test="${not empty peptides.residues}">
                             
                        <c:forEach var="residue" items="${peptides.residues}">
                        <li class="note_${vsSumNote.index}"> Residues: ${residue}</li>
                        </c:forEach>
                                 
                        </c:if> 
                         <c:if test="${not empty peptides.protein}">
                         <li>Type: Protein</li> 
                        </c:if>
                                  
                         <c:if test="${not empty peptides.organism}">
                         <li>Organism: ${peptides.organism}</li> 
                        </c:if>
                           <p></p>  
                    </ul>
                      </c:forEach> 
                </dd>
            </dl>
            
                          
               </c:if>

               
               <c:if test="${not empty proteinStructure.smallMoleculeLigands}">
             
                  
            <dl>
                <dt>Heterogens</dt>
                 <p></p>
              </dl>
                      <c:forEach var="ligand" varStatus="vsSum" items="${proteinStructure.smallMoleculeLigands}">
                    <ul style="list-style-type: square">
                        <li class="note_${vsSumNote.index}">Chain: ${ligand.chainId}</li>
                        <c:forEach var="mol" items="${ligand.molecules}" varStatus="vsSumNote">
                            
                            <li class="note_${vsSumNote.index}">Name: ${mol.moleculeName}</li>
                              <c:if test="${not empty mol.chemCompIds && fn:length(mol.chemCompIds) > 0}">
                                  <c:forEach var="chemCompIds" items="${mol.chemCompIds}">
                                  <span class="note_${vsSumNote.index}">Ligands : ${chemCompIds}</span>
                                  </c:forEach>
                                  <p></p>
                            </c:if>

                        </c:forEach>                             
                    </ul>
                     </c:forEach> 

             
               </c:if> 
           
               
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

<%-- 
    Document   : tsearch
    Created on : Nov 6, 2014, 3:23:15 PM
    Author     : joseph
--%>


<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="epfn" uri="/WEB-INF/epTagLibray.tld" %>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>

   <c:set var="pageTitle" value="Search results"/>
<%@include file="head.jspf" %>

<body class="level2 ${totalfound eq 0? 'noresults' : ''}">

 <script src="http://code.jquery.com/jquery-2.0.2.min.js"></script>

<!-- <script src="http://code.jquery.com/jquery-1.9.1.js"></script>-->
  <script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
 
<!--<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css" />-->
 
    <script>
		$(function() {
			$("#accordion").accordion({
				collapsible : true,
				active : false,
				heightStyle : "content"
			});
		});
	</script>
    
    <div id="skip-to">
            <ul>
                <li><a href="#content">Skip to main content</a></li>
                <li><a href="#local-nav">Skip to local navigation</a></li>
                <li><a href="#global-nav">Skip to EBI global navigation menu</a></li>
                <li><a href="#global-nav-expanded">Skip to expanded EBI global navigation menu (includes all sub-sections)</a></li>
            </ul>
        </div> 
    
    
    

    
             <div id="wrapper" class="container_24">

            <%@include file="header.jspf" %>
            
            <div id="content" role="main" class="grid_24 clearfix"> 
                
                       <!-- Suggested layout containers -->  
                <section >
                    <div class="grid_12zzz" style="display: table; margin-left: 0em;">
                        <%@ include file="breadcrumbs.jsp" %>
                    </div>


                </section>
                    
                    <section class="grid_6 alpha" id="search-results">
                        filter facets should be here
                    </section>
                
         <section class="grid_18" id="keywordSearchResult">       
                
<!--        <h3>enzyme portal results for ${organismName}</h3>-->
                <div  style="text-align: center;">
                    
                    <h5>showing ${page.numberOfElements} results of ${page.totalElements} results found <p style="color: red;">page ${page.number + 1} of ${page.totalPages} pages</p> </h5>
                
                </div>
        
      
<div style="width: 100%;">
             
              <div class="action-buttons">
                  <%@include file="basket-buttons.jspf" %>
              </div>
              
           
              
                                <c:url var="firstUrl" value="/taxonomy/page=1?entryid=${taxId}&entryname=${organismName}" />
                                <c:url var="lastUrl" value="/taxonomy/page=${page.totalPages}?entryid=${taxId}&entryname=${organismName}" />
                                <c:url var="prevUrl" value="/taxonomy/page=${currentIndex - 1}?entryid=${taxId}&entryname=${organismName}" />
                                <c:url var="nextUrl" value="/taxonomy/page=${currentIndex + 1}?entryid=${taxId}&entryname=${organismName}" />
              
   
                                        <c:if test="${page.totalElements gt page.size}">	


<div id="paginationNav" style="text-align: right;">

                                                <c:choose>
                                                    <c:when test="${currentIndex == 1}">
                                                       <a class="disabled" href="#">previous</a>

                                                    </c:when>
                                                    <c:otherwise>
                                                        <a id="prevButton" href="${prevUrl}">previous</a>

                                                    </c:otherwise>
                                                </c:choose>
                                                <c:forEach var="i" begin="${beginIndex}" end="${endIndex}">

                                                    <c:choose>

                                                        <c:when test="${not empty summaryEntries}">
                                                            <c:url var="pageUrl" value="/taxonomy/page=${i}?entryid=${taxId}&entryname=${organismName}" />
                                                        </c:when>
                                                    </c:choose> 


                                                    <c:choose>
                                                        <c:when test="${i == currentIndex}">
                                                            <a class="active" href="${pageUrl}"><c:out value="${i}" /></a>
                                                            </c:when>
                                                            <c:otherwise>
                                                            <a href="${pageUrl}"><c:out value="${i}" /></a>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:forEach> 
                                                    <c:choose>
                                                        <c:when test="${currentIndex == page.totalPages}">
                                                       <a class="disabled" href="#">next</a>

                                                    </c:when>
                                                    <c:otherwise>
                                                        <a id="nextButton" href="${nextUrl}">next </a>
                                                        </c:otherwise>
                                                    </c:choose>                        

                                            </div>
                                            <div class="clearfix"></div>
                                            <!-- // Pagination END -->

                                        </c:if>            
              
              
        
         <div class="clear"></div>
          <div class="line"></div>
          <div class="resultContent"> 
        
        
        <c:forEach var="enzyme" items="${summaryEntries}">
         
      <c:set var="theSpecies" value="${enzyme.relatedspecies[0]}" />
            
            <div class="resultItem grid_24">
               
             <div class="summary-header">

    <c:if test='${not empty enzyme.name }'>
        <a href="${pageContext.request.contextPath}/search/${enzyme.accession}/enzyme">
            <span class="enzymeName">${fn:substring(enzyme.proteinName, 0, 100)}</span>
            <span class="hidden">${enzyme.name}</span>
            [${empty theSpecies.species.commonname?
                theSpecies.species.scientificname :
                theSpecies.species.commonname}]
        </a>
    </c:if>
</div>   
   
<!--  protein structure              -->

<c:choose>
    <c:when test="${empty enzyme.relatedspecies}">
        <a href="#" ><span class="displayMsg" style="font-size:small;text-align:center " > No Result was found for this Selection.</span></a>
    </c:when>
   <c:when test="${not empty errorMessage}">
        <a href="#" ><span class="displayMsg" style="font-size:small;text-align:center " > No Result was found for this Selection.</span></a>
    </c:when>
    <c:otherwise>
        <div class="proteinImg grid_3">
            <c:set var="imgFile" value='${theSpecies.pdbeaccession[0]}'/>
            <c:set var="imgFooter" value=""/>
            <c:if test="${empty imgFile}">
                <c:forEach var="relSp" items="${enzyme.relatedspecies}">
                    <c:if test="${empty imgFile and not empty relSp.pdbeaccession}">
                        <c:set var="imgFile" value="${relSp.pdbeaccession[0]}"/>
                        <c:set var="imgFooter">
                            <spring:message code="label.entry.proteinStructure.other.species"/>
                            ${empty relSp.species.commonname?
                                relSp.species.scientificname : relSp.species.commonname}
                        </c:set>
                    </c:if>
                </c:forEach>
            </c:if>
            <c:choose>
                <c:when test="${empty imgFile}">
                    <div style="position: absolute; width: 110px; height: 90px;
                         background-color: #fff;text-align: center;
                         opacity: 0.6; vertical-align: middle;
                         margin-top: 0px; padding: 0px;">No structure available</div>
                    <img src="${pageContext.request.contextPath}/resources/images/noStructure-light-small.png"
                         style="border-radius: 10px;"
                         alt="No structure available"
                         title="No structure available"/>
                </c:when>
                <c:otherwise>
                    <%-- FIXME: hardcoded --%>
                    <c:set var="imgLink"
                           value="http://www.ebi.ac.uk/pdbe-srv/view/images/entry/${fn:toLowerCase(imgFile)}_cbc120.png"/>
                    <a class="noLine" style="border-bottom-style: none" target="blank" href="${imgLink}">
                        <img src="${imgLink}"
                             alt="PDB ${imgFile}" onerror="noImage(this);"/>
                    </a>
                    <div class="imgFooter">${imgFooter}</div>
                </c:otherwise>
            </c:choose>
            <c:if test='${imgFile != "" && imgFile != null}'>
            </c:if>
        </div>
    </c:otherwise>
</c:choose>


<!--protein structure ends-->
                
                
   <div class="desc grid_21">
    <c:if test="${not empty enzyme.function}">
        <div>
          
            <b>Function </b>:
            <c:choose>
                <c:when test="${fn:length(fn:split(enzyme.function, ' ')) gt searchConfig.maxTextLength}">
                    <c:forEach var="word" items="${fn:split(enzyme.function,' ')}"
                        begin="0" end="${searchConfig.maxTextLength-1}"> ${word}</c:forEach>
                    <span id="fun_${resultItemId}" style="display: none">
                        <c:forEach var="word" items="${fn:split(enzyme.function,' ')}"
                            begin="${searchConfig.maxTextLength}"> ${word}</c:forEach>
                    </span>
                    <a class="showLink" id="fun_link_${resultItemId}">... Show more about function</a>
                </c:when>
                <c:otherwise>
                    ${enzyme.function}
                </c:otherwise>
            </c:choose>
        </div>
    </c:if>
    <c:set var="synonym" value="${enzyme.synonym}"/>
    <c:set var="synonymSize" value="${fn:length(synonym)}"/>
    <c:set var="synLimitedDisplayDefault" value="${5}"/>
    <c:set var="synLimitedDisplay" value="${synLimitedDisplayDefault}"/>
    <c:if test='${synonymSize>0}'>
        <div id ="synonym">
            <b>Synonyms</b>:
            <c:if test="${synonymSize > 0 && synonymSize <= synLimitedDisplay}">
                <c:set var="synLimitedDisplay" value="${synonymSize}"/>
            </c:if>

            <c:set var="hiddenSyns" value=""/>
            <c:forEach var="i" begin="0" end="${synLimitedDisplay-1}">
                <c:out value="${synonym[i]}"/>;

            </c:forEach>
            <c:if test="${synonymSize>synLimitedDisplay}">
                <span id='syn_${resultItemId}' style="display: none">
                    <c:forEach var="i" begin="${synLimitedDisplay}" end="${synonymSize-1}">
                        <c:out value="${synonym[i]}"/>;

                    </c:forEach>
                </span>
                <a class="showLink" id="<c:out value='syn_link_${resultItemId}'/>">Show more synonyms</a>
            </c:if>
        </div>
    </c:if>

    <!-- disease begins here-->
    <c:set var="enzymeDisease" value="${enzyme.diseases}"/>
    <c:set var="enzymeDiseaseSize" value="${fn:length(enzymeDisease)}"/>
    <c:set var="disLimitedDisplayDefault" value="${5}"/>
    <c:set var="disLimitedDisplay" value="${disLimitedDisplayDefault}"/>
    
    <div id="enzymeDisease ">
        <c:if test="${ enzymeDiseaseSize >0}">
            <b>Diseases :</b>
            <c:if test="${enzymeDiseaseSize > 0 && enzymeDiseaseSize <= disLimitedDisplay}">
                <c:set var="disLimitedDisplay" value="${enzymeDiseaseSize}"/>
            </c:if>
            <c:set var="hiddenDis" value=""/>
            <c:forEach var="i" begin="0" end="${disLimitedDisplay-1}">
                <xchars:translate>
                    <span class="resultPageDisease" style="border-bottom-style:none">${enzymeDisease[i].name}</span>;
                </xchars:translate>
            </c:forEach>

            <c:if test="${enzymeDiseaseSize>disLimitedDisplay}">
                <span id='dis_${resultItemId}' style="display: none">
                    <c:forEach var="i" begin="${disLimitedDisplay}" end="${enzymeDiseaseSize-1}">
                        <xchars:translate>
                            <span class="resultPageDisease" style="border-bottom-style:none">${enzymeDisease[i].name}</span>;
                        </xchars:translate>
                    </c:forEach>
                </span>
                <a class="showLink" id="<c:out value='dis_link_${resultItemId}'/>">Show more diseases</a>
            </c:if>
        </c:if>
    </div>
          <!-- disease ends here-->

    <div>
        <div>
            <!--display = 3 = 2 related species + 1 default species -->
            <c:set var="relSpeciesMaxDisplay" value="${6}"/>
            <c:set var="relspecies" value="${enzyme.relatedspecies}"/>
            <c:set var="relSpeciesSize" value="${fn:length(relspecies)}"/>
            <c:if test="${relSpeciesSize gt 0}">
                <b>Species:</b>
                <c:if test="${relSpeciesSize <= relSpeciesMaxDisplay}">
                    <c:set var="relSpeciesMaxDisplay" value="${relSpeciesSize}"/>
                </c:if>
                <c:forEach var="i" begin="0" end="${relSpeciesMaxDisplay -1}">
                    <c:choose>
                        <c:when test="${empty relspecies[i].species.commonname}">
                            [<a class="popup" href='search/${relspecies[i].uniprotaccessions[0]}/enzyme'
                            >${relspecies[i].species.scientificname}<span>${relspecies[i].species.scientificname}</span></a
                            ><c:if test="${not empty relspecies[i].scoring}">
                                <span class="score S${
                                      relspecies[i].scoring.bitScore gt 1000? '10':
                                          fn:substringBefore(relspecies[i].scoring.bitScore/100, '.')}"
                                      title="Bit score for BLAST search. E-value = ${relspecies[i].scoring.evalue}"
                                        >${relspecies[i].scoring.bitScore}</span></c:if
                            >]
                        </c:when>
                        <c:otherwise>
                            [<a class="popup" href='search/${relspecies[i].uniprotaccessions[0]}/enzyme'
                            >${relspecies[i].species.commonname}<span>${relspecies[i].species.scientificname}</span></a
                            ><c:if test="${not empty relspecies[i].scoring}">
                            <span class="score S${
                                  relspecies[i].scoring.bitScore gt 1000? '10':
                                      fn:substringBefore(relspecies[i].scoring.bitScore/100, '.')}"
                                  title="Bit score for BLAST search. E-value = ${relspecies[i].scoring.evalue}"
                                    >${relspecies[i].scoring.bitScore}</span></c:if
                            >]
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
                <c:if test="${relSpeciesSize > relSpeciesMaxDisplay}">
                    <span id="relSpecies_${resultItemId}" style="display: none">
                        <c:forEach var = "i" begin="${relSpeciesMaxDisplay}" end="${relSpeciesSize-1}">
                            <c:choose>
                                <c:when test="${empty relspecies[i].species.commonname}">
                                    [<a class="popup" href='search/${relspecies[i].uniprotaccessions[0]}/enzyme'
                                    >${relspecies[i].species.scientificname}<span>${relspecies[i].species.scientificname}</span></a
                                    ><c:if test="${not empty relspecies[i].scoring}">
                                        <span class="score S${
                                              relspecies[i].scoring.bitScore gt 1000? '10':
                                                  fn:substringBefore(relspecies[i].scoring.bitScore/100, '.')}"
                                              title="Bit score for BLAST search. E-value = ${relspecies[i].scoring.evalue}"
                                                >${relspecies[i].scoring.bitScore}</span></c:if
                                    >]
                                </c:when>
                                <c:otherwise>
                                    [<a class="popup" href='search/${relspecies[i].uniprotaccessions[0]}/enzyme'
                                    >${relspecies[i].species.commonname}<span>${relspecies[i].species.scientificname}</span></a
                                    ><c:if test="${not empty relspecies[i].scoring}">
                                        <span class="score S${
                                              relspecies[i].scoring.bitScore gt 1000? '10':
                                                  fn:substringBefore(relspecies[i].scoring.bitScore/100, '.')}"
                                              title="Bit score for BLAST search. E-value = ${relspecies[i].scoring.evalue}"
                                                >${relspecies[i].scoring.bitScore}</span></c:if
                                    >]
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </span>
                    <a class="showLink" id="<c:out value='relSpecies_link_${resultItemId}'/>">Show more species</a>
                </c:if>
            </c:if>
        </div>
    </div>

</div>             
                
                
                
                
                
                
                
            </div>
            
           <div class="clear"></div>
<c:set var="resultItemId" value="${resultItemId+1}"/> 
            
            
            
            
        </c:forEach>
        
            </div>
            </section>
                          </div>

    <%@include file="footer.jspf" %>
    
        </div> <!--! end of #wrapper -->
    </body>
</html>

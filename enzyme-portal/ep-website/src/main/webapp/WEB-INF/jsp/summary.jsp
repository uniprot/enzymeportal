<%-- 
    Document   : summaryX
    Created on : Sep 28, 2017, 2:22:09 PM
    Author     : <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="epfn" uri="/WEB-INF/epTagLibray.tld" %>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script>
    $(document).ready(function () {
      $('#relSpecies_${resultItemId}').hide();
      $('#dis_${resultItemId}').hide();
      $('#syn_${resultItemId}').hide();
      $('#gene_${resultItemId}').hide();
      $('#fun_${resultItemId}').hide();

    });
</script>

<%--
proteinName == proteinName
enzyme.primaryAccession        ==== theSpecies.uniprotaccessions[0]
enzyme.primaryOrganism === theSpecies.species.commonname
enzyme.entryType == entryType

     enzyme.List<String> geneName;

     enzyme.PdbImage primaryImage
             - pdbId ===== theSpecies.pdbeaccession[0]
             - specie === relSp.species.commonname





      enzyme.function == = theSpecies.enzymeFunction

     enzyme.List<RelSpecies> getRelatedSpecies  ==== relatedspecies
            - accession === accession
           - commonName === species.commonname
           - scientificName === species.scientificname

--%>




<c:set var="primAcc" value="${enzyme.primaryAccession}"/>

<div class="resultItem">

<div class="summary-header">
        <c:if test="${showCheckbox != false}">
        <input type="checkbox" class="forBasket"
               title="Select entry"
               value="${epfn:getBasketId(enzyme)}"/>
    </c:if>
    <%--  Basket Bingo
    <c:if test="${showCheckbox != false}">
        <input type="checkbox" class="forBasket"
               title="Select entry"
               value="${epfn:getSummaryBasketId(enzyme)}"/>
    </c:if>
    
    --%>
    
    
    <c:if test='${not empty enzyme.proteinName }'>
        <h4>
        <a href="${pageContext.request.contextPath}/search/${primAcc}/enzyme">
            <span class="enzymeName">${fn:substring(enzyme.proteinName, 0, 100)}</span>
            [${enzyme.primaryOrganism}]
        </a>
        </h4>
            <c:choose>
                <c:when test="${enzyme.entryType eq 0}">
                    <small title="Reviewed by UniProt" class="icon-uniprot reviewed-icon" data-icon="s"></small>
                </c:when>
                <c:otherwise>
                    <small title="UniProt unreviewed" class="icon-uniprot unreviewed-icon" data-icon="t"></small>
                </c:otherwise>
            </c:choose>
    </c:if>
</div>

<div class="row>">

<c:choose>
   <c:when test="${not empty errorMessage}">
        <a href="#" ><span class="displayMsg" style="font-size:small;text-align:center " > No Result was found for this Selection.</span></a>
    </c:when>
    <c:otherwise>
        <div class="proteinImg large-2 columns">
            <c:set var="imgFile" value='${enzyme.primaryImage.pdbId}'/>
            <c:set var="imgFooter" value=""/>
            <c:set var="specieWithImage" value="${primAcc}"/>
            <c:if test="${not empty imgFile}">
                
           
                        <c:set var="imgFile" value="${enzyme.primaryImage.pdbId}"/>
                        <c:set var="specieWithImage" value="${enzyme.primaryImage.specie}"/>
                        <c:if test="${enzyme.primaryOrganism ne enzyme.primaryImage.specie}">
                            <c:set var="imgFooter">
                            <spring:message code="label.entry.proteinStructure.other.species"/>
                            ${enzyme.primaryImage.specie}
                        </c:set>  
                        </c:if>



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
                    <%-- old url
                    <c:set var="imgLink"
                           value="http://www.ebi.ac.uk/pdbe-srv/view/images/entry/${fn:toLowerCase(imgFile)}_cbc120.png"/>
                    --%>
                                        <c:set var="imgLink"
                           value="http://www.ebi.ac.uk/pdbe/static/entry/${fn:toLowerCase(imgFile)}_deposited_chain_front_image-200x200.png"/>
                    <a class="noLine" style="border-bottom-style: none" target="_blank" href="${pageContext.request.contextPath}/search/${specieWithImage}/proteinStructure">
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

<div class="desc large-10 columns">
    <c:if test="${not empty enzyme.function}">
        <div>
            <c:set var="function" value='${enzyme.function}'/>
            <b>Function </b>:
            <c:choose>
                <c:when test="${fn:length(fn:split(function, ' ')) gt searchConfig.maxTextLength}">
                    <c:forEach var="word" items="${fn:split(function,' ')}"
                        begin="0" end="${searchConfig.maxTextLength-1}"> ${word}</c:forEach>
                    <span id="fun_${resultItemId}">
                        <c:forEach var="word" items="${fn:split(function,' ')}"
                            begin="${searchConfig.maxTextLength}"> ${word}</c:forEach>
                    </span>
                    <a class="showLink" id="fun_link_${resultItemId}">... Show more about function</a>
                </c:when>
                <c:otherwise>
                    ${function}
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
            <b>Other names</b>:
            <c:if test="${synonymSize > 0 && synonymSize <= synLimitedDisplay}">
                <c:set var="synLimitedDisplay" value="${synonymSize}"/>
            </c:if>

            <c:set var="hiddenSyns" value=""/>
            <c:forEach var="i" begin="0" end="${synLimitedDisplay-1}">
                <c:out value="${synonym[i]}"/>;

            </c:forEach>
            <c:if test="${synonymSize>synLimitedDisplay}">
                <span id='syn_${resultItemId}' >
                    <c:forEach var="i" begin="${synLimitedDisplay}" end="${synonymSize-1}">
                        <c:out value="${synonym[i]}"/>;

                    </c:forEach>
                </span>
                <a class="showLink" id="<c:out value='syn_link_${resultItemId}'/>">Show more other names</a>
            </c:if>
        </div>
    </c:if>
    
    
    <c:set var="geneName" value="${enzyme.geneName}"/>
    <c:set var="geneSize" value="${fn:length(geneName)}"/>
    <c:set var="geneLimitedDisplayDefault" value="${5}"/>
    <c:set var="geneLimitedDisplay" value="${geneLimitedDisplayDefault}"/>
    <c:if test='${geneSize>0}'>
        <div id ="genename">
            <b>Gene name</b>:
            <c:if test="${geneSize > 0 && geneSize <= geneLimitedDisplay}">
                <c:set var="geneLimitedDisplay" value="${geneSize}"/>
            </c:if>

            <c:set var="hiddenGenes" value=""/>
            <c:forEach var="i" begin="0" end="${geneLimitedDisplay-1}">
                <c:out value="${geneName[i]}"/>;

            </c:forEach>
            <c:if test="${geneSize>geneLimitedDisplay}">
                <span id='gene_${resultItemId}' >
                    <c:forEach var="i" begin="${geneLimitedDisplay}" end="${geneSize-1}">
                        <c:out value="${geneName[i]}"/>;
                    </c:forEach>
                </span>
                <a class="showLink" id="<c:out value='gene_link_${resultItemId}'/>">Show more Gene names</a>
            </c:if>
        </div>
    </c:if>

   
    
    <!-- disease begins here-->
    

    <c:set var="enzymeDisease" value="${enzyme.diseases}"/>
    <c:set var="enzymeDiseaseSize" value="${fn:length(enzymeDisease)}"/>
    <c:set var="disLimitedDisplayDefault" value="${5}"/>
    <c:set var="disLimitedDisplay" value="${disLimitedDisplayDefault}"/>
     <c:set var="disLink" value="${pageContext.request.contextPath}/search/${primAcc}/diseaseDrugs/"/>

    <div id="enzymeDisease ">
        <c:if test="${ enzymeDiseaseSize >0}">

            <b>Diseases :</b>
            <c:if test="${enzymeDiseaseSize > 0 && enzymeDiseaseSize <= disLimitedDisplay}">
                <c:set var="disLimitedDisplay" value="${enzymeDiseaseSize}"/>
            </c:if>
            <c:set var="hiddenDis" value=""/>
            <c:forEach var="i" begin="0" end="${disLimitedDisplay-1}">

                <xchars:translate>
                    <span class="resultPageDisease" style="border-bottom-style:none"><a href="${pageContext.request.contextPath}/search/${primAcc}/diseaseDrugs">${enzymeDisease[i]}</a></span>;
                </xchars:translate>
            </c:forEach>

            <c:if test="${enzymeDiseaseSize>disLimitedDisplay}">
                <span id='dis_${resultItemId}' >
                    <c:forEach var="i" begin="${disLimitedDisplay}" end="${enzymeDiseaseSize-1}">
                        <xchars:translate>
                            <span class="resultPageDisease" style="border-bottom-style:none"><a href="${pageContext.request.contextPath}/search/${primAcc}/diseaseDrugs">${enzymeDisease[i]}</a></span>;
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
            <c:set var="relSpeciesMaxDisplay" value="${5}"/>
            <c:set var="relspecies" value="${enzyme.relatedSpecies}"/>
            <c:set var="relSpeciesSize" value="${fn:length(relspecies)}"/>
            <c:if test="${relSpeciesSize gt 0}">
                <b>Species:</b>
                <c:if test="${relSpeciesSize <= relSpeciesMaxDisplay}">
                    <c:set var="relSpeciesMaxDisplay" value="${relSpeciesSize}"/>
                </c:if>
                <c:forEach var="i" begin="0" end="${relSpeciesMaxDisplay-1}">
                    
                    
                        [<a class="popup" href='search/${relspecies[i].accession}/enzyme'>
                            ${relspecies[i].commonName}<span>${relspecies[i].scientificName}</span></a>]
                            
              
                            
                    <%--
                    <c:choose>
                        <c:when test="${empty relspecies[i].species.commonname}">
                            [<a class="popup" href='search/${relspecies[i].uniprotaccessions[0]}/enzyme'
                            >${relspecies[i].species.scientificname}<span>${relspecies[i].species.scientificname}</span></a
                            ><c:if test="${relspecies[i].scoring eq true}">
                                <span class="score S${
                             fn:substringBefore(relspecies[i].identity/10 , '.') }"
                                      title="Score = ${relspecies[i].score}"
                                      > ${relspecies[i].identity}&#37;</span>
                            </c:if
                            >]
                        </c:when>
                        <c:otherwise>
                            [<a class="popup" href='search/${relspecies[i].uniprotaccessions[0]}/enzyme'
                            >${relspecies[i].species.commonname}<span>${relspecies[i].species.scientificname}</span></a
                            ><c:if test="${ relspecies[i].scoring eq true}">
                            <span class="score S${
                                   fn:substringBefore(relspecies[i].identity/10 , '.') }"
                                  title="Score = ${relspecies[i].score}"
                                    > ${relspecies[i].identity}&#37;</span>


                            </c:if
                            >]
                        </c:otherwise>
                    </c:choose>
                            
                    --%>
                    
                </c:forEach>
                <c:if test="${relSpeciesSize > relSpeciesMaxDisplay}">
                    <span id="relSpecies_${resultItemId}" style="display: none">
                        <c:forEach var = "i" begin="${relSpeciesMaxDisplay}" end="${relSpeciesSize-1}">
                            <c:choose>
                                <c:when test="${empty relspecies[i].commonName}">
                                    [<a class="popup" href='search/${relspecies[i].accession}/enzyme'
                                        >${relspecies[i].scientificName}<span>${relspecies[i].scientificName}</span></a>
                                     ]
                                </c:when>
                                <c:otherwise>
                                    [<a class="popup" href='search/${relspecies[i].accession}/enzyme'
                                        >${relspecies[i].commonName}<span>${relspecies[i].scientificName}</span></a>
                                    ]
                            
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </span>
                    <a class="showLink" id="<c:out value='relSpecies_link_${resultItemId}'/>">Show more species</a>
                </c:if>
            </c:if>
        </div>
    </div>

<%--
            <c:if test="${not empty enzyme.ec}">
               <b>EC : </b>
              
                   <c:forEach var="ec" items="${enzyme.ec}">
              <span>
            <a href="${pageContext.request.contextPath}/search/ec/${ec}"><c:out value="${ec}"/></a>  
             </span>
                   </c:forEach>

               <br/>
            </c:if>
 --%>

<!--            catalytic activities-->
    <%--<c:if test="${not empty enzyme.enzymeCatalyticActivitySet && empty enzyme.function && empty enzyme.synonym}">--%>
    
    <%--
    
    <c:if test="${not empty enzyme.enzymeCatalyticActivitySet}">
        <c:set var="enzymeCatalyticActivitySize" value="${fn:length(enzyme.enzymeCatalyticActivitySet)}"/>

        <c:if test="${enzymeCatalyticActivitySize eq 1}">
           <b>Catalytic Activity: </b>
        </c:if>
        <c:if test="${enzymeCatalyticActivitySize gt 1}">
          <b>Catalytic Activities: </b>
        </c:if>

        <ul class="catalytic-activity-list">
            <c:forEach items="${enzyme.enzymeCatalyticActivitySet}" var="activity"  begin="0" end="0">
                <li class="reaction">${activity.catalyticActivity}</li>
            </c:forEach>
            <c:if test="${enzymeCatalyticActivitySize gt 1}">
                <li><a href="${pageContext.request.contextPath}/search/${primAcc}/reactionsPathways">more...</a></li>
            </c:if>
        </ul>
    </c:if>

         --%>
          
</div>
</div>

</div>
<div class="clear"></div>
<c:set var="resultItemId" value="${resultItemId+1}"/>


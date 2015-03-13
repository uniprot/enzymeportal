<%-- 
    Document   : reactionsPathways
    Created on : Aug 15, 2011, 4:03:53 PM
    Author     : hongcao
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<script language="JavaScript" type="text/javascript" src="${pageContext.request.contextPath}/resources/javascript/biojs/Biojs.js"></script>
<script language="JavaScript" type="text/javascript" src="${pageContext.request.contextPath}/resources/javascript/biojs/Biojs.Rheaction.js"></script>

<script language="JavaScript" type="text/javascript" src="${pageContext.request.contextPath}/resources/javascript/biojs/jquery-1.6.4.js"></script>

<link href=" ${pageContext.request.contextPath}/resources/javascript/biojs/biojs.Rheaction.css" rel="stylesheet" type="text/css" />
<link href=" ${pageContext.request.contextPath}/resources/javascript/biojs/biojspcviz.css" rel="stylesheet" type="text/css" />


<div id="reactionContent" class="summary">
    <h2><c:out value="${enzymeModel.name}"/></h2>
    <c:set var="reactionpathways" value="${enzymeModel.reactionpathway}"/>
     <c:set var="pathways" value="${enzymeModel.pathways}"/>
    <c:choose>
        <c:when test="${empty reactionpathways && empty pathways}">
            <p>There is no information available about reactions catalised by
                this enzyme.</p>
            </c:when>
            <c:otherwise>
                <c:if test="${fn:length(reactionpathways) > 1}">
                <div class="main_link"><b>${fn:length(reactionpathways)} reactions found</b></div>  
            </c:if>
            <c:if test="${fn:length(reactionpathways) == 1}">
                <div class="main_link"><b>${fn:length(reactionpathways)} reaction found</b></div>  
            </c:if>
            <c:forEach items="${reactionpathways}" var="reactionpathway"
                       varStatus="rpVs">
                 <c:set var="reactions" value="${reactionpathway.reactions}"/>
                <c:set var="reaction" value="${reactionpathway.reaction}"/>
                <c:set var="pathwayLinks" value="${reactionpathway.pathways}"/>
                <c:set var="pathwaysSize" value="${fn:length(pathwayLinks)}"/>

                
                    <c:if test="${reactions == null}">
                        <b><spring:message code="label.entry.reactionsPathways.found.text.alt" arguments="${pathwaysSize}"/></b>
                    </c:if>
                    <c:if test="${reactions != null}">
                        
                <c:forEach items="${reactions}" var="reaction" varStatus="loop">          
                        <c:set var="rheaEntryUrl" value="${rheaEntryBaseUrl}${reaction.id}"/>
            <div class="reaction block">            
                        <b>
                            <!--<a target="blank" href="${rheaEntryUrl}"><c:out value="${reaction.name}" escapeXml="false"/></a>-->
                            <c:out value="${reaction.name}" escapeXml="false"/> 
                        </b>
                        <br/>
                        <div id="bjs" style="margin-top: 10px">
                         
                        </div>
                        
                            <script>

                            window.onload = function() {
                            var instance = new Biojs.Rheaction({
                            target: 'bjs',
                            id:'${reaction.id}',
                            proxyUrl:'${pageContext.request.contextPath}/proxy.jsp'

                            });
                            //instance.setId("${reaction.id}");
                            //console.log(${reaction.id});
                            }; 
                            </script>                    
                        
                        
                        <%--
                        <div class="equation hidden">
                            <table>
                                <tr>
                                    <c:set var="reactants" value="${reaction.equation.reactantlist}"/>
                                    <c:set var="reactantsSize" value="${fn:length(reactants)}"/>
                                    <c:set var="products" value="${reaction.equation.productlist}"/>
                                    <c:set var="productsSize" value="${fn:length(products)}"/>
                                    <c:set var="counter" value="${1}"/>
                                    <c:forEach items="${reactants}" var="reactant">
                                        <c:if test="${reactant.count gt 1}">
                                            <th><fmt:formatNumber
                                                    value="${reactant.count}"
                                                    type="number"
                                                    maxFractionDigits="0"/>
                                            </th>
                                        </c:if>
                                        <td>
                                            <c:set var="chebiImageUrl" value="${chebiImageBaseUrl}${reactant.molecule.id}${chebiImageParams}"/>
                                            <c:set var="chebiEntryUrl" value="${chebiEntryBaseUrl}${reactant.molecule.id}${chebiEntryBaseUrlParam}"/>
                                            <a target="blank" href="${chebiEntryUrl}">
                                                <img src="${chebiImageUrl}" alt="${reactant.molecule.title}"/>
                                            </a>
                                        </td>
                                        <c:if test="${counter < reactantsSize}">
                                            <td width="1%">
                                                <b>+</b>
                                            </td>
                                        </c:if>
                                        <c:set var="counter" value="${counter+1}"/>
                                    </c:forEach>

                                    <td>
                                        <b><c:out value="${reaction.equation.direction}"/></b>
                                    </td>

                                    <c:set var="counter" value="${1}"/>
                                    <c:forEach items="${products}" var="product">
                                        <c:if test="${product.count gt 1}">
                                            <th><fmt:formatNumber
                                                    value="${product.count}"
                                                    type="number"
                                                    maxFractionDigits="0"/>
                                            </th>
                                        </c:if>
                                        <td>
                                            <c:set var="chebiImageUrl" value="${chebiImageBaseUrl}${product.molecule.id}${chebiImageParams}"/>
                                            <c:set var="chebiEntryUrl" value="${chebiEntryBaseUrl}${product.molecule.id}${chebiEntryBaseUrlParam}"/>
                                            <a target="blank" href="${chebiEntryUrl}">
                                                <img src="${chebiImageUrl}" alt="${product.molecule.title}"/>
                                            </a>
                                        </td>
                                        <c:if test="${counter < productsSize}">
                                            <td>
                                                <b>+</b>
                                            </td>
                                        </c:if>
                                        <c:set var="counter" value="${counter+1}"/>
                                    </c:forEach>
                                </tr>
                            </table>
                        </div> --%>
                        <div id="reactionDesc-${rpVs.index}">
                            <c:out value="${reaction.description}" escapeXml="false"/>
                        </div>
                        <div id="extLinks-${rpVs.index}">
                            <c:if test="${not empty reaction.xrefs}">
                                <div class="reactome inlineLinks">
                                    <a target="blank" href="${reaction.xrefs[0]}">
                                        <spring:message code="label.entry.reactionsPathways.link.reactome.reaction"/>
                                    </a>
                                </div>
                            </c:if>
                            <c:if test="${not empty reaction.id}">
                                <div class="rhea inlineLinks">
                                    <a target="blank" href="${rheaEntryUrl}">
                                        <spring:message code="label.entry.reactionsPathways.link.rhea"/>
                                    </a>
                                </div>
                            </c:if>
                            <c:set var="macielinks" value="${reactionpathway.mechanism}"/>
                            <c:if test="${fn:length(macielinks) > 0}">
                                <c:set var="macieEntryUrl" value="${macielinks[0].href}"/>
                                <div class="macie inlineLinks">
                                    <a target="blank" href="${macieEntryUrl}">
                                        <spring:message code="label.entry.reactionsPathways.link.macie"/>
                                    </a>
                                </div>
                            </c:if>

                        </div>

                    <!--                            <script>
                                                                                    
  window.onload = function() {
 var instance = new Biojs.Rheaction({
  target: 'bjs${reaction.id}',
  id:'${reaction.id}',
  proxyUrl:'${pageContext.request.contextPath}/proxy.jsp'
    
 });
 //instance.setId("${reaction.id}");
  //console.log(${reaction.id});
 }; 
                            </script>             -->
                            
                 </c:forEach>
 </div>
            </c:if>
 
            </c:forEach>
                         </div>
                <c:if test="${not empty enzymeModel.accession}">
                
                    <!-- PCViz Widget template and script code start -->
                    <div id="pcviz-widget" class="block reaction" data-uid="${enzymeModel.accession}">
                        <h3>Pathway context</h3>

                        <table class="grid">
                            <tr class="subsection">
                                <th>Pathway View</th>
                                <th>Description</th>
                            </tr>
                            <tr>
                                <td width="70%" height="500px" id="pcviz-widget-container">
                                </td>
                                <td width="30%">
                                    <div id="pcviz-description">
                                        <center>
                                            Loading pathway information from <a href="http://www.pathwaycommons.org/pc2/" target="_blank">Pathway Commons 2</a>...
                                        </center>
                                    </div>
                                </td>
                            </tr>
                        </table>

                    </div>
                    <!-- PCViz Widget template code end -->
                    <script language="JavaScript" type="text/javascript" src="${pageContext.request.contextPath}/resources/javascript/biojs/biojspcviz.js"></script>
                    <script language="JavaScript" type="text/javascript" src="${pageContext.request.contextPath}/resources/javascript/biojs/biojspcviz.custom.js"></script>
                </c:if>

                <div style="margin-top: 10px"></div>

                    <c:set var="pathwaysSize" value="${fn:length(pathways)}"/>
                  <c:set var="rpVs.index" value="${fn:length(reactionpathways)}"/>
                    <c:if test="${pathwaysSize>0}" >
                        <c:if test="${reactionpathways != null}">
                            <spring:message code="label.entry.reactionsPathways.found.text" arguments="${pathwaysSize}"/>
                        </c:if>
                        <div id="pathways-${rpVs.index}">
                                 <c:forEach var="pathway" items="${pathways}">
                               
                                <div id="pathway-${rpVs.index}-${pathway.id}">
                                    <fieldset>
                                        <legend>Loading pathway (${pathway.id})...</legend>
                                        <img src="${pageContext.request.contextPath}/resources/images/loading.gif"
                                             alt="Loading..."/>
                                    </fieldset> 
                                </div>
                                <script>
                                    if (document.getElementById('${pathway.id}')) {
                                        $('#pathway-${rpVs.index}-${pathway.id}')
                                                .html('<fieldset><legend>${pathway.id}</legend>See <a href="#${pathway.id}">above</a>.</fieldset>');
                                    } else {
                                        $('#pathway-${rpVs.index}-${pathway.id}')
                                                .load("${pageContext.request.contextPath}/ajax/reactome/${pathway.id}");
                                                        $('#pathway-${rpVs.index}-${pathway.id}')
                                                                .before('<span id="${pathway.id}"></span>')
                                                    }
                             
                                                    
                                </script>
                            </c:forEach>
                        </div>
                    </c:if>
              
          
            <div class="provenance">
                <ul>
                    <li class="note_0">Data Source:
                        <a href="http://www.reactome.org/ReactomeGWT/entrypoint.html">Reactome</a> &AMP; <a href="http://www.ebi.ac.uk/rhea/" >Rhea</a> &AMP; <a href="http://www.pathwaycommons.org/">Pathway Commons</a></li>
                    <li class="note_1">Reactome is an open-source, open access, manually curated and peer-reviewed pathway database. </li>
                    <li class="note_2">Rhea is a freely available, manually annotated database of chemical reactions created in collaboration with the Swiss Institute of Bioinformatics (SIB).All data in Rhea is freely accessible and available for anyone to use. </li>
                    <li class="note_2">Pathway Commons is a network biology resource and acts as a convenient point of access to biological pathway information collected from public pathway databases, which you can search, visualize and download. All data is freely available, under the license terms of each contributing database.</li>
                </ul>
            </div>
        </c:otherwise>
    </c:choose> 

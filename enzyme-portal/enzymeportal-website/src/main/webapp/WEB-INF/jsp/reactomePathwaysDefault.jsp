<%-- 
    Document   : reactomePathways
    Created on : Jul 19, 2018, 12:42:58 PM
    Author     : Joseph
--%>


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%--
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars"%>
--%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>




<div id="reactionContent" class="summary">



<!--                <div style="margin-top: 10px"></div>-->
 <c:set var="pathways" value="${enzymeModel.pathways}"/>
                    <c:set var="pathwaysSize" value="${fn:length(pathways)}"/>
                  <c:set var="rpVs.index" value="${fn:length(pathways)}"/>
                    <c:if test="${pathwaysSize>0}" >
                        <c:if test="${pathways != null}">
                            <spring:message code="label.entry.reactionsPathways.found.text" arguments="${pathwaysSize}"/>
                        </c:if>
                        <div id="pathways-${rpVs.index}">                       
                                 <c:forEach var="pathway" items="${pathways}">
                                   
                                <div id="pathway-${rpVs.index}-${pathway.id}">
                                    <fieldset>
                                        <legend>Loading pathway (${pathway.id})...</legend>
                                        <img src="${pageContext.request.contextPath}/resources/images/loading32.gif"
                                             alt="Loading..." class="center"/>
                                    </fieldset> 
                                </div>
                                <script>

                                        $('#pathway-${rpVs.index}-${pathway.id}')
                                                .load("${pageContext.request.contextPath}/ajax/reactome/${pathway.id}");
                                                        $('#pathway-${rpVs.index}-${pathway.id}')
                                                                .before('<span id="${pathway.id}"></span>');
                                                   
                             
                                                    
                                </script>
                            </c:forEach>
                        </div>
                    </c:if>
              
</div>
            <div class="provenance">
                <ul>
                    <li class="note_0">Data Source:
                        <a href="https://reactome.org/content/query?q=${enzymeModel.accession}">Reactome</a> &AMP; 
<!--                        <a href="https://www.pathwaycommons.org/pcviz/#neighborhood/${enzymeModel.accession}">Pathway Commons</a></li>-->
                    <li class="note_1">Reactome is an open-source, open access, manually curated and peer-reviewed pathway database. </li>

<!--                    <li class="note_2">Pathway Commons is a network biology resource and acts as a convenient point of access to biological pathway information collected from public pathway databases, which you can search, visualize and download. All data is freely available, under the license terms of each contributing database.</li>-->
                </ul>
            </div>

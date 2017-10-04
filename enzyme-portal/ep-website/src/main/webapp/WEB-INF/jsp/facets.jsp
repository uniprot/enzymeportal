<%-- 
    Document   : facets
    Created on : Oct 2, 2017, 12:25:35 PM
    Author     : <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="epfn" uri="/WEB-INF/epTagLibray.tld" %>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<div>
    
    <input type="hidden" id="filtersApplied" value="${filtersApplied}"></input>
   
    <%--
    <c:if test="${facet.id eq 'TAXONOMY'}">
        <div class="sublevel1">
            <div class="subTitle">Species</div>
            <ul>
                <c:forEach var="v" items="${facet.facetValues}">
                    <li><input id="TAXONOMY_${v.value}" name="filterFacet" value="TAXONOMY:${v.value}" type="checkbox" onChange="this.form.submit()"> ${v.label} (${v.count})</li>
                </c:forEach>
            </ul>
        </div>
    </c:if>
    
    --%>
    
                
                                    <c:if test="${facet.id eq 'TAXONOMY'}">
                                         <div class="sublevel1">
                                            <div class="subTitle">Organism</div>
                                            <ul>
                                                <li>
                                                    <input id="organismsSearch" />
                                                    <script>
                                                        var options = {
                                                            data:[
                                                                <c:forEach var="org" items="${facet.facetValues}">
                                                                {"value": "${org.label}", "taxId": "TAXONOMY:${org.value}"},
                                                                </c:forEach>
                                                                  ],
                                                            placeholder: "Search for Organism",
                                                            getValue: "value",
                                                            list: {
                                                                    match: {
                                                                        enabled: true
                                                                    },
                                                                    onClickEvent: function() {
                                                                        console.log("just clicked in the autocomplete didnt ya?!");
                                                                        var value = $("#organismsSearch").getSelectedItemData().taxId;
                                                                        //alert("value baby: "+value);
                                                                        $("#auto-complete-holder").val(value).trigger("change");
                                                                        
                                                                        var bingo = $("#auto-complete-holder").val();
                                                                        alert("bingo baby: "+bingo);
                                                                        
                                                                        
                                                                        
                                                                        var bingo2 = $("#filtersApplied").val();
                                                                       // alert("filtersApplied baby: "+bingo2);
                                                                        //$("#auto-complete-holder").appendTo("#facetFilterForm");
                                                                       
                                                                    var newone = document.createElement("input");
                                                                        newone.setAttribute('type', 'hidden');
                                                                        newone.setAttribute('value', 'BOYO!');
                                                                        newone.setAttribute('id', 'newone1');
                                                                        
                                                                        
                                                                        $("#newone1").appendTo("#facetFilterForm");
                                                                       
                                                                       
                                                                        
                                                                     alert($("#facetFilterForm").serialize());
                                                                        //$("#facetFilterForm").submit();
                                                                        
                                                              
                                                                        
                                                                        
                                                                    }
                                                                }
                                                        };
                                                        $("#organismsSearch").easyAutocomplete(options);
                                                    </script>
<%--
                                                    <div class="filterContent">
                                                         <c:set var="speciesList" value="${facet.facetValues}"/>
                                                         <c:set var="speciesListSize" value="${fn:length(facet.facetValues)}"/>
                                                         <c:set var="speciesParams" value="${searchModel.searchparams.species}"/>
                                                    </div>
    
--%>

                                                    <input type="hidden" id="auto-complete-holder" name="filterFacet2" ></input>
                                                    

                                                    
                                                    
                                                </li>

                                                <div id="organismList">
                                                <c:set var="facetSize" value="${fn:length(facet.facetValues)}"/>
                                                   <c:forEach var="v" items="${facet.facetValues}">
                                                      <li><input id="TAXONOMY_${v.value}" name="filterFacet" value="TAXONOMY:${v.value}" type="checkbox" onChange="this.form.submit()"> ${v.label} (${v.count})</li>
                                                   </c:forEach>
                                                </div>
                                             </ul>
                                         </div>
                                     </c:if>
                                    
                                         
    
    
    
    
    
<!-- Note : cofactor, inhibitors and activators should all be under Compound    <h3>Compound</h3>-->
    <c:if test="${facet.id eq 'cofactor'}">
        <div class="sublevel1">
            <div class="subTitle">Cofactors</div>
            <ul>
                <c:forEach var="v" items="${facet.facetValues}">
                    <li><input id="cofactor_${v.value}" name="filterFacet" value="cofactor:${v.value}" type="checkbox" onChange="this.form.submit()"> ${v.label} (${v.count})</li>
                </c:forEach>
            </ul>
        </div>
    </c:if>

    <c:if test="${facet.id eq 'inhibitor'}">
        <div class="sublevel1">
            <div class="subTitle">Inhibitors</div>
            <ul>
                <c:forEach var="v" items="${facet.facetValues}">
                    <li><input id="inhibitor_${v.value}" name="filterFacet" value="inhibitor:${v.value}" type="checkbox" onChange="this.form.submit()"> ${v.label} (${v.count})</li>
                </c:forEach>
            </ul>
        </div>
    </c:if>
    <c:if test="${facet.id eq 'activator'}">
        <div class="sublevel1">
            <div class="subTitle">Activators</div>
            <ul>
                <c:forEach var="v" items="${facet.facetValues}">
                    <li><input id="activator_${v.value}" name="filterFacet" value="activator:${v.value}" type="checkbox" onChange="this.form.submit()"> ${v.label} (${v.count})</li>
                </c:forEach>
            </ul>
        </div>
    </c:if>
    <c:if test="${facet.id eq 'OMIM'}">
        <div class="sublevel1">
            <div class="subTitle">Diseases</div>
            <ul>
                <c:forEach var="v" items="${facet.facetValues}">
                    <li><input id="OMIM_${v.value}" name="filterFacet" value="OMIM:${v.value}" type="checkbox" onChange="this.form.submit()"> ${v.label} (${v.count})</li>
                </c:forEach>
            </ul>
        </div>
    </c:if>

    <c:if test="${facet.id eq 'enzyme_family'}">
        <div class="sublevel1">
            <div class="subTitle">Enzyme Family</div>
            <ul>
                <c:forEach var="v" items="${facet.facetValues}">
                    <li><input id="enzyme_family_${v.value}" name="filterFacet" value="enzyme_family:${v.value}" type="checkbox" onChange="this.form.submit()"> ${v.label} (${v.count})</li>
                </c:forEach>
            </ul>
        </div>
    </c:if>
</div>
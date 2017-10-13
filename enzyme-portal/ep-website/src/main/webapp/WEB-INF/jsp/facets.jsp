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
                            data: [
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
                                onClickEvent: function () {
                                    var value = $("#organismsSearch").getSelectedItemData().taxId;
                                    var input = jQuery('<input type="hidden" name="filterFacet" id="auto-complete-holder">');
                                    input.val(value).trigger("change");
                                    jQuery('#facetFilterForm').append(input);
                                    $("#facetFilterForm").submit();
                                }
                            }
                        };
                        $("#organismsSearch").easyAutocomplete(options);
                    </script>

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
                <div id="cofactorsList">
                <c:forEach var="v" items="${facet.facetValues}">
                    <li><input id="cofactor_${v.value}" name="filterFacet" value="cofactor:${v.value}" type="checkbox" onChange="this.form.submit()"> ${v.label} (${v.count})</li>
                    </c:forEach>
                </div>
            </ul>
        </div>
    </c:if>

    <c:if test="${facet.id eq 'inhibitor'}">
        <div class="sublevel1">
            <div class="subTitle">Inhibitors</div>
            <ul>
              <div id="inhibitorsList">
                <c:forEach var="v" items="${facet.facetValues}">
                    <li><input id="inhibitor_${v.value}" name="filterFacet" value="inhibitor:${v.value}" type="checkbox" onChange="this.form.submit()"> ${v.label} (${v.count})</li>
                    </c:forEach>
              </div>
            </ul>
        </div>
    </c:if>
    <c:if test="${facet.id eq 'activator'}">
        <div class="sublevel1">
            <div class="subTitle">Activators</div>
            <ul>
                <div id="activatorsList">
                <c:forEach var="v" items="${facet.facetValues}">
                    <li><input id="activator_${v.value}" name="filterFacet" value="activator:${v.value}" type="checkbox" onChange="this.form.submit()"> ${v.label} (${v.count})</li>
                    </c:forEach>
                </div>
            </ul>
        </div>
    </c:if>

    <c:if test="${facet.id eq 'OMIM'}">
        <div class="sublevel1">
            <div class="subTitle">Diseases</div>
            <ul>
                <c:if test="${fn:length(facet.facetValues) > 10}">
                    <li>
                        <input id="diseasesSearch" />
                        <script>
                            var options = {
                            data: [
                            <c:forEach var="dis" items="${facet.facetValues}">
                            {"value": "${dis.label}", "disId": "OMIM:${dis.value}"},
                            </c:forEach>
                            ],
                                    placeholder: "Search for Disease",
                                    getValue: "value",
                                    list: {
                                    match: {
                                    enabled: true
                                    },
                                            onClickEvent: function () {
                                            var value = $("#diseasesSearch").getSelectedItemData().disId;
                                            var input = jQuery('<input type="hidden" name="filterFacet" id="auto-complete-holder">');
                                            input.val(value).trigger("change");
                                            jQuery('#facetFilterForm').append(input);
                                            $("#facetFilterForm").submit();
                                            }
                                    }
                            };
                            $("#diseasesSearch").easyAutocomplete(options);
                        </script>
                    </li>
                </c:if>

                <div id="diseasesList">
                    <c:forEach var="v" items="${facet.facetValues}">
                        <li><input id="OMIM_${v.value}" name="filterFacet" value="OMIM:${v.value}" type="checkbox" onChange="this.form.submit()"> ${v.label} <span class="facetCount">(${v.count})</span></li>
                        </c:forEach>
                </div>
            </ul>
        </div>
    </c:if>

    <c:if test="${facet.id eq 'enzyme_family'}">
        <div class="sublevel1">
            <div class="subTitle">Enzyme Family</div>
            <ul>
                <div id="enzymeList">
                <c:forEach var="v" items="${facet.facetValues}">
                    <li><input id="enzyme_family_${v.value}" name="filterFacet" value="enzyme_family:${v.value}" type="checkbox" onChange="this.form.submit()"> ${v.label} (${v.count})</li>
                    </c:forEach>
                </div>
            </ul>
        </div>
    </c:if>
</div>
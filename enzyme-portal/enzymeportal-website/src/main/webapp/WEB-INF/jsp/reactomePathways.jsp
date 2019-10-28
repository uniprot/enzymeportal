<%-- 
    Document   : reactomePathways
    Created on : 19-Jun-2019, 11:17:13
    Author     : joseph
--%>

<!doctype html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="pageTitle" value="Entry page"/>
<%@include file="head.jspf" %>

<body class="level2 full-width">

    <div id="wrapper">

        <%@include file="header.jspf" %>

        <div id="content" role="main" class="clearfix">
            <%@ include file="breadcrumbs.jsp" %>

            <c:set var="selectedSpecies" value="${enzymeModel.species}" />
            <c:set var="relSpecies" value="${enzymeModel.relatedspecies}"/>
            <c:set var="enzyme" value="${enzymeModel.enzyme}"/>

            <c:set var="requestedfield" value="pathways"/>

            <%@include file="relspecies.jsp" %>



            <div class="clearfix"></div>
            <div class="gradient">
                <div wicket:id="reference" class="row content">
                    <div class="large-3 columns column1">
                        <ul class="no-bullet">
                            <li id="enzyme" class="protein">
                                <a href="enzyme" class="tab">
                                    <span class="labelEP icon icon-conceptual" data-icon="P">
                                        Protein Summary
                                    </span>
                                </a>
                            </li>
                            <li id="structure"  class="structure" >
                                <a href="proteinStructure" class="tab">
                                    <span class="labelEP icon icon-conceptual icon-c4" data-icon="s">
                                        Protein Structure
                                    </span>
                                </a>
                            </li>
                            <li id="reaction" class="reaction">

                                <a href="reactionsMechanisms" class="tab">

                                    <span class="labelEP icon icon-conceptual" data-icon="y">
                                        Reactions & Mechanisms

                                    </span>
                                </a>
                            </li>
                            <li id="pathway" class="pathway">

<!--                                    <a href="${pageContext.request.contextPath}/search/${enzymeModel.accession}/pathways" class="tab ${pathwaysSelected}">
                                        <span class="labelEP icon icon-conceptual" data-icon="y">
                                            Pathways

                                        </span>
                                    </a>-->

                                <a href="pathways" class="tab selected">
                                    <span class="labelEP icon icon-conceptual" data-icon="y">
                                        Pathways

                                    </span>
                                </a>

                            </li>

                            <li id="molecule" class="molecule">
                                <a href="molecules" class="tab ">
                                    <span class="labelEP icon icon-conceptual" data-icon="b">
                                        Small Molecules
                                    </span>
                                </a>
                            </li>
                            <li id="disease" class="disease">
                                <a href="diseaseDrugs" class="tab">
                                    <span class="labelEP icon icon-species" data-icon="v">
                                        Diseases
                                    </span>
                                </a>
                            </li>
                            <li id="literature" class="literature">
                                <a href="literature" class="tab">
                                    <span class="labelEP icon icon-conceptual" data-icon="l">
                                        Literature
                                    </span>
                                </a>
                            </li>
                        </ul>
                    </div>
                    <div class="large-9 columns column2">    

                        <div id="reactionContent" class="summary">

                            <c:set var="pathways" value="${enzymeModel.pathways}"/>
                            <c:set var="pathwaysSize" value="${fn:length(pathways)}"/>
                            <c:set var="rpVs.index" value="${fn:length(pathways)}"/>
                            <c:choose>
                                <c:when test="${pathwaysSize>0}" >
                                    <c:if test="${pathways != null}">

                                        <span>This enzyme might be present in ${pathwaysSize} pathway(s);</span>
                                    </c:if>
                                    <div id="pathways-${rpVs.index}">  

                                        <div id="wait">
                                            <fieldset>
                                                <legend>Loading (${pathwaysSize}) pathways ...</legend>
                                                <img src="${pageContext.request.contextPath}/resources/images/loading32.gif"
                                                     alt="Loading..." class="center"/>
                                            </fieldset> 
                                        </div>   




                                        <script>

                                            $(document).ajaxStart(function () {
                                                $("#wait").css("display", "block");
                                            });

                                            $(document).ajaxComplete(function () {
                                                $("#wait").css("display", "none");
                                            });


                                            $.ajax({
                                                type: "GET",
                                                url: "${pageContext.request.contextPath}/ajax/reactome/${enzymeModel.accession}",

                                                        success: function (data)
                                                        {


                                                            $.each(data, function (i, result)
                                                            {




                                                                var fieldset = $("<fieldset>");
                                                                var legend = $("<legend>");
                                                                legend.text(result.name);
                                                                fieldset.append(legend);
                                                                var descDiv = $("<div>");
                                                                descDiv.addClass("marginTop0.5");
                                                                descDiv.text(result.description);
                                                                fieldset.append(descDiv);

                                                                if (result.image !== null) {
                                                                    var imageDiv = $("<div>");
                                                                    var imageLink = document.createElement('a');
                                                                    imageLink.setAttribute("href", result.url);
                                                                    imageLink.setAttribute("target", "blank");
                                                                    var image = document.createElement("img");
                                                                    image.setAttribute("src", result.image);

                                                                    image.setAttribute("alt", result.id);
                                                                    imageLink.appendChild(image);
                                                                    imageDiv.append(imageLink);


                                                                    fieldset.append(imageDiv);
                                                                }




                                                                var linkDiv = $("<div>");
                                                                linkDiv.addClass("inlineLinks");

                                                                var rl = document.createElement("A");
                                                                var t = document.createTextNode("View pathway in Reactome");
                                                                rl.setAttribute("href", result.url);
                                                                rl.setAttribute("target", "_blank");
                                                                rl.appendChild(t);



                                                                linkDiv.append(rl);

                                                                fieldset.append(linkDiv);

                                                                //fieldset.append(loadingDiv);

                                                                $("#data").append(fieldset);


                                                            });

                                                        }


                                                    });

                                        </script>




                                        <div id="data"></div>
                                    </div>

                                    <div class="provenance">
                                        <ul>
                                            <li class="note_0">Data Source:
                                                <a href="https://reactome.org/content/query?q=${enzymeModel.accession}">Reactome</a> 
                                            <li class="note_1">Reactome is an open-source, open access, manually curated and peer-reviewed pathway database. </li>

                                        </ul>
                                    </div>
                                </c:when>

                                <c:otherwise>
                                    <span class="noResults">There is no pathway information available for this enzyme.</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>        <!--       end large-9 col-->
                    <div class="clearfix"></div>
                </div>

            </div>

            <%@include file="footer.jspf" %>

        </div> <!--! end of #wrapper -->
    </div>

</body>
</html>


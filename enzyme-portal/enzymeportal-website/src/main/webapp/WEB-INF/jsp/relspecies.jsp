<%-- 
    Document   : relspecies
    Created on : 19-Jun-2019, 12:17:40
    Author     : joseph
--%>

                <section>
                    <div class="header row">


                        <div class="row" id="title-row">
                            <section class="large-3 columns">
                                <img src="/enzymeportal/resources/images/protein_page_logo2.png">
                            </section>
                            <section class="large-9 columns">
                                <%--
                               <h2><c:out value="${enzymeModel.name}"/> </h2>
                                --%>
                                <c:choose>
                                    <c:when test="${enzymeModel.entryType eq 0}">             
                                        <h2 title="Reviewed by UniProt" class="icon-uniprot reviewed-icon" data-icon="s"><c:out value="${enzymeModel.proteinName}"/> </h2>
                                    </c:when>
                                    <c:otherwise>
                                        <h2 title="UniProt unreviewed" class="icon-uniprot unreviewed-icon" data-icon="t"><c:out value="${enzymeModel.proteinName}"/> </h2>
                                    </c:otherwise>
                                </c:choose>

                                <div class="entry-buttons">
                                    <c:if test="${empty basket ||empty basket[enzymeModel.accession]}">
                                        <input type="hidden" id="enzymeId" value="${enzymeModel.accession}"/>
                                        <a id="add-to-basket" href="#" class="icon icon-generic btn" data-icon="b"> Add to Basket</a>
                                        <script>
                                            $('#add-to-basket').click(function () {
                                                ajaxBasket($('#enzymeId').val(), true);
                                                $(this).hide();
                                            });
                                        </script>
                                    </c:if>
                                    <strong>&nbsp;&nbsp;Organism:</strong>
                                    <div class="panel">
                                        <div class="classification">
                                            <div class='box selected ${fn:replace(selectedSpecies.scientificname, " ", "_")}'>
                                                <span class="name"><c:out value="${selectedSpecies.commonname}"/></span>
                                                <span class="extra"
                                                      title="${selectedSpecies.scientificname}"
                                                      style="overflow: hidden;">${selectedSpecies.scientificname}</span>
                                            </div>
                                        </div>
                                        <div class="selection">
                                            <ul>
                                                <c:if test="${fn:length(relSpecies)<=0}">
                                                    <c:forEach begin="0" end="${fn:length(relSpecies)}" var="i">
                                                        <c:set var="species" value="${relSpecies[i].species}"/>
                                                        <c:set var="select" value=""/>
                                                        <c:if test="${i==0}">
                                                            <c:set var="select" value="selected"/>
                                                        </c:if>
                                                        <li class="${select}">
                                                            <a href="../${relSpecies[i].uniprotaccessions[0]}/${requestedfield}">
                                                                <div class='box ${fn:replace(species.scientificname, " ", "_")}'>
                                                                    <span class="name"><c:out value="${species.commonname}"/></span>
                                                                    <span class="extra"
                                                                          title="${species.scientificname}"
                                                                          style="overflow: hidden;">${species.scientificname}</span>
                                                                </div>
                                                            </a>
                                                        </li>
                                                    </c:forEach>
                                                </c:if>
                                                <c:if test="${fn:length(relSpecies)>0}">
                                                    <c:forEach begin="0" end="${fn:length(relSpecies)-1}" var="i">
                                                        <c:set var="species" value="${relSpecies[i].species}"/>
                                                        <c:set var="select" value=""/>
                                                        <c:if test="${i==0}">
                                                            <c:set var="select" value="selected"/>
                                                        </c:if>
                                                        <li class="${select}">
                                                            <a href="../${relSpecies[i].accession}/${requestedfield}">
                                                                <div class='box ${fn:replace(species.scientificname, " ", "_")}'>
                                                                    <span class="name"><c:out value="${species.commonname}"/></span>
                                                                    <span class="extra"
                                                                          title="${species.scientificname}"
                                                                          style="overflow: hidden;">${species.scientificname}</span>
                                                                </div>
                                                            </a>
                                                        </li>
                                                    </c:forEach>
                                                </c:if>
                                            </ul>
                                        </div>
                                    </div>


                                </div>


                            </section>
                        </div>






                    </div>
                </section>
<%--
    Document   : index
    Created on : Sep 3, 2012, 12:11:34 PM
    Author     : joseph
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!doctype html>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!-- paulirish.com/2008/conditional-stylesheets-vs-css-hacks-answer-neither/ -->
<!--[if lt IE 7]> <html class="no-js ie6 oldie" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js ie7 oldie" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js ie8 oldie" lang="en"> <![endif]-->
<!-- Consider adding an manifest.appcache: h5bp.com/d/Offline -->
<!--[if gt IE 8]><!-->
<html class="no-js" lang="en">
    <!--<![endif]-->

    <c:set var="pageTitle" value="Home &lt; Enzyme Portal &lt; EMBL-EBI"/>
    <%@include file="head.jspf" %>

    <body class="level2">
        <!-- add any of your classes or IDs -->
        <%@include file="skipto.jspf" %>

        <div id="wrapper" class="">

            <%@include file="header-home.jspf" %>

            <div id="wrapper" class="container_24">
                <div id="content" role="main" class="clearfix">

                    <div class="row">
                        <!-- <div class="large-2 columns">
                            <img src="/enzymeportal/resources/images/enzyme_page_logo.jpg">
                            </div> -->

                        <section class="large-12 columns search-panel">
                            <h1>Search Enzymes</h1>

                            <div id="homepage-search" >
                                <c:if test="${not fn:containsIgnoreCase(pageTitle, 'Advanced Search')}">
                                    <%@ include file="frontierSearchBox.jsp" %>
                                </c:if>


                        </section>
                        <section class=" large-10 large-offset-1 ">
                            Enzyme Portal integrates publicly available information about enzymes,
                            such as reaction mechanism, small-molecule chemistry, biochemical pathways and drug compounds.
                        </section>

                    </div>
                    <%--
            <div class="row highlight-panel large-12">
                <h1>Analyse data</h1>
                <a href="https://www.ebi.ac.uk/thornton-srv/transform-miner/" target="_blank">
                    <div class="primary-tile">
                        <h2>Transform-MinER</h2>
                        <div>
                            <img src="/enzymeportal/resources/images/transform.png"><br/>
                            <div class="tile-text">
                                Transforms molecules to find enzyme matches for novel substrate
                            </div>
                        </div>
                    </div>
                </a>
                <a href="https://www.ebi.ac.uk/Tools/services/web/toolform.ebi?tool=ncbiblast&database=enzymeportal" target="_blank">
                    <div class="primary-tile">
                        <h2>Sequence search</h2>
                        <div>
                            <img src="/enzymeportal/resources/images/sequence.png"><br/>
                            <div class="tile-text">
                                Run a sequence search against known enzymes
                            </div>
                        </div>
                    </div>
                </a>
                <a href="/enzymeportal/basket">
                    <div class="primary-tile">
                        <h2>Compare enzymes</h2>
                        <div>
                            <img src="/enzymeportal/resources/images/compare.png"><br/>
                            <div class="tile-text">
                                Compare two enzymes; function, sequence, structure
                            </div>
                        </div>
                    </div>
                </a>

                    </div>
                    --%>
                    <div class="row highlight-panel browse-section large-12">
                        <div class="row">
                            <h1>Browse enzymes</h1>
                            <a href="browse/enzymes">
                                <div class="primary-tile">
                                    <h2>By enzyme classification</h2>
                                    <div class="tile-text">
                                        <img src="/enzymeportal/resources/images/browseby/enzyme.png"><br/>
                                    </div>
                                </div>
                            </a>
                            <a href="browse/disease">
                                <div class="primary-tile">
                                    <h2>By diseases</h2>
                                    <div class="tile-text">
                                        <img src="/enzymeportal/resources/images/browseby/diseases.png"><br/>
                                    </div>
                                </div>
                            </a>
                            <a href="browse/taxonomy">
                                <div class="primary-tile">
                                    <h2>By taxonomy</h2>
                                    <div class="tile-text">
                                        <img src="/enzymeportal/resources/images/taxon.png"><br/>
                                    </div>
                                </div>
                            </a>
                        </div>
                        <div class="row">
                            <a href="browse/pathways">
                                <div class="primary-tile">
                                    <h2>By pathways</h2>
                                    <div class="tile-text">
                                        <img src="/enzymeportal/resources/images/pathways.png"><br/>
                                    </div>
                                </div>
                            </a>
                            <a href="browse/families">
                                <div class="primary-tile">
                                    <h2>By enzyme families</h2>
                                    <div class="tile-text">
                                        <img src="/enzymeportal/resources/images/browseby/protein_family.png"><br/>
                                    </div>
                                </div>
                            </a>

                            <a href="browse/cofactors">
                                <div class="primary-tile">
                                    <h2>By cofactors</h2>
                                    <div class="tile-text" >
                                        <img src="/enzymeportal/resources/images/browseby/cofactor.png"><br/>
                                    </div>

                                </div>
                            </a>

                            <a href="browse/metabolites">
                                <div class="primary-tile">
                                    <h2>By metabolites</h2>
                                    <div class="tile-text" >
                                        <img src="/enzymeportal/resources/images/browseby/cofactor.png"><br/>
                                    </div>

                                </div>
                            </a>       

                        </div>
                    </div>
                    <div class="row highlight-panel large-12">
                        <h1>Analyse data</h1>
                        <a rel="noopener noreferrer" href="https://www.ebi.ac.uk/thornton-srv/transform-miner/" target="_blank">
                            <div class="primary-tile">
                                <h2>Transform-MinER</h2>
                                <div>
                                    <img src="/enzymeportal/resources/images/transform.png"><br/>
                                    <div class="tile-text">
                                        Transforms molecules to find enzyme matches for novel substrate
                                    </div>
                                </div>
                            </div>
                        </a>
                        <a rel="noopener noreferrer" href="https://www.ebi.ac.uk/Tools/services/web/toolform.ebi?tool=ncbiblast&database=enzymeportal" target="_blank">
                            <div class="primary-tile">
                                <h2>Sequence search</h2>
                                <div>
                                    <img src="/enzymeportal/resources/images/sequence.png"><br/>
                                    <div class="tile-text">
                                        Run a sequence search against known enzymes
                                    </div>
                                </div>
                            </div>
                        </a>
                        <a href="/enzymeportal/basket">
                            <div class="primary-tile">
                                <h2>Compare enzymes</h2>
                                <div>
                                    <img src="/enzymeportal/resources/images/compare.png"><br/>
                                    <div class="tile-text">
                                        Compare two enzymes; function, sequence, structure
                                    </div>
                                </div>
                            </div>
                        </a>

                    </div>
                    <div class="row highlight-panel large-12">

                        <div class="technical-tile">
                            <h1>About Enzyme Portal</h1>
                            <div class="tile-text">
                                The Enzyme Portal integrates publicly available information about enzymes, such as reaction mechanism, small-molecule chemistry, biochemical pathways and drug compounds.
                                <br>
                                <a rel="noopener noreferrer" target="_blank" href="https://www.youtube.com/channel/UCLXd9kRfdlSVLx9Snsi1qIA">
                                    <img class="thumbnail" src="/enzymeportal/resources/images/youtube2.png">
                                </a>
                                <p><a href="about">more...</a></p>
                            </div>                          
                        </div>

                        <div class="technical-tile">
                            <h1>Technical documents</h1>
                            <div>
                                <div class="tile-text">
                                    <p>The Enzyme Portal is an open source project developed at the EMBL-EBI and the source code is freely available and can be downloaded from <a href="https://github.com/computingfacts/enzymeportal" target="_blank" rel="noopener noreferrer">GitHub</a>,
                                        an online project hosting service.</p>

                                    <p>Additionally, the technical documentation (Javadoc, project information) is also available online.</p>
                                </div>
                            </div>
                        </div>

                        <div class="technical-tile">
                            <h1>Enzyme Portal resources</h1>
                            <div>
                                <div class="tile-text">
                                    <p>To build its reports, the Enzyme Portal uses data from
                                        <a rel="noopener noreferrer" href="http://www.uniprot.org" target="_blank">UniProtKB</a>,
                                        <a rel="noopener noreferrer" href="http://www.ebi.ac.uk/pdbe/" target="_blank">PDBe</a>,
                                        <a rel="noopener noreferrer" href="https://www.ebi.ac.uk/chembl/" target="_blank">ChEMBL</a>,
                                        <a rel="noopener noreferrer" href="http://www.ebi.ac.uk/chebi/" target="_blank">ChEBI</a>,
                                        <a rel="noopener noreferrer" href="http://www.reactome.org/" target="_blank">REACTOME</a>,
                                        <a rel="noopener noreferrer" href="http://www.ebi.ac.uk/rhea/" target="_blank">Rhea</a>,
                                        <a rel="noopener noreferrer" href="http://www.ebi.ac.uk/intenz/" target="_blank">IntEnz</a>
                                        and many more resources.</p>
                                    <p>
                                        <a href="about">more...</a>
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div id="content" role="main" class="row">
                <%@include file="footer.jspf" %>
            </div>
            <!--! end of #wrapper -->
        </div>
        <script src="resources/javascript/search.js" type="text/javascript"></script>
        <%--<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>--%>

    </body>
</html>

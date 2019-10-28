<%--
    Document   : about
    Created on : Sep 5, 2012, 2:18:11 PM
    Author     : joseph
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<!-- paulirish.com/2008/conditional-stylesheets-vs-css-hacks-answer-neither/ -->
<!--[if lt IE 7]> <html class="no-js ie6 oldie" lang="en"> <![endif]-->
<!--[if IE 7]> <html class="no-js ie7 oldie" lang="en"> <![endif]-->
<!--[if IE 8]> <html class="no-js ie8 oldie" lang="en"> <![endif]-->
<!-- Consider adding an manifest.appcache: h5bp.com/d/Offline -->
<!--[if gt IE 8]><!-->
<html class="no-js" lang="en"> <!--<![endif]-->
    <c:set var="pageTitle" value="About Us &lt; Enzyme Portal &gt; EMBL-EBI"/>
    <%@include file="head.jspf" %>

    <body class="level2 full-width"><!-- add any of your classes or IDs -->
        <%@include file="skipto.jspf" %>

        <div id="wrapper">

            <%@include file="header.jspf" %>

            <div id="content" role="main" class="clearfix">

                <!-- If you require a breadcrumb trail, its root should be your service.
                        You don't need a breadcrumb trail on the homepage of your service... -->
                <nav id="breadcrumb">
                    <p>
                        <a href=".">Enzyme Portal</a> &gt;
                        About Enzyme Portal
                    </p>
                </nav>

                <!-- Example layout containers -->

                <!-- Suggested layout containers -->



                <div class="row">

                    <div class="large-8 columns">

                        <div id="intro" style="background-color: white;">
                            <div class="panel-pane pane-custom pane-3 clearfix">
                                <h3 class="pane-title">About Enzyme Portal</h3>
                                <p>
                                    The Enzyme Portal is for those interested in
                                    the biology of enzymes and proteins with enzymatic
                                    activity.

                                    It integrates publicly available
                                    information about enzymes, such as small-molecule chemistry,
                                    biochemical pathways and drug compounds. It provides a concise
                                    summary of information from:
                                <ul>
                                    <li><a target="_blank" href="http://www.uniprot.org/help/uniprotkb">UniProt
                                            Knowledgebase</a></li>
                                    <li><a target="_blank" href="http://www.ebi.ac.uk/pdbe/">Protein Data Bank in
                                            Europe</a></li>
                                    <li><a target="_blank" href="http://www.ebi.ac.uk/rhea">Rhea</a>, a database
                                        of enzyme-catalyzed reactions
                                    </li>
                                    <li><a target="_blank" href="http://www.reactome.org">Reactome</a>, a database
                                        of biochemical pathways
                                    </li>
                                    <li>
                                        <a target="_blank" href="https://www.ebi.ac.uk/thornton-srv/m-csa/">M-CSA</a>, a database of enzyme reaction mechanisms
                                    </li>
                                    <li><a target="_blank" href="http://www.ebi.ac.uk/intenz">IntEnz</a>, a resource
                                        with enzyme nomenclature information
                                    </li>
                                    <li>
                                        <a target="_blank" href="https://omim.org/">OMIM</a>, An Online Catalog of Human Genes and Genetic Disorders.
                                    </li>
                                    <li><a target="_blank" href="http://www.ebi.ac.uk/chebi">ChEBI</a> and
                                        <a target="_blank" href="https://www.ebi.ac.uk/chembl/">ChEMBL</a>,
                                        which contain information about small molecule chemistry and
                                        bioactivity
                                    </li>

                                </ul>
                                The Enzyme Portal brings together lots of diverse
                                information about enzymes and displays it in an organized overview.
                                It covers a large number of species including the key model
                                organisms, and provides a simple way to compare orthologs.
                                </p>
                                <!--   </div> -->

                            </div>
                        </div>

                    </div>

                    <div class="large-4 columns ">
<!--                        <div class="row">
                            <div class="card">
                                <div class="card-divider">
                                    <h4>Trending</h4>
                                </div>
                                <div class="card-section">
                                    <ul class="split">
                                        <li><a href="http://www.ebi.ac.uk/training/online/course/enzyme-portal-quick-tour"
                                               class="icon icon-generic icon-c1" data-icon="t"> Quick Tour</a></li>
                                        <li><a href="https://www.ncbi.nlm.nih.gov/pubmed/28158609"
                                               class="icon icon-conceptual icon-c8" data-icon="l"> Publications</a></li>
                                        <li><a href="https://github.com/uniprot/enzymeportal"
                                               class="icon icon-generic icon-c8" data-icon=";"> Documentation</a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>-->
                        <div class="row">

                            <div class=" panels-flexible-region-about_us-right_ panels-flexible-region-last ">
                                <div class="inside panels-flexible-region-inside panels-flexible-region-about_us-right_-inside panels-flexible-region-inside-last">
                                    <div class="shortcuts">

                                        <h4>Popular</h4>
                                        <ul class="split">
                                            <li><a href="http://www.ebi.ac.uk/training/online/course/enzyme-portal-quick-tour"
                                                   class="icon icon-generic icon-c1" data-icon="t"> Quick Tour</a></li>
                                            <li><a href="https://www.ncbi.nlm.nih.gov/pubmed/28158609"
                                                   class="icon icon-conceptual icon-c8" data-icon="l"> Publications</a></li>
                                            <li><a href="https://github.com/uniprot/enzymeportal"
                                                   class="icon icon-generic icon-c8" data-icon=";"> Documentation</a></li>
                                        </ul>

                                    </div>
                                    <!--      first video-->

                                    <!--                    <div class="shortcuts">
                                                            <div>
                                                                <h4 > Learn more about Enzyme Portal</h4>
                                                                <div>
                                                                    <iframe src="https://www.youtube.com/embed/eCHWYLVN230" frameborder="0" allowfullscreen></iframe>
                                                                </div>
                                                            </div>
                                                        </div>-->


                                    <!--
                                                        <div>
                                                            <h4><span id="internal-source-marker_0.35187150686265334" class="icon icon-generic icon-c4" data-icon=")">Technical Documents</span></h4>
                                                            <p><span>The Enzyme Portal is an open source project developed at the EMBL-EBI and the source code is freely available, and can be downloaded from <a href="https://github.com/ebi-cheminf/enzymeportal">GitHub</a>, an online project hosting service. </span>
                                                            </p>
                                                        </div>-->
                                </div>
                            </div>
                            <hr class="panel-separator"/>
                        </div>


                        <div class="row">
                            <div class="shortcuts">

                                <h4 > Learn more about Enzyme Portal</h4>
                                <div>
                                    <iframe width="400" height="300" src="https://www.youtube.com/embed/fx36dBa5s60" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>
<!--                                    <iframe src="https://www.youtube.com/embed/eCHWYLVN230" frameborder="0" allowfullscreen></iframe>-->
                                </div>

                            </div> 
                            <hr class="panel-separator"/>
                        </div>


                        <div class="row">
                            <div class="shortcuts">
                                <h4><span id="internal-source-marker_0.35187150686265334" class="icon icon-generic icon-c4" data-icon=")">Technical Documents</span></h4>
                                <p><span>The Enzyme Portal is an open source project developed at the EMBL-EBI and the source code is freely available, and can be downloaded from <a href="https://github.com/ebi-cheminf/enzymeportal">GitHub</a>, an online project hosting service. </span>
                                </p>
                            </div>
                        </div>

                    </div>
                    <!-- End example layout containers -->

                </div>

                <%@include file="footer.jspf" %>
            </div>
            </div>
            <!--! end of #wrapper -->

    </body>

</html>

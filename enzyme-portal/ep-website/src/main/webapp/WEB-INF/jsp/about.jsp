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

<body class="level2"><!-- add any of your classes or IDs -->
<%@include file="skipto.jspf" %>

<div id="wrapper" class="container_24">

    <%@include file="header.jspf" %>

    <div id="content" role="main" class="grid_24 clearfix">

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

        <!-- <div class="content">
            <div class="panel-flexible panels-flexible-about_us clearfix" >
        <div class="panel-flexible-inside panels-flexible-about_us-inside">-->
        <!--<div class="panels-flexible-row panels-flexible-row-about_us-main-row panels-flexible-row-first clearfix">
          <div class="inside panels-flexible-row-inside panels-flexible-row-about_us-main-row-inside panels-flexible-row-inside-first clearfix">-->

        <section class="grid_18 alpha">
            <!--<div class="panels-flexible-region panels-flexible-region-about_us-center panels-flexible-region-first ">
              <div class="inside panels-flexible-region-inside panels-flexible-region-about_us-center-inside panels-flexible-region-inside-first">-->

            <div id="intro" style="background-color: white;">
                <div class="panel-pane pane-custom pane-3 clearfix">

                    <h3 class="pane-title">About Enzyme Portal</h3>


                    <!--   <div class="pane-content"> -->

                    <p></p>

<!--                    <div class="caption caption-right">
                        <div style="width: 301px;">
                            <%-- b>Welcome to the Enzyme Portal</b --%>
                            <iframe class="grid_24"
                                    src="http://www.youtube.com/embed/Kldp0WXcxUM"
                                    frameborder="0"
                                    allowfullscreen></iframe>
                        </div>
                        Enzyme Portal - An Introduction
                    </div>
                    <p></p>-->
                    <!--<p>The European Bioinformatics Institute is part of <a href="http://www.embl.org/" class=" external">EMBL</a>, Europe?s flagship laboratory for the life sciences. EMBL-EBI provides freely available&nbsp;<a href="http://www.ebi.ac.uk/services">data from life science experiments</a> covering the full spectrum of molecular biology. About 20% of our institute is devoted to <a href="http://www.ebi.ac.uk/research">investigator-led research</a> using computational approaches to unravel the secrets of life. Our extensive <a href="http://www.ebi.ac.uk/training/">training</a> programme helps researchers in academia and <a href="http://www.ebi.ac.uk/industry/">industry</a> to make the most of the incredible amount of data being produced every day in life science experiments.</p>
                    <p>We are a non-profit, intergovernmental organisation funded by <a href="http://www.embl.de/aboutus/general_information/organisation/member_states/index.html" class=" external">EMBL member states</a>. Our 520 staff hail from 43 countries, and we welcome a regular stream of visiting scientists throughout the year. We are located on the <a href="http://frontier.ebi.ac.uk/about/visit-us">Wellcome Trust Genome Campus</a> in Hinxton, Cambridge in the United Kingdom.</p>-->

                    <p>
                        The Enzyme Portal is for those interested in
                        the biology of enzymes and proteins with enzymatic
                        activity.
                        <!--	</p>

                            <p>-->
                        It integrates publicly available
                        information about enzymes, such as small-molecule chemistry,
                        biochemical pathways and drug compounds. It provides a concise
                        summary of information from:
                    <ul>
                        <li><a href="http://www.uniprot.org/help/uniprotkb">UniProt
                            Knowledgebase</a></li>
                        <li><a href="http://www.ebi.ac.uk/pdbe/">Protein Data Bank in
                            Europe</a></li>
                        <li><a href="http://www.ebi.ac.uk/rhea">Rhea</a>, a database
                            of enzyme-catalyzed reactions
                        </li>
                        <li><a href="http://www.reactome.org">Reactome</a>, a database
                            of biochemical pathways
                        </li>
                        <li><a href="http://www.ebi.ac.uk/intenz">IntEnz</a>, a resource
                            with enzyme nomenclature information
                        </li>
                        <li><a href="http://www.ebi.ac.uk/chebi">ChEBI</a> and
                            <a href="https://www.ebi.ac.uk/chembl/">ChEMBL</a>,
                            which contain information about small molecule chemistry and
                            bioactivity
                        </li>
<!--                        <li><a href="http://www.ebi.ac.uk/thornton-srv/databases/CoFactor/">CoFactor</a>
                            and <a href="http://www.ebi.ac.uk/thornton-srv/databases/MACiE/">MACiE</a>
                            for highly detailed, curated information about cofactors and reaction
                            mechanisms.
                        </li>-->
                    </ul>
                    The Enzyme Portal brings together lots of diverse
                    information about enzymes and displays it in an organized overview.
                    It covers a large number of species including the key model
                    organisms, and provides a simple way to compare orthologs.
                    </p>
                    <!--   </div> -->


                </div>
            </div>

            <div class="panel-separator"></div>
            <!--  </div>
            </div> -->

        </section>

        <section class="grid_6 omega">

            <div class="panels-flexible-region panels-flexible-region-about_us-right_ panels-flexible-region-last ">
                <div class="inside panels-flexible-region-inside panels-flexible-region-about_us-right_-inside panels-flexible-region-inside-last">

                    <div class="shortcuts">
                        <div class="panel-pane pane-custom pane-8 clearfix">

                            <h3 class="pane-title">Popular</h3>


                            <!--   <div class="pane-content"> -->


                            <ul class="split">
                                <li><a href="http://www.ebi.ac.uk/training/online/course/enzyme-portal-quick-tour"
                                       class="icon icon-generic icon-c1" data-icon="t">Quick Tour</a></li>
                                <li><a href="http://nar.oxfordjournals.org/content/41/D1/D773.full"
                                       class="icon icon-conceptual icon-c8" data-icon="l">Publications</a></li>
                                <li><a href="http://ebi-cheminf.github.com/enzymeportal/ep/"
                                       class="icon icon-generic icon-c8" data-icon=";">Documentation</a></li>

                            </ul>
                            <!--<ul class="split">

                             <li><a href="http://ebi-cheminf.github.com/enzymeportal/ep/" class="icon icon-generic icon-c8" data-icon=";">Documentation</a></li>

                            </ul>-->
                            <!--   </div> -->


                        </div>
                    </div>
                    <!--      first video-->
                    <div class="panel-separator"></div>

                    <div class="shortcuts">
                        <div class="panel-pane pane-custom pane-9 clearfix">

                            <h3 class="pane-title"> Learn more about Enzyme Portal</h3>


                            <div class="pane-content">

                                <%-- b>Explore Enzyme Portal</b --%>

                                <iframe class="grid_24"
                                        src="https://www.youtube.com/embed/Wq273CZrgSM"
                                        frameborder="0"
                                        allowfullscreen></iframe>

                            </div>
                        </div>
                    </div>
                    <div class="panel-separator"></div>

                    <div class="panel-pane pane-custom pane-10 clearfix">


                        <div class="pane-content">

                            <h3><span id="internal-source-marker_0.35187150686265334" class="icon icon-generic icon-c4"
                                      data-icon=")">Technical Documents<br>
</span></h3>

                            <p><span>The Enzyme Portal is an open source project developed at the EMBL-EBI and the source code is freely available, and can be downloaded from <a
                                    href="https://github.com/ebi-cheminf/enzymeportal">GitHub</a>, an online project hosting service. </span>
                            </p>

                            <p><span>Additionally, the technical documentation (Javadoc, project information) is also <a
                                    href="http://ebi-cheminf.github.com/enzymeportal/ep/">available online</a> </span>
                            </p>

                        </div>


                    </div>
                </div>
            </div>
        </section>

        <!-- End example layout containers -->

    </div>

    <%@include file="footer.jspf" %>

</div>
<!--! end of #wrapper -->

</body>

</html>

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
<!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->

<c:set var="pageTitle" value="Home &lt; Enzyme Portal &lt; EMBL-EBI"/>
<%@include file="head.jspf" %>

<body class="level2"><!-- add any of your classes or IDs -->
    <%@include file="skipto.jspf" %>


    <div id="wrapper" class="container_24">
    
    <%@include file="header.jspf" %>
               
    <div id="content" role="main" class="grid_24 clearfix">
        
        <section class="grid_24">
            <div id="browse-box-container" class="prefix_4 grid_24">
                <a href="browse/disease" class="browse-box disease-box grid_4">
                    <h4>Diseases</h4>
                    <span class="icon icon-generic" data-icon="B"></span>
                </a>
                <a href="browse/enzymes"  class="browse-box enzyme-box grid_4">
                    <h4>Enzyme Classification</h4>
                    <span class="icon icon-conceptual" data-icon="b"></span>
                </a>
                <a href="browse/taxonomy"  class="browse-box taxonomy-box grid_4">
                    <h4>Taxonomy</h4>
                    <span class="icon icon-conceptual" data-icon="o"></span>
                </a>
                <a href="browse/pathways"  class="browse-box gene-box grid_4">
                    <h4>Pathways</h4>
                    <span class="icon icon-conceptual" data-icon="y"></span>
                </a>
            </div>
        </section>
        
                <a href="browse/reactions"  class="browse-box gene-box grid_6 push_10">
                    <h4>Reactions (testing only)</h4>
                    <span class="icon icon-conceptual" data-icon="y"></span>
                </a>

        <section class="grid_24">
            <div class="grid_6">
                <h4>About Enzyme Portal</h4>
                <p>The Enzyme Portal integrates publicly available information about enzymes, such as small-molecule chemistry, biochemical pathways and drug compounds.</p>
                <p><a href="about">more...</a></p>
            </div>
            <div class="grid_6">
                <h4>Enzyme Portal Resources</h4>
                <p>To build its reports, the Enzyme Portal uses data from <a href="http://www.uniprot.org" target="_blank">UniProtKb</a>,
                    <a href="http://www.ebi.ac.uk/pdbe/" target="_blank">PDBe</a>, <a href="https://www.ebi.ac.uk/chembl/" target="_blank">ChEMBL</a>,
                    <a href="http://www.ebi.ac.uk/chebi/" target="_blank">ChEBI</a>, <a href="http://www.reactome.org/" target="_blank">REACTOME</a>,
                    <a href="http://www.ebi.ac.uk/thornton-srv/databases/MACiE/" target="_blank">MACiE</a>, <a href="http://www.ebi.ac.uk/rhea/" target="_blank">Rhea</a>,
                    <a href="http://www.ebi.ac.uk/intenz/" target="_blank">IntEnz</a> and many more resources.</p>
                <p><a href="about">more...</a></p>
            </div>
            <div class="grid_6">
                <h4>Technical documents</h4>
                <p>The Enzyme Portal is an open source project developed at the EMBL-EBI and the source code is freely available and can be downloaded from <a href="https://github.com/computingfacts/enzymeportal" target="_blank">GitHub</a>, an online project hosting service.</p>
                <p>Additionally, the technical documentation (Javadoc, project information) is also available online.</p>
            </div>
            <div class="grid_6">
                <h4>FAQ</h4>
                <p>Read our frequently asked questions <a href="faq">here</a>.</p>
            </div>
        </section>

    <%@include file="footer.jspf" %>
    
  </div> <!--! end of #wrapper -->
  <script src="resources/javascript/search.js" type="text/javascript"></script>
  <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>

</body>
</html>

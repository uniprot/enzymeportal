<%-- 
    Document   : faq
    Created on : Sep 5, 2012, 1:49:51 PM
    Author     : joseph
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<!-- paulirish.com/2008/conditional-stylesheets-vs-css-hacks-answer-neither/ -->
<!--[if lt IE 7]> <html class="no-js ie6 oldie" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js ie7 oldie" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js ie8 oldie" lang="en"> <![endif]-->
<!-- Consider adding an manifest.appcache: h5bp.com/d/Offline -->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->

<c:set var="pageTitle" value="FAQ &lt; Enzyme Portal &gt; EMBL-EBI"/>
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
		    FAQ
			</p>
  	</nav>

    	
    <!-- Example layout containers -->
	   
<!--    <section>
   		<h2>Frequently Asked Questions</h2>
   		<p>Your content</p>										
		</section> -->
		
<!--		<section>
			<h3>Level 3 heading</h3>
			<p>More content in a full-width container.</p>
		
			<h4>Level 4 heading</h4>
			<p>More content in a full-width container.</p>
		</section>-->

            <section>
                 <h3>Frequently Asked Questions</h3>
                <ul>
                    <li><a href="faq#01">How is the EnzymePortal different from BRENDA?</a></li>
                    <li><a href="faq#02">What is the meaning of those compounds used as filters for the search results?</a></li>
                    <li><a href="faq#03">What are the figures in colourful labels which appear with the results of a protein sequence search?</a></li>
                    <li><a href="#04">Why does the small molecules tab show
                        fewer bioactive compounds than the ChEMBL page?</a>
                    <li><a href="#05">How can I combine search filters?</a>
                </ul>
                <a name="01"></a><h4>How is the EnzymePortal different from BRENDA?</h4>
                <fieldset>
                <p>The <a href="/enzymeportal" class="showLink" >EnzymePortal</a>  is a one-stop shop for enzyme-related information in resources developed at the EBI. It accumulated this information and aims to present it to the scientist with a unified user experience. The EnzymePortal team does not curate enzyme information and therefore is a secondary information resource or portal. At some point, a user interested in more detail will always leave the EP pages and refer to the information in the underlying primary database (Uniprot, PDB, etc.) directly.
                </p><p><a href="http://www.brenda-enzymes.info/" >BRENDA</a>  is the most comprehensive resource about enzymes world-wide and has invested a great amount into the abstraction and curation about enzymes and their related information. BRENDA contains valuable information that can not be found in the EnzymePortal at the moment, such as kinetic, specifity, stability, application, disease-related and engineering data. As a primary resource, BRENDA could be a candidate for an information source for the EP in the future.</p>
                </fieldset>
                <a name="02"></a><h4>What is the meaning of those compounds used as filters for the search results?</h4>
                <fieldset>
                    <p>The compounds listed along the search results are any small molecules which are related to them, be it as reactants, products, activators, inhibitors or cofactors of those enzymes. They can be used as filters to narrow your search if you are particularly interested in the biochemistry of concrete chemicals, for example enzymes using manganese as a cofactor.</p>
                </fieldset>
                <a name="03"></a><h4>What are the figures in colourful labels which appear with the results of a protein sequence search?</h4>
                <fieldset>
                    <p>The figures are <a href="http://www.ncbi.nlm.nih.gov/books/NBK21097/#A614">bit scores</a> for the blast search of the given sequence against the shown enzyme. The colour is a hint for the match: red means a close match, blue a loose one. It helps to locate best results at first sight.</p><p>For more information about scores, please refer to the <a href="http://www.ncbi.nlm.nih.gov/books/NBK21097/">BLAST documentation</a>.</p>
                </fieldset>
                <a id="04"></a><h4>Why does the small molecules tab show fewer
                    bioactive compounds than the ChEMBL page?</h4>
                <fieldset>
                    <p>The small molecules tab shows any bioactive compounds
                        described in ChEMBL as interacting with the enzyme. The
                        link <i>See all bioactive compounds in ChEMBL</i> will
                        take to a ChEMBL page where you may see many more. The
                        difference is due to a filter applied by the Enzyme
                        Portal, whereby only those compounds more likely to have
                        a significative activity on the function of the enzyme
                        are shown.</p>
                </fieldset>
                <a id="05">How can I combine search filters?</a>
                <fieldset>
                    <p>Search results can be filtered using the facets shown on
                        the left hand side in three blocks: species, chemical
                        compounds and diseases. Checking more than one filter
                        in the same block narrows the search to those results
                        matching <i>any</i> of them. Checking filters in
                        different blocks shows only results matching <i>all</i>
                        of them.<br/>
                        For example, if you search for <code>sildenafil</code>
                        and then check the filter <i>Species &gt; Human</i> you
                        will see only the enzymes found in human.</p> Now,
                        checking an additional species - say <i>Mouse</i> - will
                        restrict the list of results to those found
                        <i>either</i> in human or mouse. If you now select a
                        filter in another block - <i>Chemical compounds &gt;
                        Drug > Sildenafil</i>, for example - the results shown
                        will be those enzymes affected by sildenafil <i>and</i>
                        present in <i>either</i> human or mouse.</p>
                </fieldset>
            </section>
		<!-- End example layout containers -->
			
    </div>
    
    <%@include file="footer.jspf" %>
    
  </div> <!--! end of #wrapper -->
  
  
</body>
</html>



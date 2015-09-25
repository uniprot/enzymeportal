<%-- 
    Document   : faq
    Created on : Sep 5, 2012, 1:49:51 PM
    Updated on : Sep 24, 2015, 11:20:58 AM
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



                <section>
                    <h3>Frequently Asked Questions</h3>
                    <ul>
                        <li><a href="faq#01">How is the Enzyme Portal different from BRENDA?</a></li>
                        <li><a href="faq#02">How can I see species-related information for a given enzyme?</a></li>
                        <li><a href="faq#03">How can I combine search filters?</a></li>
                        <li><a href="faq#04">What is the meaning of those compounds used as filters for the search results?</a></li>
                        <li><a href="faq#05">Why does the small molecules tab show fewer Inhibitors than the ChEMBL page?</a></li>
                        <li> <a href="faq#06"> How to use the "Disease", "Enzyme Classification", "Taxonomy" and "Pathways" boxes on the home page? </a></li>

                        <li><a href="faq#07">How can I search for a disease or a pathway that is not displayed on the Disease or Pathways pages?</a></li>

                        <li><a href="faq#08">Are all species available from the "Taxonomy" page?</a></li>

                        <li> <a href="faq#09">What can do I with enzymes in my basket?</a>  </li>

                        <li><a href="faq#10">Can I download the results from the search?</a></li>
                        <li><a href="faq#11">Where can I find the old Enzyme Portal pages?</a></li>

                    </ul>

                    <!--     text-->

                    <a id="01"></a><h4>How is the Enzyme Portal different from BRENDA?</h4>
                    <fieldset>
                        <p>The <a href="/enzymeportal" class="showLink" >EnzymePortal</a>  is a one-stop shop for enzyme-related information in resources developed at the EBI. It accumulated this information and aims to present it to the scientist with a unified user experience. The EnzymePortal team does not curate enzyme information and therefore is a secondary information resource or portal. At some point, a user interested in more detail will always leave the Enzyme Portal pages and refer to the information in the underlying primary database (UniprotKB, PDB, etc.) directly. </p>
                        <p><a href="http://www.brenda-enzymes.info/" target="_blank" >BRENDA</a>  is the most comprehensive resource about enzymes worldwide and has invested a great amount into the abstraction and curation about enzymes and their related information. BRENDA contains valuable information that cannot be found in the EnzymePortal at the moment, such as kinetic, specificity, stability, application, and disease-related data. As a primary resource, BRENDA could be a candidate for an information source for the EnzymePortal in the future.</p>
                    </fieldset>


                    <a id="02"></a><h4>How can I see species-related information for a given enzyme?</h4>
                    <fieldset>
                        <p>When enzymes have identical name but belong to different species they are grouped into one entry. To see the information related to one particular species use the drop down menu at the top of the enzyme page. Please see <a class="showLink" href="https://www.youtube.com/watch?v=7doCBhF_XrQ" target="_blank">video</a> </p>     
                    </fieldset>

                    <a id="03"><h4>How can I combine search filters?</h4></a>
                    <fieldset>
                        <p>Search results can be filtered using the facets shown on
                            the left hand side in three blocks: species, chemical
                            compounds, diseases and enzyme family. Checking more than one filter
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

                    <a id="04"></a><h4>What is the meaning of those compounds used as filters for the search results?</h4>
                    <fieldset>
                        <p>The compounds listed along the search results are any small molecules which are related to them, be it as activators, inhibitors or cofactors of those enzymes. They can be used as filters to narrow your search if you are particularly interested in the biochemistry of concrete chemicals, for example enzymes using manganese as a cofactor.</p>
                    </fieldset> 

                    <a id="05"></a><h4>Why does the small molecules tab show fewer
                        Inhibitors than the ChEMBL page?</h4>
                    <fieldset>
                        <p>The small molecules tab shows any Inhibitors
                            described in ChEMBL as inhibiting the enzyme. The
                            link <i>See all inhibitors in ChEMBL</i> will
                            link to a ChEMBL page where you may see many more. The
                            difference is due to a filter applied by the Enzyme
                            Portal, whereby only those compounds more likely to have
                            a significative activity on the function of the enzyme
                            are shown.</p>
                    </fieldset>

                    <a id="06"></a><h4>How to use the "Disease", "Enzyme Classification", "Taxonomy" and "Pathways" boxes on the home page?</h4>           
                    <fieldset>
                        <p>The home page shows boxes that represent the types of data you can browse using Enzyme Portal. For example, you can click on "Disease" and view a list of all diseases that have enzymes linked to them in Enzyme Portal. You can also search for your disease of interest within this list. Clicking on a disease name will take you to a results page listing all enzymes related to that disease. You can explore data in similar ways through the "Enzyme Classification", "Taxonomy" and "Pathways" boxes.</p> 
                    </fieldset>        

                    <a id="07"><h4>How can I search for a disease or a pathway that is not displayed on the Disease or Pathways pages?</h4></a>
                    <fieldset>
                        <p>The Disease or Pathways pages display only a limited list of the diseases or pathways available in alphabetical order. To search for a disease or a pathway that is not displayed on the page, use the search box on the top of the page. The search box uses a controlled vocabulary. Just start typing the first few letters of the disease or pathway and a drop down menu will appear where the most relevant suggestion can be selected.</p>  
                    </fieldset>           


                    <a id="08"><h4>Are all species available from the "Taxonomy" page?</h4></a>
                    <fieldset>
                        <p>At the moment, only model organisms of major taxa such as Homo sapiens or Escherichia coli are available through the "Taxonomy" browse page.</p>
                    </fieldset>

                    <a id="09"><h4>What can do I with enzymes in my basket?</h4></a>
                    <fieldset>
                        <p>The Enzyme Portal basket offers a tool to compare two enzymes. Just select your enzymes of interest and add them to your basket. Once inside your basket, select an organism for the enzymes you wish to compare using the dropdowns under the button called "Compare selected". Then click on the button to compare the selected enzymes. The results will show you a comparison of the content for each enzyme, a comparison of their structures (if available) and a button to launch a comparison of the protein sequences. </p>    
                    </fieldset>


                    <a id="10"><h4>Can I download the results from the search?</h4></a>

                    <fieldset>
                        <p>Currently it is not possible to download search results, however, this feature will be available in future releases of the enzyme portal.</p>
                    </fieldset>



                    <a id="11"><h4>Where can I find the old Enzyme Portal pages?</h4></a>
                    <fieldset>
                        <p>The old Enzyme Portal pages can be found at <a href="http://www.ebi.ac.uk/enzymeportal/legacy/">http://www.ebi.ac.uk/enzymeportal/legacy/</a>. Please note that these pages are no longer updated and will be phased out at the end of 2015.</p>
                    </fieldset>
                    <a id="12"></a>
                    <fieldset>

                    </fieldset>
                </section>
                <!-- End layout containers -->

            </div>

            <%@include file="footer.jspf" %>

        </div> <!--! end of #wrapper -->


    </body>
</html>



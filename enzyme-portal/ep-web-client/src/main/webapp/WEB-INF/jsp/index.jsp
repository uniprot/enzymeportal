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
                            <div style="margin-left: auto; margin-right: auto;
                         width: 50%;">

                        <h2>Welcome to the Enzyme Portal</h2> 
                        <p>You can search this integrated resource to find information about the biology of a protein with enzymatic activity.</p>
                        <p>The Enzyme Portal integrates publicly available information about enzymes, such as small-molecule chemistry, biochemical pathways and drug compounds. It brings together lots of diverse information about enzymes, and covers a large number of species including the key model organisms. 
                          The search results are displayed as a summary, and give you a simple way to compare orthologues.
                        </p>
                    <p>
		Give it a try and 
		<a href="http://www.ebi.ac.uk/support/index.php?query=Enzyme+portal&referrer=http://www.ebi.ac.uk/enzymeportal/">let
                    us know</a> what you think!</p>
<!--                        <p> The Enzyme Portal is for those interested
                        in the biology of enzymes and proteins with enzymatic
                        activity.</p>-->
                 
<!--                        <div style="text-align: right;">-->
<p><p style="text-align: right;"><a href="about" class="showLink" >More about the
                                Enzyme Portal...</a></p>
<!--                    </div>-->
                    </div>
            <div style="margin-left: auto; margin-right: auto;
                          width: 50%;">
 <h3 class="pane-title">Enzyme Portal Resources</h3>
                <div class="shortcuts grid_24">

               <div class="panel-pane pane-custom pane-8 clearfix" >

<!--        <h3 class="pane-title">Enzyme Portal Resources</h3>-->

 
  <div class="containers grid_24">
<!--      <section class="grid_24">-->
         <ul id="mycarousel" class="jcarousel-skin-aqua grid_24">
            <li>
                <div class="grid_24">
                    <img class="grid_3 alpha" src="resources/images/inc/uniprot_logo.gif" />
                    <span class="grid_21 omega">
                     <h4>UniProtKB</h4>
                     <p>The UniProt Knowledgebase (UniProtKB) is the central hub for the collection of functional information on proteins, with accurate, consistent and rich annotation.</p>
                     <p>The UniProt databases are the UniProt Knowledgebase (UniProtKB), the UniProt Reference Clusters (UniRef), and the UniProt Archive (UniParc).</p>
                  </span>
               </div>
            </li>
            <li>
               <div>
                   <img class="grid_3 alpha" src="resources/images/inc/pdbe_logo.png" />
                   <span class="grid_21 omega">
                     <h4>PDBe</h4>
                     <p> PDBe is the European resource for the collection, organisation and dissemination of data on biological macromolecular structures. In collaboration with the other worldwide Protein Data Bank (wwPDB) partners - the Research Collaboratory for Structural Bioinformatics (RCSB) and BioMagResBank (BMRB) in the USA and the Protein Data Bank of Japan (PDBj) - we work to collate, maintain and provide access to the global repository of macromolecular structure data.</p> 
<!--                     <a href="/" title="Link">Click this Link</a>-->
                  </span>
               </div>
            </li>
            <li>
               <div>
                  <img class="grid_3 alpha" src="resources/images/inc/chembl_logo.png" />
                  <span class="grid_21 omega">
                     <h4>ChEMBL</h4>
                     <p>ChEMBL is a database of bioactive drug-like small molecules, it contains 2-D structures, calculated properties (e.g. logP, Molecular Weight, Lipinski Parameters, etc.) and abstracted bioactivities (e.g. binding constants, pharmacology and ADMET data).</p>
                     <p>The data is abstracted and curated from the primary scientific literature, and cover a significant fraction of the SAR and discovery of modern drugs</p>
                  </span>
              </div>
            </li>  
                     <li>
               <div>
                  <img class="grid_3 alpha" src="resources/images/inc/ChEBI_logo.png" />
                  <span class="grid_21 omega">
                     <h4>ChEBI</h4>
                     <p>Chemical Entities of Biological Interest (ChEBI) is a freely available dictionary of molecular entities focused on ?small? chemical compounds. The term ?molecular entity? refers to any constitutionally or isotopically distinct atom, molecule, ion, ion pair, radical, radical ion, complex, conformer, etc., identifiable as a separately distinguishable entity. The molecular entities in question are either products of nature or synthetic products used to intervene in the processes of living organisms.</p>
                  </span>
              </div>
            </li> 
                     <li>
               <div>
                  <img class="grid_3 alpha" src="resources/images/inc/reactome.png" />
                  <span class="grid_21 omega">
                     <h4>REACTOME</h4>
                     <p>REACTOME is an open-source, open access, manually curated and peer-reviewed pathway database. Pathway annotations are authored by expert biologists, in collaboration with Reactome editorial staff and cross-referenced to many bioinformatics databases. These include NCBI Entrez Gene, Ensembl and UniProt databases, the UCSC and HapMap Genome Browsers, the KEGG Compound and ChEBI small molecule databases, PubMed, and Gene Ontology.</p>
                  </span>
              </div>
            </li> 
               <li>
               <div>
                  <img class="grid_3 alpha" src="resources/images/inc/no-image.jpeg" />
                  <span class="grid_21 omega">
                     <h4>MACiE</h4>
                     <p>MACiE, which stands for Mechanism, Annotation and Classification in Enzymes, is a collaborative project between the Thornton Group at the European Bioinformatics Institute and the Mitchell Group at the University of St Andrews (initially within the Unilever Centre for Molecular Informatics part of the University of Cambridge). Metal MACiE, a database of catalytic metal ions, with a view to understanding the functions of the roles and activity of catalytic metals in enzymes.</p>
                  </span>
              </div>
            </li> 
                          <li>
               <div>
                  <img class="grid_3 alpha" src="resources/images/inc/rhea.png" />
                  <span class="grid_21 omega">
                     <h4>Rhea</h4>
                     <p>Rhea is a freely available, manually annotated database of chemical reactions created in collaboration with the Swiss Institute of Bioinformatics (SIB).
All data in Rhea is freely accessible and available for anyone to use.</p>
                  </span>
              </div>
            </li> 
              <li>
               <div>
                  <img class="grid_3 alpha" src="resources/images/inc/intenz.gif" />
                  <span class="grid_21 omega">
                     <h4>IntEnz</h4>
                     <p>IntEnz (Integrated relational Enzyme database) is a freely available resource focused on enzyme nomenclature. IntEnz is created in collaboration with the Swiss Institute of Bioinformatics (SIB). This collaboration is responsible for the production of the ENZYME resource. 
IntEnz contains the recommendations of the Nomenclature Committee of the International Union of Biochemistry and Molecular Biology (NC-IUBMB) on the nomenclature and classification of enzyme-catalysed reactions.</p>
                  </span>
              </div>
            </li> 
         </ul>
<!--      </section>-->
    
         <div class="jcarousel-control">
            <a href="#">1</a>
            <a href="#">2</a>
            <a href="#">3</a>
            <a href="#">4</a>
            <a href="#">5</a>
            <a href="#">6</a>
            <a href="#">7</a>
            <a href="#">8</a>
         </div>
    
<!--      </div>-->






        
<!--                              <ul class="split">
                                  <li>UniProt</li>
                                  <li>ChEBI</li>
                                  <li>ChEMBL</li>
                                  <li>PDBe</li>
                                </ul>
                                <ul class="split">
                                    <li>Reactome</li>
                                    <li>Rhea</li>
                                    <li>IntEnz</li>
                                    <li>MACiE</li>
                                </ul> -->
        
                   
                    
                    </div></div>
                
                </div>
                
                
            </section>
                
			
    </div>
       
    <%@include file="footer.jspf" %>
    
  </div> <!--! end of #wrapper -->

<script src="resources/javascript/search.js" type="text/javascript"></script>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
<script src="resources/javascript/jquery.jcarousel.min.js" type="text/javascript"></script>
  
       <script type="text/javascript">
            /**
            * We use the initCallback callback
            * to assign functionality to the controls
            */
            function mycarousel_initCallback(carousel) {
                jQuery('.jcarousel-control a').bind('click', function () {
                    carousel.scroll(jQuery.jcarousel.intval(jQuery(this).text()));
                    return false; 
                });

                jQuery('#mycarousel-next').bind('click', function () {
                    carousel.next();
                    return false;
                });

                jQuery('#mycarousel-prev').bind('click', function () {
                    carousel.prev();
                    return false;
                });

                // Pause autoscrolling if the user moves with the cursor over the clip.
        	    carousel.clip.hover(function() {
        	        carousel.stopAuto();
        	    }, function() {
        	        carousel.startAuto();
        	    });

            };

            // Ride the carousel...
            jQuery(document).ready(function() {
              jQuery('#mycarousel').jcarousel({
              scroll: 1, animation:700, visible:1, auto:5, wrap:"both",
                    initCallback: mycarousel_initCallback,
                    itemVisibleInCallback: {
                        onAfterAnimation: function (c, o, i, s) {
                            i = (i - 1) % $('#mycarousel li').size();
                            jQuery('.jcarousel-control a').removeClass('active').addClass('inactive');
                            jQuery('.jcarousel-control a:eq(' + i + ')').removeClass('inactive').addClass('active');
                        }
                    }
          });  
      });
      </script>
  
</body>
</html>

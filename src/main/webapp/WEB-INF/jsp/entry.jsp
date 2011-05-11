<%-- 
    Document   : entry
    Created on : May 6, 2011, 7:40:14 PM
    Author     : hongcao
--%>
<!DOCTYPE html>
<html>
    <head>        
        <title>Enzyme Entry</title>
        <link media="screen" href="resources/lib/spineconcept/css/960gs/reset.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="resources/lib/spineconcept/css/960gs/text.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="resources/lib/spineconcept/css/960gs/960.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="resources/lib/spineconcept/css/common.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="resources/lib/spineconcept/css/summary.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="resources/lib/spineconcept/css/literature.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="resources/lib/spineconcept/css/species.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="resources/lib/spineconcept/javascript/jquery-ui/css/custom-theme/jquery-ui-1.8.11.custom.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="resources/css/enzyme.css" type="text/css" rel="stylesheet" />
        <script src="resources/lib/spineconcept/javascript/jquery-1.5.1.min.js" type="text/javascript"></script>
        <script src="resources/lib/spineconcept/javascript/jquery-ui/js/jquery-ui-1.8.11.custom.min.js" type="text/javascript"></script>
        <script src="resources/lib/spineconcept/javascript/summary.js" type="text/javascript"></script>
    </head>
    <body>
        <div class="page container_12">
            <div class="grid_12">
                <div class="breadcrumbs" id="breadcrumbs">
                    <ul>
                        <li class="first"><a href="">EBI</a></li>
                        <li><a href="">Databases</a></li>
                        <li><a href="">Enzymes</a></li>
                        <li><a href="">Summary</a></li>
                    </ul>
                </div>
            </div>
            <div class="grid_12">
                <h1 wicket:id="title">Enzyme Summary</h1>
            </div>
            <!--Species combo box-->
            <div class="grid_12 header"  style="">
                <div class="container_12">
                    <div class="grid_4 prefix_4 suffix_3 alpha">
                        <div wicket:id="classification">
                            <div class="classification">
                                <div class="label">ORGANISMS</div>
                                <div class="box selected Homo_sapiens">
                                    <span class="name">Human</span>
                                    <span class="extra">Homo sapiens</span>
                                </div>
                            </div>
                            <div class="selection">
                                <ul>
                                    <li class="selected">
                                        <div class="box Homo_sapiens">
                                            <span class="name">Human</span>
                                            <span class="extra">Homo sapiens</span>
                                        </div>
                                    </li>
                                    <li>
                                        <div class="box Mus_musculus">
                                            <span class="name">House mouse</span>
                                            <span class="extra">Mus musculus</span>
                                        </div>
                                    </li>
                                    <li>
                                        <a href="MultiReferencePage.html">
                                            <span>
                                                <span class="box Rattus_norvegicus">
                                                    <span class="name">Rat</span>
                                                    <span class="extra">Rattus norvegicus</span>
                                                </span>
                                                <span class="cardinality">x2</span>
                                            </span>
                                        </a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="grid_1 omega">
                        <div class="menu">
                            <a href="http://www.ebi.ac.uk/inc/help/search_help.html" class="help">Help</a>
                            <a href="" wicket:id="print" class="print"><span wicket:id="printLabel">Print</span></a>
                        </div>
                    </div>
                </div>
            </div>            
            <!--Tab column-->
            <div class="grid_12">
                <div id="reference" class="content">
                    <div class="column1">
                        <ul>
                            <li id="enzyme" class="tab protein selected">
                                <a href="">
                                    <span class="inner_tab">
                                        <span class="icon"></span>
                                        <span class="label">Enzyme</span>
                                    </span>
                                </a>
                            </li>
                            <li id="structure" class="tab structure">
                                <a href="">
                                    <span class="inner_tab">
                                        <span class="icon"></span>
                                        <span class="label">Protein Structure</span>
                                    </span>
                                </a>
                            </li>
                            <li id="reaction" class="tab reaction">
                                <a href="">
                                    <span class="inner_tab">
                                        <span class="icon"></span>
                                        <span class="label">Reactions &amp; pathways</span>
                                    </span>
                                </a>
                            </li>
                            <li id="molecule" class="tab molecule">
                                <span class="inner_tab">
                                    <span class="icon"></span>
                                    <span class="label">Small Molecules</span>
                                </span>
                            </li>
                            <li id="disease" class="tab disease">
                                <a href="">
                                    <span class="inner_tab">
                                        <span class="icon"></span>
                                        <span class="label">Disease</span>
                                    </span>
                                </a>
                            </li>
                            <li id="literature" class="tab literature">
                                <a href="">
                                    <span class="inner_tab">
                                        <span class="icon"></span>
                                        <span class="label">Literature</span>
                                    </span>
                                </a>
                            </li>
                        </ul>
                    </div>
                    <div class="column2">
                        <!--The id will be generated dynamically depending
                        the event. Eg.: if the page is loaded at 1st time
                        then the id is enzymeContent, if the reactions tab was
                        clicked then the id is reactionContent -->
                        <div id="test" class="node">
                        <!--This div is generated at the server side-->
                            <div class="view">
                                <div id="enzymeContent" class="summary">
                                    <h2>Enzyme title</h2>
                                    <dl>
                                        <dt>Function</dt>
                                        <dd>
                                            <ul>
                                                <li>Enzyme function</li>
                                            </ul>
                                        </dd>
                                    </dl>
                                    <dl>
                                        <dt>EC Classification</dt>
                                        <dd>
                                            <ul>
                                                <li>Enzyme classification</li>
                                            </ul>
                                        </dd>
                                    </dl>
                                    <dl>
                                        <dt>Enzyme Type</dt>
                                        <dd>
                                            <ul>
                                                <li>Enzyme Type</li>
                                            </ul>
                                        </dd>
                                    </dl>
                                    <dl>
                                        <dt>Protein Sequence</dt>
                                        <dd>
                                            <ul>
                                                <li>Sequence info</li>
                                            </ul>
                                        </dd>
                                    </dl>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="grid_12">
                <div class="footer">&copy;
                    <a target="_top" href="http://www.ebi.ac.uk/" title="European Bioinformatics Institute Home Page">European Bioinformatics Institute</a>
				  2011. EBI is an Outstation of the
                    <a href="http://www.embl.org/" target="_blank" title="European Molecular Biology Laboratory Home Page">European Molecular Biology Laboratory</a>.
                </div>
            </div>
            <div class="clear"></div>
        </div>
    </body>
</html>

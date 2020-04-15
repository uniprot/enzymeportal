<%-- 
    Document   : website-statistics
    Created on : 07-Feb-2020, 15:10:23
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
    <c:set var="pageTitle" value="Release Statistics &lt; Enzyme Portal &gt; EMBL-EBI"/>
    <!--    <script src="https://code.highcharts.com/highcharts.src.js"></script>-->

    <script src="https://code.highcharts.com/highcharts.js"></script>
    <script src="https://code.highcharts.com/modules/series-label.js"></script>
    <script src="https://code.highcharts.com/modules/exporting.js"></script>
    <script src="https://code.highcharts.com/modules/export-data.js"></script>
    <script src="https://code.highcharts.com/modules/accessibility.js"></script>
    <%--
    <%@include file="head.jspf" %>
    --%>
    <%@include file="head.jspf" %>
    <style>
        .highcharts-figure, .highcharts-data-table table {
            min-width: 360px; 
            max-width: 800px;
            margin: 1em auto;
        }

        .highcharts-data-table table {
            font-family: Verdana, sans-serif;
            border-collapse: collapse;
            border: 1px solid #EBEBEB;
            margin: 10px auto;
            text-align: center;
            width: 100%;
            max-width: 500px;
        }
        .highcharts-data-table caption {
            padding: 1em 0;
            font-size: 1.2em;
            color: #555;
        }
        .highcharts-data-table th {
            font-weight: 600;
            padding: 0.5em;
        }
        .highcharts-data-table td, .highcharts-data-table th, .highcharts-data-table caption {
            padding: 0.5em;
        }
        .highcharts-data-table thead tr, .highcharts-data-table tr:nth-child(even) {
            background: #f8f8f8;
        }
        .highcharts-data-table tr:hover {
            background: #f1f7ff;
        }      



    </style>

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
                    <div id="intro" style="background-color: white;">
                        <div class="panel-pane pane-custom pane-3 clearfix">
                            <h2 class="pane-title header">Enzyme Portal Release Statistics</h2>

                            <div class="large-12 columns">

                                <div class="row">
                                    <div class="large-6 columns"> 

                                        <figure class="highcharts-figure">

                                            <div id="barc"></div>



                                        </figure>  
                                    </div>
                                    <div class="large-6 columns">
                                        <figure class="highcharts-figure">
                                            <div id="bar_cat"></div>    
                                        </figure>

                                    </div>



                                    <h4 class="subheader">Release Statistics for ${month}, ${year}</h4>
                                    <table class="responsive-card-table unstriped">
                                        <thead>
                                            <tr>

                                                <th>Description</th>
                                                <th>Resource</th>
                                                <th>Number of unique entries</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr>

                                                <td>Enzyme entries (Protein Level)</td>
                                                <td data-label="uniprot">UniProtKB</td>
                                                <td data-label="uniprot">${webStat.uniprot}</td>

                                            </tr>
                                            <tr>
                                                <td>Enzyme entries with Experimental Evidence</td>
                                                <td data-label="exp">UniProtKB</td>

                                                <td data-label="exp">${webStat.expEvidence}</td>
                                            </tr>
                                            <tr>

                                                <td>Enzyme entries with Protein Structures</td>
                                                <td data-label="pdb">PDBe</td>
                                                <td data-label="pdb">${webStat.proteinStructure}</td>

                                            </tr>
                                            <tr>

                                                <td>Enzyme entries with Diseases</td>
                                                <td data-label="omim">OMIM</td>
                                                <td data-label="omim">${webStat.disease}</td>

                                            </tr>
                                            <tr>

                                                <td>Enzyme entries with Pathways</td>
                                                <td data-label="reactome">Reactome</td>
                                                <td data-label="reactome">${webStat.pathways}</td>

                                            </tr>
                                            <tr>

                                                <td>Enzyme entries with Catalytic Activities</td>
                                                <td data-label="catalyticActivities">UniProtKB</td>
                                                <td data-label="catalyticActivities">${webStat.catalyticActivities}</td>

                                            </tr>

                                            <tr>

                                                <td>Enzyme entries with Reactions</td>
                                                <td data-label="rhea">RHEA</td>
                                                <td data-label="rhea">${webStat.rheaReaction}</td>

                                            </tr>
                                            <tr>

                                                <td>Enzyme entries with Metabolites</td>
                                                <td data-label="metabolights">MetaboLights</td>
                                                <td data-label="metabolights">${webStat.metabolites}</td>

                                            </tr>
                                            <tr>

                                                <td>Enzyme entries with Cofactors</td>
                                                <td data-label="cofactors">ChEBI</td>
                                                <td data-label="cofactors">${webStat.cofactors}</td>

                                            </tr>
                                            <tr>

                                                <td>Enzyme entries with Activators</td>
                                                <td data-label="activators">ChEMBL</td>
                                                <td data-label="activators">${webStat.activators}</td>

                                            </tr>
                                            <tr>

                                                <td>Enzyme entries with Inhibitors</td>
                                                <td data-label="inhibitors">ChEMBL</td>
                                                <td data-label="inhibitors">${webStat.inhibitors}</td>

                                            </tr>

                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>





                    </div>
                    <div class="large-12 columns ">

                        <div class="row">
                            <hr/>
                            <h3 class="subheader">Cross-referenced databases</h3>
                            <div class="large-6 columns">
                                <figure class="highcharts-figure">

                                    <div id="pie-chart"></div>

                                </figure>
                            </div>

                            <div class="large-6 columns">
                                <figure class="highcharts-figure">
                                    <div id="bar"></div>
                                </figure>
                            </div>
                            <%--
                            <h4 class="subheader">Release Statistics (XREF) for ${month}, ${year}</h4>
                            <table class="responsive-card-table unstriped">
                                <thead>
                                    <tr>


                                        <th>Resource</th>
                                        <th>No. Unique Xrefs</th>
                                    </tr>
                                </thead>
                                <tbody>

                                    <tr>


                                        <td data-label="proteinStructure">PDBe</td>
                                        <td data-label="proteinStructure">${webStat.pdb}</td>

                                    </tr>
                                    <tr>


                                        <td data-label="disease">OMIM</td>
                                        <td data-label="disease">${webStat.omim}</td>

                                    </tr>
                                    <tr>


                                        <td data-label="pathways">Reactome</td>
                                        <td data-label="pathways">${webStat.reactome}</td>

                                    </tr>

                                    <tr>


                                        <td data-label="metabolites">MetaboLights</td>
                                        <td data-label="metabolites">${webStat.metabolights}</td>

                                    </tr>
                                    <tr>


                                        <td data-label="rheaReaction">RHEA</td>
                                        <td data-label="rheaReaction">${webStat.rhea}</td>

                                    </tr>
                                    <tr>


                                        <td data-label="cofactors">ChEBI</td>
                                        <td data-label="cofactors">${webStat.chebi}</td>

                                    </tr>
                                    <tr>


                                        <td data-label="activators">ChEMBL</td>
                                        <td data-label="activators">${webStat.chembl}</td>

                                    </tr>


                                </tbody>
                            </table>
                                        --%>
                        </div>
                    </div>


                    <div class="large-12 columns ">
                        <figure class="highcharts-figure">

                            <div id="container"></div>
                        </figure>     


                    </div>                                                        

                    <div class="large-4 columns hidden">

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

                <!--bar chart-->

                <script>
                    $(document).ready(function () {
                        $.ajax({
                            type: 'GET',
                            url: '/enzymeportal/service/releases/list',
                            success: function (json) {

                                //var uniprot = [];
                                var intenz = [];
                                var omim = [];
                                var pdb = [];
                                var metabolights = [];
                                var kegg = [];
                                var rhea = [];
                                var reactome = [];
                                var chembl = [];
                                var chebi = [];
                                var months = [];
                              
                                $.each(json, function (index, dataItem) {

                                    //uniprot.push(dataItem.uniprot);
                                    intenz.push(dataItem.intenz);
                                    omim.push(dataItem.omim);
                                    pdb.push(dataItem.pdbe);
                                    metabolights.push(dataItem.metabolights);
                                    kegg.push(dataItem.kegg);
                                    rhea.push(dataItem.rhea);
                                    reactome.push(dataItem.reactome);
                                    chembl.push(dataItem.chembl);
                                    chebi.push(dataItem.chebi);
                                    months.push(dataItem.monthShortName);
                                 
                                   

                                });

//                                Highcharts.setOptions({
//                                    colors: ['#058DC7', '#207a7a', '#50B432', '#ED561B', '#DDDF00', '#24CBE5', '#64E572', '#FF9655', '#FFF263', '#6AF9C4']
//                                });
                                Highcharts.chart('bar', {
                                    chart: {
                                        type: 'column'
                                    },

                                    title: {
                                        text: '<h4>Enzyme Portal cross-references - ${year}</h4>'
                                        
                                    },

//                                    subtitle: {
//                                        text: 'Source: http://www.ebi.ac.uk/enzymeportal/'
//                                    },

                                    credits: {
                                        enabled: false
                                    },

                                    xAxis: {
                                        categories: [months[0], months[1], months[2], months[3], months[4], months[5], months[6], months[7], months[8], months[9], months[10], months[11]], crosshair: true
                                    },
                                    yAxis: {
                                        min: 0,
                                        title: {
                                            text: 'Number of unique entries.'
                                        }
                                    },
                                    tooltip: {
                                        headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                                        pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                                                '<td style="padding:0"><b>{point.y:.1f} </b></td></tr>',
                                        footerFormat: '</table>',
                                        shared: true,
                                        useHTML: true
                                    },
                                    plotOptions: {
                                        column: {
                                            pointPadding: 0.2,
                                            borderWidth: 0
                                        }
                                    },

                                    series: [

//uniprot, pdb, reactome,rhea, intenz, chebi, kegg, metabolight, omim, chembl

                                        {
                                            name: 'PDBe',
                                            data: pdb,
                                            color: '#74b360'
                                        },
                                        {
                                            name: 'Reactome',
                                            data: reactome,
                                            color: '#2F9EC2'
                                        },
                                        {
                                            name: 'Rhea',
                                            data: rhea,
                                            color: '#e60003'
                                        },
                                        {
                                            name: 'ChEBI',
                                            data: chebi,
                                            color: '#0E4976'
                                        },
                                        {
                                            name: 'Kegg',
                                            data: kegg,
                                            //color: '#660033'
                                            color: '#eca5c9'

                                        },
                                        {
                                            name: 'MetaboLights',
                                            data: metabolights,
                                            color: '#de8b0f'

                                        },
                                        {
                                            name: 'Omim',
                                            data: omim,
                                            color: '#333'
                                        },
                                        {
                                            name: 'ChEMBL',
                                            data: chembl,
                                            color: '#207a7a'
                                        }],

                                    responsive: {
                                        rules: [{
                                                condition: {
                                                    maxWidth: 500
                                                },
                                                chartOptions: {
                                                    legend: {
                                                        layout: 'horizontal',
                                                        align: 'center',
                                                        verticalAlign: 'bottom'
                                                    }
                                                }
                                            }]
                                    },
                                    exporting: {

                                        buttons: {
                                            contextButton: {
                                                menuItems: ['viewFullscreen', 'printChart', 'separator', 'downloadPNG', 'downloadJPEG', 'downloadPDF', 'downloadSVG']
                                            }
                                        }
                                    }

                                });
                                //end chart 
                            }
                        });
                    });
                </script> 

                <!--end bar chart-->
                <!--pie chart-->
                <script>
                    $(document).ready(function () {
                        $.ajax({
                            type: 'GET',
                            url: '/enzymeportal/service/releases',
                            success: function (json) {


//
//                                Highcharts.setOptions({
//                                    colors: ['#058DC7', '#207a7a', '#50B432', '#ED561B', '#DDDF00', '#24CBE5', '#64E572', '#FF9655', '#FFF263', '#6AF9C4']
//                                });
                                Highcharts.chart('pie-chart', {
                                    chart: {
                                        plotBackgroundColor: null,
                                        plotBorderWidth: null,
                                        plotShadow: false,
                                        type: 'pie'
                                    },

                                    title: {
                                        text: 'Enzyme Portal cross-references - ${month}, ${year}'
                                    },
//                                    subtitle: {
//                                        text: 'Source: http://www.ebi.ac.uk/enzymeportal/'
//                                    },

                                    tooltip: {
                                        pointFormat: '<b>{point.percentage:.1f}%</b> of all {series.total} cross referenced entries'
                                    },
                                    accessibility: {
                                        point: {
                                            valueSuffix: '%'
                                        }
                                    },
                                    credits: {
                                        enabled: false
                                    },

                                    plotOptions: {
                                        pie: {
                                            allowPointSelect: true,
                                            cursor: 'pointer',
                                            dataLabels: {
                                                enabled: true
                                            },
                                            showInLegend: true
                                        }
                                    },

                                    series: [{
                                            name: 'Enzyme Portal',
                                            colorByPoint: true,
                                            data: [

                                                {
                                                    name: 'Omim',
                                                    y: json.omim,
                                                    color: '#333'
                                                }, {
                                                    name: 'PDBe',
                                                    y: json.pdbe,
                                                    color: '#74b360'
                                                }, {
                                                    name: 'MetaboLights',
                                                    y: json.metabolights,
                                                    color: '#de8b0f'
                                                }, {

                                                    name: 'Kegg',
                                                    y: json.kegg,
                                                    // color: '#660033'
                                                    color: '#eca5c9'

                                                }, {
                                                    name: 'Rhea',
                                                    y: json.rhea,
                                                    color: '#e60003'
                                                }, {

                                                    name: 'Reactome',
                                                    y: json.reactome,
                                                    color: '#2F9EC2'
                                                }, {

                                                    name: 'ChEMBL',
                                                    y: json.chembl,
                                                    color: '#207a7a'
                                                }, {
                                                    name: 'ChEBI',
                                                    y: json.chebi,
                                                    color: '#0E4976'
                                                }]
                                        }],

                                    exporting: {

                                        buttons: {
                                            contextButton: {
                                                menuItems: ['viewFullscreen', 'printChart', 'separator', 'downloadPNG', 'downloadJPEG', 'downloadPDF', 'downloadSVG']
                                            }
                                        }
                                    }

                                });
                                //end chart 
                            }

                        });
                    });
                </script> 
                <!--end pie chart-->




                <!--bar chart components-->

                <script>
                    $(document).ready(function () {
                        $.ajax({
                            type: 'GET',
                            url: '/enzymeportal/service/releases/component/list',
                            success: function (json) {



                                var proteinStructure = [];
                                var disease = [];
                                var pathways = [];
                                var catalyticActivities = [];
                                var metabolites = [];
                                var rheaReaction = [];
                                var cofactors = [];
                                var inhibitors = [];
                                var activators = [];

                                $.each(json, function (index, dataItem) {

                                    proteinStructure.push(dataItem.proteinStructure);
                                    disease.push(dataItem.diseases);
                                    pathways.push(dataItem.pathways);
                                    catalyticActivities.push(dataItem.catalyticActivities);
                                    metabolites.push(dataItem.metabolites);
                                    rheaReaction.push(dataItem.reactions);
                                    cofactors.push(dataItem.cofactors);
                                    activators.push(dataItem.activators);
                                    //expEvidence.push(dataItem.expEvidence);
                                    inhibitors.push(dataItem.inhibitors);

                                });

                                const months = new Array();


                    <c:forEach var="cat" items="${category}">
                                months.push("${cat}");

                    </c:forEach>



//                                Highcharts.setOptions({
//                                    colors: ['#058DC7', '#207a7a', '#50B432', '#ED561B', '#DDDF00', '#24CBE5', '#64E572', '#FF9655', '#FFF263', '#6AF9C4']
//                                });
                                Highcharts.chart('bar_cat', {
                                    chart: {
                                        type: 'column'
                                                //zoomType: 'xy'
                                    },

                                    title: {
                                        text: 'Enzyme Portal Data Release -  ${year}'
                                    },

//                                    subtitle: {
//                                        text: 'Source: http://www.ebi.ac.uk/enzymeportal/'
//                                    },

                                    credits: {
                                        enabled: false
                                    },

                                    xAxis: {
                                      
                                        categories: [months[0], months[1], months[2], months[3], months[4], months[5], months[6], months[7], months[8], months[9], months[10], months[11]], crosshair: true
                                    },
                                    yAxis: {
                                        min: 0,
                                        title: {
                                            text: 'Num unique entries'
                                        }
                                    },
                                    tooltip: {
                                        headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                                        pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                                                '<td style="padding:0"><b>{point.y:.1f} </b></td></tr>',
                                        footerFormat: '</table>',
                                        shared: true,
                                        useHTML: true
                                    },
                                    plotOptions: {
                                        column: {
                                            pointPadding: 0.2,
                                            borderWidth: 0
                                        }
                                    },

                                    series: [

                                        {
                                            name: 'Pathways',
                                            data: pathways,
                                            color: '#2F9EC2'
                                        }
                                        ,
                                        {

                                            name: 'Protein Structure',
                                            data: proteinStructure,
                                            color: '#74b360'
                                        },
                                        {
                                            name: 'Diseases',
                                            data: disease,
                                            color: '#333'
                                        },
                                        {
                                            name: 'Inhibitors',
                                            data: inhibitors,
                                            color: '#08f4f4'
                                        },
                                        {
                                            name: 'Activators',
                                            data: activators,
                                            color: '#207a7a'
                                        }


                                    ],

                                    responsive: {
                                        rules: [{
                                                condition: {
                                                    maxWidth: 500
                                                },
                                                chartOptions: {
                                                    legend: {
                                                        layout: 'horizontal',
                                                        align: 'center',
                                                        verticalAlign: 'bottom'
                                                    }
                                                }
                                            }]
                                    },
                                    exporting: {

                                        buttons: {
                                            contextButton: {
                                                menuItems: ['viewFullscreen', 'printChart', 'separator', 'downloadPNG', 'downloadJPEG', 'downloadPDF', 'downloadSVG']
                                            }
                                        }
                                    }

                                });
                                //end chart 
                            }
                        });
                    });
                </script> 

                <script>
                    $(document).ready(function () {
                        $.ajax({
                            type: 'GET',
                            url: '/enzymeportal/service/releases/component/list',
                            success: function (json) {

                                var proteinStructure = [];
                                var disease = [];
                                var pathways = [];
                                var catalyticActivities = [];
                                var metabolites = [];
                                var rheaReaction = [];
                                var cofactors = [];
                                var inhibitors = [];
                                var activators = [];
                                var expEvidence = [];

                                $.each(json, function (index, dataItem) {

                                    proteinStructure.push(dataItem.proteinStructure);
                                    disease.push(dataItem.diseases);
                                    pathways.push(dataItem.pathways);
                                    catalyticActivities.push(dataItem.catalyticActivities);
                                    metabolites.push(dataItem.metabolites);
                                    rheaReaction.push(dataItem.reactions);
                                    cofactors.push(dataItem.cofactors);
                                    activators.push(dataItem.activators);
                                    expEvidence.push(dataItem.expEvidence);
                                    inhibitors.push(dataItem.inhibitors);

                                });

                                const months = new Array();


                    <c:forEach var="cat" items="${category}">
                                months.push("${cat}");

                    </c:forEach>

//                                Highcharts.setOptions({
//                                    colors: ['#058DC7', '#207a7a', '#50B432', '#ED561B', '#DDDF00', '#24CBE5', '#64E572', '#FF9655', '#FFF263', '#6AF9C4']
//                                });
                                Highcharts.chart('barc', {
                                    chart: {
                                        type: 'column'
                                    },

                                    title: {
                                        text: 'Enzyme Portal Data Release - ${year}'
                                    },

//                                    subtitle: {
//                                        text: 'Source: http://www.ebi.ac.uk/enzymeportal/'
//                                    },

                                    credits: {
                                        enabled: false
                                    },

                                    xAxis: {
                                        categories: [months[0], months[1], months[2], months[3], months[4], months[5], months[6], months[7], months[8], months[9], months[10], months[11]], crosshair: true
                                    },
                                    yAxis: {
                                        min: 0,
                                        title: {
                                            text: 'Num unique entries'
                                        }
                                    },
                                    tooltip: {
                                        headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                                        pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                                                '<td style="padding:0"><b>{point.y:.1f} </b></td></tr>',
                                        footerFormat: '</table>',
                                        shared: true,
                                        useHTML: true
                                    },
                                    plotOptions: {
                                        column: {
                                            pointPadding: 0.2,
                                            borderWidth: 0
                                        }
                                    },

                                    series: [
 
                                        {
                                            name: 'Catalytic Activities',
                                            data: catalyticActivities,
                       
                                            color: '#dfb3b4'
                                        },
                                        {
                                            name: 'Reactions',
                                            data: rheaReaction,
                                            color: '#e60003'

                                        },
                                        {
                                            name: 'Metabolites',
                                            data: metabolites,
                                            color: '#de8b0f'

                                        },
                                        {
                                            name: 'Cofactors',
                                            data: cofactors,
                                            color: '#0E4976'
                                        }

                                    ],

                                    responsive: {
                                        rules: [{
                                                condition: {
                                                    maxWidth: 500
                                                },
                                                chartOptions: {
                                                    legend: {
                                                        layout: 'horizontal',
                                                        align: 'center',
                                                        verticalAlign: 'bottom'
                                                    }
                                                }
                                            }]
                                    },
                                    exporting: {

                                        buttons: {
                                            contextButton: {
                                                menuItems: ['viewFullscreen', 'printChart', 'separator', 'downloadPNG', 'downloadJPEG', 'downloadPDF', 'downloadSVG']
                                            }
                                        }
                                    }

                                });
                                //end chart 
                            }
                        });
                    });
                </script> 





                <!--end bar chart components-->


            </div>
        </div>
        <!--! end of #wrapper -->

    </body>

</html>


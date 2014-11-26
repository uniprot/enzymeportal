<%-- 
    Document   : organisms
    Created on : Nov 3, 2014, 4:45:22 PM
    Author     : joseph
--%>


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="Fn" uri="/WEB-INF/epTagLibray.tld" %>
<!DOCTYPE html>
<html>
<head>
    <c:set var="pageTitle" value="Model Organisms"/>
    <%@include file="head.jspf" %>
</head>
<body class="level2"><!-- add any of your classes or IDs -->
<div id="skip-to">
    <ul>
        <li><a href="#content">Skip to main content</a></li>
        <li><a href="#local-nav">Skip to local navigation</a></li>
        <li><a href="#global-nav">Skip to EBI global navigation menu</a></li>
        <li><a href="#global-nav-expanded">Skip to expanded EBI global navigation menu (includes all sub-sections)</a>
        </li>
    </ul>
</div>

<div id="wrapper" class="container_24">
    <%@include file="header.jspf" %>
    <script src="http://d3js.org/d3.v3.min.js"></script>
    <style>

    .node circle {
      fill: #fff;
      stroke: steelblue;
      stroke-width: 1.5px;
    }

    .node {
      font: 10px sans-serif;
    }

    .link {
      fill: none;
      stroke: #ccc;
      stroke-width: 1.5px;
    }

    text {
        font-size: 1.2em;
    }
    </style>
    <script>
    var data = {
        "name":"",
        "children":[
        {
            "name":"Bacteria",
            "children":[
                {
                    "name":"Bacillus subtilis (strain 168)", 
                    "taxid":224308
                },{
                    "name":"Escherichia coli (strain K12)", 
                    "taxid":83333
                },{
                    "name":"Mycobacterium tuberculosis (strain ATCC 25618 / H37Rv)", 
                    "taxid":83332
                }
        ]},
        {
            "name":"Eukaryota",
            "children":[
                {
                    "name":"Dictyostelium discoideum",
                    "taxid":44689
                },{
                    "name":"Viridiplantae",
                    "children":[
                        {
                            "name":"Oryza sativa subsp. japonica",
                            "taxid":39947
                        },{
                            "name":"Arabidopsis thaliana",
                            "taxid":3702
                        }
                    ]
                },{
                    "name":"Metazoa",
                    "children":[
                        {
                            "name":"Ecdysozoa",
                            "children":[{
                                    "name":"Drosophila melanogaster",
                                    "taxid":7227
                                },{
                                    "name":"Caenorhabditis elegans",
                                    "taxid":6239
                                }
                            ]
                        },
                        {
                            "name":"Vertebrata",
                            "children":[
                                {
                                    "name":"Danio rerio",
                                    "taxid":7955
                                },{
                                    "name":"Mammalia",
                                    "children":[
                                        {
                                            "name":"Bos taurus",
                                            "taxid":9913
                                        },{
                                            "name":"Euarchontoglires",
                                            "children":[
                                                {
                                                    "name":"Homo sapiens",
                                                    "taxid":9606
                                                },{
                                                    "name":"Rodentia",
                                                    "children":[{
                                                        "name":"Mus musculus",
                                                        "taxid":10090
                                                    },{
                                                        "name":"Rattus norvegicus",
                                                        "taxid":10116
                                                    }]
                                                }
                                            ]
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                }

            ]
        }
        ]
    };

    </script>

    <div id="content" role="main" class="grid_24 clearfix">
        <h2>Taxonomy</h2>
        <div id="taxonomy-tree"></div>
    </div>

    <script>

        var width = 960,
                height = 900;

        var cluster = d3.layout.cluster()
                .size([height, width - 200]);

        var diagonal = d3.svg.diagonal()
                .projection(function(d) { return [d.y, d.x]; });

        var svg = d3.select("#taxonomy-tree").append("svg")
                .attr("width", width)
                .attr("height", height)
                .append("g")
                .attr("transform", "translate(40,0)");

            var nodes = cluster.nodes(data),
                    links = cluster.links(nodes);

            var link = svg.selectAll(".link")
                    .data(links)
                    .enter().append("path")
                    .attr("class", "link")
                    .attr("d", diagonal);

            var node = svg.selectAll(".node")
                    .data(nodes)
                    .enter().append("g")
                    .attr("class", "node")
                    .attr("transform", function(d) { return "translate(" + d.y + "," + d.x + ")"; })

            node.append("circle")
                    .attr("r", 4.5);

            node.append("a")
                    .attr("xlink:href", function(d){
                        return "/enzymeportal/taxonomy?entryid=" + d.taxid + "&entryname=" + d.name + "&AMP;searchparams.type=KEYWORD&searchparams.previoustext=" + d.name + "&searchparams.start=0&searchparams.text="+ d.name;
                    })
                .append("text")
                    .attr("dx", function(d) { return d.children ? -8 : 8; })
                    .attr("dy", 3)
                    .style("text-anchor", function(d) { return d.children ? "end" : "start"; })
                    .text(function(d) { return d.name; });

        d3.select(self.frameElement).style("height", height + "px");

    </script>

    <%@include file="footer.jspf" %>
</div>
<!--! end of #wrapper -->
</body>
</html>

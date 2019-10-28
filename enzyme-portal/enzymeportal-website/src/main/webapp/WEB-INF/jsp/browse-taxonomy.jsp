<%--
    Document   : organisms
    Created on : Nov 3, 2014, 4:45:22 PM
    Author     : joseph
--%>


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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

        <div id="wrapper">
            <%@include file="header.jspf" %>
            <script src="https://d3js.org/d3.v3.min.js"></script>

            <div id="content" role="main" class="clearfix">
                <h1>Taxonomy</h1>

                <div class="container-browse-search">
                    <input id="taxonomy-input" autocomplete="off" type="text" placeholder="Organism's name e.g homo sapiens, Rat">
                </div>        


                <div id="taxonomy-tree"></div>
                <img id="spinner" src="${pageContext.request.contextPath}/resources/images/loading128.gif" style="display:none;" class="center"/>
            </div>

            <!--    autocomplete-->


            <script>
                var options = {
                    url: function (phrase) {
                        return "/enzymeportal/service/taxonomy-autocomplete-service";
                    },
                    placeholder: "Organism's name e.g homo sapiens, Rat e.t.c",
                    getValue: function (element) {
                       // return element.scientificName + "(" + element.commonName + ")";
                        return element.scientificName;
                        //return  element.commonName ;
                    },
                    highlightPhrase: true,
                    template: {
                        type: "description",
                        fields: {
                            description: "commonName"
                        }
                    },

                    ajaxSettings: {
                        dataType: "json",
                        method: "GET",
                        data: {
                            dataType: "json"
                        }
                    },

                    preparePostData: function (data) {
                        data.name = $("#taxonomy-input").val();

                        return data;
                    },

                    list: {
                        maxNumberOfElements: 30,
                        match: {
                            enabled: false
                        }
                        ,
                        onChooseEvent: function () {
                            var clickedName = $("#taxonomy-input").getSelectedItemData().commonName;

                            var clickedId = $("#taxonomy-input").getSelectedItemData().taxId;


                            var url = '/enzymeportal/enzymes?searchKey='
                                    + clickedId
                                    + '&searchparams.start=0&searchparams.text='
                                    + clickedName
                                    + '&keywordType=TAXONOMY'
                                    + '&searchId=' + clickedId;

                            console.log(url);

                            window.location.href = url;
                        }
                    }
                };

                $("#taxonomy-input").easyAutocomplete(options);
            </script>

            <!--    d3 tree-->
            <script>
                var data;
                var element = $("#taxonomy-tree");
                function getOrganismHierarchy() {
                    return $.ajax("/enzymeportal/resources/javascript/organisms.json").then(function (d) {
                        return d;
                    });
                }

                function getOrganismCount(taxids) {
                    return $.ajax({
                        url: '/enzymeportal/service/taxonomy-service',
                        type: 'get',
                        data: {taxids: taxids.toString()},
                        success: function (response) {
                            return response;
                        },
                        error: function () {
                            // alert("error");
                        }
                    });
                }

                function organismService() {
                    $("#spinner").show();
                    var taxids = [];
                    getOrganismHierarchy().then(function (data) {
                        getTaxids(data, taxids);
                        var organismCountsPromise = getOrganismCount(taxids).then(function (count) {
                            var mashed = mashData(data, count);
                            draw(mashed);
                        });
                    });

                    getTaxids = function (organism, taxids) {
                        if (organism.taxid) {
                            taxids.push(organism.taxid);
                        }
                        if (organism.children) {
                            organism.children.forEach(function (d) {
                                getTaxids(d, taxids);
                            });
                        }
                    };

                    mashData = function (organism, counts) {
                        if (organism.taxid) {
                            counts.forEach(function (info) {
                                if (organism.taxid === info.taxId)
                                    organism.num_enzymes = info.numEnzymes;
                            });
                        }
                        if (organism.children) {
                            organism.children.forEach(function (d) {
                                mashData(d, counts);
                            });
                        }
                        return organism;
                    };

                    draw = function (data) {
                        var width = 960,
                                height = 800;

                        var cluster = d3.layout.cluster()
                                .size([height, width - 470]);

                        var diagonal = d3.svg.diagonal()
                                .projection(function (d) {
                                    return [d.y, d.x];
                                });

                        var svg = d3.select(element[0]).append("svg")
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
                                .attr("transform", function (d) {
                                    return "translate(" + d.y + "," + d.x + ")";
                                });

                        node.append("circle")
                                .attr("r", 4.5);

                        node.append("a")
                                .attr("xlink:href", function (d) {

                                    if (d.taxid) {
                                        return '/enzymeportal/enzymes?searchKey=' + d.name
                                                + '&searchparams.type=KEYWORD&searchparams.previoustext='
                                                + d.name
                                                + '&searchparams.start=0&searchparams.text='
                                                + d.name
                                                + '&keywordType=TAXONOMY'
                                                + '&searchId=' + d.taxid;
                                    }
                                })
                                .append("text")
                                .attr("dx", function (d) {
                                    return d.children ? -8 : 8;
                                })
                                .attr("dy", 3)
                                .attr("class", function (d) {
                                    return d.taxid ? "endnode" : "midnode"
                                })
                                .style("text-anchor", function (d) {
                                    return d.children ? "end" : "start";
                                })
                                .text(function (d) {
                                    return (d.num_enzymes) ? d.name + " - " + d.num_enzymes + " enzymes" : d.name;
                                });

                        d3.select(self.frameElement).style("height", height + "px");
                        $("#spinner").hide();
                    };
                    //}
                    // };
                }
                ;
                organismService();
            </script>



            <%@include file="footer.jspf" %>
        </div>
        <!--! end of #wrapper -->
    </body>
</html>

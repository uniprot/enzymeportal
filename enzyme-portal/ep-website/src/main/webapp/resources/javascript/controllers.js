/**
 *  Module
 *
 * Description
 */
//var enzymeApp = angular.module('enzyme-portal-app', ['ui.bootstrap']);
// var enzymeApp = angular.module('enzyme-portal-app', ['mm.foundation']);

enzymeApp.controller('TypeAheadController', ['$scope', '$http', '$location',
    function ($scope, $http, $location) {

        $scope.idMappings = [];

        $scope.searchForEnzymes = function (val) {
            return $http.get('/enzymeportal/service/search', {
                params: {
                    name: val
                }
            }).then(function (response) {

                return $.map(response.data, function (s, i) {
                    return s.suggestion;
                });

            });
        };

        $scope.getPathways = function (val) {
            return $http.get('/enzymeportal/service/pathways', {
                params: {
                    name: val
                }
            }).then(function (response) {
                return $.map(response.data, function (d, i) {
                    $scope.idMappings[d.pathwayName] = d.pathwayGroupId;
                    return d.pathwayName;
                });
            });
        };

        $scope.getDiseases = function (val) {
            return $http.get('/enzymeportal/service/diseases', {
                params: {
                    name: val
                }
            }).then(function (response) {
                return $.map(response.data, function (d, i) {
                    $scope.idMappings[d.name] = d.id;
                    return d.name;
                });
            });
        };

        $scope.onSelectPathways = function ($model) {
            var name = $model;
            var groupId = $scope.idMappings[$model];
            $scope.selectedItem = $model;


            var url = '/enzymeportal/enzymes?searchKey='
                    + name
                    + '&searchparams.start=0&searchparams.text='
                    + name
                    + '&keywordType=PATHWAYS'
                    + '&searchId=' + groupId;

            window.location.href = url;



        };

        $scope.onSelectDiseases = function ($model) {
            var name = $model;
            var id = $scope.idMappings[$model];

            $scope.selectedItem = $model;
            var url = '/enzymeportal/enzymes?searchKey=' + name
                    + '&searchparams.type=KEYWORD&searchparams.previoustext='
                    + name
                    + '&searchparams.start=0&searchparams.text='
                    + name
                    + '&keywordType=DISEASE'
                    + '&searchId=' + id;

            window.location.href = url;
        };

    }]);

enzymeApp.factory('organismService', function ($http) {
    return {
        getOrganismHierarchy: function () {
            return $http.get('/enzymeportal/resources/javascript/organisms.json')
                    .then(function (resp) {
                        return resp.data;
                    });
        },
        getOrganismCount: function (taxids) {
            return $http.get('/enzymeportal/service/organism-count', {
                params: {
                    taxids: taxids.toString()
                }
            }).then(function (resp) {
                return resp.data;
            });
        }
    };
});


enzymeApp.directive('taxonomyTree', ['$q', 'organismService', function ($q, organismService) {
        return {
            link: function (scope, element, attrs) {
                $("#spinner").show();
                var taxids = [];

                var organismPromise = organismService.getOrganismHierarchy();
                organismPromise.then(function (organisms) {
                    getTaxids(organisms, taxids);
                    var organismCountsPromise = organismService.getOrganismCount(taxids);
                    organismCountsPromise.then(function (count) {
                        var mashed = mashData(organisms, count);
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
                }

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
                }

                draw = function (data) {
                    var width = 960,
                            height = 800;

                    var cluster = d3.layout.cluster()
                            .size([height, width - 470]);

                    var diagonal = d3.svg.diagonal()
                            .projection(function (d) {
                                return [d.y, d.x];
                            });

                    //console.log(element);
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
                            })

                    node.append("circle")
                            .attr("r", 4.5);

                    node.append("a")
                            .attr("xlink:href", function (d) {

                                if (d.taxid) {
                                    return '/enzymeportal/enzymes?searchKey=' + d.taxid
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
                }
            }
        };
    }]);

enzymeApp.controller('TaxonomyTreeController', ['$scope', '$http', 'OrganismCount', function ($scope, $http, OrganismCount) {
        $scope.getCountForOrganisms(function (taxids) {

        });
    }]);

/*
    This code is to handle events related to PCViz widget.
    Requires jQuery and biojs-pcviz components

*/
$(document).ready(function() {
        // This is going to be our UniProt ID
        var uid = $('#pcviz-widget').attr("data-uid");
        var orgUrl = 'http://www.pathwaycommons.org/pcviz/#neighborhood/'+ uid;

        // This requires BioJS-PCViz library
        var biojspcviz = require('biojs-pcviz');

        // This is the HTML element that we are going to fill out based on click events
        var pcvizDescEl =  $("#pcviz-description");

        // Create an instance of PCViz
        var pcviz = new biojspcviz({
            el: $('#pcviz-widget-container'),
            query: uid,
            onLoad: function(msg) {
                // Show overall stats
                var myContent = "Pathway neighborhood of <b>" + uid + "</b> contains <b>" + msg.numberOfNodes +
                    " genes</b> and <b>" + msg.numberOfEdges + " interactions</b>. " +
                    " Click on any of the genes or interactions to see more information. <br><br>" +
                    "Information provided by <a href='http://www.pathwaycommons.org/pc2/' target='_blank'>Pathway Commons 2</a>.";

                if(msg.numberOfNodes<=1) {
                    var myContent = "No interactions found<br><br>" +
                        "Information provided by <a href='http://www.pathwaycommons.org/pc2/' target='_blank'>Pathway Commons 2</a>.";
                    $('#pcviz-widget').hide();
                }

                pcvizDescEl.html( myContent );
                return this;
            },

            onNodeClick: function(msg)  {
                var annotation = msg.annotation;
                var uniprotId = msg.uniprot ? msg.uniprot : annotation.geneUniprotMapping;
                var summary = msg.uniprotdesc == null ? annotation.geneSummary : annotation.uniprotdesc;
                var myContent =  "<table class='grid'>" +
                    "<tr><th>Gene</th><td>" + msg.id + "</td></tr>" +
                    "<tr><th>Aliases</th><td>" + annotation.geneAliases.replace(/:/g, ", ") + "</td></tr>" +
                    "<tr><th>Desciption</th><td>" + summary + "</td></tr>" +
                    "<tr><th>Uniprot ID</th><td><a target='_blank' href='http://www.ebi.ac.uk/enzymeportal/search/" + uniprotId + "/enzyme'>" + uniprotId + "</a></td></tr>" +
                    "</table>";

                pcvizDescEl.html( myContent );
                return this;
            },

            onEdgeClick: function(msg) {
                var myContent =  "<table class='grid'>" +
                    "<tr><th>Interaction</th><td>" + msg.id.replace(/-/g, " ") + "</td></tr>" +
                    "<tr><th>Publications (PMIDs)</th><td>" + msg.pubmed.join(", ") + "</td></tr>" +
                    "<tr><th>Participant #1</th><td>" + msg.source + "</td></tr>" +
                    "<tr><th>Participant #2</th><td>" + msg.target + "</td></tr>" +
                    "<tr><th>Co-Citations</th><td>" + msg.cited + "</td></tr>" +
                    "</table>";
                pcvizDescEl.html( myContent );
                return this;
            },

            onFail: function(msg) {
                // Something went wrong, let's fallback to the default mechanism: open new window
                $("#pcviz-widget").hide();
                console.log("Loading PCViz widget failed: " + msg + ". Opening a new window instead.");
                window.open(orgUrl, "_blank");

                return this;
            }
        });

        // Visualize the neighborhood of uid in a div using this new instance
        pcviz.neighborhood();

    });


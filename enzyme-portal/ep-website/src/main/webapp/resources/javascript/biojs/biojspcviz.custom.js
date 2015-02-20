/*
    This code is to handle events related to PCViz widget.
    Requires jQuery and biojs-pcviz components

*/
$(document).ready(function() {
    $(".pcviz a").click(function(e) {
        e.preventDefault();
        console.log("Gotcha!");

        $("#pcviz-widget").slideDown();
        $("#pcviz-widget-container").html($("#pcviz-widget-iframe").html());
    });

    $("#close-pcviz").click(function(e) {
        e.preventDefault();
        $("#pcviz-widget").slideUp();
    });

    $("#pcviz-widget").hide();
});


$(document).ready( function() {

    receiveMessage = function(event) {
        //if(event.origin !== "http://www.pathwaycommons.org" || event.origin !== "http://awabi.cbio.mskcc.org") { return; }

        // Now let's show the contents in a nice formatted way
        var msg = JSON.parse( event.data );
        var msgType = msg.type;
        var content = msg.content;

        console.log("Message received from the widget:")
        console.log( msg );


        var myEl = $("#pcviz-description").html();

        if(msgType == "pcvizloaded") {
            myEl = "Pathway neighborhood of <b>PDEBC</b> contains <b>" + content.numberOfNodes + " genes</b> and <b>" + content.numberOfEdges + " interactions</b>. " +
            " Click on any of the genes or interactions to see more information. <br><br>" +
            "Information provided by <a href='http://pathwaycommons.org/pc2' target='_blank'>Pathway Commons 2</a>.";

        } else if( msgType == "pcvizclick" ) {
            var info = content.info;

            switch(content.where) {
                case "background":
                    break;
                case "edge":
                    var myEl =  "<table class='grid'>" +
                        "<tr><th>Interaction</th><td>" + info.id.replace(/-/g, " ") + "</td></tr>" +
                        "<tr><th>Publications (PMIDs)</th><td>" + info.pubmed.join(", ") + "</td></tr>" +
                        "<tr><th>Participant #1</th><td>" + info.source + "</td></tr>" +
                        "<tr><th>Participant #2</th><td>" + info.target + "</td></tr>" +
                        "<tr><th>Co-Citations</th><td>" + info.cited + "</td></tr>" +
                        "</table>";
                    break;
                case "node":
                    var annotation = info.annotation;
                    var uniprotId = info.uniprot ? info.uniprot : annotation.geneUniprotMapping;
                    var summary = info.uniprotdesc == null ? annotation.geneSummary : info.uniprotdesc;
                    var myEl =  "<table class='grid'>" +
                        "<tr><th>Gene</th><td>" + info.id + "</td></tr>" +
                        "<tr><th>Aliases</th><td>" + annotation.geneAliases.replace(/:/g, ", ") + "</td></tr>" +
                        "<tr><th>Desciption</th><td>" + summary + "</td></tr>" +
                        "<tr><th>Uniprot ID</th><td><a target='_blank' href='http://www.ebi.ac.uk/enzymeportal/search/" + uniprotId + "/enzyme'>" + uniprotId + "</a></td></tr>" +
                        "</table>";
                    break;
            }
        }


        $("#pcviz-description").html( myEl );
    };

    window.addEventListener("message", receiveMessage, false);
});


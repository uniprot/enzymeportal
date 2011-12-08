/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function() {
    //search default text
    $("#searchbox").live("blur", function(){
          var default_value = $(this).attr("rel");
          if ($(this).val() == ""){
                  $(this).val(default_value);
          }
    }).live("focus", function(){
          var default_value = $(this).attr("rel");
          if ($(this).val() == default_value){
                  $(this).val("");
          }
    });
    /*
     * handles show more and show less links
     */
   $("a.showLink").click(function(event) {
        var clickedId = event.target.id;
        var idClickedSplit = clickedId.split("_");
        /*id of the link is made up by 3 parts:
            part 1: name of the div (eg.: syn) this is used to distinguish the show more
            link of synonyms from the show more link in other divs
            part 2: "link" to distinguish the link for show more link from other
            ordinary links
            part 3: the order of the result item to distinguish the show more button
            in the result list is click. In case of filters of species or compounds
            the order is always 0
            */
        var idPrefixClicked = idClickedSplit[0];
        var itemClicked = idClickedSplit[1];
        var orderOfItemClicked = idClickedSplit[2];
            var idOfHiddenText = "#"+idPrefixClicked+"_"+orderOfItemClicked;
            var jqClickedId= "#"+clickedId;
            var linkValue = $(jqClickedId).text();
            var splitLinkName = linkValue.split(" ");
            if (jQuery.inArray('more', splitLinkName) > -1){
                $(idOfHiddenText).show();
                $(jqClickedId).html(linkValue.replace('more','less'));
            } else {
                $(idOfHiddenText).hide();
                $(jqClickedId).html(linkValue.replace('less','more'));
            }
    });

    var pageClicked = false;
    $("#pagination").click(function(event) {
        var clickedId = event.target.id;
        var nextStart = $("#nextStart").val();
        var prevStart = $("#prevStart").val();
        if (clickedId == "prevButton") {
            $("#start").val(prevStart);
            pageClicked = true;
            $('#searchButton').trigger("click");
        } else if (clickedId == "nextButton"){
            $("#start").val(nextStart);
            pageClicked = true;
            $('#searchButton').trigger("click");
        }
    });

    $("#searchButton").click(function(event) {
        if (!pageClicked)
            $("#start").val(0);
    });

/**
 * Submits form when Filter button is clicked
 */
    $("#filterButton").click(function(event) {
        $('#searchButton').trigger("click");
    });

    /**
     * Save the form parameters for the breadcrumb link
     */

    $("#searchForm").submit( function () {        
        var breadcrumb=$('#searchForm').serialize();
         $("#breadcrumb").val(breadcrumb);         
    } );

    $("#searchBreadcrumb").click(function(event) {
        var breadcrumb=$('#searchForm').serialize();
         $("#breadcrumb").val(breadcrumb);
         alert(breadcrumb);
    });
});
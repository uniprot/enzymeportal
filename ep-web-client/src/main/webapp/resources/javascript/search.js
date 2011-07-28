/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


$(document).ready(function() {
    var hiddenSynPrefixId = "#hidenSyns_";
    var showMoreLink = "Show more";
    var showLessLink = "Show less";
   $("a").click(function(event) {
        var clickedId = event.target.id;
        var idClickedSplit = clickedId.split("_");
        var idPrefixClicked = idClickedSplit[0];
        var orderOfItemClicked = idClickedSplit[2];

        var idOfHiddenText = "#"+idPrefixClicked+"_"+orderOfItemClicked;            

        var jqClickedId= "#"+clickedId;
        var newValue = $(jqClickedId).text();

        if (newValue == showMoreLink) {
            $(idOfHiddenText).toggle(true);
            $(jqClickedId).html(showLessLink);
        } else {
            $(idOfHiddenText).toggle(false);
             $(jqClickedId).html(showMoreLink);
        }

    });
});
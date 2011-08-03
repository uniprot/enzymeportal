/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


$(document).ready(function() {
    //var showMoreLink = "Show more";
    //var showLessLink = "Show less"; 
   $("a").click(function(event) {
       var linkName = "link";
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
        if (itemClicked == linkName) {
            var idOfHiddenText = "#"+idPrefixClicked+"_"+orderOfItemClicked;
            var jqClickedId= "#"+clickedId;
            var linkValue = $(jqClickedId).text();
            var splitLinkName = linkValue.split(" ");
            var size = splitLinkName.length;
            var linkLastName = splitLinkName[size-1];
            
            if (linkLastName == "more") {
                $(idOfHiddenText).show();
                $(jqClickedId).html(linkValue.replace(linkLastName,"less"));
            } else {
                $(idOfHiddenText).hide();
                $(jqClickedId).html(linkValue.replace(linkLastName,"more"));
            }
        }
    });
});
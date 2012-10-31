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
//    $("a.showLink").click(function(event) {
//        var clickedId = event.target.id;
//        var idClickedSplit = clickedId.split("_");
//        /*id of the link is made up by 3 parts:
//            part 1: name of the div (eg.: syn) this is used to distinguish the show more
//            link of synonyms from the show more link in other divs
//            part 2: "link" to distinguish the link for show more link from other
//            ordinary links
//            part 3: the order of the result item to distinguish the show more button
//            in the result list is click. In case of filters of species or compounds
//            the order is always 0
//            */
//        var idPrefixClicked = idClickedSplit[0];
//        var orderOfItemClicked = idClickedSplit[2];
//        var idOfHiddenText = "#"+idPrefixClicked+"_"+orderOfItemClicked;
//        var jqClickedId= "#"+clickedId;
//        var linkValue = $(jqClickedId).text();
//
//        if (linkValue.indexOf(" more about ") > -1){
//            $(idOfHiddenText).show();
//            $(jqClickedId).html(linkValue
//                .replace('more','less').replace('... ',''));
//        } else if (linkValue.indexOf(" less about ") > -1){
//            $(idOfHiddenText).hide();
//            $(jqClickedId).html('... ' + linkValue.replace('less','more'));
//        } else if (linkValue.indexOf(" more ") > -1){
//            $(idOfHiddenText).show();
//            $(jqClickedId).html(linkValue.replace('more','fewer'));
//        } else if (linkValue.indexOf(" fewer ") > -1) {
//            $(idOfHiddenText).hide();
//            $(jqClickedId).html(linkValue.replace('fewer','more'));
//        }
//    });
    
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
            //$("#start").val(prevStart);
            $("#filtersFormStart").val(prevStart);
            pageClicked = true;
            //$('#searchButton').trigger("click");
            document.forms.filtersForm.submit();
        } else if (clickedId == "nextButton"){
            //$("#start").val(nextStart);
            $("#filtersFormStart").val(nextStart);
            pageClicked = true;
            //$('#searchButton').trigger("click");
            document.forms['filtersForm'].submit();
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
    
    //    $(function(){
    //  $('#searchButton').click(function(){
    //    var val = [];
    //    $(':checkbox:checked').each(function(i){
    //      val[i] = $(this).val();
    //    });
    //  });
    //});
     
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

function noImage(source){
	source.src = "resources/images/noStructure-light.png";
	// disable onerror to prevent endless loop
	source.onerror = "";
	source.style.backgroundColor = '#eee';
    source.style.borderRadius='10px';
    source.style.opacity='0.5';
	return true;
}


/**
 * Auto complete for species, compounds and diseases filters
 */
function ResultAutoComplete(id, dataArray,theForm,hiddenCheckbox) {
    
 $( "#"+id ).autocomplete({
            source: dataArray,
            minLength: 1,
                    

                    
            width: 200,
            max: 10,
            highlight: true,
            scroll: true,
            scrollHeight: 300,
            autoFill: true,
            // mustMatch: true,
            matchContains: false,
            formatItem: function(data, i, n, value) {
                return value;
            },
            formatResult: function(data, value) {
                return value;
            },
              
              
            focus: function( event, ui ) {
                       
                               
                return true;
                       
            },
                      
            select: function( event, ui ) {

      
                $( "#"+hiddenCheckbox ).val( ui.item.value );
                                  
                                       
                $("#"+theForm).submit();

                                
                return true;
            }
 
                        
        });

}

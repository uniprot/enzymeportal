$(document).ready(function() {
    //search default text
    //    $("#searchbox").live("blur", function(){
    //    $("#local-searchbox").live("blur", function(){
    //        var default_value = $(this).attr("rel");
    //        if ($(this).val() == ""){
    //            $(this).val(default_value);
    //        }});
    //    }).live("focus", function(){
    //        var default_value = $(this).attr("rel");
    //        if ($(this).val() == default_value){
    //            $(this).val("");
    //        }
    //    });
    //    
    //   
    //    //    for blast search text box
    //    $("#search-keyword-text").live("blur", function(){
    //        var default_value = $(this).attr("rel");
    //        if ($(this).val() == ""){
    //            $(this).val(default_value);
    //        }
    //    }).live("focus", function(){
    //        var default_value = $(this).attr("rel");
    //        if ($(this).val() == default_value){
    //            $(this).val("");
    //        }
    //    });





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
            if (jQuery.inArray('function', splitLinkName) > -1){
                $(jqClickedId).html(linkValue.replace('more','less'));
            } else {
                $(jqClickedId).html(linkValue.replace('more','fewer'));
            }
        } else {
            $(idOfHiddenText).hide();
            $(jqClickedId).html(linkValue.replace(/less|fewer/g,'more'));
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
    
    //    $[ "ui" ][ "autocomplete" ].prototype["_renderItem"] = function( ul, item) {
    //        var keywords = $.trim(this.term).split(' ').join('|');
    //        var output = item.label.replace(new RegExp("(" + keywords + ")", "gi"), '<span class="ui-menu-item-highlight">$1</span>');
    //        //var output = item.label.replace(new RegExp("(" + keywords + ")", "gi"), '<strong>$1</strong>');
    //
    //
    //        return $( "<li></li>" ) 
    //        .data( "item.autocomplete", item )
    //        .append( $( "<a></a>" ).html( output ) )
    //        .appendTo( ul );
    //    };  
    
    

    
    //    to get unique element in the array list
    $.extend({
        distinct : function(anArray) {
            var result = [];
            $.each(anArray, function(i,v){
                if ($.inArray(v, result) == -1) result.push(v);
            });
            return result;
        }
    });


    var unique = $.distinct(dataArray);



    // to remove null values from the array list
    unique = $.grep(unique,function(n){
        //return(n);
        return (n !== " " && n != null);
    });
  
    //auto complete function
    
    $( "#"+id ).autocomplete({
        source: unique,
        minLength: 1,
        delay : 0,     
        
                    
        width: 200,
        max: 10,
        highlight: true,
        scroll: true,
        scrollHeight: 300,
        //autoFill: true,
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


        
//       $.ui.autocomplete.prototype._renderItem = function (ul, item) {
//
//        var keywords = $.trim(this.term).split(' ').join('|');
//        var output = item.label.replace(new RegExp("(" + keywords + ")", "gi"), '<span class="ui-menu-item-highlight">$1</span>');
//         //var output = item.label.replace(new RegExp("(" + keywords + ")", "gi"), '<strong>$1</strong>');
//
//        return $("<li></li>")
//         .append($("<a></a>").html(output))
//            //.append('<a>'  + output + '</a>')
//            .appendTo(ul);
//    };


}



/**
 * The keys in this object represent filter groups, and can take only one of
 * these values:
 * species|compounds|diseases
 * , and map to the arrays of available filters for species, compounds and
 * diseases respectively. The objects in each array have properties 'id' (the
 * scientific name for species) and 'name', and maybe more (see the JSPs using
 * this script).
 * @type {{}}
 * @deprecated
 */
var filterTables = {};

/**
 * The keys in this object represent filter groups, and can take only one of
 * these values:
 * species|compounds|diseases
 * , and map to the arrays of currently applied filters for species, compounds
 * and diseases respectively.
 * @type {{}}
 */
var checkedFilters = {};

var uncheckedFilters = {};

/**
 * The keys in this object represent filter groups, and can take only one of
 * these values:
 * species|compounds|diseases
 * , and map to the number of currently displayed filters for species, compounds
 * and diseases respectively.
 * @type {{}}
 */
var displayedFilters = {};

var maxNum = 20;

/**
 * Adds a checkbox to the filters list.
 * @param filterGroup the group this filter belongs to.
 * @param obj the object to display a filter for.
 * @param selected is the control checked by default?
 */
function addCheckbox(filterGroup, obj, selected){
    if (obj == '') return;
    var cb = $('<input/>', {
        "type":"checkbox", 
        "name":"searchparams."+filterGroup,
        "value": (filterGroup == 'species'? obj.id : obj.name),
        onclick:"form.submit()"
    });
    if (selected) cb.attr("checked", "checked");
    var label = $('<span>');
    if (obj.name){
        label.text(obj.name);
        label.append($('<span>', {
            text:obj.id
        }));
    } else {
        label.text(obj.id);
    }
    $('<div>').addClass("filterItem").addClass(filterGroup).append(cb, label)
    .appendTo($('#'+filterGroup+'_filters_'+(selected? 'y':'n')));
    displayedFilters[filterGroup]++;
}
 
function addCheckboxCompound(filterGroup, obj, selected){
    if (obj == '') return;
    var cb = $('<input/>#accordion', {
        "type":"checkbox", 
        "name":"searchparams."+filterGroup,
        "value": (filterGroup == 'species'? obj.id : obj.name),
        onclick:"form.submit()"
    });
    if (selected) cb.attr("checked", "checked");
   
  
   //add the link to the source of this item 
    var link = $("<a>", {

   href: ""+obj.url+"",
   target :"_blank"
   
  });
  
    //add a span to the link to hold the text to be displayed
    var label = $('<span>').addClass("popup");
   var popup = $('<span>').text(obj.id);

   link.append(popup);
   
   
    if (obj.name){
        label.text(obj.name);
        link.attr("title", obj.name);
        //label.append($('<span>', { text:obj.id}));
        label.append($(link));
    } else {
        label.text(obj.id);
        label.append($(link));
    }
        
     
             
        
    var newItem = $('<div>').addClass("filterItem").addClass(filterGroup).append(cb, label);
        
    if(selected){
        $(newItem).appendTo($('#'+filterGroup+'_filters_y'));
    } else {
    	$(newItem).appendTo($('#'+ obj.role.toLowerCase()));
    }
    var currentSize = $(newItem).siblings().length;
        
    if (currentSize >=  maxNum) {
        $(newItem).addClass("hidden").addClass("extra");
    }
        
 
    displayedFilters[filterGroup]++;
    
}


function addCheckboxDisease(filterGroup, obj, selected){
    if (obj == '') return;
    var cb = $('<input/>', {
        "type":"checkbox", 
        "name":"searchparams."+filterGroup,
        "value": (filterGroup == 'species'? obj.id : obj.name),
        onclick:"form.submit()"
    });
    if (selected) cb.attr("checked", "checked");
 
    
     //add the link to the source of this item 
    var link = $("<a>", {

   href: ""+obj.url+"",
   target :"_blank"
   
  });
  
    //add a span to the link to hold the text to be displayed
    var label = $('<span>').addClass("popup");
   var popup = $('<span>').text(obj.id);

   link.append(popup);
   
   
    if (obj.name){
        label.text(obj.name);
        link.attr("title", obj.name);
        //label.append($('<span>', { text:obj.id}));
        label.append($(link));
    } else {
        label.text(obj.id);
        label.append($(link));
    }
    
    $('<div>').addClass("filterItem").addClass(filterGroup).append(cb, label)
    .appendTo($('#'+filterGroup+'_filters_'+(selected? 'y':'n')));
    displayedFilters[filterGroup]++;
}
/**
 * Add unselected checkboxes to the list of filters.
 * @param filterGroup the group this filter belongs to.
 * @param from index of the first filter to add.
 * @param num number of new filters to add.
 * @param link the link triggering this method.
 */
function addUnselectedCheckboxes(filterGroup, from, num, link){
    var loading = $('#loading_'+link.getAttribute('id'));
    $(link).hide();
    loading.show();
    for (var i = from; i < from+num; i++){
        addCheckbox(filterGroup, uncheckedFilters[filterGroup][i], false);
    }
    loading.hide();
}
/*
 *same as the above method but this is specifically for diseases
 **
 */
function addUnselectedCheckboxesDiseases(filterGroup, from, num, link){
    var loading = $('#loading_'+link.getAttribute('id'));
    $(link).hide();
    loading.show();
    for (var i = from; i < from+num; i++){
        addCheckboxDisease(filterGroup, uncheckedFilters[filterGroup][i], false);
    }
    loading.hide();
}
/**
 * toggle show more and show less
 */
function showMore(anchor) {
    $(anchor).html("Show less");
    $(anchor).attr("onclick" , "showLess(this)");
    $(anchor).siblings('.extra').removeClass('hidden');
    
}

/**
 *toggle show more and show less
 */
function showLess(anchor) {
    var num = $(anchor).siblings().length;
    $(anchor).html("Show all " + num);
    $(anchor).attr("onclick" , "showMore(this)");
    $(anchor).siblings('.extra').addClass('hidden');
    
}


/**
 * adds the show more links when the number of items exceeds maxNum
 */
function addShowMoreLinks() {
    $('#accordion').children('div').each(function() {
            
        var items = $(this).children().length;
        
        if (items >= maxNum) {
            var linkText = "Show all " + items;
        
            $(this).append('<a class="showmore" href="javascript:void(0)" onclick="showMore(this)">'+ linkText+'</a>');
            
        }
        
    });

 

}

/**
 * check content and
 * hides the accordion header (h3) if there are no elements on that section
 */
function checkContent() {
  $('.head').each(function(){
      
      var div = $(this).next();
      
      if ($(div).children().length==0){
          $(this).hide();          
      }
  
  });
}

/**
 * Captures clicks to links in the ChEBI iframe (chemical structure search).
 */
function captureChebiClicks(){
    var frame = $('iframe')[0].contentWindow.document;
    $(frame).on('click', 'a', function(event) {
        // Get the link to ChEBI including the compound name:
        var link = this.parentsUntil('table', 'tr').siblings()
                .find('a:contains("CHEBI")');
        var structure = frame.forms['goBackStructureSearch']
                .structure;
        return false;
    });
}

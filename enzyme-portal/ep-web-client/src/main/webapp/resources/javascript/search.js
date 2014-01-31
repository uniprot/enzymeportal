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
    $("#paginationNav").click(function(event) {
        var clickedId = event.target.id;
        var nextStart = $("#nextStart").val();
        var prevStart = $("#prevStart").val();
        if (clickedId == "prevButton") {
            $("#filtersFormStart").val(prevStart);
            pageClicked = true;
            document.forms.filtersForm.submit();
        } else if (clickedId == "nextButton"){
            $("#filtersFormStart").val(nextStart);
            pageClicked = true;
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


 
$("#"+hiddenCheckbox ).val(ui.item.value);

$("input[type='checkbox'][name='" + hiddenCheckbox + "'][value='" + ui.item.value + "']").prop('checked', true);
       
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
        "value": obj.name,
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


function countItems() {
    $('.num').each(function(){
      
        var div = $(this).parent().next();
        $(this).html('  ( '+$(div).children().size()+' )');

    });
}


var BASKET_SIZE = 'ep.basket.size=';

/**
 * Stores the current number of enzymes in the basket into a cookie, which
 * should expire after the tomcat session expires.
 * @param size
 */
function storeBasketSize(size){
    document.cookie = BASKET_SIZE + size + ';path=/enzymeportal;max-age=1800';
}

/**
 * Shows the current number of enzymes in the basket.
 */
function showBasketSize(){
	var basketSize = 0;
	var dc = document.cookie;
	var bsIndex = dc.indexOf(BASKET_SIZE);
	if (dc && bsIndex > -1){
		bsStart = bsIndex + BASKET_SIZE.length;
		bsEnd = dc.indexOf(';', bsStart);
		basketSize = bsEnd == -1?
				dc.substring(bsStart) : dc.substring(bsStart, bsEnd);
	}
	$('.basketSize').text(basketSize);
}

/**
 * (De)selects one summary for the basket.
 * @param event a change event in a checkbox.
 */
function selectForBasket(event){
    var cb = event.target;
    var id = cb.value;
    var checked = cb.checked;
	ajaxBasket(id, checked);
}

/**
 * (Un)checks all basket checkboxes in the page.
 * Sends one single ajax request with a semicolon-separated list of basket IDs.
 * @param event The event triggering this action, namely the (de)select all
 * 		buttons.
 */
function basketAll(event){
	var id = '';
	$('input.forBasket').each(function(index, elem){
		if (id.length > 0) id += ';';
		id += elem.value;
	});
	var checked = event.target.value == '+';
	ajaxBasket(id, checked);
	$('input.forBasket').each(function(index, elem){
		elem.checked = checked;
		//if (checked) $(elem).attr('checked', 'checked');
		//else $(elem).removeAttr('checked');
	});
}

/**
 * Removes one summary from the basket.
 * @param event The event (button click) triggering this method.
 */
function removeFromBasket(event){
	btn = event.target;
	ajaxBasket(btn.value, false);
	$(btn).parent().parent().remove();
	updateCompareButton();
}

/**
 * Sends an AJAX request to the server to update the basket (add or remove
 * enzymes).
 * @param id the basket ID(s) to be added/removed. Note that events triggered
 * 		from a checkbox send only one basket ID, but buttons send more
 * 		(10 according to the current pagination configuration).
 * @param checked <code>true</code> if the enzyme is added, <code>false</code>
 * 		if removed.
 */
function ajaxBasket(id, checked){
	var thisFunction = this;
    var params = {};
    params.id = id;
    params.checked = checked;
    jQuery.ajax({
    	dataType: "text",
        url: window.location.pathname.replace(/search.*|basket/, "ajax/basket"),
        data: params,
        context: thisFunction,
        success: function(basketSize){
        	storeBasketSize(basketSize);
        	showBasketSize();
        },
        error: function(xhr, status, message){
        	alert(message);
        }
    });
}

/**
 * Updates the compare button (disabled/enabled) according to the number of
 * selected enzymes to compare, and also the text shown according to the
 * total number of remaining summaries in the basket.
 */
function updateCompareButton(){
    var all = 0, sel = 0;
    $('select.toCompare').each(function(){
    	all++;
        if (this.value != '') sel++;
    });
    if (all == 0){
    	$('div#basketEmptyMsg').show();
    	$('div#basketOneMsg').hide();
    	$('div#basketFullMsg').hide();
    } else if (all == 1){
    	$('div#basketEmptyMsg').hide();
    	$('div#basketOneMsg').show();
    	$('div#basketFullMsg').hide();
    } else {
    	$('div#basketEmptyMsg').hide();
    	$('div#basketOneMsg').hide();
    	$('div#basketFullMsg').show();
    	showBasketSize();
    }
    if (sel == 2){
        $('#compareButton').removeAttr('disabled');
        $('#compareButton').attr('title',
                'Proceed to compare selected enzymes');
    } else {
        $('#compareButton').attr('disabled', 'disabled');
        $('#compareButton').attr('title', 'Please select exactly 2 enzymes.');
	}
}

/**
 * Prefix used for the names of Chemical Structure Search parameters stored in
 * the sessionStorage.
 */
var EPCSS_PREFIX = 'EPCSS-';

/**
 * Saves the structure search parameters in session storage for later use.
 */
function saveDrawnStructure(){
    try {
	    var chebiDoc = $('#chebiIframe')[0].contentWindow.document;
	    /* This does not work with IE10:
	    var inputs = $(chebiDoc).find('#goBackStructureSearch').find('input');
	    */
	    var backForm = chebiDoc.getElementById('goBackStructureSearch');
	    if (!backForm){
	    	sessionStorage.removeItem(EPCSS_PREFIX + 'results');
	    	return; // nothing available to store.
	    }
	    // We are seeing the results of the structure search:
	    sessionStorage.setItem(EPCSS_PREFIX + 'results', 'true');
	    var inputs = backForm.getElementsByTagName('input');
	    for (var i = 0; i < inputs.length; i++){
	        var inputName = $(inputs[i]).attr('name');
	        if (typeof inputName != 'undefined'){
	            var inputValue = $(inputs[i]).attr('value');
	            sessionStorage.setItem(EPCSS_PREFIX + inputName, inputValue);
	        }
	    }
	    console.log('Stored inputs');
	    // The image for the drawn structure:
    	var strImg = $(chebiDoc).find('img[src*="randomId"]')[0];
    	if (typeof(strImg) != 'undefined'){
    		saveDrawnImg(strImg);
    	    console.log('Stored image');
    	}
    } catch (e) {
    	console.log("Storage failed: " + e);
    }
}

/*
 * Saves the image of the structure drawn by the user in the session storage
 * under the name <code>drawnImg</code>.
 * @param strImg the HTML img element containing the image.
 */
function saveDrawnImg(strImg){
    var imgCanvas = document.createElement("canvas");
    imgCanvas.width = strImg.width;
    imgCanvas.height = strImg.height;
    var imgContext = imgCanvas.getContext("2d");
    imgContext.drawImage(strImg, 0, 0, strImg.width, strImg.height);
    var imgAsDataURL = imgCanvas.toDataURL("image/png");
    sessionStorage.setItem("drawnImg", imgAsDataURL);
}


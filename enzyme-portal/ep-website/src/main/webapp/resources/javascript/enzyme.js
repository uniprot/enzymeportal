$( document ).ready(function() {
  // Handler for .ready() called.






    $(function() {
        //start function
        $('#structure').click(function(e) {
            //alert();
            /*
            $('.tab.selected').removeClass('selected');
            $('enzymeContent').toggle();
            $('#structure').addClass('selected');
            $('testHidden').toggle();
            */
        });
        //end function




    });




    $(".enzymeName").click(function() {
        $(this).parent().next("#proteinList").toggle();
        $(this).toggleClass("downTwizzle");
        $(this).parent().toggleClass("proteinSectionOpen");

    });

    $(".toggleSpeciesList").click(function() {
          console.log("inside");
          $(this).parent().next(".speciesFullList").toggle();
      });


    var filtersAppliedList = $("#filtersApplied").val();
    var type = typeof(filtersAppliedList);
    filtersAppliedList = filtersAppliedList.replace('[','');
    filtersAppliedList = filtersAppliedList.replace(']','');
    var filtersAppliedArray = filtersAppliedList.split(',');

    $.each(filtersAppliedArray,function (index, value) {
        facetToTick = $.trim(value);
        facetToTick = facetToTick.replace(/\:/g, '_');
        $("#"+facetToTick).prop('checked', true);
    });



});
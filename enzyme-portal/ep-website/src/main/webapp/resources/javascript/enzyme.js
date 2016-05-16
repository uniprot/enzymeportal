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
        console.log("inside");
        $(this).parent().next("#proteinList").toggle();
        $(this).toggleClass("downTwizzle");
        $(this).parent().toggleClass("proteinSectionOpen");

    });








    console.log("2 in enzyme.js");


});
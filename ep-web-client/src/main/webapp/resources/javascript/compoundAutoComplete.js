/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



    function epAutoComplete(data,dataID) {
        var speciesAutocompleteDataSource = [];
                //speciesAutocompleteDataSource.push("${speciesList[i].commonname}","${speciesList[i].scientificname}");
                //speciesAutocompleteDataSource.push("${speciesList[i].scientificname}");
               // speciesAutocompleteDataSource.push("${speciesList[i].commonname}");
               // console.log(speciesAutocompleteDataSource["${i}"]);
               
             // speciesAutocompleteDataSource.push({label:"${speciesList[i].commonname}",value:"${speciesList[i].scientificname}"});
              
              speciesAutocompleteDataSource.push(data, dataID);
              

              //console.log(speciesAutocompleteDataSource);
              
            var theInputBox = document.getElementById("compounds_AUTOCOMPLETE");
            
                $(theInputBox).autocomplete({
                    source: speciesAutocompleteDataSource,
                    minLength: 1,
                    

                    
        width: 200,
        max: 10,
        highlight: false,
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
                       
//                       
//                       $( "#_ctempList" ).val( ui.item.value );
//                       $( "#ctempList_init" ).val( ui.item.value );
//                         //$(".checkitem.span").val( ui.item.value );
//                         $( "#ctempList" ).val( ui.item.value );
//                         $( "#ctempList_selected" ).val( ui.item.value );
//                         $( "#_ctempList_selected" ).val( ui.item.value );
//  
//                        $( "specieAT" ).val( ui.item.value );
                        
                        document.getElementById("compounds_AUTOCOMPLETE").val(ui.item.value);
                        
                        return true;
                       
                      },
                      
                      		select: function( event, ui ) {

                                 $( "#_ctempList" ).val( ui.item.value );
				 $( "#ctempList" ).val( ui.item.value );
                                 $( "#ctempList_init" ).val( ui.item.value );
                                 $( "#ctempList_selected" ).val( ui.item.value );
                                 $( "#_ctempList_selected" ).val( ui.item.value );
                                  
                                 $( "#specieAT" ).val( ui.item.value );
                                
                                $("#filtersForm").submit();

                                
				return true;
			}
 
                        
                });

                
             }
       
                                                                        

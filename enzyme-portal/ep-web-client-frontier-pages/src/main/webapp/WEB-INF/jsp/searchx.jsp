<%-- 
    Document   : search
    Created on : Mar 31, 2011, 7:57:06 PM
    Author     : hongcao
--%>
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib  prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="Fn" uri="/WEB-INF/epTagLibray.tld" %>


<html>
    <head>
        <title>Enzyme Portal</title>
        <link rel="stylesheet"  href="http://www.ebi.ac.uk/inc/css/contents.css"     type="text/css" />
        <link media="screen" href="resources/lib/spineconcept/css/960gs-fluid/grid.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="resources/lib/spineconcept/css/common.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="resources/lib/spineconcept/css/identification.css" type="text/css" rel="stylesheet" />
        <link media="screen" href="resources/lib/spineconcept/css/species.css" type="text/css" rel="stylesheet" />
        <script src="resources/lib/spineconcept/javascript/jquery-1.5.1.min.js" type="text/javascript"></script>
        <script src="resources/lib/spineconcept/javascript/identification.js" type="text/javascript"></script>
        <link href="resources/css/search.css" type="text/css" rel="stylesheet" />
        <script src="resources/javascript/search.js" type="text/javascript"></script>

       <link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" rel="stylesheet" type="text/css"/>
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
        <script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js"></script>
       <script src="http://yui.yahooapis.com/3.4.1/build/yui/yui-min.js"></script>
    </head>
    <body>
        <jsp:include page="header.jsp"/>
        <div class="contents">
            <div class="page container_12">            
                <jsp:include page="subHeader.jsp"/>
                <jsp:include page="searchBox.jsp"/>
                <!--Global variables-->
                <c:set var="showButton" value="Show more"/>
                <c:set var="searchText" value="${searchModel.searchparams.text}"/>
                <c:set var="startRecord" value="${pagination.firstResult}"/>
                <c:set var="searchresults" value="${searchModel.searchresults}"/>
                <c:set var="searchFilter" value="${searchresults.searchfilters}"/>
                <c:set var="summaryEntries" value="${searchresults.summaryentries}"/>
                <c:set var="summaryEntriesSize" value="${fn:length(summaryEntries)}"/>
                <c:set var="totalfound" value="${searchresults.totalfound}"/>
                <c:set var="filterSizeDefault" value="${500}"/>
                <%-- maximum length in words for a text field --%>
                <c:set var="textMaxLength" value="${60}"/>
                
               <c:set var="spTempList" value="${searchFilter.tempSpecies}"/> 
                <c:set var="spTempList1" value="${searchModel.searchresults.searchfilters.tempSpecies}"/>
                <c:set var="tempSize" value="${fn:length(spTempList)}"/>
                 <c:set var="checkedSpecies" value="${searchModel.searchparams.selectedSpecies}"/>
                 <c:set var="checkedSpecies1" value="${searchModel.searchresults.searchfilters.tempSpecies}"/>
                 <c:set var="checkedSpeciesSize" value="${fn:length(checkedSpecies)}"/>
                
                 <c:set var="cpTempList" value="${searchFilter.tempCompounds}"/> 
                 <c:set var="cptempSize" value="${fn:length(cpTempList)}"/>
                 <c:set var="checkedCompounds" value="${searchModel.searchparams.selectedCompounds}"/>
                 
                 <c:set var="dTempList" value="${searchFilter.tempDiseases}"/> 
                 <c:set var="dtempSize" value="${fn:length(dTempList)}"/>
                 <c:set var="checkedDiseases" value="${searchModel.searchparams.selectedDiseases}"/>

                <div class="grid_12 content">
                    <c:if test="${not empty summaryEntries and searchresults.totalfound gt 0}">
                        <div class="filter" style="width: 25%; float: left;">                    
                            <div class="title">
                                Search Filters
                            </div>
                            <div class="line"></div>
                            <form:form id="filtersForm" name="filtersForm" modelAttribute="searchModel" action="search" method="POST">
                                <form:hidden path="searchparams.text" />
                                <form:hidden path="searchparams.previoustext" />
                               <input type="hidden" id="filtersFormStart"
                                       name="searchparams.start" value="0"/>


               <div class="sublevel1"> 

 
       <div class="subTitle">
                                     Species: 
       </div>  
                   
         <!--auto-complete search box-->
 <div class="ui-widget">
   
<!--     <input id="specieAT" itemtype="text" style="width:100%; border-style:ridge; " class="filterSearchBox" placeholder="Search here" />-->
<input id="specieAT" itemtype="text"   class="filterSearchBox" placeholder="Search here" />

<form:checkbox path="searchparams.selectedSpecies" id="_ctempList_selected" value='' type="hidden"></form:checkbox>

        
</div> 

      <div class="filterContent">
                                    <c:set var="speciesList" value="${searchFilter.species}"/>
                                    <c:set var="speciesListSize" value="${fn:length(speciesList)}"/>
                                    <c:set var="limitedDisplay" value="${filterSizeDefault}"/>
                                    
                                    
                                    <c:if test='${not empty speciesList and speciesListSize >0}'>

                                        
                                    </c:if>               
                                    
 <!--            check if the specie was selected, and if selected, display them.-->
                      <span>
                                        
                <div >
                 
              <c:if test='${not empty spTempList}'>
                          
                           <c:forEach var="x" begin="0" end="${tempSize}">
              <c:if test='${not empty spTempList[x].scientificname}'>
     
               <c:set var="science" value="${spTempList[x].scientificname}"/> 
  
             <c:if test="${Fn:contains(checkedSpecies, science) }">
                         
                    <c:choose>
                                <c:when test="${empty spTempList[x].commonname}">
                                    <a href='#'>${spTempList[x].scientificname}</a>

                                </c:when>
                                <c:otherwise>
                                    <a class="scienceName" href='#'>${spTempList[x].commonname} <span>[${spTempList[x].scientificname}]</span></a>
                                </c:otherwise>
                               </c:choose> 
                          
                    <div class="checkItem">
                    <form:checkbox path="searchparams.selectedSpecies"  onclick="submit()" value="${spTempList[x].scientificname}" />

                     </div>
                <div class="clear"></div>
                
             </c:if>
             
        </c:if>       
    
                 </c:forEach>
                            </c:if>
                           
                       </div>
                        
                        </span>  
<!--                   end of selected species filter-->                                   
                                                             
                                    
    <c:if test="${speciesListSize > 0}">
        <c:if test="${speciesListSize <= filterSizeDefault}">
            <c:set var="limitedDisplay" value="${speciesListSize}"/>
        </c:if>

        <script>

        var  speciesAutocompleteDataSource = [];
        var compoundsAutoCompleteDataSource = [];
        var diseaseAutoCompleteDataSource = [];
       </script>
        
        <c:forEach var="i" begin="0" end="${limitedDisplay-1}">
            
             <c:choose>
                
           <c:when test="${!'input#searchparams.species6.checked.value eq checked'}" >
               <p>This is it!</p> 
                             <div class="checkItem">
	     <form:checkbox path="searchparams.species" value="${speciesList[i].scientificname}" title="${speciesList[i].commonname}"  onclick="submit()" />
                </div>
           </c:when>
             <c:otherwise>
                                                

            
           <c:if test='${not empty speciesList[i].scientificname}'>
                <div class="filterLine">
                    <div class="text">
                        <span>
                            
                            <c:choose>
                                <c:when test="${empty speciesList[i].commonname}">
                                    <a href='#'>${speciesList[i].scientificname}</a>
                                    <script>
                                        speciesAutocompleteDataSource.push("${speciesList[i].scientificname}");
                                    </script>

                                </c:when>
                                <c:otherwise>
                                    
<!-- the script for the auto-complete -->

<script>
    $(function()  {
                //speciesAutocompleteDataSource.push("${speciesList[i].commonname}","${speciesList[i].scientificname}");
                //speciesAutocompleteDataSource.push("${speciesList[i].scientificname}");
                //speciesAutocompleteDataSource.push("${speciesList[i].commonname}");
               // console.log(speciesAutocompleteDataSource["${i}"]);
              speciesAutocompleteDataSource.push({label:"${speciesList[i].commonname}",value:"${speciesList[i].scientificname}"});
             
              

              //console.log(speciesAutocompleteDataSource);
            
                $( "#specieAT" ).autocomplete({
                    source: speciesAutocompleteDataSource,
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
                       
                       //$( "#_ctempList" ).val( ui.item.value );
                       //$( "#ctempList_init" ).val( ui.item.value );
                         //$(".checkitem.span").val( ui.item.value );
                         //$( "#ctempList" ).val( ui.item.value );
                         //$( "#ctempList_selected" ).val( ui.item.value );
                         //$( "#_ctempList_selected" ).val( ui.item.value );
  
                        //$( "specieAT" ).val( ui.item.label );//to show scienctific name  uncomment
                        
                        return true;
                       
                      },
                      
                      		select: function( event, ui ) {

                                 //$( "#_ctempList" ).val( ui.item.value );
				 $( "#ctempList" ).val( ui.item.value );
                                 //$( "#ctempList_init" ).val( ui.item.value );
                                 //$( "#_ctempList_selected" ).val( ui.item.value );
                                 $( "#_ctempList_selected" ).val( ui.item.value );
                                  
                                 //$( "#specieAT" ).val( ui.item.label );
                                
                                $("#filtersForm").submit();

                                
				return true;
			}
 
                        
                });

                
             });
        </script> 
                                                                        

                                                                     

<a class="scienceName" href='#'>${speciesList[i].commonname} <span>[${speciesList[i].scientificname}]</span></a>

   </c:otherwise>
    </c:choose>
      </span>
       </div>                                                          
                <div class="checkItem">
	     <form:checkbox path="searchparams.species" value="${speciesList[i].scientificname}" title="${speciesList[i].commonname}"  onclick="submit()" />
                </div>
                <div class="clear"></div>
            </div>
        </c:if>
 
                                                
      </c:otherwise>
                                            
     </c:choose>
    </c:forEach>
    <c:if test="${speciesListSize > filterSizeDefault}">
        <div id="species_0" style="display: none">
            <c:forEach var="i" begin="${filterSizeDefault}" end="${speciesListSize-1}">
                <c:if test="${not empty speciesList[i].scientificname}">
                    <div class="filterLine">
                        <div class="text">
                            <span>
                                <c:choose>
                                    <c:when test="${empty speciesList[i].commonname}">
                                        <a href='#'>${speciesList[i].scientificname}</a>
                                    </c:when>
                                    <c:otherwise>
                                        <a class="scienceName" href='#'>${speciesList[i].commonname} <span>[${speciesList[i].scientificname}]</span></a>
                                    </c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                        <div class="checkItem" >
            <form:checkbox path="searchparams.species" value="${speciesList[i].scientificname}" title="${speciesList[i].commonname}"  onclick="submit()" />
                        </div>                                                            
                        <div class="clear"></div>
                    </div>
                </c:if>
            </c:forEach>
            <c:set var="speciesMoreSize" value="${speciesListSize-filterSizeDefault}"/>
        </div>
        <a class="showLink" onclick="" id="<c:out value='species_link_0'/>"><c:out value="See ${speciesMoreSize} more"/></a> <br/>
    </c:if>
</c:if>
</div>
</div>
                                <%-- The Start of Compounds  --%>
<BR/>


    <div class="sublevel1">
        
   
        
        
        
        
        
        
                                    <div class="subTitle">
                                        Chemical Compounds
                                    </div>
        
                                     <div class="ui-widget">

                                     <input id="compounds_AUTOCOMPLETE" class="filterSearchBox" placeholder="Search here"  />
        <form:checkbox path="searchparams.selectedCompounds" id="comptempList" value="" type="hidden" onclick="submit()"></form:checkbox>
</div>     
        
        
                                    <div class="filterContent">
                                        <c:set var="compoundList" value="${searchFilter.compounds}"/>
                                        <c:set var="compoundListSize" value="${fn:length(compoundList)}"/>
                                        <c:set var="limitedDisplay" value="${filterSizeDefault}"/>
                                        
                                        
                         <span>
                                        
                <div >
                 
              <c:if test='${not empty cpTempList}'>
                          
                           <c:forEach var="x" begin="0" end="${cptempSize}">
              <c:if test='${not empty cpTempList[x].name}'>
     
               <c:set var="compname" value="${cpTempList[x].name}"/> 
  
             <c:if test="${Fn:contains(checkedCompounds, compname) }">
                 
                         
                  
                                    <a href='#'>${cpTempList[x].name}</a>

                                                   
                    <div class="checkItem">
                    <form:checkbox path="searchparams.selectedCompounds"  onclick="submit()" value="${cpTempList[x].name}" />
                       

                     </div>
                <div class="clear"></div>
                   
             </c:if>
             
        </c:if>       
    
                 </c:forEach>
                            </c:if>
                           
                       </div>
                        
                        </span>                                     
                                        
          
                                        
                                        <c:if test="${compoundListSize > 0}">
                                            <c:if test="${compoundListSize <= filterSizeDefault}">
                                                <c:set var="limitedDisplay" value="${compoundListSize}"/>
                                            </c:if>
                                            <c:forEach var="c" begin="0" end="${limitedDisplay-1}">
                                                <div class="filterLine">
   <script>
  $(function()  {

             
                                                                                                                 
                                                         
              compoundsAutoCompleteDataSource.push({label:"${compoundList[c].name} ",value:"${compoundList[c].name}"});
              

              //console.log(speciesAutocompleteDataSource);
            
                $( "#compounds_AUTOCOMPLETE" ).autocomplete({
                    source: compoundsAutoCompleteDataSource,
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
                       
                       $( "#comptempList" ).val( ui.item.value );

  
                        //$( "#compounds_AUTOCOMPLETE" ).val( ui.item.value );
                        
                        return true;
                       
                      },
                      
                      		select: function( event, ui ) {

                                 $( "#comptempList" ).val( ui.item.value );
                                  
                                 $( "#compounds_AUTOCOMPLETE" ).val( ui.item.value );
                                
                                $("#filtersForm").submit();

                                
				return true;
			}
 
                        
                });

                
             }); 
                                                     
                                                     
                                                     
          </script>
          <div class="text">
                                                        <xchars:translate>
                                                                                                                        
                                                            <a class="compoundName" href='#'><c:out value="${compoundList[c].name}" escapeXml="false"/></a>
                                                        </xchars:translate>
                                                    </div>
                                                    <div class="checkItem">
                                                        <form:checkbox path="searchparams.compounds"  value="${compoundList[c].name}" onclick="submit()"/>
                                                    </div>
                                                    <div class="clear"></div>
                                                </div>
                                            </c:forEach>
                                        </c:if>
                                        <c:if test="${compoundListSize > filterSizeDefault}">
                                            <div id="compound_0" style="display: none">
                                                <c:forEach var="c" begin="${filterSizeDefault}" end="${compoundListSize-1}">
                                                    <div class="filterLine">
                                                        <div class="text">
                                                            <xchars:translate>
                                                              <a class="compoundName" href='#'><c:out value="${compoundList[c].name}" escapeXml="false"/></a>
                                                            </xchars:translate>
                                                        </div>
                                                        <div class="checkItem">
                                                            <form:checkbox path="searchparams.compounds" value="${compoundList[c].name}" onclick="submit()"/>
                                                        </div>
                                                        <div class="clear"></div>
                                                    </div>
                                                </c:forEach>
                                            </div>
                                            <c:set var="compoundMoreSize" value="${compoundListSize-filterSizeDefault}"/>
                                            <a class="showLink" id="<c:out value='compound_link_0'/>"><c:out value="See ${compoundMoreSize} more"/></a> <br/>
                                        </c:if>
                                    </div>
                                </div>
                                        <br/> <br/>     

                                <div class="sublevel1" >
                                    
          
  
                                    <div class="subTitle">
                                        Diseases
                                    </div>
                                    
    <div class="ui-widget">
     

      <input id="diseases_AUTOCOMPLETE" class="filterSearchBox"  placeholder="Search here" />
        <form:checkbox path="searchparams.selectedDiseases" id="DtempList" value="" type="hidden" onclick="submit()"></form:checkbox>
</div>                                   
                                                         
                                    
                                    <div class="filterContent">
                                        <c:set var="diseasesList" value="${searchFilter.diseases}"/>
                                        <c:set var="diseasesListSize" value="${fn:length(diseasesList)}"/>
                                        <c:set var="diseasesLimitedDisplay" value="${filterSizeDefault}"/>
                                        
                      <span>
                                        
                <div >
                 
              <c:if test='${not empty dTempList}'>
                          
                           <c:forEach var="x" begin="0" end="${dtempSize}">
              <c:if test='${not empty dTempList[x].name}'>
     
               <c:set var="disname" value="${dTempList[x].name}"/> 
  
             <c:if test="${Fn:contains(checkedDiseases, disname) }">
                 
                         
                  
                                    <a href='#'>${dTempList[x].name}</a>
                                    

                    <div class="checkItem">
                    <form:checkbox path="searchparams.selectedDiseases"  onclick="submit()" value="${dTempList[x].name}" />
                       

                     </div>
                <div class="clear"></div>
                   
             </c:if>
             
        </c:if>       
    
                 </c:forEach>
                            </c:if>
                           
                       </div>
                        
                        </span>                                      
                                        
                                        <c:if test="${diseasesListSize > 0}">
                                            <c:if test="${diseasesListSize <= filterSizeDefault}">
                                                <c:set var="diseasesLimitedDisplay" value="${diseasesListSize}"/>
                                            </c:if>
                                            <c:forEach var="d" begin="0" end="${diseasesLimitedDisplay-1}">
                                                <div class="filterLine">
                                                    
   <script>
  $(function()  {

             
                                                                                                                 
                                                         
              diseaseAutoCompleteDataSource.push({label:"${diseasesList[d].name} ",value:"${diseasesList[d].name}"});
              

              //console.log(speciesAutocompleteDataSource);
            
                $( "#diseases_AUTOCOMPLETE" ).autocomplete({
                    source: diseaseAutoCompleteDataSource,
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
                       
                       $( "#DtempList" ).val( ui.item.value );

  
                        //$( "#compounds_AUTOCOMPLETE" ).val( ui.item.value );
                        
                        return true;
                       
                      },
                      
                      		select: function( event, ui ) {

                                 $( "#DtempList" ).val( ui.item.value );
                                  
                                 $( "#diseases_AUTOCOMPLETE" ).val( ui.item.value );
                                
                                $("#filtersForm").submit();

                                
				return true;
			}
 
                        
                });

                
             }); 
                                                     
                                                     
                                                     
          </script>                                          
                                                    
                                                    
                                                    
                                                    
                                                    
                                                    <div class="text" id="content-block">
                                                        <xchars:translate>
                                                            <a class="diseaseName" href="#"> <c:out value="${diseasesList[d].name}" escapeXml="false"/></a>
                                                        </xchars:translate>
                                                    </div>                                                  
                                                    
                                                    
                                                    <div class="checkItem">
                                                        <form:checkbox path="searchparams.diseases" value="${diseasesList[d].name}" onclick="submit()"/>
                                                    </div>
                                                    <div class="clear"></div>
                                                </div>
                                            </c:forEach>
                                        </c:if>
                                        <c:if test="${diseasesListSize > filterSizeDefault}">
                                            <div id="disease_0" style="display: none">
                                                <c:forEach var="d" begin="${filterSizeDefault}" end="${diseasesListSize-1}">
                                                    <div class="filterLine">
                                                        <div class="text">
                                                            <xchars:translate>
                                                                <c:out value="${diseasesList[d].name}" escapeXml="false"/>
                                                            </xchars:translate>
                                                        </div>
                                                        <div class="checkItem">
                                                            <form:checkbox path="searchparams.diseases" value="${diseasesList[d].id}" onclick="submit()" />
                                                        </div>
                                                        <div class="clear"></div>
                                                    </div>
                                                </c:forEach>
                                            </div>
                                            <c:set var="diseasesMoreSize" value="${diseasesListSize-filterSizeDefault}"/>
                                            <a class="showLink" id="<c:out value='diseases_link_0'/>"><c:out value="See ${diseasesMoreSize} more"/></a> <br/>
                                        </c:if>
                                    </div>
                                </div>



                                <%-- The end of compound --%>





                            </form:form>
                        </div><!-- filter -->
                    </c:if>
                    <div id="keywordSearchResult" class="result"
                         style="width: 70%; float: left;">
                        <c:if test="${totalfound eq 0}">
                            <spring:message code="label.search.empty"/>
                        </c:if>
                        <c:if test="${not empty summaryEntries and searchresults.totalfound gt 0}">
                            <form:form modelAttribute="pagination" >
                                <div style="width: 100%;">
                                    <c:set var="totalPages" value="${pagination.lastPage}"/>
                                    <c:set var="maxPages" value="${totalPages}"/>
                                    <div class="resultText">
                                        <b>${totalfound}</b> results found for <i>${searchText}</i>,
                                        <c:if test="${totalfound ne summaryEntriesSize}">
                                            filtered to <b>${summaryEntriesSize}</b>,
                                        </c:if>
                                        displaying ${pagination.firstResult+1} - ${pagination.lastResult+1}
                                    </div>
                                    <div id="paginationNav" style="text-align: right;">
                                        <c:if test="${totalPages gt pagination.maxDisplayedPages}">
                                            <c:set var="maxPages" value="${pagination.maxDisplayedPages}"/>
                                            <c:set var="showNextButton" value="${true}"/>
                                        </c:if>
                                        <input id="prevStart" type="hidden"
                                               value="${pagination.firstResult - pagination.numberOfResultsPerPage}">
                                        <a id="prevButton" href="javascript:void(0);"
                                           style="display:${pagination.currentPage eq 1? 'none' : 'inline'}">
                                            Previous
                                        </a>
                                        Page ${pagination.currentPage} of ${totalPages}

                                        <c:if test="${pagination.lastResult+1 lt summaryEntriesSize}">
                                            <input id ="nextStart" type="hidden"
                                                   value="${startRecord + pagination.numberOfResultsPerPage}">                                    
                                            <a id="nextButton" href="javascript:void(0);">
                                                Next
                                            </a>
                                        </c:if>                         
                                    </div><!-- pagination -->
                                </div>
                                <%-- Add species filter to this form, don't lose it: --%>
                                <c:forEach var="filterSp" items="${searchModel.searchresults.searchfilters.species}">
                                    <input type="checkbox" style="display: none;" 
                                           name="searchparams.species"
                                           value="${filterSp.scientificname}" />
                                </c:forEach>
                            </form:form>
                            <div class="clear"></div>
                            <div class="line"></div>
                            <div class="resultContent">
                                <c:set var="resultItemId" value="${0}"/>
                                <c:forEach items="${summaryEntries}"
                                           begin="${pagination.firstResult}"
                                           end="${pagination.lastResult}" var="enzyme" varStatus="vsEnzymes">

                                    <%@include file="util/prioritiseSpecies.jsp" %>

                                    <c:set var="primAcc" value="${theSpecies.uniprotaccessions[0]}"/>
                                   
                                    <div class="resultItem">
                                        <c:choose>
                                      <c:when test="${empty enzyme.relatedspecies}">
                                             
                                          <a href="#" ><span class="displayMsg" style="font-size:small;text-align:center " > No Result was found for this Selection.</span></a> 
                                         
                                            </c:when>
                                            <c:otherwise>
                                        <div class="proteinImg">
                                            <c:set var="imgFile" value='${theSpecies.pdbeaccession[0]}'/>
                                            <c:set var="imgFooter" value=""/>
                                            <c:if test="${empty imgFile}">
                                                <c:forEach var="relSp" items="${enzyme.relatedspecies}">
                                                    <c:if test="${empty imgFile and not empty relSp.pdbeaccession}">
                                                        <c:set var="imgFile" value="${relSp.pdbeaccession[0]}"/>
                                                        <c:set var="imgFooter">
                                                            <spring:message code="label.entry.proteinStructure.other.species"/>
                                                            ${empty relSp.species.commonname?
                                                              relSp.species.scientificname : relSp.species.commonname}
                                                        </c:set>
                                                    </c:if>
                                                </c:forEach>
                                            </c:if>
                                            <c:choose>
                                                <c:when test="${empty imgFile}">
                                                    <div style="position: absolute; width: 110px; height: 90px;
                                                         background-color: #fff;
                                                         opacity: 0.6; vertical-align: middle;
                                                         margin-top: 0px; padding: 0px;">No structure available</div>
                                                    <img src="resources/images/noStructure-light.png"
                                                         width="110" height="90" style="border-radius: 10px;"
                                                         alt="No structure available"
                                                         title="No structure available"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:set var="imgLink"
                                                           value="http://www.ebi.ac.uk/pdbe-srv/view/images/entry/${imgFile}_cbc600.png"/>
                                                    <a target="blank" href="${imgLink}">
                                                        <img src="${imgLink}" width="110" height="90"
                                                             alt="PDB ${imgFile}" onerror="noImage(this);"/>
                                                    </a>
                                                    <div class="imgFooter">${imgFooter}</div>
                                                </c:otherwise>
                                            </c:choose>
                                            <c:if test='${imgFile != "" && imgFile != null}'>
                                            </c:if>
                                        </div>
                                        </c:otherwise>
                                        </c:choose>
                                        <div class="desc">
                                            <c:if test='${not empty enzyme.name }'>
                                            <a href="search/${primAcc}/enzyme">
                                                <c:set var="showName" value="${fn:substring(enzyme.name, 0, 100)}"/>
                                                <c:out value="${showName}"/>
                                               <!-- [<c:out value="${enzyme.uniprotid}"/>]-->
                                                [${empty theSpecies.species.commonname?
                                                   theSpecies.species.scientificname :
                                                   theSpecies.species.commonname}]
                                            </a>

                                            </c:if>

                                            <c:if test="${not empty enzyme.function}">
                                                <div>
                                                    <b>Function</b>:
                                                    <c:choose>
                                                        <c:when test="${fn:length(fn:split(enzyme.function, ' ')) gt textMaxLength}">
                                                            <c:forEach var="word" items="${fn:split(enzyme.function,' ')}"
                                                                       begin="0" end="${textMaxLength-1}">
                                                                ${word}</c:forEach>
                                                            <span id="fun_${resultItemId}" style="display: none">
                                                                <c:forEach var="word" items="${fn:split(enzyme.function,' ')}"
                                                                           begin="${textMaxLength}">
                                                                    ${word}</c:forEach>
                                                                </span>
                                                                <a class="showLink" id="fun_link_${resultItemId}">... Show more about function</a>
                                                        </c:when>
                                                        <c:otherwise>
                                                            ${enzyme.function}
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                            </c:if>
                                            <c:set var="synonym" value="${enzyme.synonym}"/>
                                            <c:set var="synonymSize" value="${fn:length(synonym)}"/>
                                            <c:set var="synLimitedDisplayDefault" value="${5}"/>
                                            <c:set var="synLimitedDisplay" value="${synLimitedDisplayDefault}"/>
                                            <c:if test='${synonymSize>0}'>
                                                <div id ="synonym">
                                                    <b>Synonyms</b>:
                                                    <c:if test="${synonymSize > 0 && synonymSize <= synLimitedDisplay}">
                                                        <c:set var="synLimitedDisplay" value="${synonymSize}"/>
                                                    </c:if>

                                                    <c:set var="hiddenSyns" value=""/>                                        
                                                    <c:forEach var="i" begin="0" end="${synLimitedDisplay-1}">
                                                        <c:out value="${synonym[i]}"/>;
                                                    </c:forEach>                                        
                                                    <c:if test="${synonymSize>synLimitedDisplay}">
                                                        <span id='syn_${resultItemId}' style="display: none">
                                                            <c:forEach var="i" begin="${synLimitedDisplay}" end="${synonymSize-1}">
                                                                <c:out value="${synonym[i]}"/>;
                                                            </c:forEach>
                                                        </span>
                                                        <a class="showLink" id="<c:out value='syn_link_${resultItemId}'/>">Show more synonyms</a>
                                                    </c:if>
                                                </div>
                                            </c:if>

                                                
                                                <!-- disease begins here-->

                                                        <div>
                                                            <c:if test="${not empty enzyme.diseases}">
                                                   <b>Disease :</b> 
                                                      
                                                            
                                                  <c:forEach var="eachspecies" items="${enzyme.diseases}">
                                                      
                                                          
                                                      <a href="#" ><span>${eachspecies.name}</span></a>
                                                
                                                 </c:forEach>                   
                                                            </c:if>     
                             
                                                </div>
<!--                                                disease ends here-->   
                                            
                                            <div>
                                                <!-- div id="in">in</div -->
                                                <div>
                                                    
                                                    <%--
                                                    <a href="search/${primAcc}/enzyme">
                                                            [${empty enzyme.species.commonname?
                                                                    enzyme.species.scientificname :
                                                                    enzyme.species.commonname}]
                                                            <!-- ${enzyme.pdbeaccession} -->
                                                    </a>
                                                    --%>
                                                    <!--display = 3 = 2 related species + 1 default species -->
                                                    <c:set var="relSpeciesMaxDisplay" value="${5}"/>
                                                    <c:set var="relspecies" value="${enzyme.relatedspecies}"/>                                        
                                                    <c:set var="relSpeciesSize" value="${fn:length(relspecies)}"/>
                                                    <c:if test="${relSpeciesSize gt 0}">
                                                        <b>Species:</b>
                                                        <c:if test="${relSpeciesSize <= relSpeciesMaxDisplay}">
                                                            <c:set var="relSpeciesMaxDisplay" value="${relSpeciesSize}"/>
                                                        </c:if>
                                                        <c:forEach var="i" begin="0" end="${relSpeciesMaxDisplay-1}">
                                                            <!-- c:if test="${relspecies[i].species.scientificname ne enzyme.species.scientificname}" -->
<!--                                                                <a href="search/${relspecies[i].uniprotaccessions[0]}/enzyme">
                                                                    [${empty relspecies[i].species.commonname?
                                                               relspecies[i].species.scientificname :
                                                               relspecies[i].species.commonname}]-->
                                                            <!-- ${relspecies[i].pdbeaccession} -->
                                                            

                                                   
                                                            <c:choose>
                                                                <c:when test="${empty relspecies[i].species.commonname}">
                                                                    <a class="popup" href='search/${relspecies[i].uniprotaccessions[0]}/enzyme'>[${relspecies[i].species.scientificname}]<span>${relspecies[i].species.scientificname}</span></a>

                                                                </c:when>
                                                                <c:otherwise>
                                                                    <a class="popup" href='search/${relspecies[i].uniprotaccessions[0]}/enzyme'>[${relspecies[i].species.commonname}]<span>${relspecies[i].species.scientificname}</span></a>

                                                                </c:otherwise>
                                                            </c:choose>
                                                            <!--                                                                </a>-->
                                                            <!-- /c:if -->                                             
                                                        </c:forEach>
                                                        <c:if test="${relSpeciesSize > relSpeciesMaxDisplay}">
                                                            <span id="relSpecies_${resultItemId}" style="display: none">
                                                                <c:forEach var = "i" begin="${relSpeciesMaxDisplay}" end="${relSpeciesSize-1}">                                                
<!--                                                                        <a href="search/${relspecies[i].uniprotaccessions[0]}/enzyme">
                                                                            [${empty relspecies[i].species.commonname?
                                                                       relspecies[i].species.scientificname :
                                                                       relspecies[i].species.commonname}]-->
                                                                    <!-- ${relspecies[i].pdbeaccession} -->


                                                                    <c:choose>
                                                                        <c:when test="${empty relspecies[i].species.commonname}">
                                                                            <a class="popup" href='search/${relspecies[i].uniprotaccessions[0]}/enzyme'>[${relspecies[i].species.scientificname}]<span>${relspecies[i].species.scientificname}</span></a>

                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <a class="popup" href='search/${relspecies[i].uniprotaccessions[0]}/enzyme'>[${relspecies[i].species.commonname}]<span>${relspecies[i].species.scientificname}</span></a>

                                                                        </c:otherwise>
                                                                    </c:choose>


                                                                    <!--                                                                        </a>-->
                                                                </c:forEach>
                                                            </span>
                                                            <a class="showLink" id="<c:out value='relSpecies_link_${resultItemId}'/>">Show more species</a>
                                                        </c:if>
                                                    </c:if>
                                                </div>
                                                  </div>




                                        </div>
                                    </div>
                                    <div class="clear"></div>
                                    <c:set var="resultItemId" value="${resultItemId+1}"/>
                                </c:forEach>
                            </div><!-- resultContent -->
                        </c:if>
                            
                    </div><!-- keywordSearchResult -->
                </div><!-- grid_12 content -->
            </div><!--  page container_12 -->
            <jsp:include page="footer.jsp"/>
        </div> <!-- contents -->
    </body>
</html>

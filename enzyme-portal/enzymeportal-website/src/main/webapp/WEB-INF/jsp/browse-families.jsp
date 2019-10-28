<%-- 
    Document   : families
    Created on : Oct 10, 2018, 12:42:39 PM
    Author     : Joseph
--%>


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="Fn" uri="/WEB-INF/epTagLibray.tld" %>
<%@ taglib prefix="ep" tagdir="/WEB-INF/tags/" %>

<!DOCTYPE html>
<html ng-app="enzyme-portal-app">
        <c:set var="pageTitle" value="Browse families"/>
        <%@include file="head.jspf" %>
    <body>
    <div id="wrapper">
        <%@include file="header.jspf" %>

        <div id="content" role="main" class="clearfix" ng-controller="TypeAheadController">
            <h1>Protein Families</h1>


            <div class="container-browse-search">
                 <input id="family-input" autocomplete="off" type="text" placeholder="Protein Family name">
            </div>
            
            


               <script>
                   var options = {
                       url: function(phrase) {
                          return "/enzymeportal/service/families";
                        },

                        getValue: function(element) {
                          return element.familyName;
                        },

                        ajaxSettings: {
                          dataType: "json",
                          method: "GET",
                          data: {
                            dataType: "json"
                          }
                        },


                        preparePostData: function(data) {
                          data.name = $("#family-input").val();
                        
                          return data;
                        },

                       list: {
                           maxNumberOfElements: 20,
                               match: {
                                   enabled: true
                               }
                               ,
                           onChooseEvent: function() {
                               var clickedName = $("#family-input").getSelectedItemData().familyName;
                               //var clickedId = $("#family-input").getSelectedItemData().familyName;
                               var clickedId = $("#family-input").getSelectedItemData().familyGroupId;

                               var url = '/enzymeportal/enzymes?searchKey='
                                       + clickedId
                                       + '&searchparams.start=0&searchparams.text='
                                       + clickedName
                                       + '&keywordType=FAMILIES'
                                       + '&searchId=' + clickedId;

                               console.log(url);

                               window.location.href = url;
                               }
                           }
                   };
                   
                   $("#family-input").easyAutocomplete(options);
               </script>


            <c:if test="${not empty uniprotFamilies}">
                <ep:alphabeticalDisplay items="${uniprotFamilies}" type="families" maxDisplay="5"/>
            </c:if>


        </div>
        <%@include file="footer.jspf" %>
    </div>
    </body>
</html>

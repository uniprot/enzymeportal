<%--
    Document   : pathways
    Created on : Oct 31, 2014, 11:38:19 AM
    Author     : joseph
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="Fn" uri="/WEB-INF/epTagLibray.tld" %>
<%@ taglib prefix="ep" tagdir="/WEB-INF/tags/" %>

<!DOCTYPE html>
<html ng-app="enzyme-portal-app">
        <c:set var="pageTitle" value="Browse pathways"/>
        <%@include file="head.jspf" %>
    <body>
    <div id="wrapper">
        <%@include file="header.jspf" %>

        <div id="content" role="main" class="clearfix" ng-controller="TypeAheadController">
            <h1>Pathways</h1>


            <div class="container-browse-search">
                 <input id="pathway-input" autocomplete="off" type="text" placeholder="Pathway name">
            </div>

               <script>
                   var options = {
                       url: function(phrase) {
                          return "/enzymeportal/service/pathways";
                        },

                        getValue: function(element) {
                          return element.name;
                        },

                        ajaxSettings: {
                          dataType: "json",
                          method: "GET",
                          data: {
                            dataType: "json"
                          }
                        },


                        preparePostData: function(data) {
                          data.name = $("#pathway-input").val();

                          return data;
                        },

                       list: {
                               match: {
                                   enabled: true
                               }
                               ,
                           onChooseEvent: function() {
                               var clickedName = $("#pathway-input").getSelectedItemData().pathwayName;
                               var clickedId = $("#pathway-input").getSelectedItemData().pathwayGroupId;
                               var url = '/enzymeportal/enzymes?searchKey='
                                       + clickedId
                                       + '&searchparams.start=0&searchparams.text='
                                       + clickedName
                                       + '&keywordType=PATHWAYS'
                                       + '&searchId=' + clickedId;

                               console.log(url);

                               window.location.href = url;
                               }
                           }
                   };
                   $("#pathway-input").easyAutocomplete(options);
               </script>


            <c:if test="${not empty pathwayList}">
                <ep:alphabeticalDisplay items="${pathwayList}" type="pathways" maxDisplay="5"/>
            </c:if>


        </div>
        <%@include file="footer.jspf" %>
    </div>
    </body>
</html>

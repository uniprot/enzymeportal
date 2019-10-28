<%-- 
    Document   : cofactors
    Created on : 07-Feb-2019, 13:55:31
    Author     : joseph
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="Fn" uri="/WEB-INF/epTagLibray.tld" %>
<%@taglib prefix="ep" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html ng-app="enzyme-portal-app">
    <c:set var="pageTitle" value="Browse Cofactors"/>
    <%@include file="head.jspf" %>
    <body>
        <div id="wrapper">
            <%@include file="header.jspf" %>

            <div id="content" role="main" class="clearfix" ng-controller="TypeAheadController">
                <h1>Cofactors</h1>


                <div class="container-browse-search">
                    <input id="cofactor-input" autocomplete="off" type="text" placeholder="Cofactor name">
                </div>




                <script>
                            var options = {
                                url: function (phrase) {
                                    return "/enzymeportal/service/cofactors";
                                },

                                getValue: function (element) {
                                    return element.compoundName;
                                },

                                ajaxSettings: {
                                    dataType: "json",
                                    method: "GET",
                                    data: {
                                        dataType: "json"
                                    }
                                },

                                preparePostData: function (data) {
                                    data.name = $("#cofactor-input").val();

                                    return data;
                                },

                                list: {
                                    maxNumberOfElements: 20,
                                    match: {
                                        enabled: true
                                    }
                                    ,
                                    onChooseEvent: function () {
                                        var clickedName = $("#cofactor-input").getSelectedItemData().compoundName;

                                        var clickedId = $("#cofactor-input").getSelectedItemData().compoundId;

                                        var url = '/enzymeportal/enzymes?searchKey='
                                                + clickedId
                                                + '&searchparams.type=KEYWORD&searchparams.previoustext='
                                                + clickedName
                                                + '&searchparams.start=0&searchparams.text='
                                                + clickedName
                                                + '&keywordType=COFACTORS'
                                                + '&searchId=' + clickedId;




                                        console.log(url);

                                        window.location.href = url;
                                    }
                                }
                            };

                            $("#cofactor-input").easyAutocomplete(options);
                </script>


                <c:if test="${not empty cofactors}">
                    <ep:alphabeticalDisplay items="${cofactors}" type="cofactors" maxDisplay="5"/>
                </c:if>


            </div>
            <%@include file="footer.jspf" %>
        </div>
    </body>
</html>


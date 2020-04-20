<%--
    Document   : frontierSearchBox
    Created on : Dec 3, 2012, 11:13:00 AM
    Author     : joseph
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib  prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%--
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars"%>
--%>

<%@ page language="java" pageEncoding="UTF-8"%>
<div >
    <div class="row">
        <div class="column medium-10 medium-offset-1" id="search-bar">


            <%--<form:form id="local-search" name="local-search" modelAttribute="searchModel" action="${pageContext.request.contextPath}/enzymes" method="POST">--%>
            <form id="local-search" name="local-search" action="${pageContext.request.contextPath}/enzymes" method="POST">


                <%--<form:hidden path="searchparams.previoustext" />--%>
                <input type="hidden" name="searchparams.previoustext" value="XxX" />

                <input type="hidden" id="searchId" name="searchId" value="${searchId}"/>
                <input type="hidden" name="searchTerm" value="${searchTerm}"/>
                <input type="hidden" name="keywordType" value="KEYWORD"/>
                <input type="hidden" id="searchKey" name="searchKey" value="${searchKey}">
                <fieldset>
                    <div class="input-group margin-bottom-none margin-top-large" >
                        <input class="input-group-field" autocomplete="off" id="local-searchbox" tabindex="1" name="searchparams.text" size="30" maxlength="100" type="text" placeholder="e.g enzyme name, gene name, EC number, UniProt AC, cofactor, Rhea ID, CHEBI ID ...">



                        <script>
                            var options = {

                                url: function (phrase) {
                                    return "/enzymeportal/service/auto";
                                },
   ////                                
   //                                 url: function (phrase) {
   //                                    return "/enzymeportal/service/search";
   //                                },    
   //                                
                                listLocation: "suggestions",
                                getValue: function (element) {

                                    return element.suggestion;
                                },

                                ajaxSettings: {
                                    dataType: "json",
                                    method: "POST",
                                    data: {
                                        dataType: "json"
                                    }
                                },

                                preparePostData: function (data) {
                                    data.name = $("#local-searchbox").val();

                                    return data;
                                },

                                list: {
                                    match: {
                                        enabled: true
                                    }
                                    ,
                                    onClickEvent: function () {
                                        var valueOfClickedItem = $("#local-searchbox").getSelectedItemData().suggestion;
                                        var input = jQuery('<input type="hidden" name="searchKey" id="auto-complete-holder">');
                                        input.val(valueOfClickedItem).trigger("change");
                  
                                        jQuery('#local-search').append(input);
                                        $("#local-search").submit();

                                    },
                                    onChooseEvent: function () {

                                        var valueOfClickedItem = $("#local-searchbox").getSelectedItemData().suggestion;
                                        var input = jQuery('<input type="hidden" name="searchKey" id="searchKey">');
                                        input.val(valueOfClickedItem).trigger("change");
                                        jQuery('#local-search').append(input);
                                        $('#search-keyword-submit').click();


                                    }
                                }
                            };
                            $("#local-searchbox").easyAutocomplete(options);
  

                        </script>

                        <!--                      <input id="auto-complete-holder" name="searchKey" type="hidden" />  -->

                        <!--                        <button style="display:none" type="submit" id="submit"></button>-->
                        <div class="input-group-button">
                            <input id="search-keyword-submit" class="button icon icon-functional" tabindex="2" type="submit" name="submit" value="1">
                        </div>
                        <i ng-show="loadingPathway" class="glyphicon glyphicon-refresh" ></i>
                    </div>

                    <p id="example" class="small">
                        Examples:
                        <a href="${pageContext.request.contextPath}/enzymes?searchparams.type=KEYWORD&searchparams.previoustext=&searchparams.start=0&searchparams.text=Cathepsin &keywordType=KEYWORD">Cathepsin</a>,
                        <a href="${pageContext.request.contextPath}/enzymes?searchparams.type=KEYWORD&searchparams.previoustext=&searchparams.start=0&searchparams.text=nagB&keywordType=KEYWORD">nagB</a>,
                        <a href="${pageContext.request.contextPath}/enzymes?searchparams.type=KEYWORD&searchparams.previoustext=&searchparams.start=0&searchparams.text=Glycosyltransferase&keywordType=KEYWORD">Glycosyltransferase</a>,
                        <a href="${pageContext.request.contextPath}/ec/2.3.2.27 ">2.3.2.27</a>,
                        <a href="${pageContext.request.contextPath}/enzymes?searchparams.type=KEYWORD&searchparams.previoustext=&searchparams.start=0&searchparams.text=P31153&keywordType=KEYWORD">P31153</a>,
                        <a href="${pageContext.request.contextPath}/enzymes?searchparams.type=KEYWORD&searchparams.previoustext=&searchparams.start=0&searchparams.text=CHEBI:18420&keywordType=KEYWORD">CHEBI:18420</a>,
                        <a href="${pageContext.request.contextPath}/enzymes?searchparams.type=KEYWORD&searchparams.previoustext=&searchparams.start=0&searchparams.text=RHEA:36487&keywordType=KEYWORD">RHEA:36487</a>
                    </p>

                    <%--
                    <p id="example" class="small">
                        Examples:
                        <a href="${pageContext.request.contextPath}/enzymes?searchparams.type=KEYWORD&searchparams.previoustext=&searchparams.start=0&searchparams.text=sildenafil&keywordType=KEYWORD">sildenafil</a>,
                        <a href="${pageContext.request.contextPath}/enzymes?searchparams.type=KEYWORD&searchparams.previoustext=&searchparams.start=0&searchparams.text=mTOR&keywordType=KEYWORD">mTOR</a>,
                        <a href="${pageContext.request.contextPath}/enzymes?searchparams.type=KEYWORD&searchparams.previoustext=&searchparams.start=0&searchparams.text=abl&keywordType=KEYWORD">abl</a>,
                        <a href="${pageContext.request.contextPath}/ec/1.1.1.1">1.1.1.1</a>,
                        <a href="${pageContext.request.contextPath}/enzymes?searchparams.type=KEYWORD&searchparams.previoustext=&searchparams.start=0&searchparams.text=cathepsin&keywordType=KEYWORD">cathepsin</a>,
                        <a href="${pageContext.request.contextPath}/enzymes?searchparams.type=KEYWORD&searchparams.previoustext=&searchparams.start=0&searchparams.text=P27361&keywordType=KEYWORD">P27361</a>,
                        <a href="${pageContext.request.contextPath}/enzymes?searchparams.type=KEYWORD&searchparams.previoustext=&searchparams.start=0&searchparams.text=pyruvate kinase&keywordType=KEYWORD">pyruvate kinase</a>
  <!--                       <a href="${pageContext.request.contextPath}/enzymes?searchparams.type=KEYWORD&searchparams.previoustext=&searchparams.start=0&searchparams.text=acetylcholinesterase&keywordType=KEYWORD">acetylcholinesterase</a>-->
                        <!-- <span class="adv"><a href="https://www.ebi.ac.uk/Tools/services/web/toolform.ebi?tool=ncbiblast&database=enzymeportal" id="adv-search" title="Advanced">Sequence Search</a></span> -->
                    </p>
                    --%>
                </fieldset>
            </form>
        </div>
    </div>
</div>
<!-- /local-search -->

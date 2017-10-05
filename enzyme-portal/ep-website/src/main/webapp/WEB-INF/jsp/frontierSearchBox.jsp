<%-- 
    Document   : frontierSearchBox
    Created on : Dec 3, 2012, 11:13:00 AM
    Author     : joseph
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib  prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ page language="java" pageEncoding="UTF-8"%>
<div >
    <div >
        <div class="column medium-10" id="search-bar">


            <%--<form:form id="local-search" name="local-search" modelAttribute="searchModel" action="${pageContext.request.contextPath}/enzymes" method="POST">--%>
            <form:form id="local-search" name="local-search" action="${pageContext.request.contextPath}/enzymes" method="POST">


             <%--<form:hidden path="searchparams.previoustext" />--%>
             <input type="hidden" name="searchparams.previoustext" value="XxX" />


             <input type="hidden" name="searchTerm" value="${searchTerm}"/>
             <input type="hidden" name="keywordType" value="KEYWORD"/>
             <fieldset>
                  <div class="input-group margin-bottom-none margin-top-large" >
                        <input class="input-group-field" autocomplete="off" id="local-searchbox" tabindex="1" name="searchparams.text" size="30" maxlength="100" type="text" placeholder="search for enzymes">

                      <script>
                          var options = {

                              url: function(phrase) {
                                 return "/enzymeportal/service/search";
                               },

                               getValue: function(element) {
                                 return element.suggestion;
                               },

                               ajaxSettings: {
                                 dataType: "json",
                                 method: "POST",
                                 data: {
                                   dataType: "json"
                                 }
                               },

                               preparePostData: function(data) {
                                 data.name = $("#local-searchbox").val();
                                 return data;
                               },

                              list: {
                                      match: {
                                          enabled: true
                                      }
                                      ,
                                      onClickEvent: function() {
                                          var valueOfClickedItem = $("#local-searchbox").getSelectedItemData().suggestion;
                                          var input = jQuery('<input type="hidden" name="searchKey" id="auto-complete-holder">');
                                          input.val(valueOfClickedItem).trigger("change");
                                          alert(valueOfClickedItem);
                                          jQuery('#local-search').append(input);
                                          $("#local-search").submit();
                                      }
                                  }
                          };
                          $("#local-searchbox").easyAutocomplete(options);

                      </script>

                    <!--  <input id="auto-complete-holder" name="searchKey" type="hidden" />  -->


                          <div class="input-group-button">
                              <input id="search-keyword-submit" class="button icon icon-functional" tabindex="2" type="submit" name="submit" value="1">
                           </div>
                        <i ng-show="loadingPathway" class="glyphicon glyphicon-refresh" ></i>
                  </div>



                  <p id="example" class="small">
                      Examples:
                      <a href="${pageContext.request.contextPath}/enzymes?searchparams.type=KEYWORD&searchparams.previoustext=&searchparams.start=0&searchparams.text=sildenafil&keywordType=KEYWORD">sildenafil</a>,
                      <a href="${pageContext.request.contextPath}/enzymes?searchparams.type=KEYWORD&searchparams.previoustext=&searchparams.start=0&searchparams.text=mTOR&keywordType=KEYWORD">mTOR</a>,
                      <a href="${pageContext.request.contextPath}/enzymes?searchparams.type=KEYWORD&searchparams.previoustext=&searchparams.start=0&searchparams.text=cathepsin&keywordType=KEYWORD">cathepsin</a>,
                      <a href="${pageContext.request.contextPath}/enzymes?searchparams.type=KEYWORD&searchparams.previoustext=&searchparams.start=0&searchparams.text=P27361&keywordType=KEYWORD">P27361</a>,
                      <a href="${pageContext.request.contextPath}/enzymes?searchparams.type=KEYWORD&searchparams.previoustext=&searchparams.start=0&searchparams.text=pyruvate kinase&keywordType=KEYWORD">pyruvate kinase</a>
                      <span class="adv"><a href="http://www.ebi.ac.uk/Tools/services/web/toolform.ebi?tool=ncbiblast&database=enzymeportal" id="adv-search" title="Advanced">Sequence Search</a></span>
                  </p>

              </fieldset>
            </form:form>
        </div>
    </div>
</div>
<!-- /local-search -->
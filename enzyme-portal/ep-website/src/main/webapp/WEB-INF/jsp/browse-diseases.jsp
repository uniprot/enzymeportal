<%--
    Document   : browse
    Created on : Jul 23, 2013, 12:17:33 PM
    Author     : joseph
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="Fn" uri="/WEB-INF/epTagLibray.tld" %>
<%@taglib prefix="ep" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>


<!-- paulirish.com/2008/conditional-stylesheets-vs-css-hacks-answer-neither/ -->
<!--[if lt IE 7]> <html class="no-js ie6 oldie" lang="en"> <![endif]-->
<!--[if IE 7]> <html class="no-js ie7 oldie" lang="en"> <![endif]-->
<!--[if IE 8]> <html class="no-js ie8 oldie" lang="en"> <![endif]-->
<!-- Consider adding an manifest.appcache: h5bp.com/d/Offline -->
<!--[if gt IE 8]><!-->
<html class="no-js" lang="en" ng-app="enzyme-portal-app"> <!--<![endif]-->
<head>
    <c:set var="pageTitle" value="Browse"/>
    <%@include file="head.jspf" %>


</head>

<body class="level2"><!-- add any of your classes or IDs -->
<div id="skip-to">
    <ul>
        <li><a href="#content">Skip to main content</a></li>
        <li><a href="#local-nav">Skip to local navigation</a></li>
        <li><a href="#global-nav">Skip to EBI global navigation menu</a></li>
        <li><a href="#global-nav-expanded">Skip to expanded EBI global navigation menu (includes all sub-sections)</a>
        </li>
    </ul>
</div>

<div id="wrapper">
    <%@include file="header.jspf" %>

    <div id="content" role="main" class="clearfix">
        <h1>Diseases</h1>
        <div class="container-browse-search">
            <input id="disease-input" autocomplete="off" type="text" placeholder="Disease name">
        </div>

          <script>
              var options = {
                  url: function(phrase) {
                     return "/enzymeportal/service/diseases";
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
                     data.name = $("#disease-input").val();

                     return data;
                   },

                  list: {
                          match: {
                              enabled: true
                          }
                          ,
                      onChooseEvent: function() {
                              var clickedName = $("#disease-input").getSelectedItemData().name;
                              var clickedId = $("#disease-input").getSelectedItemData().id;

                              var url = '/enzymeportal/enzymes?searchKey=' + clickedName
                              + '&searchparams.type=KEYWORD&searchparams.previoustext='
                              + clickedName
                              + '&searchparams.start=0&searchparams.text='
                              + clickedName
                              + '&keywordType=DISEASE'
                              + '&searchId=' + clickedId;
                      window.location.href = url;
                          }
                      }
              };
              $("#disease-input").easyAutocomplete(options);
          </script>



        <c:if test="${not empty diseaseList}">
            <ep:alphabeticalDisplay items="${diseaseList}" type="diseases" maxDisplay="5"/>
        </c:if>

    </div>

    <%@include file="footer.jspf" %>
</div>
<!--! end of #wrapper -->
</body>

</html>

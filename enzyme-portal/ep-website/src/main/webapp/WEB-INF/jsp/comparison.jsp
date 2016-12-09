<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<c:set var="pageTitle" value="Comparing enzymes"/>
<%@include file="head.jspf"%>
<body class="level2 full-width">

    <%@include file="skipto.jspf" %>

    <div id="wrapper">

        <%@include file="header.jspf" %>

        <div id="content" role="main" class="clearfix">

            <div id="comparison-outline">
                <ul>
                <c:forEach var="sc" items="${comparison.subComparisons}">
                    <li class="${sc.value.different? 'diff' : 'same'}">
                        <a href="#${sc.key}"
                            style="font-weight: bold;">${sc.key}</a>
                        <c:if test="${sc.key eq 'Summary'
                            or sc.key eq 'Small molecules'}">
                            <ul>
                            <c:forEach var="ssc"
                                items="${sc.value.subComparisons}">
                                <li class="${ssc.value.different? 'diff' : 'same'}">
                                    <a href="#${ssc.key}">${ssc.key}</a>
                                </li>
                            </c:forEach>
                            </ul>
                        </c:if>
                    </li>
                </c:forEach>
                </ul>
            </div>

<div class="row">

            <section class="large-2 columns">&nbsp;</section>
            <section class="large-10 columns">
                <h2>Comparing enzymes
                    <a href="compare?acc=${comparison.compared[0].uniprotaccessions[0]}&acc=${comparison.compared[1].uniprotaccessions[0]}"
                        class="icon icon-generic" data-icon="L"
                        style="font-size: smaller;"
                        title="permalink to this comparison"></a></h2>
            </section>
            <br clear="all"/>

</div>

        <div class="row">

          <section class="large-2 columns" id="comparison-header">
              &nbsp;
          </section>


              <section class="large-5 columns comparison header">

                  <a href="search/${comparison.compared[0].uniprotaccessions[0]}/enzyme">
                  ${comparison.compared[0].name}<br/>
                  (${comparison.compared[0].species.scientificname})
                  </a>
              </section>
              <section class="large-5 columns comparison header">

                  <a href="search/${comparison.compared[1].uniprotaccessions[0]}/enzyme">
                  ${comparison.compared[1].name}<br/>
                  (${comparison.compared[1].species.scientificname})
                  </a>
              </section>
              <br clear="all"/>



        </div>
    <div class="row">
            <c:forEach var="topComparison" items="${comparison.subComparisons}">


                <section class="large-2 columns" id="${sc.key}">&nbsp;</section>
                <section class="large-10 columns">
                    <fieldset class="comparison" id="${topComparison.key}">

                        <legend class="comparison header">${topComparison.key}</legend>
              <c:choose>
                            <c:when test="${empty topComparison.value.subComparisons}">
                                <c:set var="theComparison" value="${topComparison}"/>
                               <%@include file="comparison-sideBySide.jsp" %>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="theComparison"
                                    items="${topComparison.value.subComparisons}">
                                  <%@include file="comparison-list.jsp" %>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>

                    </fieldset>
                </section>
            </c:forEach>
          </div>

        </div>

        <%@include file="footer.jspf" %>

    </div>
</body>
</html>

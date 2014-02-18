<%-- 
    Document   : browse
    Created on : Jul 23, 2013, 12:17:33 PM
    Author     : joseph
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib  prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="Fn" uri="/WEB-INF/epTagLibray.tld" %>

<!DOCTYPE html>



<!-- paulirish.com/2008/conditional-stylesheets-vs-css-hacks-answer-neither/ -->
<!--[if lt IE 7]> <html class="no-js ie6 oldie" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js ie7 oldie" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js ie8 oldie" lang="en"> <![endif]-->
<!-- Consider adding an manifest.appcache: h5bp.com/d/Offline -->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->
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
                <li><a href="#global-nav-expanded">Skip to expanded EBI global navigation menu (includes all sub-sections)</a></li>
            </ul>
        </div>

        <div id="wrapper" class="container_24">
             <%@include file="header.jspf" %>
             
            <div id="content" role="main" class="grid_24 clearfix">
                <c:if test="${not empty diseaseList}">

                    <div class="grid_24">
                        <div class="clear"></div>
                        <c:set var="count" value="0"/>
                        <c:set var="maxDisplay" value="5"/>
                        <div class="grid_24">
                            <div class="grid_6">
                                <c:set var="startsWith" value="A"/>
                                <h3>${startsWith}</h3>
                                <ul>
                                    <c:forEach var="data" items="${diseaseList}"> 

                                        <c:choose>
                                            <c:when test="${(not empty data.name) && Fn:startsWithLowerCase(data.name, startsWith) && (count <= maxDisplay)}">
                                                <c:set var="count" value="${count + 1}"/>

                                                <form:form modelAttribute="searchModel" action="${pageContext.request.contextPath}/search/disease" commandName="searchModel" method="post">

                                                    <li><a  onclick="submit()" href="#">${data.name} (${data.numEnzyme})</a></li>
                                                    <input type="hidden" name="entryID" value="${data.id}"/>
                                                    <input type="hidden" name="entryName" value="${data.name}"/>
                                                    <form:hidden path="searchparams.type" value="KEYWORD"/>
                                                    <form:hidden path="searchparams.text" value="${data.name}"/>
                                                    <form:hidden path="searchparams.previoustext" />
                                                </form:form>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">
                                        <form action="${pageContext.request.contextPath}/browse/disease" method="post">
                                            <span><a  onclick="submit()" href="#">view all </a>${startsWith}</span>
                                            <input type="hidden" name="startsWith" value="${startsWith}"/>
                                        </form>

                                    </c:if>
                                    <c:set var="count" value="0"/>
                                </ul>

                            </div>
                            <div class="grid_6">
                                <c:set var="startsWith" value="B"/>
                                <h3>${startsWith}</h3>
                               <ul>
                                    <c:forEach var="data" items="${diseaseList}"> 

                                        <c:choose>
                                            <c:when test="${(not empty data.name) && Fn:startsWithLowerCase(data.name, startsWith) && (count <= maxDisplay)}">
                                                <c:set var="count" value="${count + 1}"/>

                                                <form:form modelAttribute="searchModel" action="${pageContext.request.contextPath}/search/disease" commandName="searchModel" method="post">

                                                    <li><a  onclick="submit()" href="#">${data.name} (${data.numEnzyme})</a></li>
                                                    <input type="hidden" name="entryID" value="${data.id}"/>
                                                    <input type="hidden" name="entryName" value="${data.name}"/>
                                                    <form:hidden path="searchparams.type" value="KEYWORD"/>
                                                    <form:hidden path="searchparams.text" value="${data.name}"/>
                                                    <form:hidden path="searchparams.previoustext" />
                                                </form:form>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">
                                        <form action="${pageContext.request.contextPath}/browse/disease" method="post">
                                            <span><a  onclick="submit()" href="#">view all </a>${startsWith}</span>
                                            <input type="hidden" name="startsWith" value="${startsWith}"/>
                                        </form>

                                    </c:if>
                                    <c:set var="count" value="0"/>
                                </ul>
                            </div>
                            <div class="grid_6">
                                <c:set var="startsWith" value="C"/>
                                <h3>${startsWith}</h3>
                               <ul>
                                    <c:forEach var="data" items="${diseaseList}"> 

                                        <c:choose>
                                            <c:when test="${(not empty data.name) && Fn:startsWithLowerCase(data.name, startsWith) && (count <= maxDisplay)}">
                                                <c:set var="count" value="${count + 1}"/>

                                                <form:form modelAttribute="searchModel" action="${pageContext.request.contextPath}/search/disease" commandName="searchModel" method="post">

                                                    <li><a  onclick="submit()" href="#">${data.name} (${data.numEnzyme})</a></li>
                                                    <input type="hidden" name="entryID" value="${data.id}"/>
                                                    <input type="hidden" name="entryName" value="${data.name}"/>
                                                    <form:hidden path="searchparams.type" value="KEYWORD"/>
                                                    <form:hidden path="searchparams.text" value="${data.name}"/>
                                                    <form:hidden path="searchparams.previoustext" />
                                                </form:form>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">
                                        <form action="${pageContext.request.contextPath}/browse/disease" method="post">
                                            <span><a  onclick="submit()" href="#">view all </a>${startsWith}</span>
                                            <input type="hidden" name="startsWith" value="${startsWith}"/>
                                        </form>

                                    </c:if>
                                    <c:set var="count" value="0"/>
                                </ul>
                            </div>
                            <div class="grid_6">
                                <c:set var="startsWith" value="D"/>
                                <h3>${startsWith}</h3>
                               <ul>
                                    <c:forEach var="data" items="${diseaseList}"> 

                                        <c:choose>
                                            <c:when test="${(not empty data.name) && Fn:startsWithLowerCase(data.name, startsWith) && (count <= maxDisplay)}">
                                                <c:set var="count" value="${count + 1}"/>

                                                <form:form modelAttribute="searchModel" action="${pageContext.request.contextPath}/search/disease" commandName="searchModel" method="post">

                                                    <li><a  onclick="submit()" href="#">${data.name} (${data.numEnzyme})</a></li>
                                                    <input type="hidden" name="entryID" value="${data.id}"/>
                                                    <input type="hidden" name="entryName" value="${data.name}"/>
                                                    <form:hidden path="searchparams.type" value="KEYWORD"/>
                                                    <form:hidden path="searchparams.text" value="${data.name}"/>
                                                    <form:hidden path="searchparams.previoustext" />
                                                </form:form>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">
                                        <form action="${pageContext.request.contextPath}/browse/disease" method="post">
                                            <span><a  onclick="submit()" href="#">view all </a>${startsWith}</span>
                                            <input type="hidden" name="startsWith" value="${startsWith}"/>
                                        </form>

                                    </c:if>
                                    <c:set var="count" value="0"/>
                                </ul>
                            </div>

                        </div>

                        <div class="grid_24">
                            <div class="grid_6">
                                <c:set var="startsWith" value="E"/>
                                <h3>${startsWith}</h3>
                               <ul>
                                    <c:forEach var="data" items="${diseaseList}"> 

                                        <c:choose>
                                            <c:when test="${(not empty data.name) && Fn:startsWithLowerCase(data.name, startsWith) && (count <= maxDisplay)}">
                                                <c:set var="count" value="${count + 1}"/>

                                                <form:form modelAttribute="searchModel" action="${pageContext.request.contextPath}/search/disease" commandName="searchModel" method="post">

                                                    <li><a  onclick="submit()" href="#">${data.name} (${data.numEnzyme})</a></li>
                                                    <input type="hidden" name="entryID" value="${data.id}"/>
                                                    <input type="hidden" name="entryName" value="${data.name}"/>
                                                    <form:hidden path="searchparams.type" value="KEYWORD"/>
                                                    <form:hidden path="searchparams.text" value="${data.name}"/>
                                                    <form:hidden path="searchparams.previoustext" />
                                                </form:form>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">
                                        <form action="${pageContext.request.contextPath}/browse/disease" method="post">
                                            <span><a  onclick="submit()" href="#">view all </a>${startsWith}</span>
                                            <input type="hidden" name="startsWith" value="${startsWith}"/>
                                        </form>

                                    </c:if>
                                    <c:set var="count" value="0"/>
                                </ul>
                            </div>
                            <div class="grid_6">
                                <c:set var="startsWith" value="F"/>
                                <h3>${startsWith}</h3>
                               <ul>
                                    <c:forEach var="data" items="${diseaseList}"> 

                                        <c:choose>
                                            <c:when test="${(not empty data.name) && Fn:startsWithLowerCase(data.name, startsWith) && (count <= maxDisplay)}">
                                                <c:set var="count" value="${count + 1}"/>

                                                <form:form modelAttribute="searchModel" action="${pageContext.request.contextPath}/search/disease" commandName="searchModel" method="post">

                                                    <li><a  onclick="submit()" href="#">${data.name} (${data.numEnzyme})</a></li>
                                                    <input type="hidden" name="entryID" value="${data.id}"/>
                                                    <input type="hidden" name="entryName" value="${data.name}"/>
                                                    <form:hidden path="searchparams.type" value="KEYWORD"/>
                                                    <form:hidden path="searchparams.text" value="${data.name}"/>
                                                    <form:hidden path="searchparams.previoustext" />
                                                </form:form>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">
                                        <form action="${pageContext.request.contextPath}/browse/disease" method="post">
                                            <span><a  onclick="submit()" href="#">view all </a>${startsWith}</span>
                                            <input type="hidden" name="startsWith" value="${startsWith}"/>
                                        </form>

                                    </c:if>
                                    <c:set var="count" value="0"/>
                                </ul>
                            </div>
                            <div class="grid_6">
                                <c:set var="startsWith" value="G"/>
                                <h3>${startsWith}</h3>
                               <ul>
                                    <c:forEach var="data" items="${diseaseList}"> 

                                        <c:choose>
                                            <c:when test="${(not empty data.name) && Fn:startsWithLowerCase(data.name, startsWith) && (count <= maxDisplay)}">
                                                <c:set var="count" value="${count + 1}"/>

                                                <form:form modelAttribute="searchModel" action="${pageContext.request.contextPath}/search/disease" commandName="searchModel" method="post">

                                                    <li><a  onclick="submit()" href="#">${data.name} (${data.numEnzyme})</a></li>
                                                    <input type="hidden" name="entryID" value="${data.id}"/>
                                                    <input type="hidden" name="entryName" value="${data.name}"/>
                                                    <form:hidden path="searchparams.type" value="KEYWORD"/>
                                                    <form:hidden path="searchparams.text" value="${data.name}"/>
                                                    <form:hidden path="searchparams.previoustext" />
                                                </form:form>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">
                                        <form action="${pageContext.request.contextPath}/browse/disease" method="post">
                                            <span><a  onclick="submit()" href="#">view all </a>${startsWith}</span>
                                            <input type="hidden" name="startsWith" value="${startsWith}"/>
                                        </form>

                                    </c:if>
                                    <c:set var="count" value="0"/>
                                </ul>
                            </div>
                            <div class="grid_6">
                                <c:set var="startsWith" value="H"/>
                                <h3>${startsWith}</h3>
                               <ul>
                                    <c:forEach var="data" items="${diseaseList}"> 

                                        <c:choose>
                                            <c:when test="${(not empty data.name) && Fn:startsWithLowerCase(data.name, startsWith) && (count <= maxDisplay)}">
                                                <c:set var="count" value="${count + 1}"/>

                                                <form:form modelAttribute="searchModel" action="${pageContext.request.contextPath}/search/disease" commandName="searchModel" method="post">

                                                    <li><a  onclick="submit()" href="#">${data.name} (${data.numEnzyme})</a></li>
                                                    <input type="hidden" name="entryID" value="${data.id}"/>
                                                    <input type="hidden" name="entryName" value="${data.name}"/>
                                                    <form:hidden path="searchparams.type" value="KEYWORD"/>
                                                    <form:hidden path="searchparams.text" value="${data.name}"/>
                                                    <form:hidden path="searchparams.previoustext" />
                                                </form:form>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">
                                        <form action="${pageContext.request.contextPath}/browse/disease" method="post">
                                            <span><a  onclick="submit()" href="#">view all </a>${startsWith}</span>
                                            <input type="hidden" name="startsWith" value="${startsWith}"/>
                                        </form>

                                    </c:if>
                                    <c:set var="count" value="0"/>
                                </ul>
                            </div>
                        </div>
                        <div class="grid_24">
                            <div class="grid_6">
                                <c:set var="startsWith" value="I"/>
                                <h3>${startsWith}</h3>
                               <ul>
                                    <c:forEach var="data" items="${diseaseList}"> 

                                        <c:choose>
                                            <c:when test="${(not empty data.name) && Fn:startsWithLowerCase(data.name, startsWith) && (count <= maxDisplay)}">
                                                <c:set var="count" value="${count + 1}"/>

                                                <form:form modelAttribute="searchModel" action="${pageContext.request.contextPath}/search/disease" commandName="searchModel" method="post">

                                                    <li><a  onclick="submit()" href="#">${data.name} (${data.numEnzyme})</a></li>
                                                    <input type="hidden" name="entryID" value="${data.id}"/>
                                                    <input type="hidden" name="entryName" value="${data.name}"/>
                                                    <form:hidden path="searchparams.type" value="KEYWORD"/>
                                                    <form:hidden path="searchparams.text" value="${data.name}"/>
                                                    <form:hidden path="searchparams.previoustext" />
                                                </form:form>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">
                                        <form action="${pageContext.request.contextPath}/browse/disease" method="post">
                                            <span><a  onclick="submit()" href="#">view all </a>${startsWith}</span>
                                            <input type="hidden" name="startsWith" value="${startsWith}"/>
                                        </form>

                                    </c:if>
                                    <c:set var="count" value="0"/>
                                </ul>
                            </div>
                            <div class="grid_6">
                                <c:set var="startsWith" value="J"/>
                                <h3>${startsWith}</h3>
                               <ul>
                                    <c:forEach var="data" items="${diseaseList}"> 

                                        <c:choose>
                                            <c:when test="${(not empty data.name) && Fn:startsWithLowerCase(data.name, startsWith) && (count <= maxDisplay)}">
                                                <c:set var="count" value="${count + 1}"/>

                                                <form:form modelAttribute="searchModel" action="${pageContext.request.contextPath}/search/disease" commandName="searchModel" method="post">

                                                    <li><a  onclick="submit()" href="#">${data.name} (${data.numEnzyme})</a></li>
                                                    <input type="hidden" name="entryID" value="${data.id}"/>
                                                    <input type="hidden" name="entryName" value="${data.name}"/>
                                                    <form:hidden path="searchparams.type" value="KEYWORD"/>
                                                    <form:hidden path="searchparams.text" value="${data.name}"/>
                                                    <form:hidden path="searchparams.previoustext" />
                                                </form:form>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">
                                        <form action="${pageContext.request.contextPath}/browse/disease" method="post">
                                            <span><a  onclick="submit()" href="#">view all </a>${startsWith}</span>
                                            <input type="hidden" name="startsWith" value="${startsWith}"/>
                                        </form>

                                    </c:if>
                                    <c:set var="count" value="0"/>
                                </ul>
                            </div>
                            <div class="grid_6">
                                <c:set var="startsWith" value="K"/>
                                <h3>${startsWith}</h3>
                               <ul>
                                    <c:forEach var="data" items="${diseaseList}"> 

                                        <c:choose>
                                            <c:when test="${(not empty data.name) && Fn:startsWithLowerCase(data.name, startsWith) && (count <= maxDisplay)}">
                                                <c:set var="count" value="${count + 1}"/>

                                                <form:form modelAttribute="searchModel" action="${pageContext.request.contextPath}/search/disease" commandName="searchModel" method="post">

                                                    <li><a  onclick="submit()" href="#">${data.name} (${data.numEnzyme})</a></li>
                                                    <input type="hidden" name="entryID" value="${data.id}"/>
                                                    <input type="hidden" name="entryName" value="${data.name}"/>
                                                    <form:hidden path="searchparams.type" value="KEYWORD"/>
                                                    <form:hidden path="searchparams.text" value="${data.name}"/>
                                                    <form:hidden path="searchparams.previoustext" />
                                                </form:form>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">
                                        <form action="${pageContext.request.contextPath}/browse/disease" method="post">
                                            <span><a  onclick="submit()" href="#">view all </a>${startsWith}</span>
                                            <input type="hidden" name="startsWith" value="${startsWith}"/>
                                        </form>

                                    </c:if>
                                    <c:set var="count" value="0"/>
                                </ul>
                            </div>
                            <div class="grid_6">
                                <c:set var="startsWith" value="L"/>
                                <h3>${startsWith}</h3>
                                <ul>
                                    <c:forEach var="data" items="${diseaseList}"> 

                                        <c:if test="${Fn:startsWithLowerCase(data.name, startsWith) && count <= maxDisplay}">
                                            <c:set var="count" value="${count + 1}"/>
                                            <form:form modelAttribute="searchModel" action="${pageContext.request.contextPath}/search/disease" commandName="searchModel" method="post">

                                                <li><a  onclick="submit()" href="#">${data.name} (${data.numEnzyme})</a></li>
                                                <input type="hidden" name="entryID" value="${data.id}"/>
                                                <input type="hidden" name="entryName" value="${data.name}"/>
                                                <form:hidden path="searchparams.type" value="KEYWORD"/>
                                                <form:hidden path="searchparams.text" value="${data.name}"/>
                                                <form:hidden path="searchparams.previoustext" />
                                            </form:form>



                                        </c:if>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">
                                        <form action="${pageContext.request.contextPath}/browse/disease" method="post">
                                            <span><a  onclick="submit()" href="#">view all </a>${startsWith}</span>
                                            <input type="hidden" name="startsWith" value="${startsWith}"/>
                                        </form>

                                    </c:if>
                                    <c:set var="count" value="0"/>
                                </ul>
                            </div>
                        </div>
                        <div class="grid_24">
                            <div class="grid_6">
                                <c:set var="startsWith" value="M"/>
                                <h3>${startsWith}</h3>
                               <ul>
                                    <c:forEach var="data" items="${diseaseList}"> 

                                        <c:choose>
                                            <c:when test="${(not empty data.name) && Fn:startsWithLowerCase(data.name, startsWith) && (count <= maxDisplay)}">
                                                <c:set var="count" value="${count + 1}"/>

                                                <form:form modelAttribute="searchModel" action="${pageContext.request.contextPath}/search/disease" commandName="searchModel" method="post">

                                                    <li><a  onclick="submit()" href="#">${data.name} (${data.numEnzyme})</a></li>
                                                    <input type="hidden" name="entryID" value="${data.id}"/>
                                                    <input type="hidden" name="entryName" value="${data.name}"/>
                                                    <form:hidden path="searchparams.type" value="KEYWORD"/>
                                                    <form:hidden path="searchparams.text" value="${data.name}"/>
                                                    <form:hidden path="searchparams.previoustext" />
                                                </form:form>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">
                                        <form action="${pageContext.request.contextPath}/browse/disease" method="post">
                                            <span><a  onclick="submit()" href="#">view all </a>${startsWith}</span>
                                            <input type="hidden" name="startsWith" value="${startsWith}"/>
                                        </form>

                                    </c:if>
                                    <c:set var="count" value="0"/>
                                </ul>
                            </div>
                            <div class="grid_6">
                                <c:set var="startsWith" value="N"/>
                                <h3>${startsWith}</h3>
                               <ul>
                                    <c:forEach var="data" items="${diseaseList}"> 

                                        <c:choose>
                                            <c:when test="${(not empty data.name) && Fn:startsWithLowerCase(data.name, startsWith) && (count <= maxDisplay)}">
                                                <c:set var="count" value="${count + 1}"/>

                                                <form:form modelAttribute="searchModel" action="${pageContext.request.contextPath}/search/disease" commandName="searchModel" method="post">

                                                    <li><a  onclick="submit()" href="#">${data.name} (${data.numEnzyme})</a></li>
                                                    <input type="hidden" name="entryID" value="${data.id}"/>
                                                    <input type="hidden" name="entryName" value="${data.name}"/>
                                                    <form:hidden path="searchparams.type" value="KEYWORD"/>
                                                    <form:hidden path="searchparams.text" value="${data.name}"/>
                                                    <form:hidden path="searchparams.previoustext" />
                                                </form:form>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">
                                        <form action="${pageContext.request.contextPath}/browse/disease" method="post">
                                            <span><a  onclick="submit()" href="#">view all </a>${startsWith}</span>
                                            <input type="hidden" name="startsWith" value="${startsWith}"/>
                                        </form>

                                    </c:if>
                                    <c:set var="count" value="0"/>
                                </ul>
                            </div>
                            <div class="grid_6">
                                <c:set var="startsWith" value="O"/>
                                <h3>${startsWith}</h3>
                               <ul>
                                    <c:forEach var="data" items="${diseaseList}"> 

                                        <c:choose>
                                            <c:when test="${(not empty data.name) && Fn:startsWithLowerCase(data.name, startsWith) && (count <= maxDisplay)}">
                                                <c:set var="count" value="${count + 1}"/>

                                                <form:form modelAttribute="searchModel" action="${pageContext.request.contextPath}/search/disease" commandName="searchModel" method="post">

                                                    <li><a  onclick="submit()" href="#">${data.name} (${data.numEnzyme})</a></li>
                                                    <input type="hidden" name="entryID" value="${data.id}"/>
                                                    <input type="hidden" name="entryName" value="${data.name}"/>
                                                    <form:hidden path="searchparams.type" value="KEYWORD"/>
                                                    <form:hidden path="searchparams.text" value="${data.name}"/>
                                                    <form:hidden path="searchparams.previoustext" />
                                                </form:form>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">
                                        <form action="${pageContext.request.contextPath}/browse/disease" method="post">
                                            <span><a  onclick="submit()" href="#">view all </a>${startsWith}</span>
                                            <input type="hidden" name="startsWith" value="${startsWith}"/>
                                        </form>

                                    </c:if>
                                    <c:set var="count" value="0"/>
                                </ul>
                            </div>
                            <div class="grid_6">
                                <c:set var="startsWith" value="P"/>
                                <h3>${startsWith}</h3>
                               <ul>
                                    <c:forEach var="data" items="${diseaseList}"> 

                                        <c:choose>
                                            <c:when test="${(not empty data.name) && Fn:startsWithLowerCase(data.name, startsWith) && (count <= maxDisplay)}">
                                                <c:set var="count" value="${count + 1}"/>

                                                <form:form modelAttribute="searchModel" action="${pageContext.request.contextPath}/search/disease" commandName="searchModel" method="post">

                                                    <li><a  onclick="submit()" href="#">${data.name} (${data.numEnzyme})</a></li>
                                                    <input type="hidden" name="entryID" value="${data.id}"/>
                                                    <input type="hidden" name="entryName" value="${data.name}"/>
                                                    <form:hidden path="searchparams.type" value="KEYWORD"/>
                                                    <form:hidden path="searchparams.text" value="${data.name}"/>
                                                    <form:hidden path="searchparams.previoustext" />
                                                </form:form>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">
                                        <form action="${pageContext.request.contextPath}/browse/disease" method="post">
                                            <span><a  onclick="submit()" href="#">view all </a>${startsWith}</span>
                                            <input type="hidden" name="startsWith" value="${startsWith}"/>
                                        </form>

                                    </c:if>
                                    <c:set var="count" value="0"/>
                                </ul>
                            </div>
                        </div>
                        <div class="grid_24">
                            <div class="grid_6">
                                <c:set var="startsWith" value="Q"/>
                                <h3>${startsWith}</h3>
                               <ul>
                                    <c:forEach var="data" items="${diseaseList}"> 

                                        <c:choose>
                                            <c:when test="${(not empty data.name) && Fn:startsWithLowerCase(data.name, startsWith) && (count <= maxDisplay)}">
                                                <c:set var="count" value="${count + 1}"/>

                                                <form:form modelAttribute="searchModel" action="${pageContext.request.contextPath}/search/disease" commandName="searchModel" method="post">

                                                    <li><a  onclick="submit()" href="#">${data.name} (${data.numEnzyme})</a></li>
                                                    <input type="hidden" name="entryID" value="${data.id}"/>
                                                    <input type="hidden" name="entryName" value="${data.name}"/>
                                                    <form:hidden path="searchparams.type" value="KEYWORD"/>
                                                    <form:hidden path="searchparams.text" value="${data.name}"/>
                                                    <form:hidden path="searchparams.previoustext" />
                                                </form:form>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">
                                        <form action="${pageContext.request.contextPath}/browse/disease" method="post">
                                            <span><a  onclick="submit()" href="#">view all </a>${startsWith}</span>
                                            <input type="hidden" name="startsWith" value="${startsWith}"/>
                                        </form>

                                    </c:if>
                                    <c:set var="count" value="0"/>
                                </ul>
                            </div>
                            <div class="grid_6">
                                <c:set var="startsWith" value="R"/>
                                <h3>${startsWith}</h3>
                               <ul>
                                    <c:forEach var="data" items="${diseaseList}"> 

                                        <c:choose>
                                            <c:when test="${(not empty data.name) && Fn:startsWithLowerCase(data.name, startsWith) && (count <= maxDisplay)}">
                                                <c:set var="count" value="${count + 1}"/>

                                                <form:form modelAttribute="searchModel" action="${pageContext.request.contextPath}/search/disease" commandName="searchModel" method="post">

                                                    <li><a  onclick="submit()" href="#">${data.name} (${data.numEnzyme})</a></li>
                                                    <input type="hidden" name="entryID" value="${data.id}"/>
                                                    <input type="hidden" name="entryName" value="${data.name}"/>
                                                    <form:hidden path="searchparams.type" value="KEYWORD"/>
                                                    <form:hidden path="searchparams.text" value="${data.name}"/>
                                                    <form:hidden path="searchparams.previoustext" />
                                                </form:form>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">
                                        <form action="${pageContext.request.contextPath}/browse/disease" method="post">
                                            <span><a  onclick="submit()" href="#">view all </a>${startsWith}</span>
                                            <input type="hidden" name="startsWith" value="${startsWith}"/>
                                        </form>

                                    </c:if>
                                    <c:set var="count" value="0"/>
                                </ul>
                            </div>
                            <div class="grid_6">
                                <c:set var="startsWith" value="S"/>
                                <h3>${startsWith}</h3>
                               <ul>
                                    <c:forEach var="data" items="${diseaseList}"> 

                                        <c:choose>
                                            <c:when test="${(not empty data.name) && Fn:startsWithLowerCase(data.name, startsWith) && (count <= maxDisplay)}">
                                                <c:set var="count" value="${count + 1}"/>

                                                <form:form modelAttribute="searchModel" action="${pageContext.request.contextPath}/search/disease" commandName="searchModel" method="post">

                                                    <li><a  onclick="submit()" href="#">${data.name} (${data.numEnzyme})</a></li>
                                                    <input type="hidden" name="entryID" value="${data.id}"/>
                                                    <input type="hidden" name="entryName" value="${data.name}"/>
                                                    <form:hidden path="searchparams.type" value="KEYWORD"/>
                                                    <form:hidden path="searchparams.text" value="${data.name}"/>
                                                    <form:hidden path="searchparams.previoustext" />
                                                </form:form>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">
                                        <form action="${pageContext.request.contextPath}/browse/disease" method="post">
                                            <span><a  onclick="submit()" href="#">view all </a>${startsWith}</span>
                                            <input type="hidden" name="startsWith" value="${startsWith}"/>
                                        </form>

                                    </c:if>
                                    <c:set var="count" value="0"/>
                                </ul>
                            </div>
                            <div class="grid_6">
                                <c:set var="startsWith" value="T"/>
                                <h3>${startsWith}</h3>
                               <ul>
                                    <c:forEach var="data" items="${diseaseList}"> 

                                        <c:choose>
                                            <c:when test="${(not empty data.name) && Fn:startsWithLowerCase(data.name, startsWith) && (count <= maxDisplay)}">
                                                <c:set var="count" value="${count + 1}"/>

                                                <form:form modelAttribute="searchModel" action="${pageContext.request.contextPath}/search/disease" commandName="searchModel" method="post">

                                                    <li><a  onclick="submit()" href="#">${data.name} (${data.numEnzyme})</a></li>
                                                    <input type="hidden" name="entryID" value="${data.id}"/>
                                                    <input type="hidden" name="entryName" value="${data.name}"/>
                                                    <form:hidden path="searchparams.type" value="KEYWORD"/>
                                                    <form:hidden path="searchparams.text" value="${data.name}"/>
                                                    <form:hidden path="searchparams.previoustext" />
                                                </form:form>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">
                                        <form action="${pageContext.request.contextPath}/browse/disease" method="post">
                                            <span><a  onclick="submit()" href="#">view all </a>${startsWith}</span>
                                            <input type="hidden" name="startsWith" value="${startsWith}"/>
                                        </form>

                                    </c:if>
                                    <c:set var="count" value="0"/>
                                </ul>
                            </div>

                        </div>
                        <div class="grid_24">
                            <div class="grid_6">
                                <c:set var="startsWith" value="U"/>
                                <h3>${startsWith}</h3>
                               <ul>
                                    <c:forEach var="data" items="${diseaseList}"> 

                                        <c:choose>
                                            <c:when test="${(not empty data.name) && Fn:startsWithLowerCase(data.name, startsWith) && (count <= maxDisplay)}">
                                                <c:set var="count" value="${count + 1}"/>

                                                <form:form modelAttribute="searchModel" action="${pageContext.request.contextPath}/search/disease" commandName="searchModel" method="post">

                                                    <li><a  onclick="submit()" href="#">${data.name} (${data.numEnzyme})</a></li>
                                                    <input type="hidden" name="entryID" value="${data.id}"/>
                                                    <input type="hidden" name="entryName" value="${data.name}"/>
                                                    <form:hidden path="searchparams.type" value="KEYWORD"/>
                                                    <form:hidden path="searchparams.text" value="${data.name}"/>
                                                    <form:hidden path="searchparams.previoustext" />
                                                </form:form>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">
                                        <form action="${pageContext.request.contextPath}/browse/disease" method="post">
                                            <span><a  onclick="submit()" href="#">view all </a>${startsWith}</span>
                                            <input type="hidden" name="startsWith" value="${startsWith}"/>
                                        </form>

                                    </c:if>
                                    <c:set var="count" value="0"/>
                                </ul>
                            </div>
                            <div class="grid_6">
                                <c:set var="startsWith" value="V"/>
                                <h3>${startsWith}</h3>
                               <ul>
                                    <c:forEach var="data" items="${diseaseList}"> 

                                        <c:choose>
                                            <c:when test="${(not empty data.name) && Fn:startsWithLowerCase(data.name, startsWith) && (count <= maxDisplay)}">
                                                <c:set var="count" value="${count + 1}"/>

                                                <form:form modelAttribute="searchModel" action="${pageContext.request.contextPath}/search/disease" commandName="searchModel" method="post">

                                                    <li><a  onclick="submit()" href="#">${data.name} (${data.numEnzyme})</a></li>
                                                    <input type="hidden" name="entryID" value="${data.id}"/>
                                                    <input type="hidden" name="entryName" value="${data.name}"/>
                                                    <form:hidden path="searchparams.type" value="KEYWORD"/>
                                                    <form:hidden path="searchparams.text" value="${data.name}"/>
                                                    <form:hidden path="searchparams.previoustext" />
                                                </form:form>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">
                                        <form action="${pageContext.request.contextPath}/browse/disease" method="post">
                                            <span><a  onclick="submit()" href="#">view all </a>${startsWith}</span>
                                            <input type="hidden" name="startsWith" value="${startsWith}"/>
                                        </form>

                                    </c:if>
                                    <c:set var="count" value="0"/>
                                </ul>
                            </div>
                            <div class="grid_6">
                                <c:set var="startsWith" value="W"/>
                                <h3>${startsWith}</h3>
                               <ul>
                                    <c:forEach var="data" items="${diseaseList}"> 

                                        <c:choose>
                                            <c:when test="${(not empty data.name) && Fn:startsWithLowerCase(data.name, startsWith) && (count <= maxDisplay)}">
                                                <c:set var="count" value="${count + 1}"/>

                                                <form:form modelAttribute="searchModel" action="${pageContext.request.contextPath}/search/disease" commandName="searchModel" method="post">

                                                    <li><a  onclick="submit()" href="#">${data.name} (${data.numEnzyme})</a></li>
                                                    <input type="hidden" name="entryID" value="${data.id}"/>
                                                    <input type="hidden" name="entryName" value="${data.name}"/>
                                                    <form:hidden path="searchparams.type" value="KEYWORD"/>
                                                    <form:hidden path="searchparams.text" value="${data.name}"/>
                                                    <form:hidden path="searchparams.previoustext" />
                                                </form:form>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">
                                        <form action="${pageContext.request.contextPath}/browse/disease" method="post">
                                            <span><a  onclick="submit()" href="#">view all </a>${startsWith}</span>
                                            <input type="hidden" name="startsWith" value="${startsWith}"/>
                                        </form>

                                    </c:if>
                                    <c:set var="count" value="0"/>
                                </ul>
                            </div>
                            <div class="grid_6">
                                <c:set var="startsWith" value="X"/>
                                <h3>${startsWith}</h3>
                               <ul>
                                    <c:forEach var="data" items="${diseaseList}"> 

                                        <c:choose>
                                            <c:when test="${(not empty data.name) && Fn:startsWithLowerCase(data.name, startsWith) && (count <= maxDisplay)}">
                                                <c:set var="count" value="${count + 1}"/>

                                                <form:form modelAttribute="searchModel" action="${pageContext.request.contextPath}/search/disease" commandName="searchModel" method="post">

                                                    <li><a  onclick="submit()" href="#">${data.name} (${data.numEnzyme})</a></li>
                                                    <input type="hidden" name="entryID" value="${data.id}"/>
                                                    <input type="hidden" name="entryName" value="${data.name}"/>
                                                    <form:hidden path="searchparams.type" value="KEYWORD"/>
                                                    <form:hidden path="searchparams.text" value="${data.name}"/>
                                                    <form:hidden path="searchparams.previoustext" />
                                                </form:form>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">
                                        <form action="${pageContext.request.contextPath}/browse/disease" method="post">
                                            <span><a  onclick="submit()" href="#">view all </a>${startsWith}</span>
                                            <input type="hidden" name="startsWith" value="${startsWith}"/>
                                        </form>

                                    </c:if>
                                    <c:set var="count" value="0"/>
                                </ul>
                            </div>
                        </div>

                        <div class="grid_24">
                            <div class="grid_6">
                                <c:set var="startsWith" value="Y"/>
                                <h3>${startsWith}</h3>
                                <ul>
                                    <c:set var="stop" value="false"/>
                                    <c:set var="c" value="0"/>
                                    <c:if test="${stop eq false}">
                                        <c:forEach var="data" items="${diseaseList}"> 

                                            <c:choose>
                                                <c:when test="${(not empty data.name) && Fn:startsWithLowerCase(data.name, startsWith) && (count <= maxDisplay)}">
                                                    <c:set var="count" value="${count + 1}"/>


                                                    <form:form modelAttribute="searchModel" action="${pageContext.request.contextPath}/search/disease" commandName="searchModel" method="post">

                                                        <li><a  onclick="submit()" href="#">${data.name} (${data.numEnzyme})</a></li>
                                                        <input type="hidden" name="entryID" value="${data.id}"/>
                                                        <input type="hidden" name="entryName" value="${data.name}"/>
                                                        <form:hidden path="searchparams.type" value="KEYWORD"/>
                                                        <form:hidden path="searchparams.text" value="${data.name}"/>
                                                        <form:hidden path="searchparams.previoustext" />
                                                    </form:form>
                                                </c:when>
                                                <c:when test="${Fn:startsWithLowerCase(data.name, startsWith) eq false}">

                                                    <c:if test="${c == 0}">

                                                        <c:set var="c" value="${c + 1}"/>


                                                        <span>No diseases starting with Letter ${startsWith} was found</span>

                                                        <c:set var="stop" value="true"/>

                                                    </c:if>

                                                </c:when>
                                            </c:choose>

                                        </c:forEach>
                                    </c:if>


                                    <c:if test="${count gt maxDisplay}">
                                        <form action="${pageContext.request.contextPath}/browse/disease" method="post">
                                            <span><a  onclick="submit()" href="#">view all </a>${startsWith}</span>
                                            <input type="hidden" name="startsWith" value="${startsWith}"/>
                                        </form>

                                    </c:if>
                                    <c:set var="count" value="0"/>
                                </ul>
                            </div>
                            <div class="grid_6">
                                <c:set var="startsWith" value="Z"/>
                                <h3>${startsWith}</h3>
                                <ul>
                                    <c:set var="stop" value="false"/>
                                    <c:set var="c" value="0"/>
                                    <c:if test="${stop eq false}">
                                        <c:forEach var="data" items="${diseaseList}"> 

                                            <c:choose>
                                                <c:when test="${(not empty data.name) && Fn:startsWithLowerCase(data.name, startsWith) && (count <= maxDisplay)}">
                                                    <c:set var="count" value="${count + 1}"/>


                                                    <form:form modelAttribute="searchModel" action="${pageContext.request.contextPath}/search/disease" commandName="searchModel" method="post">

                                                        <li><a  onclick="submit()" href="#">${data.name} (${data.numEnzyme})</a></li>
                                                        <input type="hidden" name="entryID" value="${data.id}"/>
                                                        <input type="hidden" name="entryName" value="${data.name}"/>
                                                        <form:hidden path="searchparams.type" value="KEYWORD"/>
                                                        <form:hidden path="searchparams.text" value="${data.name}"/>
                                                        <form:hidden path="searchparams.previoustext" />
                                                    </form:form>
                                                </c:when>
                                                <c:when test="${Fn:startsWithLowerCase(data.name, startsWith) eq false}">

                                                    <c:if test="${c == 0}">

                                                        <c:set var="c" value="${c + 1}"/>


                                                        <span>No diseases starting with Letter ${startsWith} was found</span>

                                                        <c:set var="stop" value="true"/>

                                                    </c:if>

                                                </c:when>
                                            </c:choose>

                                        </c:forEach>
                                    </c:if>


                                    <c:if test="${count gt maxDisplay}">
                                        <form action="${pageContext.request.contextPath}/browse/disease" method="post">
                                            <span><a  onclick="submit()" href="#">view all </a>${startsWith}</span>
                                            <input type="hidden" name="startsWith" value="${startsWith}"/>
                                        </form>

                                    </c:if>
                                    <c:set var="count" value="0"/>
                                </ul>
                            </div>

                        </div>







                    </div>

                </c:if>
                <c:if test="${not empty alldiseaseList}">
                    <div class="grid_24">

                        <h3 style="text-align: center">Diseases that starts with letter ${startsWith}</h3>
                        <ul>
                            <c:forEach var="data" items="${alldiseaseList}"> 
                                <div class="grid_6">

                                    <form:form modelAttribute="searchModel" action="${pageContext.request.contextPath}/search/disease" commandName="searchModel" method="post">

                                        <li><a id="${data.name}"  onclick="submit()" href="#${data.name}">${data.name} (${data.numEnzyme})</a></li>
                                        <input type="hidden" name="entryID" value="${data.id}"/>
                                        <input type="hidden" name="entryName" value="${data.name}"/>
                                        <form:hidden path="searchparams.type" value="KEYWORD"/>
                                        <form:hidden path="searchparams.text" value="${data.name}"/>
                                        <form:hidden path="searchparams.previoustext" />
                                    </form:form>
                                </div>

                            </c:forEach>

                        </ul>




                    </div>

                </c:if>
                <c:if test="${empty alldiseaseList && empty diseaseList}">
                                  <div class="grid_24">
                    <h3 style="text-align: center; margin-right: 10em">Browse Enzymes By EC classification</h3><br/>
                    <div class="grid_6" style="margin-left: 30em">
                        
                <div style="text-align: center; min-width: 170px">
                        <div style="text-align: left; margin-left: auto; margin-right: auto; width: 170px">
                           <ul style="list-style-type: none; padding-left: 5px; margin-left: 0px">
                                    <li><a href="${pageContext.request.contextPath}/browse/ec/1/Oxidoreductases">EC 1</a>&nbsp;&nbsp;Oxidoreductases</li>
                                        <li><a href="${pageContext.request.contextPath}/browse/ec/2/Transferases">EC 2</a>&nbsp;&nbsp;Transferases</li>
                                        <li><a href="${pageContext.request.contextPath}/browse/ec/3/Hydrolases">EC 3</a>&nbsp;&nbsp;Hydrolases</li>
                                        <li><a href="${pageContext.request.contextPath}/browse/ec/4/Lyases">EC 4</a>&nbsp;&nbsp;Lyases</li>
                                        <li><a href="${pageContext.request.contextPath}/browse/ec/5/Isomerases">EC 5</a>&nbsp;&nbsp;Isomerases</li>
                                        <li><a href="${pageContext.request.contextPath}/browse/ec/6/Ligases">EC 6</a>&nbsp;&nbsp;Ligases</li>
                                </ul>
                        </div>
                </div>

                    </div>
                </div>  
                </c:if>

            </div>




            <footer>
                <!-- Optional local footer (insert citation / project-specific copyright / etc here -->
                <!--
    <div id="local-footer" class="grid_24 clearfix">
                        <p>How to reference this page: ...</p>
                </div>
                -->
                <!-- End optional local footer -->

                <div id="global-footer" class="grid_24">

                    <nav id="global-nav-expanded">

                        <div class="grid_4 alpha">
                            <h3 class="embl-ebi"><a href="/" title="EMBL-EBI">EMBL-EBI</a></h3>
                        </div>

                        <div class="grid_4">
                            <h3 class="services"><a href="/services">Services</a></h3>
                        </div>

                        <div class="grid_4">
                            <h3 class="research"><a href="/research">Research</a></h3>
                        </div>

                        <div class="grid_4">
                            <h3 class="training"><a href="/training">Training</a></h3>
                        </div>

                        <div class="grid_4">
                            <h3 class="industry"><a href="/industry">Industry</a></h3>
                        </div>

                        <div class="grid_4 omega">
                            <h3 class="about"><a href="/about">About us</a></h3>
                        </div>

                    </nav>

                    <section id="ebi-footer-meta">
                        <p class="address">EMBL-EBI, Wellcome Trust Genome Campus, Hinxton, Cambridgeshire, CB10 1SD, UK &nbsp; &nbsp; +44 (0)1223 49 44 44</p>
                        <p class="legal">Copyright &copy; EMBL-EBI 2012 | EBI is an Outstation of the <a href="http://www.embl.org">European Molecular Biology Laboratory</a> | <a href="/about/privacy">Privacy</a> | <a href="/about/cookies">Cookies</a> | <a href="/about/terms-of-use">Terms of use</a></p>	
                    </section>

                </div>

            </footer>
        </div> <!--! end of #wrapper -->


        <!-- JavaScript at the bottom for fast page loading -->

        <c:if test="${pageContext.request.serverName!='www.ebi.ac.uk'}" >
            <script type="text/javascript">var redline = {}; redline.project_id = 185653108;</script><script id="redline_js" src="http://www.redline.cc/assets/button.js" type="text/javascript">
                
            </script>
            <script>
                $(document).ready(function() {
                    setTimeout(function(){
                        // Handler for .ready() called.
                        $("#redline_side_car").css("background-image","url(resources/images/redline_left_button.png)");
                        $("#redline_side_car").css("background-size", "23px auto");
                        $("#redline_side_car").css("display", "block");
                        $("#redline_side_car").css("width", "23px");
                        $("#redline_side_car").css("height", "63px");
                    },1000);
                });
            </script>
        </c:if>

        <!--        add twitter script for twitterapi-->
        <script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0];if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src="https://platform.twitter.com/widgets.js";fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");</script>



        <!--    now the frontier js for ebi global result-->
        <script src="//www.ebi.ac.uk/web_guidelines/js/ebi-global-search-run.js"></script>
        <script src="//www.ebi.ac.uk/web_guidelines/js/ebi-global-search.js"></script>



        <script defer="defer" src="//www.ebi.ac.uk/web_guidelines/js/cookiebanner.js"></script>  
        <script defer="defer" src="//www.ebi.ac.uk/web_guidelines/js/foot.js"></script>

        <!-- end scripts-->


        <!-- Change UA-XXXXX-X to be your site's ID -->
        <!--
      <script>
          window._gaq = [['_setAccount','UAXXXXXXXX1'],['_trackPageview'],['_trackPageLoadTime']];
          Modernizr.load({
            load: ('https:' == location.protocol ? '//ssl' : '//www') + '.google-analytics.com/ga.js'
          });
        </script>
        -->


        <!-- Prompt IE 6 users to install Chrome Frame. Remove this if you want to support IE 6.
             chromium.org/developers/how-tos/chrome-frame-getting-started -->
        <!--[if lt IE 7 ]>
            <script src="//ajax.googleapis.com/ajax/libs/chrome-frame/1.0.3/CFInstall.min.js"></script>
            <script>window.attachEvent('onload',function(){CFInstall.check({mode:'overlay'})})</script>
          <![endif]-->

    </body>

</html>

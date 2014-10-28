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
                                            <c:when test="${(not empty data.diseaseName) && Fn:startsWithLowerCase(data.diseaseName, startsWith) && (count <= maxDisplay)}">
                                                <c:set var="count" value="${count + 1}"/>                                       

                                                <li><a   href="${pageContext.request.contextPath}/search/disease?entryid=${data.meshId}&entryname=${data.diseaseName}&AMP;searchparams.type=KEYWORD&searchparams.previoustext=${data.diseaseName}&searchparams.start=0&searchparams.text=${data.diseaseName}">${data.diseaseName} (0)</a></li>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">

                                        <span><a   href="${pageContext.request.contextPath}/browse/disease/${startsWith}">view all </a>${startsWith}</span>
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
                                            <c:when test="${(not empty data.diseaseName) && Fn:startsWithLowerCase(data.diseaseName, startsWith) && (count <= maxDisplay)}">
                                                <c:set var="count" value="${count + 1}"/>

                                                <li><a   href="${pageContext.request.contextPath}/search/disease?entryid=${data.meshId}&entryname=${data.diseaseName}&AMP;searchparams.type=KEYWORD&searchparams.previoustext=${data.diseaseName}&searchparams.start=0&searchparams.text=${data.diseaseName}">${data.diseaseName} ()</a></li>        
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">
                                        <span><a   href="${pageContext.request.contextPath}/browse/disease/${startsWith}">view all </a>${startsWith}</span>

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
                                            <c:when test="${(not empty data.diseaseName) && Fn:startsWithLowerCase(data.diseaseName, startsWith) && (count <= maxDisplay)}">
                                                <c:set var="count" value="${count + 1}"/>                                       

                                                <li><a   href="${pageContext.request.contextPath}/search/disease?entryid=${data.diseaseId}&entryname=${data.diseaseName}&AMP;searchparams.type=KEYWORD&searchparams.previoustext=${data.diseaseName}&searchparams.start=0&searchparams.text=${data.diseaseName}">${data.diseaseName} (0)</a></li>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">

                                        <span><a   href="${pageContext.request.contextPath}/browse/disease/${startsWith}">view all </a>${startsWith}</span>
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

                                                <li><a   href="${pageContext.request.contextPath}/search/disease?entryid=${data.id}&entryname=${data.name}&AMP;searchparams.type=KEYWORD&searchparams.previoustext=${data.name}&searchparams.start=0&searchparams.text=${data.name}">${data.name} (${data.numEnzyme})</a></li>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">

                                        <span><a   href="${pageContext.request.contextPath}/browse/disease/${startsWith}">view all </a>${startsWith}</span>
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

                                                <li><a   href="${pageContext.request.contextPath}/search/disease?entryid=${data.id}&entryname=${data.name}&AMP;searchparams.type=KEYWORD&searchparams.previoustext=${data.name}&searchparams.start=0&searchparams.text=${data.name}">${data.name} (${data.numEnzyme})</a></li>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">

                                        <span><a   href="${pageContext.request.contextPath}/browse/disease/${startsWith}">view all </a>${startsWith}</span>
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

                                                <li><a   href="${pageContext.request.contextPath}/search/disease?entryid=${data.id}&entryname=${data.name}&AMP;searchparams.type=KEYWORD&searchparams.previoustext=${data.name}&searchparams.start=0&searchparams.text=${data.name}">${data.name} (${data.numEnzyme})</a></li>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">

                                        <span><a   href="${pageContext.request.contextPath}/browse/disease/${startsWith}">view all </a>${startsWith}</span>
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

                                                <li><a   href="${pageContext.request.contextPath}/search/disease?entryid=${data.id}&entryname=${data.name}&AMP;searchparams.type=KEYWORD&searchparams.previoustext=${data.name}&searchparams.start=0&searchparams.text=${data.name}">${data.name} (${data.numEnzyme})</a></li>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">

                                        <span><a   href="${pageContext.request.contextPath}/browse/disease/${startsWith}">view all </a>${startsWith}</span>
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

                                                <li><a   href="${pageContext.request.contextPath}/search/disease?entryid=${data.id}&entryname=${data.name}&AMP;searchparams.type=KEYWORD&searchparams.previoustext=${data.name}&searchparams.start=0&searchparams.text=${data.name}">${data.name} (${data.numEnzyme})</a></li>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">

                                        <span><a   href="${pageContext.request.contextPath}/browse/disease/${startsWith}">view all </a>${startsWith}</span>
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

                                                <li><a   href="${pageContext.request.contextPath}/search/disease?entryid=${data.id}&entryname=${data.name}&AMP;searchparams.type=KEYWORD&searchparams.previoustext=${data.name}&searchparams.start=0&searchparams.text=${data.name}">${data.name} (${data.numEnzyme})</a></li>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">

                                        <span><a   href="${pageContext.request.contextPath}/browse/disease/${startsWith}">view all </a>${startsWith}</span>
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

                                                <li><a   href="${pageContext.request.contextPath}/search/disease?entryid=${data.id}&entryname=${data.name}&AMP;searchparams.type=KEYWORD&searchparams.previoustext=${data.name}&searchparams.start=0&searchparams.text=${data.name}">${data.name} (${data.numEnzyme})</a></li>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">

                                        <span><a   href="${pageContext.request.contextPath}/browse/disease/${startsWith}">view all </a>${startsWith}</span>
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

                                                <li><a   href="${pageContext.request.contextPath}/search/disease?entryid=${data.id}&entryname=${data.name}&AMP;searchparams.type=KEYWORD&searchparams.previoustext=${data.name}&searchparams.start=0&searchparams.text=${data.name}">${data.name} (${data.numEnzyme})</a></li>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">

                                        <span><a   href="${pageContext.request.contextPath}/browse/disease/${startsWith}">view all </a>${startsWith}</span>
                                    </c:if>
                                    <c:set var="count" value="0"/>
                                </ul>
                            </div>
                            <div class="grid_6">
                                <c:set var="startsWith" value="L"/>
                                <h3>${startsWith}</h3>
                                <ul>
                                    <c:forEach var="data" items="${diseaseList}"> 

                                        <c:choose>
                                            <c:when test="${(not empty data.name) && Fn:startsWithLowerCase(data.name, startsWith) && (count <= maxDisplay)}">
                                                <c:set var="count" value="${count + 1}"/>                                       

                                                <li><a   href="${pageContext.request.contextPath}/search/disease?entryid=${data.id}&entryname=${data.name}&AMP;searchparams.type=KEYWORD&searchparams.previoustext=${data.name}&searchparams.start=0&searchparams.text=${data.name}">${data.name} (${data.numEnzyme})</a></li>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">

                                        <span><a   href="${pageContext.request.contextPath}/browse/disease/${startsWith}">view all </a>${startsWith}</span>
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

                                                <li><a   href="${pageContext.request.contextPath}/search/disease?entryid=${data.id}&entryname=${data.name}&AMP;searchparams.type=KEYWORD&searchparams.previoustext=${data.name}&searchparams.start=0&searchparams.text=${data.name}">${data.name} (${data.numEnzyme})</a></li>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">

                                        <span><a   href="${pageContext.request.contextPath}/browse/disease/${startsWith}">view all </a>${startsWith}</span>
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

                                                <li><a   href="${pageContext.request.contextPath}/search/disease?entryid=${data.id}&entryname=${data.name}&AMP;searchparams.type=KEYWORD&searchparams.previoustext=${data.name}&searchparams.start=0&searchparams.text=${data.name}">${data.name} (${data.numEnzyme})</a></li>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">

                                        <span><a   href="${pageContext.request.contextPath}/browse/disease/${startsWith}">view all </a>${startsWith}</span>
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

                                                <li><a   href="${pageContext.request.contextPath}/search/disease?entryid=${data.id}&entryname=${data.name}&AMP;searchparams.type=KEYWORD&searchparams.previoustext=${data.name}&searchparams.start=0&searchparams.text=${data.name}">${data.name} (${data.numEnzyme})</a></li>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">

                                        <span><a   href="${pageContext.request.contextPath}/browse/disease/${startsWith}">view all </a>${startsWith}</span>
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

                                                <li><a   href="${pageContext.request.contextPath}/search/disease?entryid=${data.id}&entryname=${data.name}&AMP;searchparams.type=KEYWORD&searchparams.previoustext=${data.name}&searchparams.start=0&searchparams.text=${data.name}">${data.name} (${data.numEnzyme})</a></li>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">

                                        <span><a   href="${pageContext.request.contextPath}/browse/disease/${startsWith}">view all </a>${startsWith}</span>
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
                                                <li><a   href="${pageContext.request.contextPath}/search/disease?entryid=${data.id}&entryname=${data.name}&AMP;searchparams.type=KEYWORD&searchparams.previoustext=${data.name}&searchparams.start=0&searchparams.text=${data.name}">${data.name} (${data.numEnzyme})</a></li>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">

                                        <span><a   href="${pageContext.request.contextPath}/browse/disease/${startsWith}">view all </a>${startsWith}</span>
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

                                                <li><a   href="${pageContext.request.contextPath}/search/disease?entryid=${data.id}&entryname=${data.name}&AMP;searchparams.type=KEYWORD&searchparams.previoustext=${data.name}&searchparams.start=0&searchparams.text=${data.name}">${data.name} (${data.numEnzyme})</a></li>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">

                                        <span><a   href="${pageContext.request.contextPath}/browse/disease/${startsWith}">view all </a>${startsWith}</span>
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

                                                <li><a   href="${pageContext.request.contextPath}/search/disease?entryid=${data.id}&entryname=${data.name}&AMP;searchparams.type=KEYWORD&searchparams.previoustext=${data.name}&searchparams.start=0&searchparams.text=${data.name}">${data.name} (${data.numEnzyme})</a></li>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">

                                        <span><a   href="${pageContext.request.contextPath}/browse/disease/${startsWith}">view all </a>${startsWith}</span>
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

                                                <li><a   href="${pageContext.request.contextPath}/search/disease?entryid=${data.id}&entryname=${data.name}&AMP;searchparams.type=KEYWORD&searchparams.previoustext=${data.name}&searchparams.start=0&searchparams.text=${data.name}">${data.name} (${data.numEnzyme})</a></li>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">

                                        <span><a   href="${pageContext.request.contextPath}/browse/disease/${startsWith}">view all </a>${startsWith}</span>
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

                                                <li><a   href="${pageContext.request.contextPath}/search/disease?entryid=${data.id}&entryname=${data.name}&AMP;searchparams.type=KEYWORD&searchparams.previoustext=${data.name}&searchparams.start=0&searchparams.text=${data.name}">${data.name} (${data.numEnzyme})</a></li>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">

                                        <span><a   href="${pageContext.request.contextPath}/browse/disease/${startsWith}">view all </a>${startsWith}</span>
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

                                                <li><a   href="${pageContext.request.contextPath}/search/disease?entryid=${data.id}&entryname=${data.name}&AMP;searchparams.type=KEYWORD&searchparams.previoustext=${data.name}&searchparams.start=0&searchparams.text=${data.name}">${data.name} (${data.numEnzyme})</a></li>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">

                                        <span><a   href="${pageContext.request.contextPath}/browse/disease/${startsWith}">view all </a>${startsWith}</span>
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

                                                <li><a   href="${pageContext.request.contextPath}/search/disease?entryid=${data.id}&entryname=${data.name}&AMP;searchparams.type=KEYWORD&searchparams.previoustext=${data.name}&searchparams.start=0&searchparams.text=${data.name}">${data.name} (${data.numEnzyme})</a></li>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">

                                        <span><a   href="${pageContext.request.contextPath}/browse/disease/${startsWith}">view all </a>${startsWith}</span>
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

                                                <li><a   href="${pageContext.request.contextPath}/search/disease?entryid=${data.id}&entryname=${data.name}&AMP;searchparams.type=KEYWORD&searchparams.previoustext=${data.name}&searchparams.start=0&searchparams.text=${data.name}">${data.name} (${data.numEnzyme})</a></li>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>

                                    <c:if test="${count gt maxDisplay}">

                                        <span><a   href="${pageContext.request.contextPath}/browse/disease/${startsWith}">view all </a>${startsWith}</span>
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

                                                    <li><a   href="${pageContext.request.contextPath}/search/disease?entryid=${data.id}&entryname=${data.name}&AMP;searchparams.type=KEYWORD&searchparams.previoustext=${data.name}&searchparams.start=0&searchparams.text=${data.name}">${data.name} (${data.numEnzyme})</a></li>
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
                                        <span><a   href="${pageContext.request.contextPath}/browse/disease/${startsWith}">view all </a>${startsWith}</span>

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


                                                    <li><a   href="${pageContext.request.contextPath}/search/disease?entryid=${data.id}&entryname=${data.name}&AMP;searchparams.type=KEYWORD&searchparams.previoustext=${data.name}&searchparams.start=0&searchparams.text=${data.name}">${data.name} (${data.numEnzyme})</a></li>            
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
                                        <span><a   href="${pageContext.request.contextPath}/browse/disease/${startsWith}">view all </a>${startsWith}</span>

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

                                    <li><a   href="${pageContext.request.contextPath}/search/disease?entryid=${data.meshId}&entryname=${data.diseaseName}&AMP;searchparams.type=KEYWORD&searchparams.previoustext=${data.diseaseName}&searchparams.start=0&searchparams.text=${data.diseaseName}">${data.diseaseName} (${0})</a></li>
                                </div>

                            </c:forEach>

                        </ul>




                    </div>

                </c:if>
            </div>


            <%@include file="footer.jspf" %>
        </div> <!--! end of #wrapper -->




    </body>

</html>

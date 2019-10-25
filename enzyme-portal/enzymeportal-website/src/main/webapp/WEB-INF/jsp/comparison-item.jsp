<%--
    Shows one item.
    Requires page scope variables:
        Object item: the item shown.
        Map$Entry<String,Comparison> theComparison: the comparison to which item
            belongs.
        Map$Entry<String,Comparison> topComparison: the top comparison to which
            theComparison belongs.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="epfn" uri="/WEB-INF/epTagLibray" %>

<%--<script src="https://code.jquery.com/jquery-1.9.1.min.js"></script>--%>

<!--
<link media="screen" href="${pageContext.request.contextPath}/resources/lib/spineconcept/css/960gs-fluid/grid.css" type="text/css" rel="stylesheet" />
<link media="screen" href="${pageContext.request.contextPath}/resources/lib/spineconcept/css/summary.css" type="text/css" rel="stylesheet" />
<link media="screen" href="${pageContext.request.contextPath}/resources/lib/spineconcept/css/literature.css" type="text/css" rel="stylesheet" />
<link media="screen" href="${pageContext.request.contextPath}/resources/lib/spineconcept/css/species.css" type="text/css" rel="stylesheet" />
<link media="screen" href="${pageContext.request.contextPath}/resources/lib/spineconcept/javascript/jquery-ui/css/custom-theme/jquery-ui-1.8.11.custom.css" type="text/css" rel="stylesheet" />
        <script src="${pageContext.request.contextPath}/resources/lib/spineconcept/javascript/jquery-1.5.1.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/resources/lib/spineconcept/javascript/jquery-ui/js/jquery-1.5.1.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/resources/lib/spineconcept/javascript/jquery-ui/js/jquery-ui-1.8.11.custom.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/resources/lib/spineconcept/javascript/summary.js" type="text/javascript"></script>-->

<script>
    $(document).ready(function () {

      $('#mol_XXX').hide();
      
          $("a.showLink").click(function (event) {
        
        //$('#mol_XXX').show();
        
        var clickedId = event.target.id;
        var idClickedSplit = clickedId.split("_");
        /*id of the link is made up by 3 parts:
         part 1: name of the div (eg.: syn) this is used to distinguish the show more
         link of synonyms from the show more link in other divs
         part 2: "link" to distinguish the link for show more link from other
         ordinary links
         part 3: the order of the result item to distinguish the show more button
         in the result list is click. In case of filters of species or compounds
         the order is always 0
         */
        var idPrefixClicked = idClickedSplit[0];
        var itemClicked = idClickedSplit[1];
        var orderOfItemClicked = idClickedSplit[2];
        var idOfHiddenText = "#" + idPrefixClicked + "_" + orderOfItemClicked;
        var jqClickedId = "#" + clickedId;
        var linkValue = $(jqClickedId).text();
        var splitLinkName = linkValue.split(" ");
        //$(idOfHiddenText).show();
       
   
        if (jQuery.inArray('all', splitLinkName) > -1) {
            $(idOfHiddenText).show();
            $(idOfHiddenText).css("display","inline");
             console.log("Linkname "+ splitLinkName + " hiddenId "+ idOfHiddenText);
            if (jQuery.inArray('function', splitLinkName) > -1) {
                $(jqClickedId).html(linkValue.replace('more', 'less'));
            } else {
                $(jqClickedId).html(linkValue.replace('more', 'fewer'));
            }
        } else {
            $(idOfHiddenText).hide();
            $(jqClickedId).html(linkValue.replace(/less|fewer/g, 'more'));
        }
    });
      
      

    });
</script>

<%--
<div style="background-color: yellow">
topComparison.key = ${topComparison.key}<br/>
theComparison.key = ${theComparison.key}<br/>
theComparison.value.['class'].simpleName = ${theComparison.value['class'].simpleName}<br/>
empty item? = ${empty item}<br/>
comparePath = ${comparePath}
</div>
--%>

<c:choose>
    <c:when test="${empty topComparison.value.subComparisons and
                    (topComparison.value['class'].simpleName eq 'ListComparison'
                    or topComparison.key eq 'Small molecules')}">
            No data available.
    </c:when>
    <c:when test="${theComparison.key eq 'Function' and empty item}">
        No data available.
    </c:when>
    <c:when test="${fn:contains(comparePath, 'EC classification')
                    and not empty item}">
            <a href="${intenzConfig.ecBaseUrl}${item}" target="_blank">EC
                ${item}</a>
            </c:when>
            <c:when test="${theComparison.key eq 'Sequence'}">
        <!--        <a href="${item.sequenceurl}" target="_blank">${item.length}</a>-->
            <span>${item.length}</span>
            amino acids.
            <span class="FIXME" style="display: none">Mass: ${item.weight} Da</span>
    </c:when>
    <c:when test="${theComparison.key eq 'Protein structures'}">
        <c:choose>
            <c:when test="${fn:length(item) eq 0}">
                No protein structures available.
            </c:when>
            <c:otherwise>
                <select id="ps${i}" size="5" class="ps"
                        onchange="showStructureImg(this.value, '#psImg${i}')">
                    <c:forEach var="ps" items="${item}" varStatus="psVs">
                        <option value="${ps.id}" title="${ps.name}"
                                ${psVs.first? 'selected':'' }>
                            ${ps.id} ${ps.name}
                        </option>
                    </c:forEach>
                </select>
            </c:otherwise>
        </c:choose>
    </c:when>
    <c:when test="${topComparison.key eq 'Reactions and pathways'
                    and not empty item}">
        <c:set var="rheaEntryBaseUrl"
               value="http://www.ebi.ac.uk/rhea/reaction.xhtml?id="/>

        <div style="margin-left: 2em">
            <c:if test="${not empty item.reaction.id}">
                <div>
                    <span class="comparison subheader">Reaction:</span>
                    <br/>
                    ${item.reaction.name}
                </div>
                <div>
                    <span class="comparison subheader">Rhea identifier:</span>
                    <a href="${rheaEntryBaseUrl}${item.reaction.id}"
                       target="_blank">RHEA:${item.reaction.id}</a>
                </div>
            </c:if>
            <c:if test="${not empty item.pathways}">
                <div>
                    <span class="comparison subheader">Pathways:</span>
                    <c:forEach var="pathway" items="${item.pathways}" varStatus="vs">
                        <a href="${reactomeConfig.eventBaseUrl}${pathway.id}"
                           target="_blank">${pathway.id}</a>${vs.last? '' : ','}
                    </c:forEach>
                </div>
            </c:if>
        </div>
    </c:when>
    <c:when test="${topComparison.key eq 'Small molecules'}">
        <c:choose>
            <c:when test="${empty item}">
                &nbsp;
            </c:when>
            <c:when test="${theComparison.value['class'].simpleName
                            eq 'MoleculeComparison'}">
                <c:set var="molecule" value="${item}"/>
                <%@include file="comparison-item-molecule.jspf" %>
            </c:when>
            <c:otherwise>
                <%-- a list of molecules --%>
                <%--
               <c:forEach var="molecule" items="${item}">
                   <%@include file="comparison-item-molecule.jspf" %>
               </c:forEach>
                --%>




                <c:forEach var="molecule" items="${item}"
                           begin="0"
                           end="3">

                    <%@include file="comparison-item-molecule.jspf" %>

                </c:forEach>
 
                <c:if test="${fn:length(item) >= 4}">
                    <a href="#Small molecules" id="more-molecule-trigger">Show all ${fn:length(item)} found...</a>
                    <div id="more-molecule" >
                        <c:forEach var="molecule" items="${item}"
                                   begin="4"
                                   end="${fn:length(item)-1}">

                            <%@include file="comparison-item-molecule.jspf" %>

                        </c:forEach>
                    </div>
                    <a href="#Small molecules" id="less-molecule-trigger">Show less...</a>
                </c:if>
                    
          <%--        

  <c:set var="molSize" value="${fn:length(item)}"/>
    <c:set var="maxDisplay" value="${4}"/>
  

                <c:if test="${molSize>=maxDisplay}">
                <span id='mol_XXX' >
                    <c:forEach var="molecule" items="${item}" begin="${maxDisplay}" end="${molSize-1}">
                         <%@include file="comparison-item-molecule.jspf" %>

                    </c:forEach>
                </span>
                <a class="showLink" id="<c:out value='mol_link_XXX'/>">Show all ${fn:length(item)} found</a>
            </c:if>

   --%>





            </c:otherwise>
        </c:choose>
    </c:when>
    <c:when test="${topComparison.key eq 'Diseases' and not empty item}">
        <a href="${item.url}" target="_blank"
           style="font-weight: bold">${item.name}</a>:
        ${item.description}
        <c:if test="${not empty item.evidences}">
            <br/><b>Evidence:</b>
            <ul>
                <c:forEach var="ev" items="${item.evidences}">
                    <li>${ev}</li>
                    </c:forEach>
            </ul>
        </c:if>
    </c:when>
    <c:when test="${empty item}">
        &nbsp;
    </c:when>
    <c:otherwise>
        ${item}
    </c:otherwise>
</c:choose>

<script>
    $(document).ready(function () {
        $('#more-molecule').hide();
        $('#less-molecule-trigger').hide();
        $('#more-molecule-trigger').click(function () {

            $('#more-molecule').show();
            $('#less-molecule-trigger').show();
            $('#more-molecule-trigger').hide();
        });

        $('#less-molecule-trigger').click(function () {

            $('#more-molecule').hide();
            $('#less-molecule-trigger').hide();
            $('#more-molecule-trigger').show();
        });
    });
</script>


</* global checkedFilters */

%-- 
    Document   : filter-family
    Created on : Mar 23, 2015, 3:54:09 PM
    Author     : joseph
--%>


 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib  prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="xchars" uri="http://www.ebi.ac.uk/xchars"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="Fn" uri="/WEB-INF/epTagLibray.tld" %>  
<div class="sublevel1" >
    <div class="subTitle">
        Enzyme Family (<c:out value="${fn:length(searchFilter.ecNumbers)}"></c:out>)
    </div>

    <div class="filterContent">
        <c:set var="ecList" value="${searchFilter.ecNumbers}"/>
        <c:set var="ecListSize" value="${fn:length(ecList)}"/>
        <c:set var="ecParams"
            value="${searchModel.searchparams.ecFamilies}"/>

        <c:if test="${ecListSize > 0}">


        <div id="ecFamilies_filters_y"
             style="${empty searchModel.searchparams.ecFamilies?
            'border-bottom: none' : 'border-bottom: thin solid #ddd' }"></div>
        <div id="ecFamilies_filters_n"></div>

<script>
// (See search.js if in doubt)

// Initialise variables:
var group = 'ecFamilies';
checkedFilters[group] = [];
uncheckedFilters[group] = [];
displayedFilters[group] = 0;

// Populate js variables with data from server:
//<c:forEach var="ec" items="${ecList}">
var ef = { "id": "${ec.ec}", "name": "${ec.family}" };
<c:choose><c:when test="${Fn:contains(ecParams, ec.ec)}">
checkedFilters[group][checkedFilters[group].length] = ef; //</c:when>
<c:otherwise>
uncheckedFilters[group][uncheckedFilters[group].length] = ef; //</c:otherwise>
</c:choose>
</c:forEach>

// Display filters:
for (var i = 0; i < checkedFilters[group].length; i++){
     addCheckboxEc(group, checkedFilters[group][i], true);
}
for (var i = 0; displayedFilters[group] < ${filterSizeDefault}
        && displayedFilters[group] < ${ecListSize}; i++){
     addCheckboxEc(group, uncheckedFilters[group][i], false);
}

</script>


        </c:if>
    </div>
    </div>

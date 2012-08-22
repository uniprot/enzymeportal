<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fieldset>
	<legend>${pathway.name}</legend>
	<c:if test="${not empty pathway.description}">
	<div style="margin-top: 0.5ex;">${pathway.description}</div>
	</c:if>
	<c:if test="${not empty pathway.image}">
	<div>
		<a target="blank" href="${pathway.url}">
			<img src="http://www.reactome.org/${pathway.image}"
				class="pathwayImg" alt="${pathway.id}"/>
		</a>
	</div>
	</c:if>
	<div class="inlineLinks">
	    <a target="blank" href="${pathway.url}">
            <spring:message code="label.entry.reactionsPathways.link.reactome"/>
	    </a>
	</div>
</fieldset>

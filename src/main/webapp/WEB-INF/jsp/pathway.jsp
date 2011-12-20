<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<fieldset>
	<legend>${pathway.name}</legend>
	<br/>
	<div>${pathway.description}</div>
	<div>
		<a target="blank" href="${pathway.url}">
			<img src="http://www.reactome.org/${pathway.image}" alt=""/>
		</a>
	</div>
	<div class="inlineLinks">
	    <a target="blank" href="${pathway.url}">
            <spring:message code="label.entry.reactionsPathways.link.reactome"/>
	    </a>
	</div>
</fieldset>

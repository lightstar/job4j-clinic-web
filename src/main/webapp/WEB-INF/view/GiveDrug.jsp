<%--@elvariable id="prefix" type="java.lang.String"--%>
<%--@elvariable id="_csrf" type="org.springframework.security.web.csrf.CsrfToken"--%>

<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" value="Give drug to pet" scope="page"/>
<c:set var="current" value="drug" scope="page"/>
<%@include file="Header.jsp" %>

<c:url value='${prefix}/drug/give' var="action">
    <c:param name="name" value="${param.name}"/>
</c:url>
<form action="${action}" method="post" class="form" onsubmit="return validateForm(this);">
    <div>
        <label class="element">Drug:</label>
        <p class="element"><c:out value="${param.name}"/></p>
    </div>

    <div>
        <label for="clientName" class="element">Client's name:</label>
        <input type="text" class="element text" id="clientName" name="clientName"
               value="<c:out value='${requestScope.clientName}'/>">
    </div>

    <div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <input type="submit" class="button" value="Give">
        <input type="button" class="button" value="Cancel" onclick="document.location.href='<c:url value="${prefix}/drug"/>';">
    </div>
</form>

<%@include file="Footer.jsp" %>
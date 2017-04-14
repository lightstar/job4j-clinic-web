<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" value="Update client" scope="page"/>
<%@include file="Header.jsp" %>

<c:url value='/client/update' var="action">
    <c:param name="name" value="${param.name}"/>
</c:url>
<form action="${action}" method="post" class="form" onsubmit="return validateForm(this);">
    <div>
        <label class="element">Name:</label>
        <p class="element"><c:out value="${param.name}"/></p>
    </div>

    <div>
        <label for="newName" class="element">New name:</label>
        <input type="text" class="element text" id="newName" name="newName"
               value="<c:out value='${requestScope.newName}'/>">
    </div>

    <div>
        <label for="newEmail" class="element">New email:</label>
        <input type="text" class="element text" id="newEmail" name="newEmail"
               value="<c:out value='${requestScope.newEmail}'/>">
    </div>

    <div>
        <label for="newPhone" class="element">New phone:</label>
        <input type="text" class="element text" id="newPhone" name="newPhone"
               value="<c:out value='${requestScope.newPhone}'/>">
    </div>

    <div>
        <input type="submit" class="button" value="Update">
        <input type="button" class="button" value="Cancel" onclick="document.location.href='<c:url value="/"/>';">
    </div>

    <input type="hidden" name="name" value="<c:out value='${param.name}'/>">
</form>

<%@include file="Footer.jsp" %>
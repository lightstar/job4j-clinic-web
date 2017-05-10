<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" value="Login" scope="page"/>
<c:set var="current" value="main" scope="page"/>
<%@include file="Header.jsp" %>

<form action="<c:url value='/login'/>" method="post" class="form" onsubmit="return validateForm(this);">
    <div>
        <label for="login" class="element">Login:</label>
        <input type="text" class="element text" id="login" name="login"
               value="<c:out value='${requestScope.login}'/>">
    </div>

    <div>
        <label for="password" class="element">Password:</label>
        <input type="password" class="element text" id="password" name="password" value="">
    </div>

    <div>
        <input type="submit" class="button" value="Login">
    </div>
</form>

<%@include file="Footer.jsp" %>
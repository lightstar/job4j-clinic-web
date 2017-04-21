<%--@elvariable id="roles" type="java.util.List<ru.lightstar.clinic.model.Role>"--%>

<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="title" value="Roles" scope="page"/>
<c:set var="current" value="role" scope="page"/>
<%@include file="Header.jsp" %>

<form action="<c:url value='/role/add'/>" method="post" class="above-list" onsubmit="return validateForm(this);">
    <label for="name">Name:</label>
    <input type="text" id="name" name="name" value="<c:out value='${param.name}'/>"/>
    <input type="submit" class="button" value="Add">
</form>

<c:if test="${fn:length(roles) == 0}">
    <p>
        No roles found.
    </p>
</c:if>

<c:if test="${fn:length(roles) > 0}">
    <table class="list role-list">
        <tr>
            <th>Role</th>
            <th>&nbsp;</th>
        </tr>
        <c:forEach items="${roles}" var="role">
            <tr>
                <td>
                    <c:out value="${role.name}"/>
                </td>
                <td>
                    <a href="#" onclick="deleteRole('<c:out value="${role.name}"/>');">Delete</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</c:if>

<%@include file="Footer.jsp" %>
<%--@elvariable id="roles" type="java.util.List<ru.lightstar.clinic.model.Role>"--%>

<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" value="Add client" scope="page"/>
<c:set var="current" value="main" scope="page"/>
<%@include file="Header.jsp" %>

<c:if test="${roles.size() == 0}">
    <p>
        Add some roles before adding clients.
    </p>
</c:if>

<c:if test="${roles.size() > 0}">
<c:url value='/client/add' var="action">
    <c:param name="pos" value="${param.pos}"/>
</c:url>
<form action="${action}" method="post" class="form" onsubmit="return validateForm(this);">
    <div>
        <label class="element">Position:</label>
        <p class="element"><c:out value="${param.pos}"/></p>
    </div>

    <div>
        <label for="name" class="element">Name:</label>
        <input type="text" class="element text" id="name" name="name" value="<c:out value='${param.name}'/>">
    </div>

    <div>
        <label for="role" class="element">Role:</label>
        <select class="element" id="role" name="role">
            <c:forEach items="${roles}" var="role">
                <option value="<c:out value='${role.name}'/>"
                    ${param.role == role.name ? ' selected="selected"' : ''}>
                    <c:out value="${role.name}"/>
                </option>
            </c:forEach>
        </select>
    </div>

    <div>
        <label for="email" class="element">Email:</label>
        <input type="text" class="element text" id="email" name="email" value="<c:out value='${param.email}'/>">
    </div>

    <div>
        <label for="phone" class="element">Phone:</label>
        <input type="text" class="element text" id="phone" name="phone" value="<c:out value='${param.phone}'/>">
    </div>

    <div>
        <input type="submit" class="button" value="Add">
        <input type="button" class="button" value="Cancel" onclick="document.location.href='<c:url value="/"/>';">
    </div>

    <input type="hidden" name="pos" value="<c:out value='${param.pos}'/>">
</form>
</c:if>

<%@include file="Footer.jsp" %>

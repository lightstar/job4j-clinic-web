<%--@elvariable id="prefix" type="java.lang.String"--%>
<%--@elvariable id="roles" type="java.util.List<ru.lightstar.clinic.model.Role>"--%>
<%--@elvariable id="_csrf" type="org.springframework.security.web.csrf.CsrfToken"--%>

<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" value="Add client" scope="page"/>
<c:set var="current" value="main" scope="page"/>
<%@include file="Header.jsp" %>

<c:if test="${empty roles && !clientRoleOnly}">
    <p>
        Add some roles before adding clients.
    </p>
</c:if>

<c:if test="${not empty roles || clientRoleOnly}">
<c:url value='${prefix}/client/add' var="action">
    <c:param name="pos" value="${param.pos}"/>
</c:url>
<form action="${action}" method="post" class="form" onsubmit="return validateForm(this);">
    <div>
        <label class="element">Position:</label>
        <p class="element"><c:out value="${param.pos}"/></p>
    </div>

    <div>
        <label for="name" class="element">Name:</label>
        <input type="text" class="element text" id="name" name="name" value="<c:out value='${requestScope.name}'/>">
    </div>

    <div>
        <label for="password" class="element">Password:</label>
        <input type="password" class="element text" id="password" name="password" value="">
    </div>

    <c:if test="${!clientRoleOnly}">
    <div>
        <label for="role" class="element">Role:</label>
        <select class="element" id="role" name="role">
            <c:forEach items="${roles}" var="role">
                <option value="<c:out value='${role.name}'/>"
                    ${requestScope.role == role.name ? ' selected="selected"' : ''}>
                    <c:out value="${role.name}"/>
                </option>
            </c:forEach>
        </select>
    </div>
    </c:if>

    <div>
        <label for="email" class="element">Email:</label>
        <input type="text" class="element text" id="email" name="email" value="<c:out value='${requestScope.email}'/>">
    </div>

    <div>
        <label for="phone" class="element">Phone:</label>
        <input type="text" class="element text" id="phone" name="phone" value="<c:out value='${requestScope.phone}'/>">
    </div>

    <div>
        <c:if test="${clientRoleOnly}">
            <input type="hidden" name="role" value="client" />
        </c:if>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <input type="submit" class="button" value="Add">
        <input type="button" class="button" value="Cancel" onclick="document.location.href='<c:url value="${prefix}/"/>';">
    </div>
</form>
</c:if>

<%@include file="Footer.jsp" %>
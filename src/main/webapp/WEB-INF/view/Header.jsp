<%--@elvariable id="prefix" type="java.lang.String"--%>

<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Pet clinic</title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/clinic.css'/>">
    <script type="text/javascript" src="<c:url value='/js/jquery-3.2.1.min.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/js/jquery.color.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/js/clinic.js'/>"></script>
</head>
<body>
<div class="content">

<h2>
    ${pageScope.title}

    <c:if test="${prefix != null}">
        <nav>
            <a href="<c:url value='${prefix}/'/>"<c:if test="${pageScope.current == 'main'}"> class="current"</c:if>>Main</a>
            <a href="<c:url value='${prefix}/pet'/>"<c:if test="${pageScope.current == 'pet'}"> class="current"</c:if>>Pets</a>
            <a href="<c:url value='${prefix}/drug'/>"<c:if test="${pageScope.current == 'drug'}"> class="current"</c:if>>Drugs</a>
            <a href="<c:url value='${prefix}/role'/>"<c:if test="${pageScope.current == 'role'}"> class="current"</c:if>>Roles</a>
        </nav>
    </c:if>

    <c:if test="${prefix == null}">
        <security:authorize access="isAuthenticated()">
            <nav>
                <a href="<c:url value='/'/>"<c:if test="${pageScope.current == 'main'}"> class="current"</c:if>>Main</a>
                <security:authorize access="hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')">
                    <a href="<c:url value='/pet'/>"<c:if test="${pageScope.current == 'pet'}"> class="current"</c:if>>Pets</a>
                    <a href="<c:url value='/drug'/>"<c:if test="${pageScope.current == 'drug'}"> class="current"</c:if>>Drugs</a>
                    <security:authorize access="hasRole('ROLE_ADMIN')">
                        <a href="<c:url value='/role'/>"<c:if test="${pageScope.current == 'role'}"> class="current"</c:if>>Roles</a>
                    </security:authorize>
                </security:authorize>
                <a href="<c:url value='/logout'/>">Logout</a>
            </nav>
        </security:authorize>
    </c:if>
</h2>

<c:if test="${sessionScope.error != null}">
    <p class="error"><c:out value="${sessionScope.error}"/></p>
    <c:remove var="error" scope="session" />
</c:if>

<c:if test="${sessionScope.message != null}">
    <p class="message"><c:out value="${sessionScope.message}"/></p>
    <c:remove var="message" scope="session" />
</c:if>

<c:if test="${requestScope.error != null}">
    <p class="error"><c:out value="${requestScope.error}"/></p>
</c:if>

<c:if test="${requestScope.message != null}">
    <p class="message"><c:out value="${requestScope.message}"/></p>
</c:if>
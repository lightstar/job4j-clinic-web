<%--@elvariable id="clinicService" type="ru.lightstar.clinic.ClinicService"--%>
<%--@elvariable id="error" type="String"--%>

<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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

    <h2>Add client</h2>

    <c:if test="${error != null}">
        <p class="error"><c:out value="${error}"/></p>
    </c:if>

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
</div>
</body>
</html>

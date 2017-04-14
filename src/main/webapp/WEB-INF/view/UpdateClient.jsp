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

    <h2>Update client</h2>

    <c:if test="${error != null}">
        <p class="error"><c:out value="${error}"/></p>
    </c:if>

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
</div>
</body>
</html>

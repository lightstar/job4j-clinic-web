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
    <script type="text/javascript" src="<c:url value='/js/clinic.js'/>"></script>
</head>
<body>
<h2>Add client</h2>

<c:if test="${error != null}">
    <p class="error"><c:out value="${error}"/></p>
</c:if>

<c:url value='/client/add' var="action">
    <c:param name="pos" value="${param.pos}"/>
</c:url>
<form action="${action}" method="post">
    <table class="form">
        <tr>
            <td>
                Position:
            </td>
            <td>
                <b>
                    <c:out value="${param.pos}"/>
                </b>
            </td>
        </tr>
        <tr>
            <td>
                <label for="name">Name:</label>
            </td>
            <td>
                <input type="text" id="name" name="name" value="<c:out value='${param.name}'/>">
            </td>
        </tr>
    </table>
    <input type="hidden" name="pos" value="<c:out value='${param.pos}'/>">
    <input type="submit" value="Add">
</form>
</body>
</html>

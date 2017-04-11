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

<h2>Update client pet's name</h2>

<c:if test="${error != null}">
    <p class="error">${error}</p>
</c:if>

<c:url value='/client/pet/update' var="action">
    <c:param name="name" value="${param.name}"/>
</c:url>
<form action="${action}" method="post">
    <table class="form">
        <tr>
            <td>
                Name:
            </td>
            <td>
                <b>
                    <c:out value="${param.name}"/>
                </b>
            </td>
        </tr>
        <tr>
            <td>
                <label for="petName">Pet's new name:</label>
            </td>
            <td>
                <input type="text" id="petName" name="petName" value="<c:out value='${param.petName}'/>">
            </td>
        </tr>
    </table>
    <input type="hidden" name="name" value="<c:out value='${param.name}'/>">
    <input type="submit" value="Update">
</form>
</body>
</html>
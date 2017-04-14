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

    <h2>Update client's pet</h2>

    <c:if test="${error != null}">
        <p class="error">${error}</p>
    </c:if>

    <c:url value='/client/pet/update' var="action">
        <c:param name="name" value="${param.name}"/>
        <c:param name="petName" value="${param.petName}"/>
    </c:url>
    <form action="${action}" method="post" class="form" onsubmit="return validateForm(this);">
        <div>
            <label class="element">Name:</label>
            <p class="element"><c:out value="${param.name}"/></p>
        </div>

        <div>
            <label for="newName" class="element">Pet's new name:</label>
            <input type="text" class="element text" id="newName" name="newName"
                   value="<c:out value='${requestScope.newName}'/>">
        </div>

        <div>
            <label for="newAge" class="element">Pet's new age:</label>
            <input type="text" class="element text" id="newAge" name="newAge"
                   value="<c:out value='${requestScope.newAge}'/>">
        </div>

        <div>
            <label class="element">Pet's new sex:</label>
            <span class="element" id="newSex">
                <label for="newSexM">Male</label>
                <input type="radio" class="radio" id="newSexM" name="newSex" value="m"
                       <c:if test="${requestScope.newSex == 'm'}">checked</c:if>/>

                <label for="newSexF">Female</label>
                <input type="radio" class="radio" id="newSexF" name="newSex" value="f"
                        <c:if test="${requestScope.newSex == 'f'}">checked</c:if>/>
            </span>
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

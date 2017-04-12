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

    <h2>Set client's pet</h2>

    <c:if test="${error != null}">
        <p class="error"><c:out value="${error}"/></p>
    </c:if>

    <c:url value='/client/pet/set' var="action">
        <c:param name="name" value="${param.name}"/>
    </c:url>
    <form action="${action}" method="post" class="form" onsubmit="return validateForm(this);">
        <div>
            <label class="element">Client:</label>
            <p class="element"><c:out value="${param.name}"/></p>
        </div>

        <div>
            <label for="petType" class="element">Pet's type:</label>
            <span class="element">
                <select id="petType" name="petType">
                    <c:forEach items="${clinicService.knownPetTypes}" var="petType">
                        <option value="<c:out value='${petType}'/>"
                            ${param.petType == petType ? ' selected="selected"' : ''}>
                            <c:out value="${petType}"/>
                        </option>
                    </c:forEach>
                </select>
            </span>
        </div>

        <div>
            <label for="petName" class="element">Pet's name:</label>
            <span class="element">
                <input type="text" class="text" id="petName" name="petName" value="<c:out value='${param.petName}'/>">
            </span>
        </div>

        <div>
            <input type="submit" class="button" value="Set">
            <input type="button" class="button" value="Cancel" onclick="document.location.href='<c:url value="/"/>';">
        </div>

        <input type="hidden" name="name" value="<c:out value='${param.name}'/>">
    </form>
</div>
</body>
</html>

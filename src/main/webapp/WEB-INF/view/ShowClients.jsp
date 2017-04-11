<%--@elvariable id="clinicService" type="ru.lightstar.clinic.ClinicService"--%>
<%--@elvariable id="error" type="String"--%>
<%--@elvariable id="clients" type="ru.lightstar.clinic.Client[]"--%>

<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
    <title>Pet clinic</title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/clinic.css'/>">
    <script type="text/javascript" src="<c:url value='/js/clinic.js'/>"></script>
</head>
<body>

<h2>Clients</h2>

<c:if test="${error != null}">
    <p class="error"><c:out value="${error}"/></p>
</c:if>

<form action="<c:url value='/'/>" method="get">
    <table class="form">
        <tr>
            <td>
                <label for="filterName">Client:</label>
            </td>
            <td>
                <input type="text" id="filterName" name="filterName" value="<c:out value='${param.filterName}'/>">
            </td>
            <td>
                <input type="submit" value="Filter">
            </td>
        </tr>
    </table>
</form>

<form action="<c:url value='/'/>" method="get">
    <table class="form">
        <tr>
            <td>
                <label for="filterPetName">Client's pet:</label>
            </td>
            <td>
                <input type="text" id="filterPetName" name="filterPetName" value="<c:out value='${param.filterPetName}'/>">
            </td>
            <td>
                <input type="submit" value="Filter">
            </td>
        </tr>
    </table>
</form>

<c:if test="${fn:length(clients) == 0}">
    <p>
        No clients found.
    </p>
</c:if>
<c:if test="${fn:length(clients) > 0}">
<table class="list">
    <tr>
        <th>#</th>
        <th>Client</th>
        <th>Pet</th>
        <th>&nbsp;</th>
    </tr>
    <c:forEach items="${clients}" var="client" varStatus="status">
        <tr>
            <td>
                    ${client == null ? status.count : client.position + 1}
            </td>
            <td>
                    <c:if test="${client == null}">
                        NONE
                    </c:if>
                    <c:if test="${client != null}">
                        <c:out value="${client.name}"/>
                    </c:if>
            </td>
            <td>
                    <c:if test="${client == null || client.pet.name == ''}">
                        NONE
                    </c:if>
                    <c:if test="${client != null && client.pet.name != ''}">
                        <c:out value="${client.pet}"/>
                    </c:if>
            </td>
            <td>
                <c:if test="${client == null}">
                    <c:url value='/client/add' var="addUrl">
                        <c:param name="pos" value="${status.count}"/>
                    </c:url>
                    <a href="${addUrl}">Add</a>
                </c:if>

                <c:if test="${client != null}">
                    <c:url value='/client/update' var="updateUrl">
                        <c:param name="name" value="${client.name}"/>
                    </c:url>
                    <a href="${updateUrl}">Update name</a>

                    <c:url value='/client/pet/set' var="setPetUrl">
                        <c:param name="name" value="${client.name}"/>
                    </c:url>
                    <a href="${setPetUrl}">Set pet</a>

                    <c:if test="${client.pet.name != ''}">
                        <c:url value='/client/pet/update' var="updatePetUrl">
                            <c:param name="name" value="${client.name}"/>
                        </c:url>
                        <a href="${updatePetUrl}">Update pet's name</a>

                        <a href="#" onclick="deleteClientPet('<c:out value="${client.name}"/>', '<c:out value="${pageContext.request.contextPath}"/>'); return false;">Delete pet</a>
                    </c:if>

                    <a href="#" onclick="deleteClient('<c:out value="${client.name}"/>', '<c:out value="${pageContext.request.contextPath}"/>'); return false;">Delete</a>
                </c:if>
            </td>
        </tr>
    </c:forEach>
</table>
</c:if>
</body>
</html>

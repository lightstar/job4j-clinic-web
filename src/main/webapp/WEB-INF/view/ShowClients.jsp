<%--@elvariable id="prefix" type="java.lang.String"--%>
<%--@elvariable id="clients" type="ru.lightstar.clinic.model.Client[]"--%>
<%--@elvariable id="_csrf" type="org.springframework.security.web.csrf.CsrfToken"--%>
<%--@elvariable id="me" type="java.lang.String"--%>

<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="title" value="Pet clinic" scope="page"/>
<c:set var="current" value="main" scope="page"/>
<%@include file="Header.jsp" %>

<c:if test="${!requestScope.noFilter}">
    <form action="" method="get" class="above-list">
        <select id="filterType" name="filterType">
            <option value="client" ${param.filterType == 'client' ? ' selected="selected"' : ''}>Client:</option>
            <option value="pet" ${param.filterType == 'pet' ? ' selected="selected"' : ''}>Pet:</option>
        </select>

        <input type="text" class="text" id="filterName" name="filterName" value="<c:out value='${param.filterName}'/>">

        <input type="submit" class="button" value="Filter">
    </form>
</c:if>

<c:if test="${fn:length(clients) == 0}">
    <p>
        No clients found.
    </p>
</c:if>

<c:if test="${fn:length(clients) > 0}">
    <table class="list client-list">
        <tr>
            <th>#</th>
            <th>Client</th>
            <th>Pet</th>
            <th>&nbsp;</th>
        </tr>
        <c:forEach items="${clients}" var="client">
            <tr>
                <td>
                        ${client.position + 1}
                </td>
                <td>
                    ${not empty client.name ? client.name : "NONE"}
                </td>
                <td>
                    <c:if test="${empty client.pet.name}">
                        NONE
                    </c:if>
                    <c:if test="${not empty client.pet.name}">
                        <c:out value="${client.pet}"/>
                    </c:if>
                </td>
                <td>
                    <c:if test="${empty client.name}">
                        <c:url value='${prefix}/client/add' var="addUrl">
                            <c:param name="pos" value="${client.position + 1}"/>
                        </c:url>
                        <a href="${addUrl}">Add</a>
                    </c:if>

                    <c:if test="${not empty client.name}">
                        <c:url value='${prefix}/client/update' var="updateUrl">
                            <c:param name="name" value="${client.name}"/>
                        </c:url>
                        <a href="${updateUrl}">Update</a>

                        <c:if test="${client.pet.name == ''}">
                            <c:url value='${prefix}/client/pet/set' var="setPetUrl">
                                <c:param name="name" value="${client.name}"/>
                            </c:url>
                            <a href="${setPetUrl}">Set pet</a>
                        </c:if>

                        <c:if test="${not empty client.pet.name}">
                            <c:url value='${prefix}/client/pet/update' var="updatePetUrl">
                                <c:param name="name" value="${client.name}"/>
                            </c:url>
                            <a href="${updatePetUrl}">Update pet</a>

                            <a href="#" onclick="deleteClientPet('<c:out value="${client.name}"/>',
                                    '${_csrf.parameterName}', '${_csrf.token}');">Delete pet</a>
                        </c:if>

                        <c:if test="${me != client.name}">
                            <a href="#" onclick="deleteClient('<c:out value="${client.name}"/>',
                                    '${_csrf.parameterName}', '${_csrf.token}');">Delete</a>
                        </c:if>

                        <c:url value='${prefix}/client/message' var="messagesUrl">
                            <c:param name="name" value="${client.name}"/>
                        </c:url>
                        <a href="${messagesUrl}">Messages</a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </table>
</c:if>

<%@include file="Footer.jsp" %>
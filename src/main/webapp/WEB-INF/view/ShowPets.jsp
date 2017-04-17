<%--@elvariable id="pets" type="ru.lightstar.clinic.pet.Pet[]"--%>

<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="title" value="Pets" scope="page"/>
<c:set var="current" value="pet" scope="page"/>
<%@include file="Header.jsp" %>

<c:if test="${fn:length(pets) == 0}">
    <p>
        No pets found.
    </p>
</c:if>

<c:if test="${fn:length(pets) > 0}">
    <table class="list pet-list">
        <tr>
            <th>Pet</th>
        </tr>
        <c:forEach items="${pets}" var="pet">
            <tr>
                <td>
                    <c:out value="${pet}"/>
                </td>
            </tr>
        </c:forEach>
    </table>
</c:if>

<%@include file="Footer.jsp" %>
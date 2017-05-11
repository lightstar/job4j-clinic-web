<%--@elvariable id="prefix" type="java.lang.String"--%>
<%--@elvariable id="drugService" type="ru.lightstar.clinic.DrugService"--%>
<%--@elvariable id="drugs" type="java.util.Map<ru.lightstar.clinic.drug.Drug,java.lang.Integer>"--%>
<%--@elvariable id="_csrf" type="org.springframework.security.web.csrf.CsrfToken"--%>

<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" value="Drugs" scope="page"/>
<c:set var="current" value="drug" scope="page"/>
<%@include file="Header.jsp" %>

<form action="<c:url value='${prefix}/drug/add'/>" method="post" class="above-list">
    <label for="name">Name:</label>
    <select id="name" name="name">
        <c:forEach items="${drugService.knownDrugNames}" var="name">
            <option value="<c:out value='${name}'/>"
                ${param.name == name ? ' selected="selected"' : ''}>
                <c:out value="${name}"/>
            </option>
        </c:forEach>
    </select>

    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <input type="submit" class="button" value="Add">
</form>

<c:if test="${drugs.size() == 0}">
    <p>
        No drugs found.
    </p>
</c:if>

<c:if test="${drugs.size() > 0}">
    <table class="list drug-list">
        <tr>
            <th>Drug</th>
            <th>Danger</th>
            <th>Count</th>
            <th>&nbsp;</th>
        </tr>
        <c:forEach items="${drugs}" var="drug">
            <tr>
                <td>
                    <c:out value="${drug.key.name}"/>
                </td>
                <td>
                    <c:out value="${drug.key.dangerLevel}"/>
                </td>
                <td>
                    <c:out value="${drug.value}"/>
                </td>
                <td>
                    <c:url value='${prefix}/drug/give' var="giveUrl">
                        <c:param name="name" value="${drug.key.name}"/>
                    </c:url>
                    <a href="${giveUrl}">Give</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</c:if>

<%@include file="Footer.jsp" %>
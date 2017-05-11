<%--@elvariable id="prefix" type="java.lang.String"--%>
<%--@elvariable id="clinicService" type="ru.lightstar.clinic.ClinicService"--%>
<%--@elvariable id="_csrf" type="org.springframework.security.web.csrf.CsrfToken"--%>

<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" value="Set client's pet" scope="page"/>
<c:set var="current" value="main" scope="page"/>
<%@include file="Header.jsp" %>

<c:url value='${prefix}/client/pet/set' var="action">
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
                        ${requestScope.petType == petType ? ' selected="selected"' : ''}>
                        <c:out value="${petType}"/>
                    </option>
                </c:forEach>
            </select>
        </span>
    </div>

    <div>
        <label for="petName" class="element">Pet's name:</label>
        <input type="text" class="element text" id="petName" name="petName"
               value="<c:out value='${requestScope.petName}'/>">
    </div>

    <div>
        <label for="petAge" class="element">Pet's age:</label>
        <input type="text" class="element text" id="petAge" name="petAge"
               value="<c:out value='${requestScope.petAge}'/>">
    </div>

    <div>
        <label class="element">Pet's sex:</label>
        <span class="element" id="petSex">
            <label for="petSexM">Male</label>
            <input type="radio" class="radio" id="petSexM" name="petSex" value="m"
                   <c:if test="${requestScope.petSex == 'm'}">checked</c:if>/>

            <label for="petSexF">Female</label>
            <input type="radio" class="radio" id="petSexF" name="petSex" value="f"
                   <c:if test="${requestScope.petSex == 'f'}">checked</c:if>/>
        </span>
    </div>

    <div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <input type="submit" class="button" value="Set">
        <input type="button" class="button" value="Cancel" onclick="document.location.href='<c:url value="${prefix}/"/>';">
    </div>
</form>

<%@include file="Footer.jsp" %>
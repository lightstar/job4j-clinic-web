<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" value="Update client's pet" scope="page"/>
<%@include file="Header.jsp" %>

<c:url value='/client/pet/update' var="action">
    <c:param name="name" value="${param.name}"/>
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

<%@include file="Footer.jsp" %>
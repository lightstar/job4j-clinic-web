<%--@elvariable id="error" type="String"--%>

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

<h2>${pageScope.title}</h2>

<c:if test="${error != null}">
    <p class="error"><c:out value="${error}"/></p>
</c:if>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>List Travelers On A Trip</title>
</head>
<body>
<c:if test="${not empty errorMessage}">
    <strong style="color: red">${errorMessage}</strong>
    <c:set var="errorMessage" scope="session" value="${null}"/>
    <br><br>
</c:if>
<nav>
    <ul>
        <li><a href="/web/secured/Menu.jsp">Back</a></li>
        <li><a href="/web/Logout.jsp">Logout</a></li>
    </ul>
</nav>
</body>
</html>

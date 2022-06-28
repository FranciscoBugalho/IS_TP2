<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>Trip Organizer</title>
</head>
<body>
<title>Welcome to the Trip Organizer of UC!</title>
Please choose the preferred option: <br/>
<nav>
    <ul>
        <li><a href="/web/ScreenRegister.html">Register</a></li>
        <li><a href="/web/ScreenLogin.html">Login</a></li>
    </ul>
</nav>

<c:if test="${not empty registered}">
    ${registered}
    <c:set var="registered" scope="session" value="${null}"/>
</c:if>

</body>
</html>

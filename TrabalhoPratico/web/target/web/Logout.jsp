<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>Session Ended</title>
</head>
<body>
Session has been terminated. <br/><br/>
<c:remove var="email" scope="session"/>
<c:remove var="client" scope="session"/>
<c:remove var="Name" scope="session"/>
<nav>
    <ul>
        <li><a href="/web/index.jsp">Return to main menu</a></li>
    </ul>
</nav>


</body>
</html>

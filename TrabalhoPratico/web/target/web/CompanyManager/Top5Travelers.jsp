<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>List Top 5 Travelers</title>
</head>
<body>

<strong>Top 5 Travelers</strong> <br>
<form action="listTop5Travelers" method="get">
    <ul name="listedTop5Travelers" style="list-style: none;">
        <c:forEach items="${top5}" var="t">
            <li value="${t}">${t}</li>
        </c:forEach>
        <c:set var="top5" scope="session" value="${null}"/>
    </ul>
    <br>
    <nav>
        <ul>
            <li><a href="/web/secured/Menu.jsp">Back</a></li>
            <li><a href="/web/Logout.jsp">Logout</a></li>
        </ul>
    </nav>
</form>
<br>

</body>
</html>

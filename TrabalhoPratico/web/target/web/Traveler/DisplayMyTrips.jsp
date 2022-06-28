<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>List My Trips</title>
</head>
<body>

<strong>My Trips</strong> <br>
<form action="listAllBusTripsByDate" method="get">
    <ul name="listedTrips" style="list-style: none;">
        <c:forEach items="${trips}" var="t">
            <li value="${t}">${t}</li>
        </c:forEach>
        <c:set var="trips" scope="session" value="${null}"/>
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

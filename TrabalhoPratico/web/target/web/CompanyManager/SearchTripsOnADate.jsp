<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>Search Trips On A Date</title>
</head>
<body>

<strong>Select a Date</strong> <br>
<form action="searchTripsOnADate" method="get">
    <label>Date</label>
    <input name="Departure Date" type="date" placeholder="Departure Date"/>
    <br>
    <br>
    <input type="submit" name="SearchButton" value="Search">
    <input type="submit" name="CleanSearch" value="Clean Search">
    <br>
    <br>

    <c:choose>
        <c:when test="${not empty tripsCMDate}">
            <ul name="listedTripsCMDate" style="list-style: none;">
                <c:forEach items="${tripsCMDate}" var="t">
                    <li value="${t}">${t}</li>
                </c:forEach>
            </ul>
        </c:when>
        <c:otherwise>
            <strong style="color: red">${errorMessage}</strong>
            <c:set var="errorMessage" scope="session" value="${null}"/>
        </c:otherwise>
    </c:choose>
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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>List Available Bus Trips</title>
</head>
<body>

<strong>Select a Date Interval</strong> <br>
<form action="listAllBusTripsByDate" method="get">
    <label>Start</label>
    <input name="Departure Date Start" type="date" placeholder="Departure Date" required/>
    <input name="Departure Time Start" type="time" placeholder="Departure Time" required/>
    <br>
    <br>
    <label style="margin-right: 0.1%">End</label>
    <input name="Departure Date End" type="date" placeholder="Departure Date" required/>
    <input name="Departure Time End" type="time" placeholder="Departure Time" required/>
    <br>
    <br>
    <input type="submit" name="SearchButton" value="Search">
    <br>
    <br>

    <c:choose>
        <c:when test="${not empty trips}">
            <ul name="listedTrips" style="list-style: none;">
                <c:forEach items="${trips}" var="t">
                    <li value="${t}">${t}</li>
                </c:forEach>
                <c:set var="trips" scope="session" value="${null}"/>
            </ul>
        </c:when>
        <c:otherwise>
            <strong style="color: red">${errorMessage}</strong>
            <c:set var="errorMessage" scope="session" value="${null}"/>
        </c:otherwise>
    </c:choose>
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

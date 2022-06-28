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

Choose the trip you want to see:
<br>
<br>
<c:choose>
    <c:when test="${not empty thisTrip}">
        <form action="listTravelersOnATrip" method="get">
            <label>Searched Trips</label>
            <select name="TripsSearched">
                <c:forEach items="${thisTrip}" var="t">
                    <option value="${t}" ${t == t ? 'selected' : ''}>${t}</option>
                </c:forEach>

            </select>
            <br>
            <br>
            <input type="submit" name="SearchTraveler" value="Search">
            <nav>
                <ul>
                    <li><a href="/web/secured/Menu.jsp">Back</a></li>
                    <li><a href="/web/Logout.jsp">Logout</a></li>
                </ul>
            </nav>
        </form>
        <br>
    </c:when>
    <c:otherwise>
        <c:redirect url="listTravelersOnATrip"/>
    </c:otherwise>
</c:choose>

<c:if test="${not empty travelers}">
    <strong style="margin: 1%">Travelers:</strong>
    <ul name="listedTravelers" style="list-style: none;">
        <c:forEach items="${travelers}" var="trvl">
            <li value="${trvl}">${trvl}</li>
        </c:forEach>
        <c:set var="travelers" scope="session" value="${null}"/>
    </ul>
</c:if>

</body>
</html>

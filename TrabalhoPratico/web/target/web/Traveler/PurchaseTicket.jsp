<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>Purchase Ticket</title>
</head>
<body>

<c:if test="${not empty errorMessage}">
    <strong style="color: red">${errorMessage}</strong>
    <br><br>
</c:if>

Choose the trip you want to buy: <br>
Wallet: <strong><c:out value="${client[2]}"/></strong>
<br>
<c:choose>
    <c:when test="${not empty trips}">
        <form action="purchaseTicket" method="get">
            <label>Available Trips</label>
            <select name="Trips">
                <c:forEach items="${trips}" var="t">
                    <option value="${t}" ${t == t ? 'selected' : ''}>${t}</option>
                </c:forEach>
            </select>
            <c:set var="trips" scope="session" value="${null}"/>

            <input type="submit" name="OrderByDestinationName" value="Destination Name ${arrows[0]}">
            <input type="submit" name="OrderByDepartureName" value="Departure Name ${arrows[1]}">
            <input type="submit" name="OrderByTime" value="Departure Time ${arrows[2]}">
            <input type="submit" name="OrderByPrice" value="Price ${arrows[3]}">
            <br>
            <br>
            <input type="submit" name="PurchaseButton" value="Purchase Trip">
        </form>
        <br>
    </c:when>
    <c:otherwise>
        <c:redirect url="purchaseTicket"/>
    </c:otherwise>
</c:choose>
<c:set var="errorMessage" scope="session" value="${null}"/>

<nav>
    <ul>
        <li><a href="/web/secured/Menu.jsp">Back</a></li>
        <li><a href="/web/Logout.jsp">Logout</a></li>
    </ul>
</nav>

</body>
</html>
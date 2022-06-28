<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>Refund Trips</title>
</head>
<body>

<c:if test="${not empty errorMessage}">
    <strong style="color: red">${errorMessage}</strong>
    <c:set var="errorMessage" scope="session" value="${null}"/>
    <br><br>
</c:if>

Choose the trip you want to refund: <br>
Wallet: <strong><c:out value="${client[2]}"/></strong>

<br>
<c:choose>
    <c:when test="${not empty trips}">
        <form action="refundTrips" method="get">
            <label>Available Trips For Refund</label>
            <select name="Trips">
                <c:forEach items="${trips}" var="t">
                    <option value="${t}" ${t == t ? 'selected' : ''}>${t}</option>
                </c:forEach>
            </select>
            <c:set var="trips" scope="session" value="${null}"/>

            <input type="submit" name="RefundButton" value="Refund Trip">
        </form>
        <br>
    </c:when><c:otherwise>
    <strong>You haven't bought any trips!</strong>
</c:otherwise>
</c:choose>
<nav>
    <ul>
        <li><a href="/web/secured/Menu.jsp">Back</a></li>
        <li><a href="/web/Logout.jsp">Logout</a></li>
    </ul>
</nav>
</body>
</html>

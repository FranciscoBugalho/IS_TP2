<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>Trip Organizer</title>
</head>
<body>
<c:choose>
    <c:when test="${client[0] == 'Error'}">
        ${client[1]} <br/>
        <nav>
            <ul>
                <li><a href="/web/ScreenLogin.html">Try Again</a></li>
            </ul>
        </nav>
    </c:when><c:when test="${client[0] == 'CompanyManager'}">
    <strong>Welcome : <c:out value="${client[1]}"/></strong>
    Please choose the preferred option: <br/>
    <nav>
        <ul>
            <li><a href="/web/CompanyManager/CreateBusTrips.jsp">Create Bus Trips</a></li>
            <li><a href="/web/CompanyManager/DeleteBusTripsRedirect.jsp">Delete Bus Trip</a></li>
            <li><a href="/web/CompanyManager/Top5TravelersRedirect.jsp">List Top 5 Travelers</a></li>
            <li><a href="/web/CompanyManager/SearchTripsByDate.jsp">Search Bus Trips Between Two Dates</a></li>
            <li><a href="/web/CompanyManager/SearchTripsOnADate.jsp">Search Bus Trips On a Given Date</a></li>
            <li><a href="/web/CompanyManager/ListTravelersOnATrip.jsp">List Passengers on a Given Trip</a></li>
            <li><a href="/web/secured/EditPersonalInfo.jsp">Edit Profile</a></li>
            <li><a href="/web/secured/DeleteAccount.jsp">Delete Account</a></li>
            <li><a href="/web/Logout.jsp">Logout</a></li>
        </ul>
    </nav>
</c:when><c:when test="${client[0] == 'Traveler'}">
    <p>Welcome : <c:out value="${client[1]}"/></p>
    Please choose the preferred option: <br/>
    <nav>
        <ul>
            <li><a href="/web/Traveler/ListTripsByDate.jsp">List Available Trips</a></li>
            <li><a href="/web/Traveler/ListMyTrips.jsp">List My Trips</a></li>
            <li><a href="/web/Traveler/PurchaseTicket.jsp">Purchase Ticket</a></li>
            <li><a href="/web/Traveler/RefundTripsRedirect.jsp">Refund Ticket</a></li>
            <li><a href="/web/Traveler/ChargeWallet.jsp">Charge Wallet</a></li>
            <li><a href="/web/secured/EditPersonalInfo.jsp">Edit Profile</a></li>
            <li><a href="/web/secured/DeleteAccount.jsp">Delete Account</a></li>
            <li><a href="/web/Logout.jsp">Logout</a></li>
        </ul>
    </nav>
</c:when>
</c:choose>

</body>
</html>

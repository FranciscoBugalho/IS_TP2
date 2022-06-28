<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>Register Screen</title>
</head>
<body>

Let's charge your wallet:<br>
Current Balance: <strong><c:out value="${client[2]}"/></strong>
<form action="chargeWallet" method="get">
    <label for="walletMoney">Money to add (in euros):</label><br>
    <input id="walletMoney" name="Money" type="number" placeholder="Price" step="0.1" min="5" max="25000" required/><br>
    <input type="submit" value="Pay">
</form>
<nav>
    <ul>
        <li><a href="/web/secured/Menu.jsp">Cancel</a></li>
        <li><a href="/web/Logout.jsp">Logout</a></li>
    </ul>
</nav>
</body>
</html>

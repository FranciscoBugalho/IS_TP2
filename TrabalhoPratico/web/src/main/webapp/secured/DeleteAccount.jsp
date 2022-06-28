<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>Delete Account</title>
</head>
<body>
<c:choose>
    <c:when test="${not empty triedDelete}">
        <c:set var="triedDelete" scope="session" value="${null}"/>
        Wrong password. <br/>
        <nav>
            <ul>
                <li><a href="/web/secured/DeleteAccount.jsp">Return</a></li>
            </ul>
        </nav>
    </c:when><c:otherwise>
    Are you sure you want do delete your account? This is a <strong>non-reversible</strong> operation!<br>
    Everything related to you will be deleted from the system including all traces of existing.
    <form action="deleteAccount" method="get">
        <label for="Pword">Please insert your password in order to verify your choice:</label><br>
        <input id="Pword" name="Password" type="password" placeholder="password" required> <br><br>
        <input type="submit" value="Delete Account">
    </form>
    <nav>
        <ul>
            <li><a href="/web/secured/Menu.jsp">Cancel</a></li>
            <li><a href="/web/Logout.jsp">Logout</a></li>
        </ul>
    </nav>
</c:otherwise>
</c:choose>

</body>
</html>

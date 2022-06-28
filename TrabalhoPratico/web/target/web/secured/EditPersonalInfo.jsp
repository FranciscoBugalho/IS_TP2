<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>Register Screen</title>
</head>
<body>

Let's edit your personal profile:
<form action="editClientInfo" method="get">
    <label for="Uname">Username:</label><br>
    <input id="Uname" name="Name" type="text" placeholder="username" required maxlength="16" value="${client[1]}">
    <br><br>

    <label for="Pword">Password (leave empty if you don't want to change it):</label><br>
    <input id="Pword" name="Password" type="password" placeholder="password"> <br><br>

    <input type="submit" value="Edit Profile">
</form>
<nav>
    <ul>
        <li><a href="/web/secured/Menu.jsp">Cancel</a></li>
        <li><a href="/web/Logout.jsp">Logout</a></li>
    </ul>
</nav>
</body>
</html>

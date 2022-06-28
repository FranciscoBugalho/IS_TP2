<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>Trip Organizer</title>
</head>
<body>
<form action="createBusTrip" method="get">


    <label for="depPoint">Choose a departure point:</label>
    <select name="Departure Point" id="depPoint" required>
        <optgroup label="Europa">
            <option value="Paris">Paris</option>
            <option value="Lisboa">Lisbon</option>
            <option value="Porto">Porto</option>
            <option value="Madrid">Madrid</option>
            <option value="Brussels">Brussels</option>
        </optgroup>
        <optgroup label="Asia">
            <option value="Shangai">Shangai</option>
            <option value="Tokyo">Tokyo</option>
            <option value="Dubai">Dubai</option>
        </optgroup>
        <optgroup label="America">
            <option value="California">California</option>
            <option value="Vancouver">Vancouver</option>
            <option value="Brasilia">Brasilia</option>
        </optgroup>
    </select>

    <label for="destPoint">Choose a destination:</label>
    <select name="Destination" id="destPoint" required>
        <optgroup label="Europa">
            <option value="Paris">Paris</option>
            <option value="Lisboa">Lisbon</option>
            <option value="Porto">Porto</option>
            <option value="Madrid">Madrid</option>
            <option value="Brussels">Brussels</option>
        </optgroup>
        <optgroup label="Asia">
            <option value="Shangai">Shangai</option>
            <option value="Tokyo">Tokyo</option>
            <option value="Dubai">Dubai</option>
        </optgroup>
        <optgroup label="America">
            <option value="California">California</option>
            <option value="Vancouver">Vancouver</option>
            <option value="Brasilia">Brasilia</option>
        </optgroup>
    </select>

    <input name="Departure Date" type="date" placeholder="Departure Date" required/>
    <input name="Departure Time" type="time" placeholder="Departure Time" required/>
    <input name="Capacity" type="number" placeholder="Capacity" min="1" max="255" required/>
    <input name="Price" type="number" placeholder="Price" step="0.01" min="0" max="10000" required/>
    <input type="submit" value="Create Trip">
    <br>
    <br>
    <a href="/web/secured/Menu.jsp">Back</a>
    <a href="/web/Logout.jsp">Logout</a>
</form>
</body>
</html>

package servlet.Traveler;

import server.ITravelerEJB;
import servlet.LinksEJB;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet("Traveler/listAllBusTripsByDate")
public class ListTripsByDateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String destination = "/Traveler/ListTripsByDate.jsp";
        String departureDateStart = request.getParameter("Departure Date Start");
        String departureTimeStart = request.getParameter("Departure Time Start");
        String departureDateEnd = request.getParameter("Departure Date End");
        String departureTimeEnd = request.getParameter("Departure Time End");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date startDateTime = null;
        Date endDateTime = null;
        Date todayDate = new Date();

        List<String> trips = new ArrayList<>();
        try {
            startDateTime = dateFormat.parse(departureDateStart + " " + departureTimeStart);
            endDateTime = dateFormat.parse(departureDateEnd + " " + departureTimeEnd);

            if (todayDate.after(startDateTime) && startDateTime.before(todayDate)) {
                System.out.println("Using today's date instead!");
                startDateTime = todayDate;
            }

            if (!startDateTime.before(endDateTime) && !endDateTime.after(startDateTime)) {
                request.getSession(true).setAttribute("errorMessage", "Start Date should be before End Date!");
                request.getRequestDispatcher(destination).forward(request, response);
            }
        } catch (ParseException e) {
            destination = "/error.html";
            e.printStackTrace();
        }

        try {
            ITravelerEJB it = InitialContext.doLookup(LinksEJB.TRAVELER_EJB);

            if (it != null) {
                destination = "/Traveler/ListTripsByDate.jsp";

                Timestamp start = new Timestamp(startDateTime.getTime());
                Timestamp end = new Timestamp(endDateTime.getTime());
                trips = it.getTripsSortedByDate(start, end);

                if (trips.isEmpty())
                    request.getSession(true).setAttribute("errorMessage", "There are no bus trips between the two dates!");
            } else {
                destination = "/error.html";
            }
            request.getSession(true).setAttribute("trips", trips);
            request.getRequestDispatcher(destination).forward(request, response);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
}

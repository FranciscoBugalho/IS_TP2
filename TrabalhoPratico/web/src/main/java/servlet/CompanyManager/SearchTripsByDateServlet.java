package servlet.CompanyManager;

import server.ICompanyManagerEJB;
import servlet.LinksEJB;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet("CompanyManager/searchTripsByDate")
public class SearchTripsByDateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String destination = "/CompanyManager/SearchTripsByDate.jsp";
        String button = request.getParameter("CleanSearch");

        if (button != null) {
            request.getSession(true).setAttribute("tripsCM", null);
            request.getRequestDispatcher(destination).forward(request, response);
            request.getSession(true).setAttribute("thisTrip", null);
            return;
        }

        String departureDateStart = request.getParameter("Departure Date Start");
        String departureDateEnd = request.getParameter("Departure Date End");

        if (departureDateStart == null || departureDateStart.equals("") || departureDateEnd == null || departureDateEnd.equals("")) {
            request.getSession(true).setAttribute("errorMessage", "Must complete both fields!");
            request.getRequestDispatcher(destination).forward(request, response);
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date startDateTime = null;
        Date endDateTime = null;
        Date todayDate = new Date();

        List<String> trips = new ArrayList<>();
        try {
            startDateTime = dateFormat.parse(departureDateStart + " 00:00");
            endDateTime = dateFormat.parse(departureDateEnd + " 23:59");

            /*if (todayDate.after(startDateTime) && startDateTime.before(todayDate)) {
                System.out.println("Using today's date instead!");
                startDateTime = todayDate;
            }*/

            if (!startDateTime.before(endDateTime) && !endDateTime.after(startDateTime)) {
                request.getSession(true).setAttribute("errorMessage", "Start Date should be before End Date!");
                request.getRequestDispatcher(destination).forward(request, response);
            }
        } catch (ParseException e) {
            destination = "/error.html";
            e.printStackTrace();
        }

        try {
            ICompanyManagerEJB ic = InitialContext.doLookup(LinksEJB.COMPANY_MANAGER_EJB);

            if (ic != null) {
                destination = "/CompanyManager/SearchTripsByDate.jsp";

                trips = ic.getTripsBetweenTwoDates(startDateTime, endDateTime);

                if (trips.isEmpty())
                    request.getSession(true).setAttribute("errorMessage", "There are no bus trips between the two dates!");
            } else {
                destination = "/error.html";
            }
            request.getSession(true).setAttribute("tripsCM", trips);
            request.getRequestDispatcher(destination).forward(request, response);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
}

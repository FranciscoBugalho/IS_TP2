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

@WebServlet("CompanyManager/searchTripsOnADate")
public class SearchTripsOnADateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String destination = "/CompanyManager/SearchTripsOnADate.jsp";
        String button = request.getParameter("CleanSearch");

        if (button != null) {
            request.getSession(true).setAttribute("tripsCMDate", null);
            request.getRequestDispatcher(destination).forward(request, response);
            request.getSession(true).setAttribute("thisTrip", null);
        }

        String departureDate = request.getParameter("Departure Date");
        if (departureDate == null || departureDate.equals("")) {
            request.getSession(true).setAttribute("errorMessage", "Must complete the field!");
            request.getRequestDispatcher(destination).forward(request, response);
            request.getSession(true).setAttribute("thisTrip", null);
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date dateTime = null;
        Date dateTime2 = null;

        List<String> trips = new ArrayList<>();
        try {
            dateTime = dateFormat.parse(departureDate + " 00:00");
            dateTime2 = dateFormat.parse(departureDate + " 23:59");
        } catch (ParseException e) {
            destination = "/error.html";
            e.printStackTrace();
        }

        try {
            ICompanyManagerEJB ic = InitialContext.doLookup(LinksEJB.COMPANY_MANAGER_EJB);

            if (ic != null) {
                destination = "/CompanyManager/SearchTripsOnADate.jsp";

                trips = ic.getTripsOnADate(dateTime, dateTime2);

                if (trips.isEmpty())
                    request.getSession(true).setAttribute("errorMessage", "There are no bus trips on that date!");
            } else {
                destination = "/error.html";
            }
            request.getSession(true).setAttribute("tripsCMDate", trips);
            request.getRequestDispatcher(destination).forward(request, response);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
}

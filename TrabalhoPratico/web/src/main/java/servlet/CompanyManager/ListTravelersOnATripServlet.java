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
import java.util.*;

@WebServlet("CompanyManager/listTravelersOnATrip")
public class ListTravelersOnATripServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String destination = "/CompanyManager/ListTravelersOnATrip.jsp";
        List<String> searchedTrips = (List<String>) request.getSession(true).getAttribute("thisTrip");
        String button = request.getParameter("SearchButton");


        List<String> combinedList;
        if (searchedTrips == null && button == null) {
            List<String> tripsBetweenDates = (List<String>) request.getSession(true).getAttribute("tripsCM");
            List<String> tripsOnADate = (List<String>) request.getSession(true).getAttribute("tripsCMDate");

            //System.out.println("Between dates:" + tripsBetweenDates);
            //System.out.println("On a date:" + tripsOnADate);
            Set<String> allTrips = null;
            if (tripsBetweenDates == null && tripsOnADate == null) {
                destination = "/CompanyManager/ListTravelersError.jsp";
                request.getSession(true).setAttribute("errorMessage", "Should search some trips before!");
            } else if (tripsBetweenDates == null) {
                allTrips = new LinkedHashSet<>(tripsOnADate);
            } else if (tripsOnADate == null) {
                allTrips = new LinkedHashSet<>(tripsBetweenDates);
            } else {
                allTrips = new LinkedHashSet<>(tripsBetweenDates);
                allTrips.addAll(tripsOnADate);
            }

            if (allTrips != null) {
                destination = "/CompanyManager/ListTravelersOnATrip.jsp";
                combinedList = new ArrayList<>(allTrips);
                request.getSession(true).setAttribute("thisTrip", combinedList);
            } else
                request.getSession(true).setAttribute("errorMessage", "Should search some trips before!");
        } else {
            try {
                ICompanyManagerEJB ic = InitialContext.doLookup(LinksEJB.COMPANY_MANAGER_EJB);
                List<String> travelers = new ArrayList<>();

                if (ic != null) {
                    destination = "/CompanyManager/ListTravelersOnATrip.jsp";

                    travelers = ic.getTravelersOnATrip(request.getParameter("TripsSearched"));

                    if (travelers.isEmpty())
                        request.getSession(true).setAttribute("errorMessage", "This trip have/had no travelers!");
                } else {
                    destination = "/error.html";
                }
                request.getSession(true).setAttribute("travelers", travelers);
                request.getSession(true).removeAttribute("thisTrip");
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }
        request.getRequestDispatcher(destination).forward(request, response);
    }
}

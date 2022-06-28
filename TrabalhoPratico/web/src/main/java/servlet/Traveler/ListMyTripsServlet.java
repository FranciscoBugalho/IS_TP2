package servlet.Traveler;

import server.IServer;
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

@WebServlet("Traveler/listMyTrips")
public class ListMyTripsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String destination = "/Traveler/DisplayMyTrips.jsp";
        String email = (String) request.getSession(true).getAttribute("email");
        try {
            ITravelerEJB it = InitialContext.doLookup(LinksEJB.TRAVELER_EJB);
            List<String> trips = new ArrayList<>();

            if (it != null) {
                destination = "/Traveler/DisplayMyTrips.jsp";
                trips = it.getMyTrips(email);
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

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
import java.util.ArrayList;
import java.util.List;

@WebServlet("Traveler/listRefundTrips")
public class ListRefundTripsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String destination = "/Traveler/RefundTrips.jsp";
        String email = (String) request.getSession(true).getAttribute("email");
        List<String> client = (List<String>) request.getSession(true).getAttribute("client");
        String buttonRefund = request.getParameter("RefundButton");
        try {
            ITravelerEJB it = InitialContext.doLookup(LinksEJB.TRAVELER_EJB);
            List<String> trips = new ArrayList<>();

            if (it != null) {
                destination = "/Traveler/RefundTrips.jsp";
                trips = it.getRefundTrips(email);
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

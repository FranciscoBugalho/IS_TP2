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

@WebServlet("Traveler/refundTrips")
public class RefundTicketServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String destination = "/Traveler/RefundTripsRedirect.jsp";
        String buttonRefund = request.getParameter("RefundButton");
        String email = (String) request.getSession(true).getAttribute("email");
        List<String> client = (List<String>) request.getSession(true).getAttribute("client");

        boolean redirect = true;

        try {
            ITravelerEJB it = InitialContext.doLookup(LinksEJB.TRAVELER_EJB);
            List<String> trips = new ArrayList<>();

            if (it != null) {
                List<String> result = it.refundTicket(email, request.getParameter("Trips"));

                if (Double.parseDouble(result.get(0)) == Double.parseDouble(client.get(2))) {
                    request.getSession(true).setAttribute("errorMessage", result.get(result.size() - 1));
                } else {
                    client.set(2, String.valueOf(Double.parseDouble(result.get(0))));
                    request.getSession(true).setAttribute("client", client);
                    destination = "/secured/Menu.jsp";
                    request.getRequestDispatcher(destination).forward(request, response);
                    redirect = false;
                }
                destination = "/Traveler/RefundTripsRedirect.jsp";
            } else {
                destination = "/error.html";
            }

            request.getSession(true).setAttribute("trips", trips);

            if (redirect)
                request.getRequestDispatcher(destination).forward(request, response);

        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
}
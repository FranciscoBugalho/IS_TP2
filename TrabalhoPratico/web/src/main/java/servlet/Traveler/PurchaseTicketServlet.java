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

@WebServlet("Traveler/purchaseTicket")
public class PurchaseTicketServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private boolean btn1 = false;
    private boolean btn2 = false;
    private boolean btn3 = false;
    private boolean btn4 = false;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String destination = "/Traveler/PurchaseTicket.jsp";
        String button1 = request.getParameter("OrderByDestinationName");
        String button2 = request.getParameter("OrderByDepartureName");
        String button3 = request.getParameter("OrderByTime");
        String button4 = request.getParameter("OrderByPrice");
        String buttonPurchase = request.getParameter("PurchaseButton");
        String email = (String) request.getSession(true).getAttribute("email");
        List<String> client = (List<String>) request.getSession(true).getAttribute("client");

        String btn1Order = "&#x2B06;";
        String btn2Order = "&#x2B06;";
        String btn3Order = "&#x2B06;";
        String btn4Order = "&#x2B06;";

        boolean redirect = true;

        try {
            ITravelerEJB it = InitialContext.doLookup(LinksEJB.TRAVELER_EJB);
            List<String> trips = new ArrayList<>();

            if (it != null) {
                if (button1 != null)
                    if (!btn1) {
                        btn1 = true;
                        btn1Order = "&#x2B06;";
                        trips = it.getAllDestinations(1, "ASC");
                    } else {
                        btn1 = false;
                        btn1Order = "&#x2B07;";
                        trips = it.getAllDestinations(1, "DESC");
                    }
                else if (button2 != null)
                    if (!btn2) {
                        btn2 = true;
                        btn2Order = "&#x2B06;";
                        trips = it.getAllDestinations(2, "ASC");
                    } else {
                        btn2 = false;
                        btn2Order = "&#x2B07;";
                        trips = it.getAllDestinations(2, "DESC");
                    }
                else if (button3 != null)
                    if (!btn3) {
                        btn3 = true;
                        btn3Order = "&#x2B06;";
                        trips = it.getAllDestinations(3, "ASC");
                    } else {
                        btn3 = false;
                        btn3Order = "&#x2B07;";
                        trips = it.getAllDestinations(3, "DESC");
                    }
                else if (button4 != null)
                    if (!btn4) {
                        btn4 = true;
                        btn4Order = "&#x2B06;";
                        trips = it.getAllDestinations(4, "ASC");
                    } else {
                        btn4 = false;
                        btn4Order = "&#x2B07;";
                        trips = it.getAllDestinations(4, "DESC");
                    }
                else if (buttonPurchase != null) {
                    List<String> result = it.purchaseTicket(email, Double.parseDouble(client.get(2)), request.getParameter("Trips"));

                    if (Double.parseDouble(result.get(0)) == Double.parseDouble(client.get(2))) {
                        request.getSession(true).setAttribute("errorMessage", result.get(result.size() - 1));
                    } else {
                        client.set(2, String.valueOf(Double.parseDouble(result.get(0))));
                        request.getSession(true).setAttribute("client", client);
                        destination = "/secured/Menu.jsp";
                        request.getRequestDispatcher(destination).forward(request, response);
                        redirect = false;
                    }
                } else {
                    trips = it.getAllDestinations(0, null);
                }
                destination = "/Traveler/PurchaseTicket.jsp";
            } else {
                destination = "/error.html";
            }

            List<String> arrows = new ArrayList<>();
            arrows.add(btn1Order);
            arrows.add(btn2Order);
            arrows.add(btn3Order);
            arrows.add(btn4Order);
            request.getSession(true).setAttribute("trips", trips);
            request.getSession(true).setAttribute("arrows", arrows);

            if (redirect)
                request.getRequestDispatcher(destination).forward(request, response);

        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
}
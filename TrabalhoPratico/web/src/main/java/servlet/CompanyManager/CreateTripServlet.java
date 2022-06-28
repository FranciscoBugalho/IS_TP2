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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("CompanyManager/createBusTrip")
public class CreateTripServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String departurePoint = request.getParameter("Departure Point");
        String destinationPoint = request.getParameter("Destination");
        String departureDate = request.getParameter("Departure Date");
        String departureTime = request.getParameter("Departure Time");
        int capacity = Integer.parseInt(request.getParameter("Capacity"));
        double price = Double.parseDouble(request.getParameter("Price"));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date departureDateTime = null;

        String result = null;
        String destination = "/index.html";

        try {
            departureDateTime = dateFormat.parse(departureDate + " " + departureTime);
        } catch (ParseException e) {
            destination = "/error.html";
            e.printStackTrace();
        }

        try {
            ICompanyManagerEJB ic = InitialContext.doLookup(LinksEJB.COMPANY_MANAGER_EJB);
            if (ic != null) {
                HttpSession session = request.getSession();
                String email = (String) session.getAttribute("email");
                result = ic.createBusTrip(email, departurePoint, destinationPoint, departureDateTime, capacity, price);
                System.out.println(result);
                destination = "/secured/Menu.jsp";
            } else {
                destination = "/error.html";
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }
        request.getRequestDispatcher(destination).forward(request, response);
    }
}
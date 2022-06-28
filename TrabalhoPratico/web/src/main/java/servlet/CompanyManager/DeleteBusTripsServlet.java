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
import java.util.ArrayList;
import java.util.List;

@WebServlet("CompanyManager/refundTrips")
public class DeleteBusTripsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String destination = "/CompanyManager/DeleteBusTripsRedirect.jsp";
        String buttonDelete = request.getParameter("DeleteButton");
        String email = (String) request.getSession(true).getAttribute("email");

        boolean redirect = true;

        System.out.println(buttonDelete);

        try {
            ICompanyManagerEJB ic = InitialContext.doLookup(LinksEJB.COMPANY_MANAGER_EJB);
            List<String> trips = new ArrayList<>();

            if (ic != null) {
                List<String> result = ic.deleteTrip(email, request.getParameter("Trips"));

                if (!result.get(0).equals("Trip deleted and all tickets have been refunded!")) {
                    request.getSession(true).setAttribute("errorMessage", result.get(0));
                } else {
                    destination = "/secured/Menu.jsp";
                    request.getRequestDispatcher(destination).forward(request, response);
                    redirect = false;
                }
                destination = "/CompanyManager/DeleteBusTripsRedirect.jsp";
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
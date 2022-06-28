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

@WebServlet("CompanyManager/listTop5Travelers")
public class ListTop5TravelersServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String destination = "/Traveler/Top5Travelers.jsp";
        String email = (String) request.getSession(true).getAttribute("email");
        try {
            ICompanyManagerEJB ic = InitialContext.doLookup(LinksEJB.COMPANY_MANAGER_EJB);
            List<String> top5 = new ArrayList<>();

            if (ic != null) {
                destination = "/CompanyManager/Top5Travelers.jsp";
                top5 = ic.getTop5Travelers(email);
            } else {
                destination = "/error.html";
            }
            request.getSession(true).setAttribute("top5", top5);
            request.getRequestDispatcher(destination).forward(request, response);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
}

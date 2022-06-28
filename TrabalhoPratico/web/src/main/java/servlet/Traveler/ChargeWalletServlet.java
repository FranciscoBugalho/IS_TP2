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
import java.util.List;

@WebServlet("Traveler/chargeWallet")
public class ChargeWalletServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        double money = Double.parseDouble(request.getParameter("Money"));
        String result = null;
        String destination = "/secured/Menu.jsp";
        String email = (String) request.getSession(true).getAttribute("email");
        List<String> client = (List<String>) request.getSession(true).getAttribute("client");

        try {
            ITravelerEJB it = InitialContext.doLookup(LinksEJB.TRAVELER_EJB);
            if (it != null) {
                result = it.chargeWallet(email, money);
                destination = "/secured/Menu.jsp";
                client.set(2, String.valueOf(Double.parseDouble(client.get(2)) + money));
                request.getSession(true).setAttribute("client", client);
            } else {
                destination = "/error.html";
            }
            request.getRequestDispatcher(destination).forward(request, response);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
}
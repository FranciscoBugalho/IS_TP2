package servlet;

import org.jasypt.util.password.ConfigurablePasswordEncryptor;
import server.IAuthenticate;
import server.IServer;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/loginClient")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("Email");
        String pass = request.getParameter("Password");
        List<String> result = new ArrayList<>();
        String destination = "/secured/Menu.jsp";
        if (!email.contains(" ") && !pass.contains(" ")) {

            // Encrypts Password
            ConfigurablePasswordEncryptor passwordEncryptor = new ConfigurablePasswordEncryptor();
            passwordEncryptor.setAlgorithm("SHA-1");
            passwordEncryptor.setPlainDigest(true);
            String encryptedPassword = passwordEncryptor.encryptPassword(pass);
            try {
                IAuthenticate ia = InitialContext.doLookup(LinksEJB.AUTHENTICATE_EJB);
                if (ia != null) {
                    result = ia.authenticate(email, encryptedPassword);
                    destination = "/secured/Menu.jsp";

                    // Set session attribute (email) after successful login
                    HttpSession session = request.getSession();
                    session.setAttribute("email", email);
                } else {
                    destination = "/error.html";
                }
                request.getSession(true).setAttribute("client", result);
                request.getRequestDispatcher(destination).forward(request, response);
            } catch (NamingException e) {
                e.printStackTrace();
            }
        } else {
            request.getRequestDispatcher(destination).forward(request, response);
        }
    }

}

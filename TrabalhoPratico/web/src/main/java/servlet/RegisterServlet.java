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
import java.io.IOException;

@WebServlet("/registerClient")
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("Name");
        String email = request.getParameter("Email");
        String pass = request.getParameter("Password");
        String result = null;
        String destination = "/ScreenRegister.html";
        if (!Character.isWhitespace(name.charAt(0)) && !email.contains(" ") && !pass.contains(" ")) {

            // Encrypts Password
            ConfigurablePasswordEncryptor passwordEncryptor = new ConfigurablePasswordEncryptor();
            passwordEncryptor.setAlgorithm("SHA-1");
            passwordEncryptor.setPlainDigest(true);
            String encryptedPassword = passwordEncryptor.encryptPassword(pass);
            try {
                IAuthenticate ia = InitialContext.doLookup(LinksEJB.AUTHENTICATE_EJB);
                if (ia != null) {
                    result = ia.registerClient(name, email, encryptedPassword);
                    destination = "/index.jsp";
                } else {
                    destination = "/error.html";
                }
                request.getSession(true).setAttribute("registered", result);
                request.getRequestDispatcher(destination).forward(request, response);

            } catch (NamingException e) {
                e.printStackTrace();
            }
        } else {
            request.getRequestDispatcher(destination).forward(request, response);
            ;
        }
    }

}
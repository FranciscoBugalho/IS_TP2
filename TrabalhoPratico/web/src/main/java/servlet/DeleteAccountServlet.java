package servlet;

import org.jasypt.util.password.ConfigurablePasswordEncryptor;
import server.IServer;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("secured/deleteAccount")
public class DeleteAccountServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pass = request.getParameter("Password");
        String result = null;
        String destination = "/index.jsp";
        String email = (String) request.getSession(true).getAttribute("email");
        String encryptedPassword = null;

        // Encrypts Password
        ConfigurablePasswordEncryptor passwordEncryptor = new ConfigurablePasswordEncryptor();
        passwordEncryptor.setAlgorithm("SHA-1");
        passwordEncryptor.setPlainDigest(true);
        encryptedPassword = passwordEncryptor.encryptPassword(pass);

        try {
            IServer cr = InitialContext.doLookup(LinksEJB.SERVER_EBJ);
            if (cr != null) {
                result = cr.deleteAccount(email, encryptedPassword);
            } else {
                destination = "/error.html";
            }

            if (result.equals("Wrong Password")) {
                destination = "/DeleteAccount.jsp";
                request.getSession(true).setAttribute("triedDelete", true);
                request.getRequestDispatcher(destination).forward(request, response);
                return;
            }

            // Clean session
            request.getSession().removeAttribute("client");
            request.getSession().removeAttribute("email");

            request.getRequestDispatcher(destination).forward(request, response);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
}
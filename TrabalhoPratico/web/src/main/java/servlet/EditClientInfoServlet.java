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

@WebServlet("secured/editClientInfo")
public class EditClientInfoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("Name");
        String pass = request.getParameter("Password");
        String result = null;
        String destination = "/secured/Menu.jsp";
        List<String> client = (List<String>) request.getSession(true).getAttribute("client");
        String email = (String) request.getSession(true).getAttribute("email");
        System.out.println(client);
        System.out.println(email);
        if (name.equals(client.get(1)) && pass.equals("")) {
            request.getRequestDispatcher(destination).forward(request, response);
        } else {
            String encryptedPassword = null;
            client.set(1, name);
            if (!pass.equals("")) {
                // Encrypts Password
                ConfigurablePasswordEncryptor passwordEncryptor = new ConfigurablePasswordEncryptor();
                passwordEncryptor.setAlgorithm("SHA-1");
                passwordEncryptor.setPlainDigest(true);
                encryptedPassword = passwordEncryptor.encryptPassword(pass);
            }
            try {
                IServer cr = InitialContext.doLookup(LinksEJB.SERVER_EBJ);
                if (cr != null) {
                    result = cr.editPersonalInfo(name, email, encryptedPassword);
                    destination = "/secured/Menu.jsp";
                } else {
                    destination = "/error.html";
                }
                request.getSession(true).setAttribute("client", client);
                request.getRequestDispatcher(destination).forward(request, response);
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }
    }
}
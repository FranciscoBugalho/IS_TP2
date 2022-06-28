package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebFilter("/Traveler/*")
public class SecurityFilterTraveler implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //System.out.println("WebApp: Accessing the Traveler filter...");
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpSession session = httpReq.getSession(false);
        List<String> result = new ArrayList<>();
        result = (List<String>)session.getAttribute("client");
        if (session != null && result != null && result.get(0).equals("Traveler")) {
            //System.out.println("WebApp: Verified logged in status for /Traveler!");
            chain.doFilter(request, response);
        }
        else {
            //System.out.println("WebApp: Failed to verify logged in status for /Traveler!");
            request.getRequestDispatcher("/error.html").forward(request, response);
        }
    }
}
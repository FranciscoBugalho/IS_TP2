package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/secured/*")
public class SecurityFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //System.out.println("WebApp: Accessing the secured filter...");
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpSession session = httpReq.getSession(false);
        if (session != null && session.getAttribute("email") != null) {
            //System.out.println("WebApp: Verified logged in status for /secured!");
            chain.doFilter(request, response);
        }
        else {
            //System.out.println("WebApp: Failed to verify logged in status for /secured!!");
            request.getRequestDispatcher("/error.html").forward(request, response);
        }
    }
}
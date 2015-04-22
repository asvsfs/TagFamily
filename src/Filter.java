import org.jose4j.jwt.JwtClaims;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by asvsfs on 4/21/2015.
 */
@WebFilter(filterName = "Filter")
public class Filter implements javax.servlet.Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException, IOException {

        boolean authentication = true;

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String token = req.getParameter("access_token");
        if(token == "" || token == null){
            token = ((HttpServletRequest) req).getSession().getAttribute("access_token").toString();
        }

        if (token == "" || token == null) {
            authentication = false;
        } else {

            JwtClaims claims = JWT.consumeJWT(token);
            System.out.print(token);
            if (claims == null) {
                authentication = false;
                response.sendRedirect("failed.jsp");
            } else {
                String userid = (String) claims.getClaimValue("userid");
                request.setAttribute("userid", userid);
                chain.doFilter(request, response);
            }
        }
        if (!authentication) {
            response.setStatus(401);
            response.sendRedirect("Unauthorized access!");
        }
    }

    public void init(FilterConfig config) throws ServletException {

    }



}


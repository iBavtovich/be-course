package org.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class LogRequestFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        Date date = new Date();
        String contextPath = req.getServletPath();
        String method = req.getMethod();

        System.out.println(date.toString() + " received " + method + " request " + "to " + contextPath);

        chain.doFilter(req, res);
    }

    public void destroy() {

    }
}

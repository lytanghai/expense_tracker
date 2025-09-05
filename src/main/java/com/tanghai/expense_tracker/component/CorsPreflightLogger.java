package com.tanghai.expense_tracker.component;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CorsPreflightLogger implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // Log all requests
        System.out.println("=== Incoming Request ===");
        System.out.println("Method : " + request.getMethod());
        System.out.println("URI    : " + request.getRequestURI());
        System.out.println("Origin : " + request.getHeader("Origin"));

        // Add CORS headers for testing
        response.setHeader("Access-Control-Allow-Origin", "https://expense-tracker-v2-web.onrender.com");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        // Immediately return OK for preflight OPTIONS
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            System.out.println("Preflight OPTIONS request handled!");
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        chain.doFilter(req, res);
    }
}

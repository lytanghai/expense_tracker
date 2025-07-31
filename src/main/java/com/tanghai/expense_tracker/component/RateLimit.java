package com.tanghai.expense_tracker.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimit implements Filter {

    // Map to store request counts per IP address
    private final Map<String, AtomicInteger> requestCountsPerIpAddress = new ConcurrentHashMap<>();

    // Maximum requests allowed per minute
    @Value("${server.rate_limit:10}")
    private int MAX_REQUESTS_PER_MINUTE;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String clientIpAddress = httpServletRequest.getRemoteAddr();

        // Initialize request count for the client IP address
        requestCountsPerIpAddress.putIfAbsent(clientIpAddress, new AtomicInteger(0));
        AtomicInteger requestCount = requestCountsPerIpAddress.get(clientIpAddress);

        // Increment the request count
        int requests = requestCount.incrementAndGet();

        if(httpServletRequest.getRequestURI().contains("/login") && requests > 3) {
            httpServletResponse.setStatus(423);
            httpServletResponse.getWriter().write("Too many incorrect username or password!. Please try again later.");
            return;
        }

        if (!httpServletRequest.getRequestURI().contains("/login") && requests > MAX_REQUESTS_PER_MINUTE) {
            httpServletResponse.setStatus(429);
            httpServletResponse.getWriter().write("Too many requests. Please try again later.");
            return;
        }

        // Allow the request to proceed
        chain.doFilter(request, response);

        // Optional: Reset request counts periodically (not implemented in this simple example)
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Optional: Initialization logic, if needed
    }

    @Override
    public void destroy() {
        // Optional: Cleanup resources, if needed
    }

}

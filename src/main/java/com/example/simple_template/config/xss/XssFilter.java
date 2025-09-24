package com.example.simple_template.config.xss;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.List;

@WebFilter(urlPatterns = "/*")
public class XssFilter implements Filter {

    // Define paths that should be excluded from XSS filtering
    private final List<String> excludeUrls = List.of(
            "/swagger-ui",
            "/v3/api-docs",
            "/webjars/",
            "/doc.html"
    );

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        // If the request path starts with an excluded path, skip filtering
        for (String excludeUrl : excludeUrls) {
            if (request.getRequestURI().startsWith(excludeUrl)) {
                filterChain.doFilter(request, servletResponse);
                return;
            }
        }

        // Wrap the request to filter potential XSS attacks
        XssHttpServletRequestWrapper wrapper = new XssHttpServletRequestWrapper(request);
        filterChain.doFilter(wrapper, servletResponse);
    }
}

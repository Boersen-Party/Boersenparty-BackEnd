package com.boersenparty.v_1_1.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class PermitAllFilter extends OncePerRequestFilter {

    private final RequestMatcher requestMatcher;

    public PermitAllFilter(String pattern) {
        // Allow all HTTP methods and enable case-sensitivity
        this.requestMatcher = new AntPathRequestMatcher(pattern, null, true);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (requestMatcher.matches(request)) {
            // Allow the request to proceed without additional filtering
            filterChain.doFilter(request, response);
        } else {
            // Continue with the filter chain for non-matching requests
            filterChain.doFilter(request, response);
        }
    }
}

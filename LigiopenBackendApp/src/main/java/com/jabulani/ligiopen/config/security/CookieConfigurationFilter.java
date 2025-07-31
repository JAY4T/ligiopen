package com.jabulani.ligiopen.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CookieConfigurationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        
        // Set cookie attributes
        String cookieHeader = String.format("JSESSIONID=%s; Path=/; Secure; SameSite=None; Domain=.rnbhx-102-215-34-84.a.free.pinggy.link",
            request.getSession().getId());
        
        response.addHeader("Set-Cookie", cookieHeader);
        filterChain.doFilter(request, response);
    }
}
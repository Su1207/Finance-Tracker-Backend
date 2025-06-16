package com.demo.personalfinancetracker.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class SessionAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session != null) {
            Long userId = (Long) session.getAttribute("userId");

            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Create authentication token
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userId.toString(), // principal (user identifier)
                        null, // credentials (not needed for session-based auth)
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")) // authorities
                );

                // Set authentication in security context
                SecurityContextHolder.getContext().setAuthentication(authentication);

                System.out.println("Set authentication for user: " + userId);
            }
        }

        filterChain.doFilter(request, response);
    }
}
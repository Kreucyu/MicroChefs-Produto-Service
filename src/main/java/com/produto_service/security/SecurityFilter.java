package com.produto_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        if (token != null) {
            var subject = tokenService.validateToken(token);
            if(subject != null && !subject.isEmpty()) {
                String role = tokenService.getRole(token);
                var authority = new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + role.toUpperCase());
                var authentication = new UsernamePasswordAuthenticationToken(subject, null, List.of(authority));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var header = request.getHeader("Authorization");
        if (header == null) return null;
        return header.replace("Bearer ", "").replace("\"", "");
    }
}

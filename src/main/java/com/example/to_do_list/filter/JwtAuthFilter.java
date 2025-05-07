package com.example.to_do_list.filter;

import com.example.to_do_list.service.CustomUserDefaultService;
import com.example.to_do_list.service.interfaces.IJwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter  {

    private final IJwtService jwtService;
    private final CustomUserDefaultService userDefaultService;

    public JwtAuthFilter(IJwtService jwtService, CustomUserDefaultService userDefaultService) {
        this.jwtService = jwtService;
        this.userDefaultService = userDefaultService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = authHeader.substring(7);
        String username = jwtService.extractUsername(jwtToken);

        System.out.println("doFilterInternal: username = " + username);
        if (username != null && jwtService.validateToken(jwtToken)) {
            SecurityContextHolder.clearContext();
            UserDetails user = userDefaultService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    null,
                    user.getAuthorities()
            );
            token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            System.out.println("token: " + token);
            SecurityContextHolder.getContext().setAuthentication(token);
        }

        filterChain.doFilter(request, response);
    }
}

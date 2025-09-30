package com.example.demo.Util;

import com.example.demo.service.CustomUserDetailsService;
import com.example.demo.service.UserService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.*;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserService userService;

    public AuthTokenFilter(JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        try{
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtil.validateToken(jwt)) {

                //Hämta userId direkt från JWT
                String userId = jwtUtil.extractUserId(jwt);
                // Hämta username från userId
                String username = userService.getUsernameById(userId);

                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }
        }catch (JwtException e){
            logger.error("JWT Validation failed", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT Validation failed");
            return;
        }catch (Exception e){
            logger.error("Authentication failed", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Authentication failed");
            return;
        }

        filterChain.doFilter(request, response);

    }
    private String parseJwt(HttpServletRequest request) {
        // Try to find jwt from authorization header
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {

            return headerAuth.substring(7);
        }

        // Try to get jwt from cookies
        if ( request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}

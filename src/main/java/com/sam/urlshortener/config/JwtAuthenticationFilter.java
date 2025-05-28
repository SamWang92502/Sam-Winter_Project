package com.sam.urlshortener.config;

import com.sam.urlshortener.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component //Tells Spring to treat this class as a managed bean and include it in the context.
    public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    //The core logic is inside doFilterInternal(), which is automatically called for every request.
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        //Reads the Authorization HTTP header, which is where JWTs are normally sent from the client:
        String authHeader = request.getHeader("Authorization");

        //Check header exist and It starts with "Bearer" — the standard prefix for JWTs
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                //Extracts the actual token (removes the "Bearer " prefix)
                String token = authHeader.substring(7);
                //Parses the JWT to extract the username (stored in the token’s sub or subject field)
                String username = jwtUtils.getUsernameFromToken(token);
            /*
            Ensures:
            - The token is valid and has a username
            - No authentication has been set yet (prevents overwriting existing sessions)
             */
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    //Loads user info (password, roles, etc.) from your DB or in-memory service
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    if (jwtUtils.validateToken(token, userDetails)) {
                    /*
                    - Creates an Authentication object representing the user.
                    - userDetails.getAuthorities() gives the user’s roles/permissions.
                    */
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities());
                        //Adds more context to the authentication object (like the remote IP, session ID, etc.)
                        authToken.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request));
                    /*This line tells Spring Security:
                    “Hey, we’ve verified this user’s identity. Here’s their authentication info.”
                     */
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } catch (Exception e) {
                // Prevent the filter from blocking access if JWT is invalid or missing
                System.out.println("JWT error: " + e.getMessage());
                // Optionally log or silently continue

            }
        }
        filterChain.doFilter(request, response);
    }
}
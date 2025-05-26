package com.sam.urlshortener.config;

import org.springframework.security.core.userdetails.UserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;

/*
@Component: This tells Spring to treat JwtUtils as a Spring-managed bean so it can be injected using @Autowired.
The purpose of a Spring-managed bean is to let Spring handle the creation, configuration, and lifecycle of your objects
— especially when you want to reuse or share them across multiple parts of your app (like services, utilities, or controllers).
 @Autowired is used to inject (i.e., automatically assign) one Spring-managed bean into another.
 */
@Component
public class JwtUtils {
    /*
    The secret key is used to sign and verify JWTs (JSON Web Tokens) to ensure that:
    - The token was issued by your server, not forged.
    - The token has not been tampered with.
     */
    @Value("${app.jwt.secret}")
    private String jwtSecret; // store securely in env
    @Value("${app.jwt.expirationMs}")
    private long jwtExpirationMs;

    public String generateJwtToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret) // sign the token using HMAC SHA-512
                .compact(); //.compact() is the method that builds and encodes the JWT into a final string format that can be sent over HTTP
    }

    /**
     * Validate and parse the JWT token.
     * If valid, return the username (subject); else throw an error.
     */
    public String getUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject(); // username
        } catch (ExpiredJwtException e) {
            throw new IllegalArgumentException("Token has expired");
        } catch (UnsupportedJwtException e) {
            throw new IllegalArgumentException("Unsupported JWT");
        } catch (MalformedJwtException e) {
            throw new IllegalArgumentException("Malformed JWT");
        } catch (SignatureException e) {
            throw new IllegalArgumentException("Invalid signature");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("JWT token is empty or invalid");
        }
    }
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return (username != null && username.equals(userDetails.getUsername()));
    }
}

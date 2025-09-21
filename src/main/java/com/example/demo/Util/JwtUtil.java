package com.example.demo.Util;

import com.example.demo.models.Roles;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Set;

import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    // Generate JWT Token
    public String generateToken(String username, Set<Roles> roles) {
        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .issuedAt(Date.from(Instant.now()))
                .expiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }
    // Get Username from JWT token
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
    public boolean validateToken(String token) {
        try{
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        }catch (SecurityException e) {
            System.out.println("Invalid JWT signature" + e.getMessage());
        }catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token" + e.getMessage());
        }catch (ExpiredJwtException e) {
            System.out.println("JWT token is expired" + e.getMessage());
        }catch (UnsupportedJwtException e) {
            System.out.println("JWT token is unsupported" + e.getMessage());
        }catch (IllegalArgumentException e) {
            System.out.println("JWT claims is empty" + e.getMessage());
        }
        return false;
    }




}

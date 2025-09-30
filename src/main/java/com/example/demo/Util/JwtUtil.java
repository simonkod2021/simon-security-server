package com.example.demo.Util;


import com.example.demo.models.User;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;


import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

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
    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getId())
                .issuedAt(Date.from(Instant.now()))
                .expiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }
   //Get username from id
    public String extractUserId(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
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

    public void removeToken(HttpServletResponse response) {

        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(Date.from(Instant.now()))
                .expiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

}

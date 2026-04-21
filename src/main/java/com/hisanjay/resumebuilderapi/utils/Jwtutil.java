package com.hisanjay.resumebuilderapi.utils;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class Jwtutil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public String generateToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder().subject(email).issuedAt(now).expiration(expiryDate).signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String getEmailFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // public boolean validateToken(String token) {
    // try {
    // Jwts.parser()
    // .verifyWith(getSigningKey())
    // .build()
    // .parseSignedClaims(token);
    // return true;
    // } catch (Exception e) {
    // return false;
    // }
    // }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token); // parses + validates signature + expiry

            return true;
        } catch (io.jsonwebtoken.security.SecurityException e) {
            // invalid signature
            throw new RuntimeException("Invalid JWT signature", e);
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            // expired token
            throw new RuntimeException("JWT expired", e);
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            // invalid structure
            throw new RuntimeException("Invalid JWT format", e);
        } catch (io.jsonwebtoken.JwtException e) {
            // generic JWT error
            throw new RuntimeException("JWT validation failed", e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("JWT is empty or null", e);
        }
    }
}

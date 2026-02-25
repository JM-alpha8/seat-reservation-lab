package com.kimjm.seat_reservation_lab.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {

    private final String secret = "very-secret-key-very-secret-key";
    private final long expiration = 1000 * 60 * 60;

    public String generateToken(Long userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    public Long getUserId(String token) {
        return Long.parseLong(
                Jwts.parserBuilder()
                        .setSigningKey(secret.getBytes())
                        .build()
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject()
        );
    }
}
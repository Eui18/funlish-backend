package com.example.services;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import com.example.exceptions.UnauthorizedException;
import com.example.utils.AuthenticatedUser;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtService {

    private final Dotenv dotenv = Dotenv.load();

    private final Key SECRET_KEY =
            Keys.hmacShaKeyFor(
                    dotenv.get("JWT_SECRET")
                        .getBytes(StandardCharsets.UTF_8)
            );

    public String generateToken(String userId, String role) {

        return Jwts.builder()
                .setSubject(userId)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(SECRET_KEY)
                .compact();
    }

    public AuthenticatedUser validateToken(String token) {

        try {

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String userId = claims.getSubject();
            String role = claims.get("role", String.class);

            return new AuthenticatedUser(userId, role);

        } catch (Exception e) {
            throw new UnauthorizedException("Token inválido o expirado.");
        }
    }
}
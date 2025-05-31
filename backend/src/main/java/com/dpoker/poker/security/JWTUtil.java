package com.dpoker.poker.security;

import com.dpoker.poker.config.security.jwt.Properties;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;



@Component
public class JWTUtil {

    private final Properties properties;
    private final Key secretKey;

    private enum TokenType {
        ACCESS,
        REFRESH
    };

    public JWTUtil(Properties properties) {
        this.properties = properties;
        this.secretKey = Keys.hmacShaKeyFor(properties.getSecret().getBytes());
    }

    private String generateToken(String username, TokenType type) {
        long expirationTime = type == TokenType.ACCESS ? properties.getAccessExpiration() : properties.getRefreshExpiration();
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);

        return Jwts.builder()
                .setSubject(String.format("%s:%s", username, type.name()))
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(secretKey)
                .compact();
    }

    private String getUsernameFromToken(String token, TokenType type) {
        String subject = Jwts.parserBuilder()
                .setSigningKey(this.secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        if (subject == null || !subject.endsWith(":" + type.name())) {
            throw new IllegalArgumentException("Invalid token type");
        }

        return subject.split(":")[0];
    }


    public String generateAccessToken(String username) {
        return generateToken(username, TokenType.ACCESS);
    }

    public String generateRefreshToken(String username) {
        return generateToken(username, TokenType.REFRESH);
    }

    public String getUsernameFromAccessToken(String token) {
        return getUsernameFromToken(token, TokenType.ACCESS);
    }

    public String getUsernameFromRefreshToken(String token) {
        return getUsernameFromToken(token, TokenType.REFRESH);
    }
}

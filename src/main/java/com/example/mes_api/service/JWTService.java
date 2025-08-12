package com.example.mes_api.service;

import com.example.mes_api.dto.UserDTO;
import com.example.mes_api.entity.Token;
import com.example.mes_api.entity.User;
import com.example.mes_api.repository.TokenRepository;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JWTService {

    @Autowired
    private TokenRepository tokenRepository;
    @Value("${JWT_SECRET_BASE64}")
    private String SECRET_KEY;

    public String generateToken(String username) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(15, ChronoUnit.MINUTES))) // короткий access
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public void saveTokenInDB(Long userId, String token) {
        Token refreshToken = new Token();
        refreshToken.setUserId(userId);
        refreshToken.setRefreshToken(token);
        tokenRepository.save(refreshToken);
    }

    public String createCookie(String token) {
        return ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .path("/")
                .maxAge(Duration.ofDays(1))
                .build()
                .toString();
    }

    public Claims validateToken(String token) throws JwtException {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException error) {
            throw error;
        }
    }

    public String extractUsername(String token) {
        return validateToken(token).getSubject();
    }
}
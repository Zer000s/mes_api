package com.example.mes_api.service;

import com.example.mes_api.dto.UserDTO;
import com.example.mes_api.entity.Token;
import com.example.mes_api.entity.User;
import com.example.mes_api.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

@Service
public class JWTService {

    @Autowired
    private TokenRepository tokenRepository;

    private static final String SECRET_KEY = "X/mI85Z+Dzd3dKSdNbXnUGjFYN5D7GhLM4KV/YSKKEg=";

    public String generateToken(String user) {
        return Jwts.builder()
                .setSubject(user)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))  // 1 час
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

    public Claims validateToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return validateToken(token).getSubject();
    }
}
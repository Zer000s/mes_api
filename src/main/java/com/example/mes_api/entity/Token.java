package com.example.mes_api.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String refreshToken;
    private Instant createdAt = Instant.now();

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setRefreshToken(String token) {
        this.refreshToken = token;
    }
}
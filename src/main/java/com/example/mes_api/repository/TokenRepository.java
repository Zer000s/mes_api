package com.example.mes_api.repository;

import com.example.mes_api.entity.Token;
import com.example.mes_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findTokenByRefreshToken(String refreshToken);

    Optional<Token> findTokenByUserId(Long userId);

    Optional<Token> removeTokenByRefreshToken(String refreshToken);
}
package com.example.mes_api.controller;

import com.example.mes_api.entity.Token;
import com.example.mes_api.entity.User;
import com.example.mes_api.dto.UserDTO;
import com.example.mes_api.repository.TokenRepository;
import com.example.mes_api.repository.UserRepository;
import com.example.mes_api.service.JWTService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private UserRepository userRepository;

    private JWTService jwtService;

    private PasswordEncoder passwordEncoder;

    private TokenRepository tokenRepository;

    @PostMapping("/registration")
    public ResponseEntity<Object> register(@Valid @RequestBody User user) {
        try {
            Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
            if (existingUser.isPresent()) {
                return ResponseEntity.badRequest().body("Username is already taken");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            String token = jwtService.generateToken(user.getId().toString());
            jwtService.saveTokenInDB(user.getId(), token);
            String cookie = jwtService.createCookie(token);
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie).body(user);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody User user) {
        try {
            Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
            if (existingUser.isEmpty() || !passwordEncoder.matches(user.getPassword(), existingUser.get().getPassword())) {
                return ResponseEntity.badRequest().body("Invalid credentials");
            }
            String token = jwtService.generateToken(existingUser.get().getId().toString());
            String cookie = jwtService.createCookie(token);
            UserDTO userDTO = new UserDTO(existingUser.get().getId(), existingUser.get().getUsername());
            Optional<Token> existingToken = tokenRepository.findTokenByUserId(existingUser.get().getId());
            if (existingToken.isEmpty()) {
                jwtService.saveTokenInDB(existingUser.get().getId(), token);
                return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie).body(userDTO);
            }
            existingToken.get().setRefreshToken(token);
            tokenRepository.save(existingToken.get());
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie).body(userDTO);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue("jwt") String refreshToken) {
        try {
            if(refreshToken.isEmpty()) {
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }
            Claims tokenData = jwtService.validateToken(refreshToken);
            Optional<Token> tokenIsDB = tokenRepository.findTokenByRefreshToken(refreshToken);
            String userId = tokenData.getSubject();
            if(tokenIsDB.isEmpty() || userId == null) {
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }
            String token = jwtService.generateToken(userId);
            String cookie = jwtService.createCookie(token);
            Optional<Token> existingToken = tokenRepository.findTokenByUserId(Long.parseLong(userId));
            if (existingToken.isEmpty()) {
                jwtService.saveTokenInDB(Long.parseLong(userId), token);
                return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie).body("Success");
            }
            existingToken.get().setRefreshToken(token);
            tokenRepository.save(existingToken.get());
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie).body("Success");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<Object> logout(@RequestParam String token) {
        try {
            return null;
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
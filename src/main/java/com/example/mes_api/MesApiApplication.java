package com.example.mes_api;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.crypto.SecretKey;

@SpringBootApplication
public class MesApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(MesApiApplication.class, args);
    }
}
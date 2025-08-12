package com.example.mes_api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "messages")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Message {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long fromUserId;
    private Long toUserId; // для приватного чата; для групп - можно сделать chatId 
    @Column(length = 4000)
    private String text;
    private Instant createdAt = Instant.now();
}
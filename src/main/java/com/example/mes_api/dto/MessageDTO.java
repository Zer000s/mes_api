package com.example.mes_api.dto;

import lombok.*;
import java.time.Instant;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MessageDTO {
    private Long id;
    private Long fromUserId;
    private Long toUserId;
    private String text;
    private Instant createdAt;
}
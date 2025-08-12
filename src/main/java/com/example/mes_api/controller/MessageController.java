package com.example.mes_api.controller;

import com.example.mes_api.dto.MessageDTO;
import com.example.mes_api.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;
    public MessageController(MessageService messageService) { this.messageService = messageService; }

    @PostMapping
    public ResponseEntity<?> send(@RequestBody MessageDTO dto) {
        messageService.send(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/history")
    public ResponseEntity<List<MessageDTO>> history(@RequestParam Long userA, @RequestParam Long userB) {
        return ResponseEntity.ok(messageService.history(userA, userB));
    }
}